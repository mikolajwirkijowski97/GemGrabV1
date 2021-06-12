package Game;
import Game.Player;

import java.awt.geom.Point2D;
import java.util.ArrayList;


public class Game {

    Point2D teamBlueLoc;
    Point2D teamRedLoc;
    ArrayList<Player> players;

    public Point2D getRespawnLoc(Team team){
        if(team== Team.TEAMRED) return teamRedLoc;
        else return teamBlueLoc;
    }
}
