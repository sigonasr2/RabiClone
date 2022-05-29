package sig.objects;

import sig.RabiClone;
import sig.engine.Object;
import sig.engine.Panel;
import sig.engine.Sprite;

public class Player extends Object{
    double y_acceleration = 20;
    double x_acceleration = 0;
    double x_velocity = 0;
    double y_velocity = -4;

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
        this.setY(this.getY()+displacement);
    }

    @Override
    public void draw(int[] p) {
        Draw_Sprite(RabiClone.BASE_WIDTH/2-getSprite().getWidth()/2,RabiClone.BASE_HEIGHT*(2/3d)-getSprite().getHeight()/2, this.getSprite());
    }
    
}
