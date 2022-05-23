package sig.engine;

public abstract class Object implements GameEntity{
    double x,y;
    Sprite spr;
    Panel panel;
    public Panel getPanel() {
        return panel;
    }

    public void setPanel(Panel panel) {
        this.panel = panel;
    }

    boolean markedForDeletion;

    public boolean isMarkedForDeletion() {
        return markedForDeletion;
    }

    public void setMarkedForDeletion(boolean markedForDeletion) {
        this.markedForDeletion = markedForDeletion;
    }

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
