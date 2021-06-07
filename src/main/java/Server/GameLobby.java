package Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class GameLobby implements Runnable{
    private final ArrayList<ServerConnectedPlayer> players;
    private final ServerSocket server;
    private Boolean alive;

    public GameLobby(ArrayList<ServerConnectedPlayer> players, ServerSocket socket) throws IOException {
        this.alive = true;
        this.players = players;
        this.server = socket;
    }

    public void run() {


    }
}
