package Server;
import Game.PlayerClasses.ClassName;
import java.io.*;
import java.net.Socket;
import lombok.Getter;
import lombok.Setter;

public class ServerConnectedPlayer {
    @Getter @Setter final private String pNick;
    @Getter @Setter private ClassName selectedClass;
    @Getter @Setter private Socket pSocket;
    @Getter final private ObjectOutputStream out;
    @Getter final private ObjectInputStream in;

    public ServerConnectedPlayer(Socket socket) throws IOException,ClassNotFoundException {

        pSocket = socket;

        out = new ObjectOutputStream(pSocket.getOutputStream());
        in = new ObjectInputStream(pSocket.getInputStream());
        QueueMessage msg;
        do{
            msg = (QueueMessage)in.readObject();

        }while(msg.getMessageType() != QueueMessageType.USERMSG);

        pNick = msg.getNick();
        selectedClass = msg.getClassName();
        System.out.println("player : "+pNick+" class: "+selectedClass.name());

    }
}
