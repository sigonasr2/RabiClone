package sig.objects.enemies;

import sig.RabiClone;
import sig.engine.Sprite;
import sig.objects.BunnyGirls;

public class YellowBun extends BunnyGirls{

    public YellowBun(double x, double y) {
        super(Sprite.YELLOW_STAND,Sprite.YELLOW_WALK, 6.5, RabiClone.p);
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
        setMaxJumpCount_UseDefaultStrategy();
        setGravity_UseHalfStrategy(150);
    }
    
    
}
