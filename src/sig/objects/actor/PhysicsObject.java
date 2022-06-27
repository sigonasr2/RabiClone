package sig.objects.actor;

import sig.RabiClone;
import sig.engine.AnimatedSprite;
import sig.engine.Panel;
import sig.engine.Rectangle;
import sig.engine.objects.AnimatedObject;
import sig.map.Tile;

public abstract class PhysicsObject extends AnimatedObject implements PhysicsObjectRequirements{
    final public static double GRAVITY = 1300;
    final public static byte MAX_JUMP_COUNT = 2;
    final public static double GROUND_FRICTION = 6400;
    final public static double AIR_FRICTION = 180;
    final public static double JUMP_VELOCITY = -300;
    final public static double LAND_WALKING_SPEED_LIMIT = 164;
    final public static double LAND_FALLING_SPEED_LIMIT = 500;
    final public static double X_VELOCITY_LIMIT = 246;
    final public static double Y_VELOCITY_LIMIT = 500;
    final public static double X_ACCELERATION_LIMIT = 100;
    final public static double Y_ACCELERATION_LIMIT = 100;
    final public static double GROUND_DRAG = 2000;
    final public static double AIR_DRAG = 900;
    final public static double SLIDING_VELOCITY = 164;
    final public static double SLIDING_ACCELERATION = 120;

    final public static double UNDERWATER_GRAVITY = 300;
    final public static byte UNDERWATER_MAX_JUMP_COUNT = 3;
    final public static double UNDERWATER_GROUND_FRICTION = 3200;
    final public static double UNDERWATER_AIR_FRICTION = 120;
    final public static double UNDERWATER_JUMP_VELOCITY = -900;
    final public static double UNDERWATER_WALKING_SPEED_LIMIT = 76;
    final public static double UNDERWATER_FALLING_SPEED_LIMIT = 300;
    final public static double UNDERWATER_X_VELOCITY_LIMIT = 120;
    final public static double UNDERWATER_Y_VELOCITY_LIMIT = 250;
    final public static double UNDERWATER_X_ACCELERATION_LIMIT = 50;
    final public static double UNDERWATER_Y_ACCELERATION_LIMIT = 50;
    final public static double UNDERWATER_GROUND_DRAG = 1700;
    final public static double UNDERWATER_AIR_DRAG = 1100;
    final public static double UNDERWATER_SLIDING_VELOCITY = 192;
    final public static double UNDERWATER_SLIDING_ACCELERATION = 164;

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
    protected double walking_speed_limit,falling_speed_limit;

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
        underwaterState=!isUnderwater(); //Slight hack that forces an update to physics at the start.
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
        if (movement != 0 && Math.abs(x_velocity) < walking_speed_limit) {
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
            walking_speed_limit=UNDERWATER_WALKING_SPEED_LIMIT;
            falling_speed_limit=UNDERWATER_FALLING_SPEED_LIMIT;
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
            walking_speed_limit=LAND_WALKING_SPEED_LIMIT;
            falling_speed_limit=LAND_FALLING_SPEED_LIMIT;
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

    /**
     * The <b>Copy Strategy</b> uses the same values for both land and water.
     * @param xVelocity The X velocity limit to set for land and water.
     * @param yVelocity The Y velocity limit to set for land and water.
     */
    public void setVelocityLimits_UseCopyStrategy(double xVelocity,double yVelocity) {
        setVelocityLimits(xVelocity, yVelocity, xVelocity, yVelocity);
    }

    /**
     * The <b>Half Strategy</b> uses halved values for water.
     * @param xVelocity The X velocity limit to set for land.
     * @param yVelocity The Y velocity limit to set for land.
     */
    public void setVelocityLimits_UseHalfStrategy(double xVelocity,double yVelocity) {
        setVelocityLimits(xVelocity, yVelocity, xVelocity/2, yVelocity/2);
    }

    /**
     * The <b>Default Strategy</b> uses the default values specified by the devs for "default physics" of any object.
     * <hr>The Defaults are: 
     * <ul>
        * <li><b>X Velocity Limit:</b> {@value #X_VELOCITY_LIMIT}</li>
        * <li><b>Y Velocity Limit:</b> {@value #Y_VELOCITY_LIMIT}</li>
        * <li><b>Underwater X Velocity Limit:</b> {@value #UNDERWATER_X_VELOCITY_LIMIT}</li>
        * <li><b>Underwater Y Velocity Limit:</b> {@value #UNDERWATER_Y_VELOCITY_LIMIT}</li>
     * </ul>
     */
    public void setVelocityLimits_UseDefaultStrategy() {
        setVelocityLimits(X_VELOCITY_LIMIT,Y_VELOCITY_LIMIT,UNDERWATER_X_VELOCITY_LIMIT,UNDERWATER_Y_VELOCITY_LIMIT);
    }

    @Override
    public void setAccelerationLimits(double xAccelerationLimit,double yAccelerationLimit,double underwaterXAccelerationLimit,double underwaterYAccelerationLimit) {
        this.LAND_x_acceleration_limit=xAccelerationLimit;
        this.LAND_y_acceleration_limit=yAccelerationLimit;
        this.UNDERWATER_x_acceleration_limit=underwaterXAccelerationLimit;
        this.UNDERWATER_y_acceleration_limit=underwaterYAccelerationLimit;
    }

    /**
     * The <b>Copy Strategy</b> uses the same values for both land and water.
     * @param xAcceleration The X acceleration limit to set for land and water.
     * @param yAcceleration The Y acceleration limit to set for land and water.
     */
    public void setAccelerationLimits_UseCopyStrategy(double xAcceleration,double yAcceleration) {
        setAccelerationLimits(xAcceleration, yAcceleration, xAcceleration, yAcceleration);
    }

    /**
     * The <b>Half Strategy</b> uses halved values for water.
     * @param xAcceleration The X acceleration limit to set for land.
     * @param yAcceleration The Y acceleration limit to set for land.
     */
    public void setAccelerationLimits_UseHalfStrategy(double xAcceleration,double yAcceleration) {
        setAccelerationLimits(xAcceleration, yAcceleration, xAcceleration/2, yAcceleration/2);
    }

    /**
     * The <b>Default Strategy</b> uses the default values specified by the devs for "default physics" of any object.
     * <hr>The Defaults are: 
     * <ul>
        * <li><b>X Acceleration Limit:</b> {@value #X_ACCELERATION_LIMIT}</li>
        * <li><b>Y Acceleration Limit:</b>  {@value #Y_ACCELERATION_LIMIT}</li>
        * <li><b>Underwater X Acceleration Limit:</b> {@value #UNDERWATER_X_ACCELERATION_LIMIT}</li>
        * <li><b>Underwater Y Acceleration Limit:</b> {@value #UNDERWATER_Y_ACCELERATION_LIMIT}</li>
     * </ul>
     */
    public void setAccelerationLimits_UseDefaultStrategy() {
        setAccelerationLimits(X_ACCELERATION_LIMIT,Y_ACCELERATION_LIMIT,UNDERWATER_X_ACCELERATION_LIMIT,UNDERWATER_Y_ACCELERATION_LIMIT);
    }

    @Override
    public void setMaxJumpCount(byte jumps,byte underwaterJumps) {
        this.LAND_maxJumpCount=jumps;
        this.UNDERWATER_maxJumpCount=underwaterJumps;
    }

    /**
     * The <b>Copy Strategy</b> uses the same values for both land and water.
     * @param maxJumpCount The max jump count to set for land and water.
     */
    public void setMaxJumpCount_UseCopyStrategy(byte maxJumpCount) {
        setMaxJumpCount(maxJumpCount,maxJumpCount);
    }

    /**
     * The <b>Half Strategy</b> uses halved values for water.
     * @param maxJumpCount The max jump count to set for land.
     */
    public void setMaxJumpCount_UseHalfStrategy(byte maxJumpCount) {
        setMaxJumpCount(maxJumpCount,(byte)(maxJumpCount/2));
    }


    /**
     * The <b>Default Strategy</b> uses the default values specified by the devs for "default physics" of any object.
     * <hr>The Defaults are: 
     * <ul>
        * <li><b>Max Jump Count:</b> {@value #MAX_JUMP_COUNT}</li>
        * <li><b>Underwater Max Jump Count:</b> {@value #UNDERWATER_MAX_JUMP_COUNT}</li>
     * </ul>
     */
    public void setMaxJumpCount_UseDefaultStrategy() {
        setMaxJumpCount(MAX_JUMP_COUNT,UNDERWATER_MAX_JUMP_COUNT);
    }

    @Override
    public void setGroundFriction(double groundFriction,double underwaterGroundFriction) {
        this.LAND_horizontal_friction=groundFriction;
        this.UNDERWATER_horizontal_friction=underwaterGroundFriction;
    }

    /**
     * The <b>Copy Strategy</b> uses the same values for both land and water.
     * @param groundFriction The ground friction to set for land and water.
     */
    public void setGroundFriction_UseCopyStrategy(double groundFriction) {
        setGroundFriction(groundFriction,groundFriction);
    }

    /**
     * The <b>Half Strategy</b> uses halved values for water.
     * @param groundFriction The ground friction to set for land.
     */
    public void setGroundFriction_UseHalfStrategy(double groundFriction) {
        setGroundFriction(groundFriction,groundFriction/2);
    }

    /**
     * The <b>Default Strategy</b> uses the default values specified by the devs for "default physics" of any object.
     * <hr>The Defaults are: 
     * <ul>
        * <li><b>Ground Friction</b> {@value #GROUND_FRICTION}</li>
        * <li><b>Underwater Ground Friction:</b> {@value #UNDERWATER_GROUND_FRICTION}</code></li>
     * </ul>
     */
    public void setGroundFriction_UseDefaultStrategy() {
        setGroundFriction(GROUND_FRICTION,UNDERWATER_GROUND_FRICTION);
    }

    @Override
    public void setAirFriction(double airFriction,double underwaterAirFriction) {
        this.LAND_horizontal_air_friction=airFriction;
        this.UNDERWATER_horizontal_air_friction=underwaterAirFriction;
    }

    /**
     * The <b>Copy Strategy</b> uses the same values for both land and water.
     * @param airFriction The air friction to set for land and water.
     */
    public void setAirFriction_UseCopyStrategy(double airFriction) {
        setAirFriction(airFriction,airFriction);
    }

    /**
     * The <b>Half Strategy</b> uses halved values for water.
     * @param airFriction The air friction to set for land.
     */
    public void setAirFriction_UseHalfStrategy(double airFriction) {
        setAirFriction(airFriction,airFriction/2);
    }

    /**
     * The <b>Default Strategy</b> uses the default values specified by the devs for "default physics" of any object.
     * <hr>The Defaults are: 
     * <ul>
        * <li><b>Air Friction</b> {@value #AIR_FRICTION}</li>
        * <li><b>Underwater Air Friction:</b> {@value #UNDERWATER_AIR_FRICTION}</code></li>
     * </ul>
     */
    public void setAirFriction_UseDefaultStrategy() {
        setAirFriction(AIR_FRICTION,UNDERWATER_AIR_FRICTION);
    }

    @Override
    public void setGroundDrag(double groundDrag,double underwaterGroundDrag) {
        this.LAND_horizontal_drag=groundDrag;
        this.UNDERWATER_horizontal_drag=underwaterGroundDrag;
    }

    /**
     * The <b>Copy Strategy</b> uses the same values for both land and water.
     * @param groundDrag The ground drag to set for land and water.
     */
    public void setGroundDrag_UseCopyStrategy(double groundDrag) {
        setGroundDrag(groundDrag,groundDrag);
    }

    /**
     * The <b>Half Strategy</b> uses halved values for water.
     * @param groundDrag The ground drag to set for land.
     */
    public void setGroundDrag_UseHalfStrategy(double groundDrag) {
        setGroundDrag(groundDrag,groundDrag/2);
    }

    /**
     * The <b>Default Strategy</b> uses the default values specified by the devs for "default physics" of any object.
     * <hr>The Defaults are: 
     * <ul>
        * <li><b>Ground Drag</b> {@value #GROUND_DRAG}</li>
        * <li><b>Underwater Ground Drag:</b> {@value #UNDERWATER_GROUND_DRAG}</code></li>
     * </ul>
     */
    public void setGroundDrag_UseDefaultStrategy() {
        setGroundDrag(GROUND_DRAG,UNDERWATER_GROUND_DRAG);
    }

    @Override
    public void setAirDrag(double airDrag,double underwaterAirDrag) {
        this.LAND_horizontal_air_drag=airDrag;
        this.UNDERWATER_horizontal_air_drag=underwaterAirDrag;
    }

    /**
     * The <b>Copy Strategy</b> uses the same values for both land and water.
     * @param airDrag The air drag to set for land and water.
     */
    public void setAirDrag_UseCopyStrategy(double airDrag) {
        setAirDrag(airDrag,airDrag);
    }

    /**
     * The <b>Half Strategy</b> uses halved values for water.
     * @param airDrag The air drag to set for land.
     */
    public void setAirDrag_UseHalfStrategy(double airDrag) {
        setAirDrag(airDrag,airDrag/2);
    }

    /**
     * The <b>Default Strategy</b> uses the default values specified by the devs for "default physics" of any object.
     * <hr>The Defaults are: 
     * <ul>
        * <li><b>Air Drag</b> {@value #AIR_DRAG}</li>
        * <li><b>Underwater Air Drag:</b> {@value #UNDERWATER_AIR_DRAG}</code></li>
     * </ul>
     */
    public void setAirDrag_UseDefaultStrategy() {
        setAirDrag(AIR_DRAG,UNDERWATER_AIR_DRAG);
    }

    @Override
    public void setSlidingVelocity(double slidingVelocity,double underwaterSlidingVelocity) {
        this.LAND_sliding_velocity=slidingVelocity;
        this.UNDERWATER_sliding_velocity=underwaterSlidingVelocity;
    }

    /**
     * The <b>Copy Strategy</b> uses the same values for both land and water.
     * @param slidingVelocity The sliding velocity to set for land and water.
     */
    public void setSlidingVelocity_UseCopyStrategy(double slidingVelocity) {
        setSlidingVelocity(slidingVelocity,slidingVelocity);
    }

    /**
     * The <b>Half Strategy</b> uses halved values for water.
     * @param slidingVelocity The sliding velocity to set for land.
     */
    public void setSlidingVelocity_UseHalfStrategy(double slidingVelocity) {
        setSlidingVelocity(slidingVelocity,slidingVelocity/2);
    }

    /**
     * The <b>Default Strategy</b> uses the default values specified by the devs for "default physics" of any object.
     * <hr>The Defaults are: 
     * <ul>
        * <li><b>Sliding Velocity</b> {@value #SLIDING_VELOCITY}</li>
        * <li><b>Underwater Sliding Velocity:</b> {@value #UNDERWATER_SLIDING_VELOCITY}</code></li>
     * </ul>
     */
    public void setSlidingVelocity_UseDefaultStrategy() {
        setSlidingVelocity(SLIDING_VELOCITY,UNDERWATER_SLIDING_VELOCITY);
    }

    @Override
    public void setSlidingAcceleration(double slidingAcceleration,double underwaterSlidingAcceleration) {
        this.LAND_sliding_acceleration=slidingAcceleration;
        this.UNDERWATER_sliding_acceleration=underwaterSlidingAcceleration;
    }

    /**
     * The <b>Copy Strategy</b> uses the same values for both land and water.
     * @param slidingAcceleration The sliding acceleration to set for land and water.
     */
    public void setSlidingAcceleration_UseCopyStrategy(double slidingAcceleration) {
        setSlidingAcceleration(slidingAcceleration,slidingAcceleration);
    }

    /**
     * The <b>Half Strategy</b> uses halved values for water.
     * @param slidingAcceleration The sliding acceleration to set for land.
     */
    public void setSlidingAcceleration_UseHalfStrategy(double slidingAcceleration) {
        setSlidingAcceleration(slidingAcceleration,slidingAcceleration/2);
    }

    /**
     * The <b>Default Strategy</b> uses the default values specified by the devs for "default physics" of any object.
     * <hr>The Defaults are: 
     * <ul>
        * <li><b>Sliding Acceleration</b> {@value #SLIDING_ACCELERATION}</li>
        * <li><b>Underwater Sliding Acceleration:</b> {@value #UNDERWATER_SLIDING_ACCELERATION}</code></li>
     * </ul>
     */
    public void setSlidingAcceleration_UseDefaultStrategy() {
        setSlidingAcceleration(SLIDING_ACCELERATION,UNDERWATER_SLIDING_ACCELERATION);
    }

    @Override
    public void setJumpVelocity(double jumpVelocity,double underwaterJumpVelocity) {
        this.LAND_jump_velocity=jumpVelocity;
        this.UNDERWATER_jump_velocity=underwaterJumpVelocity;
    }

    /**
     * The <b>Copy Strategy</b> uses the same values for both land and water.
     * @param jumpVelocity The jump velocity to set for land and water.
     */
    public void setJumpVelocity_UseCopyStrategy(double jumpVelocity) {
        setJumpVelocity(jumpVelocity,jumpVelocity);
    }

    /**
     * The <b>Half Strategy</b> uses halved values for water.
     * @param jumpVelocity The jump velocity to set for land.
     */
    public void setJumpVelocity_UseHalfStrategy(double jumpVelocity) {
        setJumpVelocity(jumpVelocity,jumpVelocity/2);
    }

    /**
     * The <b>Default Strategy</b> uses the default values specified by the devs for "default physics" of any object.
     * <hr>The Defaults are: 
     * <ul>
        * <li><b>Jump Velocity</b> {@value #JUMP_VELOCITY}</li>
        * <li><b>Underwater Jump Velocity:</b> {@value #UNDERWATER_JUMP_VELOCITY}</code></li>
     * </ul>
     */
    public void setJumpVelocity_UseDefaultStrategy() {
        setJumpVelocity(JUMP_VELOCITY,UNDERWATER_JUMP_VELOCITY);
    }

    @Override
    public void setGravity(double gravity, double underwaterGravity) {
        this.LAND_gravity=gravity;
        this.UNDERWATER_gravity=underwaterGravity;
    }

    /**
     * The <b>Copy Strategy</b> uses the same values for both land and water.
     * @param gravity The gravity to set for land and water.
     */
    public void setGravity_UseCopyStrategy(double gravity) {
        setGravity(gravity,gravity);
    }

    /**
     * The <b>Half Strategy</b> uses halved values for water.
     * @param gravity The gravity to set for land.
     */
    public void setGravity_UseHalfStrategy(double gravity) {
        setGravity(gravity,gravity/2);
    }

    /**
     * The <b>Default Strategy</b> uses the default values specified by the devs for "default physics" of any object.
     * <hr>The Defaults are: 
     * <ul>
        * <li><b>Gravity</b> {@value #GRAVITY}</li>
        * <li><b>Underwater Gravity:</b> {@value #UNDERWATER_GRAVITY}</code></li>
     * </ul>
     */
    public void setGravity_UseDefaultStrategy() {
        setGravity(GRAVITY,UNDERWATER_GRAVITY);
    }

    public double getGravity() {
        return gravity;
    }
}
