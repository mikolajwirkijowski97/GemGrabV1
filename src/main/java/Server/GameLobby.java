package Server;

import Client.GemGrab;
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
    private ArrayList<HeroInfo> players;
    private final ServerSocket server;
    private Boolean alive;
    private GameHandler logic;
    private long timer = 0;
    private GemGrab game;

    public GameLobby(ArrayList<ServerConnectedPlayer> playersConnection, ServerSocket socket) throws IOException {
        this.game = new GemGrab();
        this.alive = true;
        this.playersConnection = playersConnection;
        this.server = socket;
        this.players = new ArrayList<HeroInfo>();
        createHeroes();
        TiledMap map = new TmxMapLoader().load("src/main/Assets/Tiles/gemgrab.tmx");
        this.logic = new GameHandler(players, 30, (TiledMapTileLayer) map.getLayers().get("Collision"));
    }

    public void run() {
        createHeroes();
        sendSnapshot();

        boolean running = true;

        long last_time = System.nanoTime();
        while(!Thread.interrupted() && running ){
            long time = System.nanoTime();
            if(timer>=1/ logic.getTickrate()){
                logic.update();
                sendSnapshot();
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

            HeroInfo newHero = null;
            Hero tmp;
            switch (player.getSelectedClass()) {
                case Heavy:
                    tmp = new Heavy(player.getUid(), team);
                    newHero = new HeroInfo(tmp);
                    break;
                case Soldier:
                    tmp = new Soldier(player.getUid(), team);
                    newHero = new HeroInfo(tmp);
                    break;

            }
            players.add(newHero);
        }
    }

    private void sendSnapshot(){
        ArrayList<HeroInfo> snapshot = logic.getPlayers();

        SnapshotMessage snapshotMessage = new SnapshotMessage(snapshot);
        for(ServerConnectedPlayer player: playersConnection){
            try{
                sendMessageTo(player, snapshotMessage);
            }
            catch(IOException e){
                System.out.println("SendSnapshot IOEXCEPTION"+e.toString());
            }

        }



    }
    public void sendMessageTo(ServerConnectedPlayer player, SnapshotMessage msg) throws IOException {
        ObjectOutputStream out = player.getOut();
        out.writeObject(msg);
    }

    private ServerConnectedPlayer getPlayerConnection(int uid){
        for(ServerConnectedPlayer connection : playersConnection){
            if(connection.getUid() == uid) {return connection;}
        }
        return null;
    }
}
