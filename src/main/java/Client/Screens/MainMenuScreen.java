package Client.Screens;

import Client.GemGrab;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import org.lwjgl.opengl.GL20;
import Game.PlayerClasses.ClassName;


public class MainMenuScreen implements Screen {
    final GemGrab game;
    private Stage stage;
    private Skin skin;

    private int START_POSITIONX;
    private int START_POSITIONY;

    private int COL_W = 150;
    private int COL_H = 45;

    TextButton startButton;
    TextButton classButton;

    private ClassName[] classes;
    private int currentClassChoice;

    public MainMenuScreen(GemGrab game) {
        this.game = game;
        this.stage = new Stage(game.camera.getViewport());
        Gdx.input.setInputProcessor(stage);
        START_POSITIONX = game.WIDTH / 2;
        START_POSITIONY = game.HEIGHT / 2;


        skin = new Skin(Gdx.files.internal("src/main/Skins/Default/uiskin.json"));

        startButton = new TextButton("START", skin);
        startButton.setSize(COL_W, COL_H);
        startButton.setPosition(START_POSITIONX - startButton.getWidth() / 2, START_POSITIONY);
        stage.addActor(startButton);

        startButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new QueueScreen(game, getChoice() ));
            }
        });


        classes = ClassName.values();
        currentClassChoice = 0;
        classButton = new TextButton(nextClass(), skin);
        classButton.setSize(COL_W, COL_H);
        classButton.setPosition(START_POSITIONX - startButton.getWidth() / 2, START_POSITIONY - COL_H * 2);
        classButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                classButton.setText(nextClass());
            }
        });
        stage.addActor(classButton);
    }

    public String nextClass() {
        if (currentClassChoice < classes.length-1) {
            return classes[++currentClassChoice].name();
        } else {
            currentClassChoice = -1;
            return classes[++currentClassChoice].name();
        }
    }

    public ClassName getChoice() {
        return classes[currentClassChoice];
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update(delta);

        stage.draw();

        game.batch.begin();


        game.batch.end();

    }

    public void update(float delta) {
        stage.act(delta);

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, false);
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
