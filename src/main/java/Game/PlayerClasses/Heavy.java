package Game.PlayerClasses;
import Game.GameHandler;
import Game.Team;
import Game.Hero;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;


public class Heavy extends Hero {

    @Override
    protected Boolean use_attack(){
        return true;
    }

    @Override
    protected Boolean use_ult() {
        return null;
    }

    @Override
    protected void resetHp(){ }

    public Heavy(int id, Team team){
        super(new Sprite(new Texture("src/main/Assets/Heros/Heavy.png")),id,400,10,
                team, 2,10);
    }

}
