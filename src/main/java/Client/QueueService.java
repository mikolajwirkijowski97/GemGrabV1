package Client;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import lombok.extern.java.Log;
import static java.lang.Thread.sleep;
import Server.QueueMessage;
@Log
public class QueueService {
     private int queueCount;
     private int qPort = 8080;
     private String host = "localhost";
     private Socket socket;
     private int retryCount = 10;

    @Getter final private ObjectOutputStream out;
    @Getter final private ObjectInputStream in;

     //Socket Connection handled in this constructor, bad connection handled here, stream creation throws to caller
     public QueueService() throws IOException{
         for(int i=0;i<retryCount;i++){
             try{
                 socket = new Socket(host,qPort);
                 break;
             }catch(IOException ex){
                 try{
                     sleep(1000);
                     System.out.println("Server offline, will retry in a moment.");
                 }catch(InterruptedException ex2){ log.log(Level.WARNING,ex2.getMessage(),ex2); }
             }
         }
         out = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
         in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
     }

     public int getQueueCount() throws ClassNotFoundException,IOException{
         QueueMessage serverMessage = (QueueMessage)in.readObject();
         return serverMessage.getPlayerCount();
     }

}
