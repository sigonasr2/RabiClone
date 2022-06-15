package sig.objects.actor;

import sig.engine.Rectangle;

public interface PhysicsObjectRequirements {
    boolean rightKeyHeld();
    boolean leftKeyHeld();
    Rectangle getCollisionBounds();
    Rectangle setCollisionBounds();
    void setVelocityLimits(double x,double y);
    void setAccelerationLimits(double x,double y);
    void setMaxJumpCount(byte jumps);
    void setGroundFriction(double x);
    void setAirFriction(double x);
    void setGroundDrag(double x);
    void setAirDrag(double x);
    void setSlidingVelocity(double x);
    void setSlidingAcceleration(double x);
    void setJumpVelocity(double x);
}
