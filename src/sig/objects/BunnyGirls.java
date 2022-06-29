package sig.objects;

import sig.engine.Alpha;
import sig.engine.AnimatedSprite;
import sig.engine.Panel;
import sig.engine.Rectangle;
import sig.engine.Transform;
import sig.objects.actor.PhysicsObject;

public class BunnyGirls extends PhysicsObject{

    AnimatedSprite walkingSprite;
    AnimatedSprite standingSprite;
    double lastMoved = 0;
    double lastJumped = 0;
    boolean moveDir = false;
    double moveTimer = 0;

    protected BunnyGirls(AnimatedSprite spr,AnimatedSprite walkingSpr, double animationSpd, Panel panel) {
        super(spr, animationSpd, panel);
        this.walkingSprite=walkingSpr;
        this.standingSprite=spr;
    }

    @Override
    public void update(double updateMult) {
        super.update(updateMult);
        lastMoved+=updateMult;
        lastJumped+=updateMult;
        if (lastMoved>5) {
            switch ((int)(Math.random()*3)) {
                case 0:{
                    moveDir=true;
                    setAnimatedSpr(walkingSprite);
                    moveTimer=Math.random()*3;
                }break;
                case 1:{
                    moveDir=false;
                    setAnimatedSpr(walkingSprite);
                    moveTimer=Math.random()*3;
                }break;
            }
            lastMoved=0;
        }
        if (lastJumped>3) {
            if (Math.random()<=0.4&&jumpCount>0) {
                y_velocity = jump_velocity;
                jumpCount--;
                lastJumped=2.5+Math.random()*0.5;
                lastMoved=4.5+Math.random()*0.5;
            } else {
                lastJumped=0;
            }
        }
        moveTimer-=updateMult;
        if(moveTimer<=0){
            setAnimatedSpr(standingSprite);
        }
    }

    @Override
    public boolean rightKeyHeld() {
        return moveTimer>0&&moveDir;
    }

    @Override
    public boolean leftKeyHeld() {
        return moveTimer>0&&!moveDir;
    }

    @Override
    public Rectangle setCollisionBounds() {
        return new Rectangle(10,6,12,25);
    }

    @Override
    public boolean isFriendlyObject() {
        return false;
    }

    @Override
    public void draw(byte[] p) {        
    }

    @Override
    public Transform getSpriteTransform() {
        return moveDir?Transform.HORIZONTAL:Transform.NONE;
    }
    
}
