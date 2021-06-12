package Server;

import Game.PlayerClasses.ClassName;
import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class QueueMessage implements Serializable {

    @Getter @Setter private int playerCount;
    @Getter @Setter private int port;
    @Getter @Setter private String nick;
    @Getter @Setter private ClassName className;
    @Getter @Setter private QueueMessageType messageType;
}
