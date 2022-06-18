package sig.objects.enemies;

import sig.RabiClone;
import sig.engine.Sprite;
import sig.objects.BunnyGirls;
import sig.objects.actor.PhysicsObject;

public class YellowBun extends BunnyGirls{

    public YellowBun(double x, double y) {
        super(Sprite.YELLOW_STAND,Sprite.YELLOW_WALK, 6.5, RabiClone.p);
        setX(x);
        setY(y);
        setAccelerationLimits(100, 100);
        setVelocityLimits(500, 500);
        setGroundDrag(2000);
        setGroundFriction(PhysicsObject.NORMAL_FRICTION);
        setAirDrag(800);
        setAirFriction(180);
        setSlidingVelocity(164);
        setSlidingAcceleration(120);
        setJumpVelocity(PhysicsObject.NORMAL_JUMP_VELOCITY);
        setGravity(150);
    }
    
    
}
