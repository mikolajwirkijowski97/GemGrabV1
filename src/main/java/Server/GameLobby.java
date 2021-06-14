package Server;

import Game.GameHandler;
import Game.Hero;
import Game.PlayerClasses.Heavy;
import Game.PlayerClasses.Soldier;
import Game.Team;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import org.lwjgl.system.CallbackI;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class GameLobby implements Runnable {
    private final ArrayList<ServerConnectedPlayer> playersConnection;
    private ArrayList<Hero> players;
    private final ServerSocket server;
    private Boolean alive;
    private GameHandler logic;
    private long timer = 0;

    public GameLobby(ArrayList<ServerConnectedPlayer> playersConnection, ServerSocket socket) throws IOException {
        this.alive = true;
        this.playersConnection = playersConnection;
        this.server = socket;
        createHeroes();
        TiledMap map = new TmxMapLoader().load("src/main/Assets/Tiles/gemgrab.tmx");
        this.logic = new GameHandler(players, 30, (TiledMapTileLayer) map.getLayers().get("Collision"));
    }

    public void run() {
        createHeroes();

        boolean running = true;

        long last_time = System.nanoTime();
        while(!Thread.interrupted() && running ){
            long time = System.nanoTime();
            if(timer>=1/60){
                logic.update();;
            }

            timer += time - last_time/1000000000;
            last_time = time;
        }

    }

    public void createHeroes() {
        int teamswitch = playersConnection.size() / 2 - 1;
        Team team = Team.TEAMBLUE;
        for (ServerConnectedPlayer player : playersConnection) {
            teamswitch--;
            if (teamswitch == 0) {
                team = Team.TEAMRED;
            }

            Hero newHero = null;
            switch (player.getSelectedClass()) {
                case Heavy:
                    newHero = new Heavy(player.getUid(), team);
                    break;
                case Soldier:
                    newHero = new Soldier(player.getUid(), team);
                    break;

            }
            players.add(newHero);
        }
    }

    private void sendSnapshot(){
        ArrayList<Hero> snapshot = logic.getPlayers();
        SnapshotMessage snapshotMessage = new SnapshotMessage(players);



    }
}
