package sig.engine.objects;

import sig.engine.Transform;

public interface GameEntity {
    public void update(double updateMult);
    public void draw(byte[] p);
    public Transform getSpriteTransform();
}
