package Client.Screens;

import Client.GemGrab;
import Client.QueueService;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.lwjgl.opengl.GL20;
import lombok.RequiredArgsConstructor;

import java.io.IOException;


public class QueueScreen implements Screen {
    final GemGrab game;
    BitmapFont font = new BitmapFont();
    QueueService service;

    public QueueScreen(GemGrab game){
        this.game = game;
        service = null;
        try{
            service = new QueueService();
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
        font.draw(game.batch, "Players: "+String.valueOf(playerNumber)+"/6", game.WIDTH/2, game.HEIGHT/2);

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
