package sig.objects;

import sig.RabiClone;
import sig.engine.Object;
import sig.engine.Panel;
import sig.engine.Sprite;
import sig.map.CollisionType;
import sig.map.Tile;

import java.awt.event.KeyEvent;

public class Player extends Object{
    double y_acceleration = 1;
    double y_acceleration_limit = 100;
    double x_acceleration = 0;
    double x_acceleration_limit = 100;
    double x_velocity = 0;
    double x_velocity_limit = 128;
    double y_velocity = 5;
    double y_velocity_limit = 192;

    double horizontal_drag = 2000;
    double horizontal_friction = 500;
    double horizontal_air_drag = 100;
    double horizontal_air_friction = 7;

    public Player(Panel panel) {
        super(panel);
        this.setSprite(Sprite.NANA_SMALL);
        setX(RabiClone.BASE_WIDTH/2-getSprite().getWidth()/2);
        setY(RabiClone.BASE_HEIGHT*(2/3d)-getSprite().getHeight()/2);
    }


    @Override
    public void update(double updateMult) {
        int right = KeyHeld(KeyEvent.VK_RIGHT)?1:0;
        int left = KeyHeld(KeyEvent.VK_LEFT)?1:0;


        x_velocity = 
            Math.abs(x_velocity+x_acceleration*updateMult)>x_velocity_limit
                ?Math.signum(x_velocity+x_acceleration*updateMult)*x_velocity_limit
                :x_velocity+x_acceleration*updateMult;
        y_velocity =
            Math.abs(y_velocity+y_acceleration*updateMult)>y_velocity_limit
                ?Math.signum(y_velocity+y_acceleration*updateMult)*y_velocity_limit
                :y_velocity+y_acceleration*updateMult;
        double displacement = y_velocity*updateMult;
        boolean groundCollision = false;
        for(int i=0;i<4;i++){
            double check_distance = (displacement/4)*(i+1);
            Tile checked_tile_top = RabiClone.CURRENT_MAP.getTile((int)getX()/Tile.TILE_WIDTH, (int)(getY()+check_distance)/Tile.TILE_HEIGHT);
            Tile checked_tile_bottom_right = RabiClone.CURRENT_MAP.getTile((int)(getX()+getSprite().getWidth()/2)/Tile.TILE_WIDTH, (int)(getY()+getSprite().getHeight()/2+check_distance)/Tile.TILE_HEIGHT);
            Tile checked_tile_bottom_left = RabiClone.CURRENT_MAP.getTile((int)(getX()-getSprite().getWidth()/2)/Tile.TILE_WIDTH, (int)(getY()+getSprite().getHeight()/2+check_distance)/Tile.TILE_HEIGHT);
            //System.out.println((int)getX()/Tile.TILE_WIDTH);
            if(checked_tile_bottom_right.getCollision()==CollisionType.BLOCK||checked_tile_bottom_left.getCollision()==CollisionType.BLOCK){
                setY((getY()+check_distance));
                y_acceleration = 0;
                y_velocity = 0;
                groundCollision=true;
                break;
            }
        }
        if (!groundCollision){
            this.setY(this.getY()+displacement);
            handleKeyboardMovement(updateMult, right-left, horizontal_air_friction, horizontal_air_drag);
        } else {
            handleKeyboardMovement(updateMult, right-left, horizontal_friction, horizontal_drag);
        }
        this.setX(this.getX()+x_velocity*updateMult);
    }


    private void handleKeyboardMovement(double updateMult, int movement, double friction, double drag) {
        if (movement!=0) {
            x_acceleration=drag*(movement);
        } else {
            if (x_velocity!=0) {
                x_velocity=x_velocity>0
                    ?x_velocity-friction*updateMult>0
                        ?x_velocity-friction*updateMult
                        :0
                    :x_velocity+friction*updateMult<0
                        ?x_velocity+friction*updateMult
                        :0;
            }
            x_acceleration=0;
        }
    }

    @Override
    public void draw(byte[] p) {
        Draw_Sprite(RabiClone.BASE_WIDTH/2-getSprite().getWidth()/2,RabiClone.BASE_HEIGHT*(2/3d)-getSprite().getHeight()/2, this.getSprite());
    }
    
}
