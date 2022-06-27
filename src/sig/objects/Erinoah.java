package sig.objects;

import sig.RabiClone;
import sig.engine.Rectangle;
import sig.engine.Sprite;
import sig.engine.Transform;
import sig.objects.actor.PhysicsObject;

public class Erinoah extends PhysicsObject{

    double lastMoved = 0;
    double lastJumped = 0;
    boolean moveDir = false;
    double moveTimer = 0;

    public Erinoah(double x, double y) {
        super(Sprite.ERINOAH,6.5,RabiClone.p);
        setX(x);
        setY(y);
        setAccelerationLimits(100, 100);
        setVelocityLimits(500, 500);
        setGroundDrag(2000);
        setGroundFriction(PhysicsObject.GROUND_FRICTION);
        setAirDrag(800);
        setAirFriction(180);
        setSlidingVelocity(164);
        setSlidingAcceleration(120);
        setJumpVelocity(PhysicsObject.JUMP_VELOCITY);
        setGravity(450);
    }

    @Override
    public Rectangle setCollisionBounds() {
        return new Rectangle(12,4,24,38);
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
                    moveTimer=Math.random()*3;
                }break;
                case 1:{
                    moveDir=false;
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
    }

    @Override
    public void draw(byte[] p) {
    }

    @Override
    public Transform getSpriteTransform() {
        return moveDir?Transform.HORIZONTAL:Transform.NONE;
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
    public boolean isFriendlyObject() {
        return false;
    }    
}
