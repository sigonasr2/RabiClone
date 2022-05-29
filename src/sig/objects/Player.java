package sig.objects;

import sig.RabiClone;
import sig.engine.Object;
import sig.engine.Panel;
import sig.engine.Sprite;
import sig.map.CollisionType;
import sig.map.Tile;

public class Player extends Object{
    double y_acceleration = 15;
    double x_acceleration = 0;
    double x_velocity = 0;
    double y_velocity = 5;

    public Player(Panel panel) {
        super(panel);
        this.setSprite(Sprite.NANA_SMALL);
        setX(RabiClone.BASE_WIDTH/2-getSprite().getWidth()/2);
        setY(RabiClone.BASE_HEIGHT*(2/3d)-getSprite().getHeight()/2);
    }


    @Override
    public void update(double updateMult) {
        y_velocity += y_acceleration*updateMult;
        double displacement = y_velocity*updateMult;
        boolean is_collision = false;
        for(int i=0;i<4;i++){
            double check_distance = (displacement/4)*(i+1);
            Tile checked_tile_top = RabiClone.CURRENT_MAP.getTile((int)getX()/Tile.TILE_WIDTH, (int)(getY()+check_distance)/Tile.TILE_HEIGHT);
            Tile checked_tile_bottom = RabiClone.CURRENT_MAP.getTile((int)(getX()+getSprite().getWidth())/Tile.TILE_WIDTH, (int)(getY()+getSprite().getHeight()+check_distance)/Tile.TILE_HEIGHT);
            //System.out.println((int)getX()/Tile.TILE_WIDTH);
            if(checked_tile_bottom.getCollision()==CollisionType.BLOCK){
                setY((getY()+check_distance)+getSprite().getHeight()/2);
                y_acceleration = 0;
                y_velocity = 0;
                is_collision=true;
                break;
            }
        }
        if (!is_collision){
            this.setY(this.getY()+displacement);
        }
    }

    @Override
    public void draw(int[] p) {
        Draw_Sprite(RabiClone.BASE_WIDTH/2-getSprite().getWidth()/2,RabiClone.BASE_HEIGHT*(2/3d)-getSprite().getHeight()/2, this.getSprite());
    }
    
}
