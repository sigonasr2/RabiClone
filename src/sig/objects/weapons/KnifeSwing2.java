package sig.objects.weapons;

import sig.engine.AnimatedSprite;
import sig.engine.Panel;
import sig.engine.Transform;
import sig.objects.actor.AttachableObject;
import sig.objects.actor.PhysicsObject;
import sig.objects.actor.State;
import sig.engine.objects.AnimatedObject;
import sig.engine.objects.Object;

public class KnifeSwing2 extends AttachableObject{

    final byte frameCount = 5; //Number of frames before animation ends.

    public KnifeSwing2(AnimatedSprite spr, double animationSpd, Panel panel, Object attachedObj) {
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
    public void collisionEvent(AnimatedObject obj) {
        //System.out.println("Bonk");
        if(obj instanceof PhysicsObject){
            PhysicsObject pobj = (PhysicsObject)obj;
            if(pobj.state!=State.STAGGER){
                if(getSpriteTransform()==Transform.NONE){
                    pobj.staggerDuration=0.3;
                    pobj.x_velocity = -500;
                    pobj.y_velocity = -300;
                    pobj.state = State.STAGGER;
                }else{
                    pobj.staggerDuration=0.3;
                    pobj.x_velocity = 500;
                    pobj.y_velocity = -300;
                    pobj.state = State.STAGGER;
                }
            }
        }
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
