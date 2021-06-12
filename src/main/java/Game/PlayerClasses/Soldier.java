package Game.PlayerClasses;

import Game.Player;


public class Soldier extends Player {

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
}
