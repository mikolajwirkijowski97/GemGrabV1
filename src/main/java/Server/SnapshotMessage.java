package Server;

import Game.Hero;
import Game.PlayerClasses.ClassName;
import java.io.Serializable;
import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SnapshotMessage implements Serializable {
    @Getter private ArrayList<HeroInfo> players;

}
