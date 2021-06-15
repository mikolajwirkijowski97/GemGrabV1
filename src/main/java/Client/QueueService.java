package Client;
import Client.Screens.GameScreen;
import Game.GameHandler;
import lombok.Getter;
import Game.PlayerClasses.ClassName;
import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import lombok.extern.java.Log;
import static java.lang.Thread.sleep;
import Server.QueueMessage;
import Server.QueueMessageType;
@Log
public class QueueService implements Runnable {
     @Getter private int queueCount;

     private int retryCount = 10;
     private int qPort = 8080;
     private int lPort = 8181;
     private String host = "localhost";
     private Socket socket;
     private GemGrab game;
     private ClassName classChoice;
    @Getter final private ObjectOutputStream out;
    @Getter final private ObjectInputStream in;

     //Socket Connection handled in this constructor, bad connection handled here, stream creation throws to caller
     public QueueService(ClassName classChoice,GemGrab game)  throws IOException {
         queueCount = 0;
         this.classChoice = classChoice;
         this.game = game;
         for(int i=0;i<retryCount;i++){
             try{
                 socket = new Socket(host,qPort);
                 System.out.println("Connection success.");
                 break;
             }catch(IOException ex){
                 try{
                     sleep(1000);
                     System.out.println("Server offline, will retry in a moment.");
                 }catch(InterruptedException ex2){ log.log(Level.WARNING,ex2.getMessage(),ex2); }
             }
         }
         System.out.println("Initialising streams");
         out = new ObjectOutputStream(socket.getOutputStream());
         System.out.println("Initialising streams...");
         in = new ObjectInputStream(socket.getInputStream());
         System.out.println("QueueService initialised success");
     }

     public void updateQueueCount() throws ClassNotFoundException, IOException{
         System.out.println("Blocking thread func:getQueueCount()");
         QueueMessage serverMessage = (QueueMessage)in.readObject();
         System.out.println("Unblocked thread func:getQueueCount()");
         queueCount = serverMessage.getPlayerCount();
     }

    @Override
    public void run() {
         System.out.println("Sending user info");
         QueueMessage msg = new QueueMessage(0,0,game.getUid(), classChoice,QueueMessageType.USERMSG);
         sendMessage(msg);
        while(queueCount < 4){
            try{
                updateQueueCount();
                System.out.println(getQueueCount());
                System.out.println("MESSAGE OK");

            }catch(IOException|ClassNotFoundException e){
                System.out.println("Couldnt get QueueCount due to :");
                System.out.println(e.toString());
            }
        }
        QueueMessage connectionMessage = null;
        Socket lobbySocket = null;
        try{
            System.out.println("Waiting for connection message");
            connectionMessage = (QueueMessage)in.readObject();
            System.out.println("Received connection message");
            lobbySocket = new Socket(host,lPort);

        }catch(IOException|ClassNotFoundException e){
            System.out.println("Couldnt get receive connection message due to :");
            System.out.println(e.toString());
        }

        game.setScreen(new GameScreen(game,lobbySocket));

    }

    public void sendMessage(QueueMessage msg) {
        try{
         out.writeObject(msg);
        }
        catch(IOException e){
            System.out.println("send message failed");
            System.out.println(e.toString());
        }
    }
}
