package Game;

import Game.Team;
import lombok.Getter;
import lombok.Setter;
import lombok.RequiredArgsConstructor;
import java.awt.geom.Point2D;

@RequiredArgsConstructor
public abstract class Player {
    @Getter private final int id;

    @Getter @Setter private Point2D pos;

    @Getter @Setter private double rotation;

    @Getter private final int baseHp;
    @Getter @Setter private int hp;
    @Getter @Setter private final Team team;

    private final double attack_cooldown;
    @Getter @Setter private double attack_timer;

    private final double ult_cooldown;
    @Getter @Setter private double ult_timer;

    private final Game game;


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
