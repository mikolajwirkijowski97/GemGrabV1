package Game;

import Game.Team;
import lombok.Getter;
import lombok.Setter;

import java.awt.geom.Point2D;


@Getter
@Setter
public abstract class Player {

    @Getter @Setter private Point2D pos;

    @Getter @Setter private double rotation;

    @Getter @Setter private int hp;
    @Getter @Setter private Team team;

    private double attack_cooldown;
    @Getter @Setter private double attack_timer;

    private double ult_cooldown;
    @Getter @Setter private double ult_timer;

    private Game game;


    protected abstract Boolean use_attack();

    protected abstract Boolean use_ult();

    protected abstract void resetHp();

    public void takeDamage(int dmg) {
        hp -= dmg;
    }

    protected void die() {
        setPos(game.getRespawnLoc(team));
    }


}
