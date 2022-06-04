package sig.engine;

public abstract class AnimatedObject extends Object{

    double currentFrame;
    double animationSpd; 
    AnimatedSprite animatedSpr;

    protected AnimatedObject(AnimatedSprite spr, double animationSpd, Panel panel) {
        super(panel);
        this.spr=this.animatedSpr=spr;
        this.animationSpd=animationSpd;
        this.currentFrame=0;
    }

    public void update(double updateMult) {
        this.currentFrame+=this.animationSpd*updateMult;
    }

    public double getCurrentFrame() {
        return currentFrame;
    }

    public void setCurrentFrame(double currentFrame) {
        this.currentFrame = currentFrame;
    }

    public double getAnimationSpd() {
        return animationSpd;
    }

    public void setAnimationSpd(double animationSpd) {
        this.animationSpd = animationSpd;
    }

    public AnimatedSprite getAnimatedSpr() {
        return animatedSpr;
    }

    public void setAnimatedSpr(AnimatedSprite animatedSpr) {
        this.animatedSpr = animatedSpr;
    }
    
}
