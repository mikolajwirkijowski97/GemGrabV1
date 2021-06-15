package Server;

import Client.GemGrab;
import Game.PlayerClasses.ClassName;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.ArrayList;


public class QueueServer implements Runnable {
    private ServerSocket QServer;
    private int lobbyPort = 8181;
    private int qPort = 8080;
    private ArrayList<Thread> lobbyThreads;

    public QueueServer(){
        try{
            QServer = new ServerSocket(qPort);
        }
        catch(IOException e){
            System.out.println(e.toString());
        }
        this.lobbyThreads = new ArrayList<Thread>();

    }

    public static void main (String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("GEM GRAB");
        config.setIdleFPS(60);
        config.useVsync(true);

        config.setWindowedMode(1,1);
        new Lwjgl3Application(new GdxContext(),config);

    }
    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                ArrayList<ServerConnectedPlayer> queuedPlayers = new ArrayList<>();
                while (queuedPlayers.size() < 4) {
                    //dodanie gracza do poczekalni
                    Socket newPlayerSocket = QServer.accept();
                    System.out.println("Connection from user accepted");
                    ServerConnectedPlayer newPlayer = new ServerConnectedPlayer(newPlayerSocket);
                    queuedPlayers.add(newPlayer);

                    //wysłanie wiadomości o ilości osób w poczekalni
                    System.out.println("Count msg sent");
                    QueueMessage pCountMsg = new QueueMessage(queuedPlayers.size(),0, 0,
                            ClassName.Soldier, QueueMessageType.UPDATEPLAYERCOUNT); // #TODO dodać konstruktory bez niepotrzebnych pól

                    sendMessageTo(queuedPlayers, pCountMsg);
                }

                //dodanie graczy z poczekalni do lobby
                ServerSocket lobbySocket = new ServerSocket(lobbyPort);
                ArrayList<ServerConnectedPlayer> lobbyPlayers = new ArrayList<>();

                for (ServerConnectedPlayer player : queuedPlayers) {
                    //wiadomość do gracza z prośbą połączenia się z lobby
                    QueueMessage connectionMsg = new QueueMessage(0,lobbyPort,0, ClassName.Soldier,QueueMessageType.CONNECTTOLOBBY);
                    sendMessageTo(player, connectionMsg);
                    System.out.println("connectionmsg sent");
                    //socket gracz<->lobby
                    Socket newPlayerLobbySocket = lobbySocket.accept();
                    System.out.println("lobbysocket accepted");


                    ServerConnectedPlayer newPlayer = new ServerConnectedPlayer(newPlayerLobbySocket,player.getUid(),player.getSelectedClass());
                    System.out.println("Created ServerConnectedPlayer instance");
                    lobbyPlayers.add(newPlayer);

                }

                Thread newLobby = new Thread(new GameLobby(lobbyPlayers,lobbySocket));
                newLobby.start();
                lobbyThreads.add(newLobby);


            } catch (IOException | ClassNotFoundException e) {
                System.out.println(e.toString());
            }
        }
    }

    public void sendMessageTo(ArrayList<ServerConnectedPlayer> players, QueueMessage msg) throws IOException {
        for (ServerConnectedPlayer player : players) {
            sendMessageTo(player, msg);
        }

    }

    public void sendMessageTo(ServerConnectedPlayer player, QueueMessage msg) throws IOException {
            ObjectOutputStream out = player.getOut();
            out.writeObject(msg);
    }
}
