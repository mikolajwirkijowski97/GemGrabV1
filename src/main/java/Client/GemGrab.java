package Client;

import Client.Tools.GameCamera;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class GemGrab extends Game {
    public static final int WIDTH = 960;
    public static final int HEIGHT = 640;

    public GameCamera cam;
    public SpriteBatch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();
        cam = new GameCamera(WIDTH, HEIGHT);


        this.setScreen(new QueueScreen(this));
    }

    @Override
    public void render () {
        batch.setProjectionMatrix(cam.combined());
        super.render();
    }


    @Override
    public void resize(int width, int height) {
        cam.update(width, height);
        super.resize(width, height);
    }
}
