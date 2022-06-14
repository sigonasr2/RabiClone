package sig.objects;

import sig.RabiClone;
import sig.engine.Rectangle;
import sig.engine.Sprite;
import sig.engine.Transform;
import sig.objects.actor.PhysicsObject;
import sig.objects.actor.RenderedObject;

public class Erinoah extends PhysicsObject implements RenderedObject{

    public Erinoah(double x, double y) {
        super(Sprite.ERINOAH,6.5,RabiClone.p);
        setX(x);
        setY(y);
    }

    @Override
    public Rectangle setCollisionBounds() {
        return new Rectangle(12,4,24,38);
    }

    @Override
    public void update(double updateMult) {
        super.update(updateMult);
    }

    @Override
    public void draw(byte[] p) {
    }

    @Override
    public Transform getSpriteTransform() {
        return Transform.NONE;
    }

    @Override
    public boolean rightKeyHeld() {
        return false;
    }

    @Override
    public boolean leftKeyHeld() {
        return false;
    }
}
