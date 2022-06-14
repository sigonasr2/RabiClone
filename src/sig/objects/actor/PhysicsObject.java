package sig.objects.actor;

import sig.RabiClone;
import sig.engine.AnimatedSprite;
import sig.engine.Panel;
import sig.engine.objects.AnimatedObject;
import sig.engine.objects.CollisionEntity;
import sig.map.Tile;

public abstract class PhysicsObject extends AnimatedObject implements CollisionEntity,PhysicsObjectRequirements{
    final public static double GRAVITY = 1300;
    final public static double NORMAL_FRICTION = 6400;
    final public static double NORMAL_JUMP_VELOCITY = -300;
    final public static double WALKING_SPEED_LIMIT = 164;

    State state = State.IDLE;
    double x_velocity,y_velocity;
    double x_acceleration,y_acceleration;
    double x_velocity_limit,y_velocity_limit;
    boolean groundCollision;
    byte maxJumpCount=2;
    byte jumpCount=0;

    double horizontal_air_friction,horizontal_air_drag;
    double horizontal_friction,horizontal_drag;

    protected PhysicsObject(AnimatedSprite spr, double animationSpd, Panel panel) {
        super(spr, animationSpd, panel);
    }

    @Override
    public void update(double updateMult) {
        super.update(updateMult);
        handleMovementPhysics(updateMult);
    }

    protected void handleMovementPhysics(double updateMult) {
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
                    if (checkCollision(getX()-getSprite().getWidth()/2+getCollisionBox().getX2()-1,y+getCollisionBox().getY2()-getSprite().getHeight()/2)||
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
                    if (checkCollision(getX()-getSprite().getWidth()/2+getCollisionBox().getX2()-1,y+getCollisionBox().getY()-getSprite().getHeight()/2)||
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

        double startingX=getX();
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
                        //x_velocity = ;
                    }
                } 
            }
        }
        if (!groundCollision){
            this.setY(this.getY()+displacement_y);
            y_acceleration = GRAVITY;
            if(y_velocity>0 && state!=State.SLIDE){
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
    
}
