package sig.objects;

import sig.RabiClone;
import sig.engine.Object;
import sig.engine.Panel;
import sig.engine.Sprite;

public class Player extends Object{

    public Player(Panel panel) {
        super(panel);
        this.setSprite(Sprite.NANA_SMALL);
        setX(RabiClone.BASE_WIDTH/2-getSprite().getWidth()/2);
        setY(RabiClone.BASE_HEIGHT*(2/3d)-getSprite().getHeight()/2);
    }


    @Override
    public void update(double updateMult) {
    }

    @Override
    public void draw(int[] p) {
        Draw_Sprite(this.getX(), this.getY(), this.getSprite());
    }
    
}
