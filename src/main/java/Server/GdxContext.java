package Server;

import com.badlogic.gdx.Game;


public class GdxContext extends Game {

    @Override
    public void create() {
        QueueServer qs = new QueueServer();
        qs.run();

    }
}
