package sig.objects.weapons;

import sig.engine.AnimatedSprite;
import sig.engine.Panel;
import sig.engine.Transform;
import sig.objects.actor.AttachableObject;
import sig.engine.objects.Object;

public class KnifeSwing extends AttachableObject{

    final byte frameCount = 5; //Number of frames before animation ends.

    public KnifeSwing(AnimatedSprite spr, double animationSpd, Panel panel, Object attachedObj) {
        super(spr, animationSpd, panel, attachedObj);
    }

    @Override
    public void update(double updateMult) {
        super.update(updateMult);
        if (getCurrentFrame()>frameCount) {
            setMarkedForDeletion(true);
            return;
        }
        if (getSpriteTransform()==Transform.HORIZONTAL) {
            setX(getAttachedObject().getX()+getAnimatedSpr().getWidth()/2);
        } else {
            setX(getAttachedObject().getX()-getAnimatedSpr().getWidth()/2);
        }
        setY(getAttachedObject().getY());
    }

    @Override
    public void draw(byte[] p) {
    }

    @Override
    public Transform getSpriteTransform() {
        return getAttached().getSpriteTransform();
    }

    @Override
    public boolean isFriendlyObject() {
        return true;
    }    
}
