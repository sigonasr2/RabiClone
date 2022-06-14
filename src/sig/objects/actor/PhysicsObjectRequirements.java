package sig.objects.actor;

import sig.engine.Rectangle;

public interface PhysicsObjectRequirements {
    boolean rightKeyHeld();
    boolean leftKeyHeld();
    Rectangle getCollisionBounds();
    Rectangle setCollisionBounds();
}
