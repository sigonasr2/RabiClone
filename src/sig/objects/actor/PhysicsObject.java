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
    final public static double UNDERWATER_GRAVITY = 300;
    final public static double UNDERWATER_NORMAL_FRICTION = 3200;
    final public static double UNDERWATER_NORMAL_JUMP_VELOCITY = -900;
    final public static double UNDERWATER_WALKING_SPEED_LIMIT = 76;
    final public static double UNDERWATER_FALLING_SPEED_LIMIT = 300;

    public State state = State.IDLE;
    public double x_velocity;
    protected double staggerDuration = 0;
    protected State resumeState=null;
    protected double uncontrollableDuration = 0;
    protected double invulnerabilityDuration = 0;
    public double y_velocity;
    protected double gravity = GRAVITY;
    protected double x_acceleration,y_acceleration;
    protected boolean groundCollision;
    protected byte jumpCount=0;
    protected byte maxJumpCount=2;
    protected double x_velocity_limit,y_velocity_limit;
    protected double x_acceleration_limit,y_acceleration_limit;
    protected double jump_velocity;
    protected double horizontal_air_friction,horizontal_air_drag;
    protected double horizontal_friction,horizontal_drag;
    protected double sliding_velocity,sliding_acceleration;

    /* LAND is for all values pertaining to being on land. Stored because our active physics values CAN (and probably will) change!
     * Therefore these are storage containers for what the real values used are.
     * The handling of switching between land and underwater physics should be abstracted away and not require refactoring of the code in any capacity.
     * The game will automatically know which values to use.
     */
    private double LAND_gravity;
    private double LAND_x_velocity_limit,LAND_y_velocity_limit;
    private double LAND_x_acceleration_limit,LAND_y_acceleration_limit;
    private double LAND_jump_velocity;
    private byte LAND_maxJumpCount;
    private double LAND_horizontal_air_friction,LAND_horizontal_air_drag;
    private double LAND_horizontal_friction,LAND_horizontal_drag;
    private double LAND_sliding_velocity,LAND_sliding_acceleration;

    private double UNDERWATER_gravity;
    private double UNDERWATER_x_velocity_limit,UNDERWATER_y_velocity_limit;
    private double UNDERWATER_x_acceleration_limit,UNDERWATER_y_acceleration_limit;
    private double UNDERWATER_jump_velocity;
    private byte UNDERWATER_maxJumpCount;
    private double UNDERWATER_horizontal_air_friction,UNDERWATER_horizontal_air_drag;
    private double UNDERWATER_horizontal_friction,UNDERWATER_horizontal_drag;
    private double UNDERWATER_sliding_velocity,UNDERWATER_sliding_acceleration;

    /** <b>Not to be exposed!</b> Internally tracks if we're underwater and when it switches, sets all the physics values appropriately.*/
    private boolean underwaterState=false;

    protected PhysicsObject(AnimatedSprite spr, double animationSpd, Panel panel) {
        super(spr, animationSpd, panel);
        setCollisionBox(setCollisionBounds());
    }

    @Override
    public void update(double updateMult) {
        super.update(updateMult);
        setPhysicsBasedOnLandOrUnderwaterStatus();
        handleMovementPhysics(updateMult);
        if(state==State.STAGGER && staggerDuration>0){
            staggerDuration-=updateMult;
        }else
        if(state==State.STAGGER && staggerDuration<=0){
            state=resumeState;
        }
        if(state==State.UNCONTROLLABLE && uncontrollableDuration>0){
            uncontrollableDuration-=updateMult;
        }else
        if(state==State.UNCONTROLLABLE && uncontrollableDuration<=0){
            state=resumeState;
        }
        if (invulnerabilityDuration>0) {
            invulnerabilityDuration-=updateMult;
        }
    }

    protected void handleMovementPhysics(double updateMult) {
        if (state==State.STAGGER||state==State.UNCONTROLLABLE) {
            return;
        }
        int right = rightKeyHeld()?1:0;
        int left = leftKeyHeld()?1:0;
        if(state==State.SLIDE||state==State.BELLYSLIDE){
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
                        if (state != State.SLIDE&&state!=State.BELLYSLIDE) {
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

    /**
     * How long to set invincibility for this object.
     * @param duration Amount of time in seconds.
     */
    public void setInvulnerability(double duration) {
        this.invulnerabilityDuration = duration;
    }

    public boolean isInvulnerable() {
        return this.invulnerabilityDuration>0;
    }

    /**
     * Sets how long this object will remain in the stagger state.
     * Automatically resets the state to the previous state the object
     * was in when the stagger state completes.
     * @param duration Amount of time in seconds.
     * */
    public void setStagger(double duration) {
        staggerDuration=duration;
        if (state!=State.STAGGER) {
            resumeState=state;
        }
        state=State.STAGGER;
    }

    /**
     * Sets how long this object will remain in the uncontrollable state.
     * Automatically resets the state to the previous state the object
     * was in when the uncontrollable state completes.
     * @param duration Amount of time in seconds.
     * */
    public void setUncontrollable(double duration) {
        uncontrollableDuration=duration;
        if (state!=State.UNCONTROLLABLE) {
            resumeState=state;
        }
        state=State.UNCONTROLLABLE;
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

    public void setPhysicsBasedOnLandOrUnderwaterStatus() {
        if (isUnderwater()&&!underwaterState) {
            gravity=UNDERWATER_gravity;
            x_velocity_limit=UNDERWATER_x_velocity_limit;y_velocity_limit=UNDERWATER_y_velocity_limit;
            x_acceleration_limit=UNDERWATER_x_acceleration_limit;y_acceleration_limit=UNDERWATER_y_acceleration_limit;
            jump_velocity=UNDERWATER_jump_velocity;
            maxJumpCount=UNDERWATER_maxJumpCount;
            horizontal_air_friction=UNDERWATER_horizontal_air_friction;
            horizontal_air_drag=UNDERWATER_horizontal_air_drag;
            horizontal_friction=UNDERWATER_horizontal_friction;
            horizontal_drag=UNDERWATER_horizontal_drag;
            sliding_velocity=UNDERWATER_sliding_velocity;
            sliding_acceleration=UNDERWATER_sliding_acceleration;
            underwaterState=true;
        } else
        if (!isUnderwater()&&underwaterState) {
            gravity=LAND_gravity;
            x_velocity_limit=LAND_x_velocity_limit;y_velocity_limit=LAND_y_velocity_limit;
            x_acceleration_limit=LAND_x_acceleration_limit;y_acceleration_limit=LAND_y_acceleration_limit;
            jump_velocity=LAND_jump_velocity;
            maxJumpCount=LAND_maxJumpCount;
            horizontal_air_friction=LAND_horizontal_air_friction;
            horizontal_air_drag=LAND_horizontal_air_drag;
            horizontal_friction=LAND_horizontal_friction;
            horizontal_drag=LAND_horizontal_drag;
            sliding_velocity=LAND_sliding_velocity;
            sliding_acceleration=LAND_sliding_acceleration;
            underwaterState=false;
        }
    }

    @Override
    public void setVelocityLimits(double xVelocity,double yVelocity,double underwaterXVelocity,double underwaterYVelocity) {
        this.LAND_x_velocity_limit=xVelocity;
        this.LAND_y_velocity_limit=yVelocity;
        this.UNDERWATER_x_velocity_limit=underwaterXVelocity;
        this.UNDERWATER_y_velocity_limit=underwaterYVelocity;
    }

    @Override
    public void setAccelerationLimits(double xAccelerationLimit,double yAccelerationLimit,double underwaterXAccelerationLimit,double underwaterYAccelerationLimit) {
        this.LAND_x_acceleration_limit=xAccelerationLimit;
        this.LAND_y_acceleration_limit=yAccelerationLimit;
        this.UNDERWATER_x_acceleration_limit=underwaterXAccelerationLimit;
        this.UNDERWATER_y_acceleration_limit=underwaterYAccelerationLimit;
    }

    @Override
    public void setMaxJumpCount(byte jumps,byte underwaterJumps) {
        this.LAND_maxJumpCount=jumps;
        this.UNDERWATER_maxJumpCount=underwaterJumps;
    }

    @Override
    public void setGroundFriction(double groundFriction,double underwaterGroundFriction) {
        this.LAND_horizontal_friction=groundFriction;
        this.UNDERWATER_horizontal_friction=underwaterGroundFriction;
    }

    @Override
    public void setAirFriction(double airFriction,double underwaterAirFriction) {
        this.LAND_horizontal_air_friction=airFriction;
        this.UNDERWATER_horizontal_air_friction=underwaterAirFriction;
    }

    @Override
    public void setGroundDrag(double groundDrag,double underwaterGroundDrag) {
        this.LAND_horizontal_drag=groundDrag;
        this.UNDERWATER_horizontal_drag=underwaterGroundDrag;
    }

    @Override
    public void setAirDrag(double airDrag,double underwaterAirDrag) {
        this.LAND_horizontal_air_drag=airDrag;
        this.UNDERWATER_horizontal_air_drag=underwaterAirDrag;
    }

    @Override
    public void setSlidingVelocity(double slidingVelocity,double underwaterSlidingVelocity) {
        this.LAND_sliding_velocity=slidingVelocity;
        this.UNDERWATER_sliding_velocity=underwaterSlidingVelocity;
    }

    @Override
    public void setSlidingAcceleration(double slidingAcceleration,double underwaterSlidingAcceleration) {
        this.LAND_sliding_acceleration=slidingAcceleration;
        this.UNDERWATER_sliding_acceleration=underwaterSlidingAcceleration;
    }

    @Override
    public void setJumpVelocity(double jumpVelocity,double underwaterJumpVelocity) {
        this.LAND_jump_velocity=jumpVelocity;
        this.UNDERWATER_jump_velocity=underwaterJumpVelocity;
    }

    @Override
    public void setGravity(double gravity, double underwaterGravity) {
        this.LAND_gravity=gravity;
        this.UNDERWATER_gravity=underwaterGravity;
    }

    public double getGravity() {
        return gravity;
    }
}
