package Game;
import Game.Player;

import java.awt.geom.Point2D;


public class Game {

    Point2D teamBlueLoc;
    Point2D teamRedLoc;


    public Point2D getRespawnLoc(Team team){
        if(team== Team.TEAMRED) return teamRedLoc;
        else return teamBlueLoc;
    }
}
