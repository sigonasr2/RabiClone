package sig.objects;

import sig.DrawLoop;
import sig.RabiClone;
import sig.engine.Object;
import sig.engine.Panel;
import sig.engine.Sprite;
import java.awt.event.KeyEvent;

public class Player extends Object{

    public Player(Panel panel) {
        super(panel);
        this.setSprite(Sprite.NANA_SMALL);
        setX(Math.random()*RabiClone.BASE_WIDTH);
        setY(Math.random()*RabiClone.BASE_HEIGHT);
    }


    @Override
    public void update(double updateMult) {
        int right = KeyHeld(KeyEvent.VK_RIGHT)?1:0;
        int left = KeyHeld(KeyEvent.VK_LEFT)?1:0;
        int up = KeyHeld(KeyEvent.VK_UP)?1:0;
        int down = KeyHeld(KeyEvent.VK_DOWN)?1:0;
        if (right-left!=0) {
            setX(getX()+(right-left)*32*updateMult);
        }
        if (up-down!=0) {
            setY(getY()+(down-up)*32*updateMult);
        }
    }

    @Override
    public void draw(int[] p) {
        DrawLoop.Draw_Sprite(this.getX(), this.getY(), this.getSprite());
    }
    
}
