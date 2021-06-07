package Server;

import java.io.*;
import java.util.ArrayList;


public class GameLobby implements Runnable{
    private final ArrayList<ServerConnectedPlayer> players;
    private final ArraBufferedReader in;
    private final PrintWriter out;

    public GameLobby(ArrayList<ServerConnectedPlayer> players, int port) throws IOException {
        this.playerSockets = players;

        in = new BufferedReader(new InputStreamReader(playerSockets.getInputStream()));
        out = new PrintWriter(new OutputStreamWriter(playerSockets.getOutputStream()));


    }

    public void run() {


    }
}
