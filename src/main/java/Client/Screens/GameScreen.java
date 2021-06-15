package Client.Screens;
import Game.Action;
import Game.GameHandler;
import Game.Hero;
import Client.GemGrab;
import Server.HeroInfo;
import Server.SnapshotMessage;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import lombok.Getter;
import org.lwjgl.Sys;
import org.lwjgl.opengl.GL20;
import com.badlogic.gdx.Input.Keys;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;


public class GameScreen implements Screen, InputProcessor {
    private GemGrab game;
    private GameHandler logic;
    private final Socket socket;
    @Getter  private ObjectOutputStream out;
    @Getter  private ObjectInputStream in;
    private float tickTimer=0;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    public GameScreen(GemGrab game, Socket socket){
        this.socket = socket;
        this.game = game;

        ArrayList<HeroInfo> players = null;
        try{
            System.out.println("Creating ObjectOutputStream for lobby");
            out = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("Creating ObjectInputStream for lobby");
            in = new ObjectInputStream(socket.getInputStream());

            System.out.println("Waiting for message: initSnapshot");
            SnapshotMessage initSnapshot = (SnapshotMessage)in.readObject();
            players = initSnapshot.getPlayers();


        }catch(IOException|ClassNotFoundException e){
            System.out.println("Game screen constructor, stream init");
            System.out.println(e.toString());
        }
        System.out.println("Loading map");
        map = new TmxMapLoader().load("src/main/Assets/Tiles/gemgrab.tmx");
        this.logic = new GameHandler(players,30,(TiledMapTileLayer) map.getLayers().get("Collision"));
        System.out.println("Construction of game screen finished");
    }


    @Override
    public boolean keyDown(int keycode) {
        HeroInfo player = logic.getPlayerWithID(game.getUid());
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
        HeroInfo player = logic.getPlayerWithID(game.getUid());

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


    @Override
    public void show() {

        renderer = new OrthogonalTiledMapRenderer(map,game.unitScale);
        Gdx.input.setInputProcessor(this);

    }

    @Override
    public void render(float delta) {
        tickTimer+= delta;
        if(tickTimer>=1/60){
            try{
                SnapshotMessage msg = (SnapshotMessage) in.readObject();
                logic.updateFromPlayerList(msg.getPlayers());
            }
            catch(IOException|ClassNotFoundException e){
                System.out.println("Error reading snapshot in update");
                System.out.println(e.toString());
            }
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
        ArrayList<HeroInfo> players = logic.getPlayers();

        for(HeroInfo player : players){
            Vector2 pos = player.getPosition();
            Sprite spr = null;
            switch(player.getClassName()){
                case Heavy:
                  spr  = new Sprite(new Texture("src/main/Assets/Heros/Soldier.png"));
                  spr.setSize(100,100);
                  break;
                case Soldier:
                    spr  = new Sprite(new Texture("src/main/Assets/Heros/Heavy.png"));
                    spr.setSize(100,100);
                    break;
            }


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
