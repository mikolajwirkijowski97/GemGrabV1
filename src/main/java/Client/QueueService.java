package Client;
import lombok.Getter;
import Game.PlayerClasses.ClassNames;
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
     private String host = "localhost";
     private Socket socket;
    @Getter final private ObjectOutputStream out;
    @Getter final private ObjectInputStream in;

     //Socket Connection handled in this constructor, bad connection handled here, stream creation throws to caller
     public QueueService()  throws IOException {
         queueCount = 0;

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
         QueueMessage msg = new QueueMessage(0,0,"lupsik",ClassNames.Soldier,QueueMessageType.USERMSG);
         sendMessage(msg);
        while(queueCount < 6){
            try{
                updateQueueCount();
                System.out.println(getQueueCount());
                System.out.println("MESSAGE OK");

            }catch(IOException|ClassNotFoundException e){
                System.out.println("Couldnt get QueueCount due to :");
                System.out.println(e.toString());
            }
        }
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
