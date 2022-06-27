package sig.objects.enemies;

import sig.RabiClone;
import sig.engine.Sprite;
import sig.objects.BunnyGirls;

public class RedBun extends BunnyGirls{

    public RedBun(double x, double y) {
        super(Sprite.RED_STAND,Sprite.RED_WALK, 6.5, RabiClone.p);
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
        setGravity_UseHalfStrategy(750);
    }
    
}
