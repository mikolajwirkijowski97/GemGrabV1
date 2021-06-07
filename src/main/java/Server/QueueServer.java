package Server;

import Game.PlayerClasses.ClassNames;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Queue;


public class QueueServer implements Runnable {
    private Queue<Socket> queuedConnections;
    private ServerSocket QServer;
    private int lobbyPort = 8181;
    private int qPort = 8080;
    private ArrayList<Thread> lobbyThreads;

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
                    QueueMessage pCountMsg = new QueueMessage(queuedPlayers.size(), " ",
                            ClassNames.Soldier, QueueMessageType.UPDATEPLAYERCOUNT); // #TODO dodać konstruktory bez niepotrzebnych pól

                    sendMessageTo(queuedPlayers, pCountMsg);
                }

                //dodanie graczy z poczekalni do lobby
                ServerSocket lobbySocket = new ServerSocket(lobbyPort);
                ArrayList<ServerConnectedPlayer> lobbyPlayers = new ArrayList<>();
                for (ServerConnectedPlayer player : queuedPlayers) {
                    Socket newPlayerLobbySocket = lobbySocket.accept();
                    ServerConnectedPlayer newPlayer = new ServerConnectedPlayer(newPlayerLobbySocket);
                    lobbyPlayers.add(newPlayer);
                    QueueMessage pCountMsg = new QueueMessage(queuedPlayers.size(), " ",
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
            ObjectOutputStream out = player.getOut();
            out.writeObject(msg);
        }

    }
}
