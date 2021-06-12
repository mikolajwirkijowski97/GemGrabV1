package Client.Screens;
import Client.GemGrab;
import Client.QueueService;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import org.lwjgl.opengl.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import java.io.IOException;
import java.util.ArrayList;


public class MainMenuScreen implements Screen {
    final GemGrab game;
    private Stage stage;
    private Skin skin;
    TextureAtlas buttonAtlas;
    TextButton button;
    TextButtonStyle textButtonStyle;

    public MainMenuScreen(GemGrab game){
        this.game = game;
        this.stage = new Stage(game.camera.getViewport());
        Gdx.input.setInputProcessor(stage);


        skin = new Skin();
        buttonAtlas = new TextureAtlas(Gdx.files.internal("buttons/buttons.pack"));
        skin.addRegions(buttonAtlas);
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = game.font;
        textButtonStyle.up = skin.getDrawable("up-button");
        textButtonStyle.down = skin.getDrawable("down-button");
        textButtonStyle.checked = skin.getDrawable("checked-button");
        button = new TextButton("Button1", textButtonStyle);
        stage.addActor(button);

    }
    public void createButtons(){

    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(1,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

        stage.draw();

        game.batch.begin();





        game.batch.end();

    }

    public void update(float delta){
        stage.act(delta);

    }
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width,height,false);
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
