package Client.Screens;

import Client.GemGrab;
import Client.QueueService;
import Game.PlayerClasses.ClassName;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.lwjgl.opengl.GL20;
import lombok.RequiredArgsConstructor;

import java.io.IOException;


public class QueueScreen implements Screen {
    final GemGrab game;
    QueueService service;
    ClassName classChoice;

    public QueueScreen(GemGrab game, ClassName classChoice){
        this.game = game;
        this.classChoice = classChoice;
        service = null;

        try{
            service = new QueueService(classChoice);
            Thread serviceThread = new Thread(service);
            serviceThread.start();
            System.out.println("Service thread started");
        }
        catch(IOException e){
            System.out.println(e.toString());
        }
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(1,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();

        int playerNumber = service.getQueueCount();
        game.font.draw(game.batch, "Waiting for players.",game.WIDTH/2-10,game.HEIGHT/2+100);
        game.font.draw(game.batch, "Players: "+String.valueOf(playerNumber)+"/4", game.WIDTH/2, game.HEIGHT/2);

        game.batch.end();

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

    }
}
