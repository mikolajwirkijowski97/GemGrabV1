package Server;
import Game.PlayerClasses.ClassNames;
import java.io.*;
import java.net.Socket;
import lombok.Getter;
import lombok.Setter;

public class ServerConnectedPlayer {
    @Getter @Setter final private String pNick;
    @Getter @Setter private ClassNames selectedClass;
    @Getter @Setter private Socket pSocket;
    @Getter final private ObjectOutputStream out;
    @Getter final private ObjectInputStream in;

    public ServerConnectedPlayer(Socket socket) throws IOException,ClassNotFoundException {

        pSocket = socket;

        out = new ObjectOutputStream(new BufferedOutputStream(pSocket.getOutputStream()));
        in = new ObjectInputStream(new BufferedInputStream(pSocket.getInputStream()));
        QueueMessage msg;
        do{
            msg = (QueueMessage)in.readObject();

        }while(msg.getMessageType() != QueueMessageType.USERMSG);

        pNick = msg.getNick();
        selectedClass = msg.getClassName();


    }
}
