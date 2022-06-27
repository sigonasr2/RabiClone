package sig.objects.actor;

import sig.engine.Rectangle;

public interface PhysicsObjectRequirements {
    boolean rightKeyHeld();
    boolean leftKeyHeld();
    Rectangle getCollisionBounds();
    Rectangle setCollisionBounds();
    void setVelocityLimits(double xVelocity,double yVelocity,double underwaterXVelocity,double underwaterYVelocity);
    void setAccelerationLimits(double xAccelerationLimit,double yAccelerationLimit,double underwaterXAccelerationLimit,double underwaterYAccelerationLimit);
    void setMaxJumpCount(byte jumps,byte underwaterJumps);
    void setGroundFriction(double groundFriction,double underwaterGroundFriction);
    void setAirFriction(double airFriction,double underwaterAirFriction);
    void setGroundDrag(double groundDrag,double underwaterGroundDrag);
    void setAirDrag(double airDrag,double underwaterAirDrag);
    void setSlidingVelocity(double slidingVelocity,double underwaterSlidingVelocity);
    void setSlidingAcceleration(double slidingAcceleration,double underwaterSlidingAcceleration);
    void setJumpVelocity(double jumpVelocity,double underwaterJumpVelocity);
    void setGravity(double gravity, double underwaterGravity);
}
