package sig.objects.enemies;

import sig.RabiClone;
import sig.engine.Sprite;
import sig.objects.BunnyGirls;

public class BlueBun extends BunnyGirls{

    public BlueBun(double x, double y) {
        super(Sprite.BLUE_STAND,Sprite.BLUE_WALK, 6.5, RabiClone.p);
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
        setGravity_UseHalfStrategy(950);
    }
    
    
}
