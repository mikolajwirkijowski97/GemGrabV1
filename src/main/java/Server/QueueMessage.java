package Server;

import Game.Player;
import Game.PlayerClasses.ClassNames;
import java.io.Serializable;
import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class QueueMessage implements Serializable {

    @Getter @Setter private int playerCount;
    @Getter @Setter private String nick;
    @Getter @Setter private ClassNames className;
    @Getter @Setter private QueueMessageType messageType;
}
