package sig.engine;

public abstract class Object implements GameEntity{
    double x,y;
    Sprite spr;
    Panel panel;

    protected Object(Panel panel) {
        this.panel=panel;
    }
    
    public double getX() {
        return x;
    }
    public void setX(double x) {
        this.x = x;
    }
    public double getY() {
        return y;
    }
    public void setY(double y) {
        this.y = y;
    }
    public Sprite getSprite() {
        return spr;
    }
    public void setSprite(Sprite spr) {
        this.spr = spr;
    }

	protected boolean KeyHeld(int key) {
		return panel.KEYS.getOrDefault(key,false);
	}
}
