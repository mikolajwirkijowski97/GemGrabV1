package Game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;
import lombok.Getter;
import lombok.Setter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;



public abstract class Hero  {
    public Hero(Sprite heroSprite, int id, int baseHp, int baseSpeed, Team team, double attack_cooldown, double ult_cooldown) {
        this.heroSprite = heroSprite;
        this.heroSprite.setSize(100,100);
        this.width = this.heroSprite.getWidth();
        this.height = this.heroSprite.getHeight();
        this.id = id;
        this.baseHp = baseHp;
        this.baseSpeed = baseSpeed;
        this.team = team;
        this.attack_cooldown = attack_cooldown;
        this.ult_cooldown = ult_cooldown;
    }

    @Getter private final Sprite heroSprite;
    @Setter @Getter private float increment=0;

    @Getter @Setter private Vector2 velocity = new Vector2();
    @Getter @Setter private Vector2 position= new Vector2(150,150);

    @Getter private final float width;
    @Getter private final float height;

    @Getter private final int id;



    @Getter private final int baseHp;
    @Getter private final int baseSpeed;
    @Getter @Setter private int hp;
    @Getter @Setter private final Team team;

    private final double attack_cooldown;
    @Getter @Setter private double attack_timer=0;

    private final double ult_cooldown;
    @Getter @Setter private double ult_timer=0;

    private ArrayList<Action> actions = new ArrayList<>();

    public void addAction(Action action){
        System.out.println("Action registered");
        actions.add(action);
    }
    public void doActions(){
        for(Action action : actions){
            switch(action) {
                case UP:
                    velocity.y = baseSpeed;
                    break;
                case DOWN:
                    velocity.y = -baseSpeed;
                    break;
                case LEFT:
                    velocity.x = -baseSpeed;
                    break;
                case RIGHT:
                    velocity.x = baseSpeed;
                    break;
                case STOP:
                    velocity.x = 0;
                    velocity.y = 0;
                case ULT:
                    if(ult_timer == 0){
                        use_ult();
                        ult_timer = ult_cooldown;
                    }
                    break;
                case ATTACK:
                    if(attack_timer == 0){
                        use_attack();
                        attack_timer = attack_cooldown;
                    }

            }

        }
        actions.clear();
    }



    protected abstract Boolean use_attack();

    protected abstract Boolean use_ult();

    protected abstract void resetHp();

    public void takeDamage(int dmg) {
        hp -= dmg;
    }

    protected void die() {
        hp = baseHp;
    }



    public void setX(float x){
        position.x = x;
    }
    public float getX() {return position.x;}

    public void setY(float y){
        position.y = y;
    }
    public float getY() {return position.y;}


}
