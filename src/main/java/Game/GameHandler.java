package Game;
import Client.GemGrab;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.awt.geom.Point2D;
import java.util.ArrayList;

@RequiredArgsConstructor
public class GameHandler {

    Vector2 teamBlueLoc;
    Vector2 teamRedLoc;
    @Getter private final ArrayList<Hero> players;

    private final float unitScale = GemGrab.unitScale;
    @Getter private final int tickrate;

    @Getter private final TiledMapTileLayer collisionLayer;
    @Getter private String blockedKey = "blocked";

    private boolean isCellBlocked(float x, float y) {
        TiledMapTileLayer.Cell cell = collisionLayer.getCell((int) (x/unitScale / collisionLayer.getTileWidth()), (int) (y/unitScale / collisionLayer.getTileHeight()));
        return cell != null && cell.getTile() != null;
    }

    public Vector2 getRespawnLoc(Team team){
        if(team== Team.TEAMRED) return teamRedLoc;
        else return teamBlueLoc;
    }

    public boolean collidesRight(Hero hero) {
        for(float step = 0; step <= hero.getHeight(); step += hero.getIncrement())
            if(isCellBlocked(hero.getX() + hero.getWidth(), hero.getY() + step))
                return true;
        return false;
    }

    public boolean collidesLeft(Hero hero) {
        for(float step = 0; step <= hero.getHeight(); step += hero.getIncrement())
            if(isCellBlocked(hero.getX(), hero.getY() + step))
                return true;
        return false;
    }

    public boolean collidesTop(Hero hero) {
        for(float step = 0; step <= hero.getWidth(); step += hero.getIncrement())
            if(isCellBlocked(hero.getX() + step, hero.getY() + hero.getHeight()))
                return true;
        return false;

    }

    public boolean collidesBottom(Hero hero) {
        for(float step = 0; step <= hero.getWidth(); step += hero.getIncrement())
            if(isCellBlocked(hero.getX() + step, hero.getY()))
                return true;
        return false;
    }

    public Hero getPlayerWithID(int uid){
        for(Hero player : players){
            if(player.getId()==uid){
                return player;
            }
        }
        return null;
    }

    public void update() {
        for(Hero player : players){
            player.doActions();
            Vector2 velocity = player.getVelocity();
            float speed = player.getBaseSpeed();
            // clamp velocity
            if(velocity.y > speed)
                velocity.y = speed;
            else if(velocity.y < -speed)
                velocity.y = -speed;

            // save old position
            float oldX = player.getX(), oldY = player.getY();
            boolean collisionX = false, collisionY = false;

            // move on x
            player.setX(player.getX() + velocity.x/tickrate);

            // calculate the increment for step in #collidesLeft() and #collidesRight()
            float increment = collisionLayer.getTileWidth();
            player.setIncrement(player.getWidth() < increment ? player.getWidth() / 2 : increment / 2) ;

            if(velocity.x < 0) // going left
                collisionX = collidesLeft(player);
            else if(velocity.x > 0) // going right
                collisionX = collidesRight(player);

            // react to x collision
            if(collisionX) {
                System.out.println("Collision x detected");
                player.setX(oldX);
                velocity.x = 0;
            }
            // move on y
            player.setY(player.getY() + velocity.y/tickrate);

            // calculate the increment for step in #collidesBottom() and #collidesTop()
            increment = collisionLayer.getTileHeight();
            player.setIncrement(player.getHeight() < increment ? player.getHeight() / 2 : increment / 2);
            if(velocity.y < 0) // going down
                collisionY = collidesBottom(player);
            else if(velocity.y > 0) // going up
                collisionY = collidesTop(player);
            // react to y collision
            if(collisionY) {
                System.out.println("Collision y detected");
                player.setY(oldY);
                velocity.y = 0;
            }

            player.setVelocity(velocity);

        }


    }
}
