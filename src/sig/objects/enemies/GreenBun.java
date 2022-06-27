package sig.objects.enemies;

import sig.RabiClone;
import sig.engine.Sprite;
import sig.objects.BunnyGirls;

public class GreenBun extends BunnyGirls{

    public GreenBun(double x, double y) {
        super(Sprite.GREEN_STAND,Sprite.GREEN_WALK, 6.5, RabiClone.p);
        setX(x);
        setY(y);
        setAccelerationLimits_UseDefaultStrategy();
        setVelocityLimits_UseDefaultStrategy();
        setGroundDrag_UseDefaultStrategy();
        setGroundFriction_UseDefaultStrategy();
        setAirDrag_UseDefaultStrategy();
        setAirFriction_UseDefaultStrategy();
        setSlidingVelocity_UseDefaultStrategy();
        setSlidingAcceleration_UseDefaultStrategy();
        setJumpVelocity_UseDefaultStrategy();
        setGravity_UseHalfStrategy(1550);
    }
    
    
}
