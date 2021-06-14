package Client.Screens;
import Game.Action;
import Game.GameHandler;
import Game.Hero;
import Game.PlayerClasses.*;
import Client.GemGrab;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import org.lwjgl.opengl.GL20;
import com.badlogic.gdx.Input.Keys;
import java.util.ArrayList;


public class GameScreen implements Screen, InputProcessor {
    private GemGrab game;
    private GameHandler logic;

    private float tickTimer=0;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;



    @Override
    public boolean keyDown(int keycode) {
        Hero player = logic.getPlayerWithID(game.getUid());
        switch(keycode) {
            case Keys.W:
                player.addAction(Action.UP);
                break;
            case Keys.S:
                player.addAction(Action.DOWN);
                break;
            case Keys.A:
                player.addAction(Action.LEFT);
                break;
            case Keys.D:
               player.addAction(Action.RIGHT);
               break;
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        Hero player = logic.getPlayerWithID(game.getUid());

        switch(keycode) {
            case Keys.W:
            case Keys.S:
            case Keys.A:
            case Keys.D:
                player.addAction(Action.STOP);
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    public GameScreen(GemGrab game, GameHandler logic){
        this.game = game;
        this.logic = logic;
    }
    @Override
    public void show() {
        map = new TmxMapLoader().load("src/main/Assets/Tiles/gemgrab.tmx");
        renderer = new OrthogonalTiledMapRenderer(map,game.unitScale);
        Gdx.input.setInputProcessor(this);

    }

    @Override
    public void render(float delta) {
        tickTimer+= delta;
        if(tickTimer>=1/60){
            logic.update();
            game.camera.getCam().position.set(logic.getPlayerWithID(game.getUid()).getPosition(),0);
            game.camera.getCam().update();
        }

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.setView(game.camera.getCam());
        renderer.render();

        renderer.getBatch().begin();
        drawPlayers();
        renderer.getBatch().end();
    }

    public void update(){

    }

    public void drawPlayers(){
        ArrayList<Hero> players = logic.getPlayers();

        for(Hero player : players){
            Vector2 pos = player.getPosition();
            Sprite spr = player.getHeroSprite();

            spr.setX(pos.x);
            spr.setY(pos.y);

            spr.draw(renderer.getBatch());
        }

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
    }
}
