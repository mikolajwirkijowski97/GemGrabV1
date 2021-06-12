package Client;

import Client.Screens.MainMenuScreen;
import Client.Screens.QueueScreen;
import Client.Tools.GameCamera;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class GemGrab extends Game {
    public static final int WIDTH = 960;
    public static final int HEIGHT = 640;

    public GameCamera camera;
    public SpriteBatch batch;

    public BitmapFont font;

    @Override
    public void create() {
        batch = new SpriteBatch();
        camera = new GameCamera(WIDTH, HEIGHT);
        font = new BitmapFont();

        this.setScreen(new MainMenuScreen(this));
    }

    @Override
    public void render () {
        batch.setProjectionMatrix(camera.combined());
        super.render();
    }


    @Override
    public void resize(int width, int height) {
        camera.update(width, height);
        super.resize(width, height);
    }
}
