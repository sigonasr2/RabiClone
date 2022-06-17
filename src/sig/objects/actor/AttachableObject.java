package sig.objects.actor;

import sig.engine.AnimatedSprite;
import sig.engine.Panel;
import sig.engine.objects.AnimatedObject;
import sig.engine.objects.Object;

public abstract class AttachableObject extends AnimatedObject implements Attachable{
    Object attached;

    protected AttachableObject(AnimatedSprite spr, double animationSpd, Panel panel, Object attachedObj) {
        super(spr, animationSpd, panel);
        setAttachedObject(attachedObj);
    }

    

    protected Object getAttached() {
        return attached;
    }



    protected void setAttached(Object attached) {
        this.attached = attached;
    }



    public void setAttachedObject(Object o) {
        this.attached=o;
    } 

    public Object getAttachedObject() {
        return this.attached;
    } 
    
}
