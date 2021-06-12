package Client;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;


public class DesktopLauncher {
    public static void main (String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("GEM GRAB");
        config.setIdleFPS(60);
        config.useVsync(true);

        config.setWindowedMode(960,640);
        new Lwjgl3Application(new GemGrab(),config);

    }
}