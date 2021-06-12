package Game.PlayerClasses;

import Game.Player;


public class Heavy extends Player {

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
}
