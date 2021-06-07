package Server;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Queue;


public class QueueServer implements Runnable{
    private Queue<Socket> queuedConnections;
    private ServerSocket QServer;
    private int lobbyPort = 8181;
    private int  qPort = 8080;
    private ArrayList<Thread> lobbyThreads;

    public void run()
    {
        while(!Thread.interrupted()){
            try{
                ArrayList<ServerConnectedPlayer> queuedPlayers = new ArrayList<>();
                while(queuedPlayers.size() < 6 ){
                    Socket newPlayerSocket = QServer.accept();
                    ServerConnectedPlayer newPlayer = new ServerConnectedPlayer(newPlayerSocket);
                    queuedPlayers.add(newPlayer);

                    for(ServerConnectedPlayer player : queuedPlayers){
                        ObjectOutputStream out =player.getOut();
                        QueueMessage pCountMsg = new QueueMessage(queuedPlayers.size(),newPlayer.getPNick(),
                                newPlayer.getSelectedClass(),QueueMessageType.UPDATEPLAYERCOUNT);
                        out.writeObject(pCountMsg);
                    }
                }

                ServerSocket lobbySocket = new ServerSocket(lobbyPort);
                ArrayList<ServerConnectedPlayer> lobbyPlayers = new ArrayList<>();
                for(ServerConnectedPlayer player:queuedPlayers){
                    Socket newPlayerLobbySocket = lobbySocket.accept();
                    ServerConnectedPlayer newPlayer = new ServerConnectedPlayer(newPlayerLobbySocket);
                    

                }

                Thread newLobby = new Thread(GameLobby())


            }
            catch(IOException|ClassNotFoundException e){
                System.out.println(e.toString());
            }
        }


    }
}
