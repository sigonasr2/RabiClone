package sig.objects;

import sig.engine.Panel;
import sig.engine.Sprite;
import sig.engine.objects.AnimatedObject;

public class Erinoah extends AnimatedObject{

    public Erinoah(Panel panel) {
        super(Sprite.ERINOAH, 6.5, panel);
    }

    @Override
    public void update(double updateMult) {
        super.update(updateMult);
    }

    @Override
    public void draw(byte[] p) {
        Draw_Animated_Sprite(16, 16, getAnimatedSpr(), getCurrentFrame());
    }
    
}
