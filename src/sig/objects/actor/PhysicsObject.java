package sig.objects.actor;

import sig.RabiClone;
import sig.engine.AnimatedSprite;
import sig.engine.Panel;
import sig.engine.Rectangle;
import sig.engine.objects.AnimatedObject;
import sig.map.Tile;

public abstract class PhysicsObject extends AnimatedObject implements PhysicsObjectRequirements{
    final public static double GRAVITY = 1300;
    final public static double NORMAL_FRICTION = 6400;
    final public static double NORMAL_JUMP_VELOCITY = -300;
    final public static double WALKING_SPEED_LIMIT = 164;
    final public static double FALLING_SPEED_LIMIT = 500;

    public State state = State.IDLE;
    public double x_velocity;
    public double staggerDuration = 0;
    public double y_velocity;
    protected double gravity = GRAVITY;
    protected double x_acceleration,y_acceleration;
    protected double x_velocity_limit,y_velocity_limit;
    protected double x_acceleration_limit,y_acceleration_limit;
    protected boolean groundCollision;
    protected byte maxJumpCount=2;
    protected byte jumpCount=0;
    protected double jump_velocity;
    protected double horizontal_air_friction,horizontal_air_drag;
    protected double horizontal_friction,horizontal_drag;
    protected double sliding_velocity,sliding_acceleration;

    protected PhysicsObject(AnimatedSprite spr, double animationSpd, Panel panel) {
        super(spr, animationSpd, panel);
        setCollisionBox(setCollisionBounds());
    }

    @Override
    public void update(double updateMult) {
        super.update(updateMult);
        handleMovementPhysics(updateMult);
        if(state==State.STAGGER && staggerDuration>0){
            staggerDuration-=updateMult;
        }else
        if(state==State.STAGGER && staggerDuration<=0){
            state=State.IDLE;
        }
    }

    protected void handleMovementPhysics(double updateMult) {
        if (state==State.STAGGER) {
            return;
        }
        int right = rightKeyHeld()?1:0;
        int left = leftKeyHeld()?1:0;
        if(state==State.SLIDE){
            right=0;
            left=0;
        }
        x_velocity = 
            Math.abs(x_velocity+x_acceleration*updateMult)>x_velocity_limit
                ?Math.signum(x_velocity+x_acceleration*updateMult)*x_velocity_limit
                :x_velocity+x_acceleration*updateMult;
        y_velocity =
            Math.abs(y_velocity+y_acceleration*updateMult)>y_velocity_limit
                ?Math.signum(y_velocity+y_acceleration*updateMult)*y_velocity_limit
                :y_velocity+y_acceleration*updateMult;
        double displacement_y = y_velocity*updateMult;
        double displacement_x = x_velocity*updateMult;

        boolean sideCollision = false;
        boolean hitAbove=false;

        double startingX=getX();

        if (y_velocity==0) {
            if (!(checkCollision(getX()-getSprite().getWidth()/2+getCollisionBox().getX2(),getY()-getSprite().getHeight()/2+getCollisionBounds().getY2()+2)||
            checkCollision(getX()-getSprite().getWidth()/2+getCollisionBox().getX(),getY()-getSprite().getHeight()/2+getCollisionBounds().getY2()+2))) {
                groundCollision=false;
            } else {
                groundCollision=true;
                jumpCount=maxJumpCount;
            }
        } else {
            double startingY=getY();
            groundCollision=false;

            if (displacement_y>0) {
                for (int y=(int)getY();y<startingY+displacement_y;y++) {
                    if (y==getY()) {
                        continue;
                    }
                    if (checkCollision(getX()-getSprite().getWidth()/2+getCollisionBox().getX2(),y+getCollisionBox().getY2()-getSprite().getHeight()/2)||
                    checkCollision(getX()-getSprite().getWidth()/2+getCollisionBox().getX()+1,y+getCollisionBox().getY2()-getSprite().getHeight()/2)) {
                        setY(y-0.1);
                        //System.out.println("Running"+System.currentTimeMillis());
                        y_acceleration = 0;
                        y_velocity = 0;
                        groundCollision = true;
                        if (state != State.SLIDE) {
                            state = State.IDLE;
                        }
                        break;
                    }       
                }
            } else {
                for (int y=(int)getY();y>startingY+displacement_y;y--) {
                    if (y==getY()) {
                        continue;
                    }
                    if (checkCollision(getX()-getSprite().getWidth()/2+getCollisionBox().getX2(),y+getCollisionBox().getY()-getSprite().getHeight()/2)||
                    checkCollision(getX()-getSprite().getWidth()/2+getCollisionBox().getX()+1,y+getCollisionBox().getY()-getSprite().getHeight()/2)) {
                        setY(y+1);
                        hitAbove=true;
                        state=State.FALLING;
                        y_acceleration = 0;
                        y_velocity = 0;
                        displacement_y=0;
                        //groundCollision = true;
                        break;
                    }
                }
            }
        }

        sideCollision = sideCollisionChecking(displacement_x, sideCollision, hitAbove, startingX);

      
        if (!groundCollision){
            this.setY(this.getY()+displacement_y);
            y_acceleration = gravity;
            if(y_velocity>0 && state!=State.SLIDE&&state!=State.BELLYSLIDE){
                state = State.FALLING;
            }
            if (y_velocity>FALLING_SPEED_LIMIT&&state!=State.BELLYSLIDE) {
                y_velocity=FALLING_SPEED_LIMIT;
            }
            if (!sideCollision) {
                handleKeyboardMovement(updateMult, right-left, horizontal_air_friction, horizontal_air_drag);
                this.setX(this.getX()+displacement_x);
            }
        } else {
            if (!sideCollision) {
                handleKeyboardMovement(updateMult, right-left, horizontal_friction, horizontal_drag);
                this.setX(this.getX()+displacement_x);
            }
        }
    }

    private boolean sideCollisionChecking(double displacement_x, boolean sideCollision, boolean hitAbove, double startingX) {
        if (displacement_x>0.00001) {
            for (int x=(int)getX();x<startingX+displacement_x;x++) {
                if (x==getX()) {
                    continue;
                }
                if (checkCollision(x+getCollisionBox().getX2()-getSprite().getWidth()/2,(getY()-getSprite().getHeight()/2+getCollisionBounds().getY2()))||
                checkCollision(x+getCollisionBox().getX2()-getSprite().getWidth()/2,(getY()-getSprite().getHeight()/2+getCollisionBounds().getY()))) {
                    if (!(checkCollision(x+getCollisionBox().getX2()-getSprite().getWidth()/2,(getY()-getSprite().getHeight()/2+getCollisionBounds().getY2()-2))||
                    checkCollision(x+getCollisionBox().getX2()-getSprite().getWidth()/2,(getY()-getSprite().getHeight()/2+getCollisionBounds().getY()-2)))) {
                        setY(getY()-1);
                    } else {
                        x_acceleration = 0;
                        x_velocity = 0.000001;
                        sideCollision=true;
                        setX(x-0.1);
                        break;
                    }
                } else {
                    if (!hitAbove&&!checkCollision(x+getCollisionBox().getX()-getSprite().getWidth()/2,(getY()-getSprite().getHeight()/2+getCollisionBounds().getY2()+1))&&
                    checkCollision(x+getCollisionBox().getX()-getSprite().getWidth()/2,(getY()-getSprite().getHeight()/2+getCollisionBounds().getY2()+2))) {
                        //System.out.println("Performs check."+System.currentTimeMillis());
                        setY(getY()+1);
                        //x_velocity = ;
                    }
                } 
            }
        } else 
        if (displacement_x<-0.00001) {
            for (int x=(int)getX();x>startingX+displacement_x;x--) {
                if (x==getX()) {
                    continue;
                }
                if (checkCollision((x+getCollisionBox().getX()-getSprite().getWidth()/2),getY()-getSprite().getHeight()/2+getCollisionBounds().getY2())||
                checkCollision((x+getCollisionBox().getX()-getSprite().getWidth()/2),getY()-getSprite().getHeight()/2+getCollisionBounds().getY())) {
                    if (!(checkCollision(x+getCollisionBox().getX()-getSprite().getWidth()/2,(getY()-getSprite().getHeight()/2+getCollisionBounds().getY2()-2))||
                    checkCollision(x+getCollisionBox().getX()-getSprite().getWidth()/2,(getY()-getSprite().getHeight()/2+getCollisionBounds().getY()-2)))) {
                        setY(getY()-1);
                    } else {
                        x_acceleration = 0;
                        x_velocity = -0.000001;
                        sideCollision=true;
                        setX(x+0.1);
                        break;
                    }
                } else {
                    if (!hitAbove&&!checkCollision(x+getCollisionBox().getX2()-getSprite().getWidth()/2,(getY()-getSprite().getHeight()/2+getCollisionBounds().getY2()+1))&&
                    checkCollision(x+getCollisionBox().getX2()-getSprite().getWidth()/2,(getY()-getSprite().getHeight()/2+getCollisionBounds().getY2()+2))) {
                        //System.out.println("Performs check."+System.currentTimeMillis());
                        setY(getY()+1);
                        //x_velocity =;
                    }
                } 
            }
        }
        return sideCollision;
    }

    protected boolean checkCollision(double x,double y) {
        int index = (int)y*RabiClone.BASE_WIDTH*Tile.TILE_WIDTH+(int)x;
        if (index>=0&&index<RabiClone.COLLISION.length) {
            return RabiClone.COLLISION[index];
        } else {
            return false;
        }
    }

    private void handleKeyboardMovement(double updateMult, int movement, double friction, double drag) {
        if (movement != 0 && Math.abs(x_velocity) < WALKING_SPEED_LIMIT) {
            x_acceleration = drag * (movement);
        } else {
            if (x_velocity != 0) {
                x_velocity = x_velocity > 0
                        ? x_velocity - friction * updateMult > 0
                                ? x_velocity - friction * updateMult
                                : 0
                        : x_velocity + friction * updateMult < 0
                                ? x_velocity + friction * updateMult
                                : 0;
            }
            x_acceleration = 0;
        }
    }
    
    public Rectangle getCollisionBox() {
        return collisionBox;
    }

    public void setCollisionBox(Rectangle collisionBox) {
        this.collisionBox = collisionBox;
    }

    @Override
    public Rectangle getCollisionBounds() {
        return getCollisionBox();
    }



    @Override
    public void setVelocityLimits(double x, double y) {
        this.x_velocity_limit=x;
        this.y_velocity_limit=y;
    }

    @Override
    public void setAccelerationLimits(double x, double y) {
        this.x_acceleration_limit=x;
        this.y_acceleration_limit=y;
    }

    @Override
    public void setMaxJumpCount(byte jumps) {
        this.maxJumpCount=jumps;
    }

    @Override
    public void setGroundFriction(double x) {
        this.horizontal_friction=x;
    }

    @Override
    public void setAirFriction(double x) {
        this.horizontal_air_friction=x;
    }

    @Override
    public void setGroundDrag(double x) {
        this.horizontal_drag=x;
    }

    @Override
    public void setAirDrag(double x) {
        this.horizontal_air_drag=x;
    }

    @Override
    public void setSlidingVelocity(double x) {
        this.sliding_velocity=x;
    }

    @Override
    public void setSlidingAcceleration(double x) {
        this.sliding_acceleration=x;
    }

    @Override
    public void setJumpVelocity(double x) {
        this.jump_velocity=x;
    }

    @Override
    public void setGravity(double gravity) {
        this.gravity = gravity;
    }

    public double getGravity() {
        return gravity;
    }
}
