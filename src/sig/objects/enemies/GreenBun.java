package sig.objects.enemies;

import sig.RabiClone;
import sig.engine.Sprite;
import sig.objects.BunnyGirls;
import sig.objects.actor.PhysicsObject;

public class GreenBun extends BunnyGirls{

    public GreenBun(double x, double y) {
        super(Sprite.GREEN_STAND,Sprite.GREEN_WALK, 6.5, RabiClone.p);
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
        setGravity(1550);
    }
    
    
}
