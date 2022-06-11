package sig.engine.objects;

import sig.engine.Rectangle;

public interface CollisionEntity {
    Rectangle getCollisionBounds();
    void setCollisionBounds(Rectangle bounds);
}
