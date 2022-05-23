package sig.objects;

import sig.DrawLoop;
import sig.engine.Object;
import sig.engine.Panel;
import sig.engine.Sprite;
import java.awt.event.KeyEvent;

public class Player extends Object{

    public Player(Panel panel) {
        super(panel);
        this.setSprite(Sprite.NANA);
        setX(Math.random()*panel.getWidth());
        setY(Math.random()*panel.getHeight());
    }


    @Override
    public void update(double updateMult) {
        if (KeyHeld(KeyEvent.VK_RIGHT)) {
            setX(getX()+32*updateMult);
        }
    }

    @Override
    public void draw(int[] p) {
        DrawLoop.Draw_Sprite(this.getX(), this.getY(), this.getSprite());
    }
    
}
