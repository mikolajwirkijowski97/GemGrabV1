package Game.PlayerClasses;

import Game.GameHandler;
import Game.Hero;
import Game.Team;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;


public class Soldier extends Hero {

    @Override
    protected Boolean use_attack(){
        return true;
    }

    @Override
    protected Boolean use_ult() {
        return null;
    }

    @Override
    protected void resetHp(){

    }

    public Soldier(int id, Team team){
        super( new Sprite(new Texture("src/main/Assets/Heros/Soldier.png")),id,200,
                40,team, 1,8);
    }
}
