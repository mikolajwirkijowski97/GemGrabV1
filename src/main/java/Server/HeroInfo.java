package Server;

import Game.Hero;
import Game.PlayerClasses.ClassName;
import Game.Team;
import Game.Action;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;
import lombok.Getter;
import lombok.Setter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;



public  class HeroInfo implements Serializable {

    public HeroInfo(Hero hero) {
        this.className = hero.getClassName();
        this.width = hero.getWidth();
        this.height = hero.getHeight();
        this.id = hero.getId();
        this.baseHp = hero.getBaseHp();
        this.baseSpeed = hero.getBaseSpeed();
        this.team = hero.getTeam();
        this.attack_cooldown = hero.getAttack_cooldown();
        this.ult_cooldown = hero.getUlt_cooldown();
        this.attack_timer = hero.getAttack_timer();
        this.ult_timer = hero.getUlt_timer();

        this.position = hero.getPosition();
        this.velocity = hero.getVelocity();

        this.actions = hero.getActions();
    }


    @Setter @Getter private float increment=0;
    @Getter @Setter private ClassName className;
    @Getter @Setter private Vector2 velocity = new Vector2();
    @Getter @Setter private Vector2 position= new Vector2(150,150);

    @Getter private final float width;
    @Getter private final float height;

    @Getter private final int id;



    @Getter private final int baseHp;
    @Getter private final int baseSpeed;
    @Getter @Setter private int hp;
    @Getter @Setter private final Team team;

    @Getter private final double attack_cooldown;
    @Getter @Setter private double attack_timer=0;

    @Getter private final double ult_cooldown;
    @Getter @Setter private double ult_timer=0;

    private ArrayList<Action> actions = new ArrayList<>();

    public void addAction(Action action){
        System.out.println("Action registered");
        actions.add(action);
    }
    public void doActions(){
        for(Action action : actions){
            switch(action) {
                case UP:
                    velocity.y = baseSpeed;
                    break;
                case DOWN:
                    velocity.y = -baseSpeed;
                    break;
                case LEFT:
                    velocity.x = -baseSpeed;
                    break;
                case RIGHT:
                    velocity.x = baseSpeed;
                    break;
                case STOP:
                    velocity.x = 0;
                    velocity.y = 0;
                case ULT:
                    if(ult_timer == 0){
                        use_ult();
                        ult_timer = ult_cooldown;
                    }
                    break;
                case ATTACK:
                    if(attack_timer == 0){
                        use_attack();
                        attack_timer = attack_cooldown;
                    }

            }

        }
        actions.clear();
    }



    protected  Boolean use_attack(){return true;}

    protected  Boolean use_ult(){return true;}

    protected  void resetHp(){hp=getBaseHp();}

    public void takeDamage(int dmg) {
        hp -= dmg;
    }

    protected void die() {
        hp = baseHp;
    }



    public void setX(float x){
        position.x = x;
    }
    public float getX() {return position.x;}

    public void setY(float y){
        position.y = y;
    }
    public float getY() {return position.y;}




}
