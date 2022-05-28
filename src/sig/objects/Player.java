package sig.objects;

import sig.RabiClone;
import sig.engine.Object;
import sig.engine.Panel;
import sig.engine.Sprite;

public class Player extends Object{

    public Player(Panel panel) {
        super(panel);
        this.setSprite(Sprite.NANA_SMALL);
        setX(Math.random()*RabiClone.BASE_WIDTH);
        setY(Math.random()*RabiClone.BASE_HEIGHT);
    }


    @Override
    public void update(double updateMult) {
    }

    @Override
    public void draw(int[] p) {
        Draw_Sprite(this.getX(), this.getY(), this.getSprite());
    }
    
}
