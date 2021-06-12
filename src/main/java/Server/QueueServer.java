package Server;

import Game.PlayerClasses.ClassNames;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Queue;

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

    }
    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                ArrayList<ServerConnectedPlayer> queuedPlayers = new ArrayList<>();
                while (queuedPlayers.size() < 6) {
                    //dodanie gracza do poczekalni
                    Socket newPlayerSocket = QServer.accept();
                    ServerConnectedPlayer newPlayer = new ServerConnectedPlayer(newPlayerSocket);
                    queuedPlayers.add(newPlayer);

                    //wysłanie wiadomości o ilości osób w poczekalni
                    QueueMessage pCountMsg = new QueueMessage(queuedPlayers.size(),0, " ",
                            ClassNames.Soldier, QueueMessageType.UPDATEPLAYERCOUNT); // #TODO dodać konstruktory bez niepotrzebnych pól

                    sendMessageTo(queuedPlayers, pCountMsg);
                }

                //dodanie graczy z poczekalni do lobby
                ServerSocket lobbySocket = new ServerSocket(lobbyPort);
                ArrayList<ServerConnectedPlayer> lobbyPlayers = new ArrayList<>();

                for (ServerConnectedPlayer player : queuedPlayers) {
                    //wiadomość do gracza z prośbą połączenia się z lobby
                    QueueMessage connectionMsg = new QueueMessage(0,lobbyPort," ",ClassNames.Soldier,QueueMessageType.CONNECTTOLOBBY);
                    sendMessageTo(player, connectionMsg);

                    //socket gracz<->lobby
                    Socket newPlayerLobbySocket = lobbySocket.accept();



                    ServerConnectedPlayer newPlayer = new ServerConnectedPlayer(newPlayerLobbySocket);
                    lobbyPlayers.add(newPlayer);
                    QueueMessage pCountMsg = new QueueMessage(queuedPlayers.size(),0, " ",
                            ClassNames.Soldier, QueueMessageType.UPDATEPLAYERCOUNT);

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
