package sig.objects;

import sig.RabiClone;
import sig.engine.Sprite;
import sig.engine.Transform;
import sig.engine.objects.AnimatedObject;
import sig.objects.actor.RenderedObject;

public class Erinoah extends AnimatedObject implements RenderedObject{

    public Erinoah(double x, double y) {
        super(Sprite.ERINOAH,6.5,RabiClone.p);
        setX(x);
        setY(y);
    }

    @Override
    public void update(double updateMult) {
        super.update(updateMult);
    }

    @Override
    public void draw(byte[] p) {
        Draw_Animated_Sprite(16, 16, getAnimatedSpr(), getCurrentFrame());
    }

    @Override
    public Transform getSpriteTransform() {
        return Transform.NONE;
    }
    
}
