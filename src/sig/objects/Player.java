package sig.objects;

import sig.RabiClone;
import sig.engine.Action;
import sig.engine.Panel;
import sig.engine.Rectangle;
import sig.engine.Sprite;
import sig.engine.Transform;
import sig.engine.objects.AnimatedObject;
import sig.map.Map;
import sig.map.Tile;
import sig.map.View;
import sig.objects.actor.PhysicsObject;
import sig.objects.actor.State;
import sig.objects.weapons.KnifeSwing;
import sig.utils.TimeUtils;

public class Player extends PhysicsObject{
    final static boolean LEFT = false;
    final static boolean RIGHT = true;
    final static long jump_fall_AnimationWaitTime = TimeUtils.millisToNanos(200);
    final static long slide_AnimationWaitTime = TimeUtils.millisToNanos(100);
    final static long slide_duration = TimeUtils.millisToNanos(700);
    final static long bellySlideDuration = TimeUtils.millisToNanos(1000);
    final static long weaponSwingAnimationTime = TimeUtils.millisToNanos(333);
    final static long weaponComboWaitTime = TimeUtils.millisToNanos(60);
    final static double finalComboJumpBackSpeedX = -185;
    final static double finalComboJumpBackSpeedY = -110;

    long weaponSwingTime = 0;

    State prvState = state;

    final double viewBoundaryX = RabiClone.BASE_WIDTH / 3;
    final double viewBoundaryY = RabiClone.BASE_HEIGHT / 3;
    View lastCameraView = View.FIXED;

    boolean spacebarReleased = true;
    boolean facing_direction = RIGHT;

    long spacebarPressed = RabiClone.TIME;
    long jump_slide_fall_StartAnimationTimer = -1;
    long slide_time = -1;
    long jumpHoldTime = TimeUtils.millisToNanos(150);
    long bellySlideTime = -1;

    final static long slideBufferTime = TimeUtils.millisToNanos(200);
    long slidePressed = -1;

    boolean onSlope = false;

    public Player(Panel panel) {
        super(Sprite.ERINA, 5, panel);
        setX(RabiClone.BASE_WIDTH / 2 - getAnimatedSpr().getWidth() / 2);
        setY(RabiClone.BASE_HEIGHT * (2 / 3d) - getAnimatedSpr().getHeight() / 2);
        setAccelerationLimits(100, 100);
        setVelocityLimits(246, 500);
        setGroundDrag(2000);
        setGroundFriction(PhysicsObject.NORMAL_FRICTION);
        setAirDrag(900);
        setAirFriction(180);
        setSlidingVelocity(164);
        setSlidingAcceleration(120);
        setJumpVelocity(PhysicsObject.NORMAL_JUMP_VELOCITY);
        setGravity(1300);
    }

    @Override
    public Rectangle setCollisionBounds() {
        return new Rectangle(10,6,12,25);
    }

    @Override
    public void update(double updateMult) {
        super.update(updateMult);
        handleCameraRoomMovement();

        switch (state) {
            case ATTACK:
                if (RabiClone.TIME - weaponSwingTime > weaponSwingAnimationTime) {
                    state=State.IDLE;
                }
                break;
            case ATTACK2:
                if (RabiClone.TIME - weaponSwingTime > weaponSwingAnimationTime) {
                    state=State.IDLE;
                }
                break;
            case ATTACK3:
                if (RabiClone.TIME - weaponSwingTime > weaponSwingAnimationTime) {
                    state=State.IDLE;
                }
                break;
            case FALLING:
                if (prvState != State.FALLING) {
                    jump_slide_fall_StartAnimationTimer = RabiClone.TIME;
                    setAnimatedSpr(Sprite.ERINA_JUMP_FALL1);
                    spacebarPressed = 0;
                }
                if (RabiClone.TIME - jump_slide_fall_StartAnimationTimer > jump_fall_AnimationWaitTime) {
                    setAnimatedSpr(Sprite.ERINA_JUMP_FALL);
                    jump_slide_fall_StartAnimationTimer = -1;
                }
                break;
            case IDLE:
                if (RabiClone.TIME - slidePressed <= slideBufferTime && state!=State.BELLYSLIDE) {
                    performSlide();
                    break;
                }

                jump_velocity = PhysicsObject.NORMAL_JUMP_VELOCITY;
                horizontal_friction = PhysicsObject.NORMAL_FRICTION;
                jump_slide_fall_StartAnimationTimer = -1;

                if (x_velocity != 0) {
                    setAnimatedSpr(Sprite.ERINA_WALK);
                    if (x_velocity > 0) {
                        facing_direction = RIGHT;
                    } else if (x_velocity < 0) {
                        facing_direction = LEFT;
                    }
                } else {
                    setAnimatedSpr(Sprite.ERINA);
                }
                break;
            case JUMP:
                if (jump_slide_fall_StartAnimationTimer == -1) {
                    jump_slide_fall_StartAnimationTimer = RabiClone.TIME;
                    setAnimatedSpr(Sprite.ERINA_JUMP_RISE1);
                }
                if (RabiClone.TIME - jump_slide_fall_StartAnimationTimer > jump_fall_AnimationWaitTime) {
                    setAnimatedSpr(Sprite.ERINA_JUMP_RISE);
                }
                break;
            case SLIDE:
                horizontal_friction = 0;
                if (jump_slide_fall_StartAnimationTimer == -1) {
                    jump_slide_fall_StartAnimationTimer = RabiClone.TIME;
                    setAnimatedSpr(Sprite.ERINA_SLIDE1);
                }
                if (RabiClone.TIME - jump_slide_fall_StartAnimationTimer > slide_AnimationWaitTime) {
                    setAnimatedSpr(Sprite.ERINA_SLIDE);
                }
                if (RabiClone.TIME - slide_time > slide_duration) {
                    if (KeyHeld(Action.MOVE_LEFT)) {
                        facing_direction = LEFT;
                    }
                    if (KeyHeld(Action.MOVE_RIGHT)) {
                        facing_direction = RIGHT;
                    }
                    state = State.IDLE;
                }
                if (KeyHeld(Action.MOVE_LEFT) && !KeyHeld(Action.MOVE_RIGHT)) {
                    if (facing_direction == LEFT && x_velocity > -sliding_velocity * 1.5 ||
                            facing_direction == RIGHT && x_velocity > sliding_velocity * 0.5) {
                        x_velocity -= sliding_acceleration * updateMult;
                    }
                } else if (KeyHeld(Action.MOVE_RIGHT) && !KeyHeld(Action.MOVE_LEFT)) {
                    if (facing_direction == LEFT && x_velocity < -sliding_velocity * 0.5 ||
                            facing_direction == RIGHT && x_velocity < sliding_velocity * 1.5) {
                        x_velocity += sliding_acceleration * updateMult;
                    }
                }
                break;
            case BELLYSLIDE:{
                if (RabiClone.TIME - bellySlideTime > bellySlideDuration) {
                    if (KeyHeld(Action.MOVE_LEFT)) {
                        facing_direction = LEFT;
                    }
                    if (KeyHeld(Action.MOVE_RIGHT)) {
                        facing_direction = RIGHT;
                    }
                    state = State.IDLE;
                }
                if (KeyHeld(Action.MOVE_LEFT) && !KeyHeld(Action.MOVE_RIGHT)) {
                    if (facing_direction == LEFT && x_velocity > -sliding_velocity * 1.5 ||
                            facing_direction == RIGHT && x_velocity > sliding_velocity * 0.5) {
                        x_velocity -= sliding_acceleration * updateMult;
                    }
                } else if (KeyHeld(Action.MOVE_RIGHT) && !KeyHeld(Action.MOVE_LEFT)) {
                    if (facing_direction == LEFT && x_velocity < -sliding_velocity * 0.5 ||
                            facing_direction == RIGHT && x_velocity < sliding_velocity * 1.5) {
                        x_velocity += sliding_acceleration * updateMult;
                    }
                }
            }break;
            case STAGGER:
                break;
            case UNCONTROLLABLE:
                break;
            default:
                break;
        }
        prvState = state;
        if (KeyHeld(Action.JUMP) && RabiClone.TIME - spacebarPressed < jumpHoldTime
        && state!=State.ATTACK2&&state!=State.ATTACK3) {
            y_velocity = jump_velocity;
        }
        // System.out.println(state);
    }

    @Override
    public void KeyReleased(Action a) {
        if (a == Action.JUMP) {
            spacebarPressed = 0;
            spacebarReleased = true;
        }
        if (state != State.SLIDE&&state!=State.BELLYSLIDE) {
            if ((a == Action.MOVE_LEFT) && (KeyHeld(Action.MOVE_RIGHT))) {
                facing_direction = RIGHT;
            } else if ((a == Action.MOVE_RIGHT) && (KeyHeld(Action.MOVE_LEFT))) {
                facing_direction = LEFT;
            }
        }
    }

    @Override
    @SuppressWarnings("incomplete-switch")
    public void KeyPressed(Action a) {
        switch (state) {
            case ATTACK:
                if (a==Action.ATTACK&&RabiClone.TIME-weaponSwingTime>weaponComboWaitTime) {
                    state=State.ATTACK2;
                    weaponSwingTime=RabiClone.TIME;
                }
                break;
            case ATTACK2:
                if (a==Action.ATTACK&&RabiClone.TIME-weaponSwingTime>weaponComboWaitTime) {
                    state=State.ATTACK3;
                    weaponSwingTime=RabiClone.TIME;
                }
                break;
            case ATTACK3:
                if (a==Action.ATTACK&&RabiClone.TIME-weaponSwingTime>weaponComboWaitTime) {
                    state=State.ATTACK4;
                    weaponSwingTime=RabiClone.TIME;
                    y_velocity = finalComboJumpBackSpeedY;
                    x_velocity = finalComboJumpBackSpeedX*(facing_direction?1:-1);
                }
                break;
            case IDLE:
                if (a == Action.SLIDE || a == Action.FALL) {
                    performSlide();
                }
                break;
            case FALLING:
                if (a == Action.SLIDE || a == Action.FALL) {
                    slidePressed = RabiClone.TIME;
                    // System.out.println("Queue up slide.");
                }
            case JUMP:
                if (jumpCount > 0 && spacebarReleased && (a == Action.JUMP)) {
                    jumpCount = 0;
                    y_velocity = jump_velocity;
                    spacebarReleased = false;
                    spacebarPressed = RabiClone.TIME;
                }
                break;
            case SLIDE:
                break;
            case STAGGER:
                break;
            case UNCONTROLLABLE:
                break;
            default:
                break;
        }
        if (a==Action.FALL&&(state==State.JUMP||state==State.FALLING)) {
            state=State.BELLYSLIDE;
            y_velocity=y_velocity_limit;
            x_velocity=60*(facing_direction?1:-1);
        } else
        if (a == Action.ATTACK&&(state==State.IDLE||state==State.FALLING||state==State.JUMP)&&(RabiClone.TIME-weaponSwingTime>=weaponSwingAnimationTime)) {
            RabiClone.OBJ.add(new KnifeSwing(Sprite.KNIFE_SWING,40,RabiClone.p,this));
            state=State.ATTACK;
            weaponSwingTime=RabiClone.TIME;
        }
        if (groundCollision) {
            if (spacebarReleased && (a == Action.JUMP) && jumpCount > 0
                &&state!=State.ATTACK2&&state!=State.ATTACK3) {
                state = State.JUMP;
                jumpCount--;
                y_velocity = jump_velocity;
                spacebarReleased = false;
                spacebarPressed = RabiClone.TIME;
                // System.out.println("Jump");
            }
        }
        if (state != State.SLIDE&&state!=State.BELLYSLIDE) {
            switch (a) {
                case MOVE_LEFT:
                    facing_direction = LEFT;
                    break;
                case MOVE_RIGHT:
                    facing_direction = RIGHT;
                    break;
            }
        }
    }

    private void performSlide() {
        slide_time = RabiClone.TIME;
        if (facing_direction) {
            x_velocity = sliding_velocity;
        } else {
            x_velocity = -sliding_velocity;
        }
        state = State.SLIDE;
    }

    private void handleCameraRoomMovement() {
        int tileX = (int) (getX()) / Tile.TILE_WIDTH;
        int tileY = (int) (getY()) / Tile.TILE_HEIGHT;
        double newX = RabiClone.level_renderer.getX(), newY = RabiClone.level_renderer.getY(); // By default we use a
                                                                                               // fixed view.
        View currentView = RabiClone.CURRENT_MAP.getView(tileX, tileY);
        if (currentView != lastCameraView) {
            lastCameraView = currentView;
            newX = (tileX / Tile.TILE_SCREEN_COUNT_X) * Map.MAP_WIDTH;
            newY = (tileY / Tile.TILE_SCREEN_COUNT_Y) * Map.MAP_HEIGHT;
        } else
            switch (currentView) {
                case LIGHT_FOLLOW:
                    if (getX() - RabiClone.level_renderer.getX() < viewBoundaryX) {
                        newX = getX() - viewBoundaryX;
                        int newTileX = (int) (newX) / Tile.TILE_WIDTH;
                        View newView = RabiClone.CURRENT_MAP.getView(newTileX, tileY);
                        if (newView != currentView) {
                            newX = (tileX / Tile.TILE_SCREEN_COUNT_X) * Map.MAP_WIDTH;
                        }
                    } else if (getX() - RabiClone.level_renderer.getX() > RabiClone.BASE_WIDTH - viewBoundaryX) {
                        newX = getX() - (RabiClone.BASE_WIDTH - viewBoundaryX);
                        int newTileX = (int) (getX() + viewBoundaryX) / Tile.TILE_WIDTH;
                        View newView = RabiClone.CURRENT_MAP.getView(newTileX, tileY);
                        if (newView != currentView) {
                            newX = (tileX / Tile.TILE_SCREEN_COUNT_X) * Map.MAP_WIDTH;
                        }
                    }
                    if (getY() - RabiClone.level_renderer.getY() < viewBoundaryY) {
                        newY = getY() - viewBoundaryY;
                        int newTileY = (int) (getY() - viewBoundaryY) / Tile.TILE_HEIGHT;
                        View newView = RabiClone.CURRENT_MAP.getView(tileX, newTileY);
                        if (newView != currentView) {
                            newY = (tileY / Tile.TILE_SCREEN_COUNT_Y) * Map.MAP_HEIGHT;
                        }
                    } else if (getY() - RabiClone.level_renderer.getY() > RabiClone.BASE_HEIGHT - viewBoundaryY) {
                        newY = getY() - (RabiClone.BASE_HEIGHT - viewBoundaryY);
                        int newTileY = (int) (getY() + viewBoundaryY) / Tile.TILE_HEIGHT;
                        View newView = RabiClone.CURRENT_MAP.getView(tileX, newTileY);
                        if (newView != currentView) {
                            newY = (tileY / Tile.TILE_SCREEN_COUNT_Y) * Map.MAP_HEIGHT;
                        }
                    }
                    break;
                case LIGHT_HORIZONTAL_FOLLOW:
                    if (getX() - RabiClone.level_renderer.getX() < viewBoundaryX) {
                        newX = getX() - viewBoundaryX;
                        int newTileX = (int) (newX) / Tile.TILE_WIDTH;
                        View newView = RabiClone.CURRENT_MAP.getView(newTileX, tileY);
                        if (newView != currentView) {
                            newX = (tileX / Tile.TILE_SCREEN_COUNT_X) * Map.MAP_WIDTH;
                        }
                    } else if (getX() - RabiClone.level_renderer.getX() > RabiClone.BASE_WIDTH - viewBoundaryX) {
                        newX = getX() - (RabiClone.BASE_WIDTH - viewBoundaryX);
                        int newTileX = (int) (getX() + viewBoundaryX) / Tile.TILE_WIDTH;
                        View newView = RabiClone.CURRENT_MAP.getView(newTileX, tileY);
                        if (newView != currentView) {
                            newX = (tileX / Tile.TILE_SCREEN_COUNT_X) * Map.MAP_WIDTH;
                        }
                    }
                    newY = (tileY / Tile.TILE_SCREEN_COUNT_Y) * Map.MAP_HEIGHT;
                    break;
                case LIGHT_VERTICAL_FOLLOW:
                    newX = (tileX / Tile.TILE_SCREEN_COUNT_X) * Map.MAP_WIDTH;
                    if (getY() - RabiClone.level_renderer.getY() < viewBoundaryY) {
                        newY = getY() - viewBoundaryY;
                        int newTileY = (int) (getY() - viewBoundaryY) / Tile.TILE_HEIGHT;
                        View newView = RabiClone.CURRENT_MAP.getView(tileX, newTileY);
                        if (newView != currentView) {
                            newY = (tileY / Tile.TILE_SCREEN_COUNT_Y) * Map.MAP_HEIGHT;
                        }
                    } else if (getY() - RabiClone.level_renderer.getY() > RabiClone.BASE_HEIGHT - viewBoundaryY) {
                        newY = getY() - (RabiClone.BASE_HEIGHT - viewBoundaryY);
                        int newTileY = (int) (getY() + viewBoundaryY) / Tile.TILE_HEIGHT;
                        View newView = RabiClone.CURRENT_MAP.getView(tileX, newTileY);
                        if (newView != currentView) {
                            newY = (tileY / Tile.TILE_SCREEN_COUNT_Y) * Map.MAP_HEIGHT;
                        }
                    }
                    break;
                case FIXED:
                    break;
                default:
                    break;
            }
        RabiClone.level_renderer.setX(newX < 0 ? 0 : newX);
        RabiClone.level_renderer.setY(newY < 0 ? 0 : newY);
    }

    @Override
    public void collisionEvent(AnimatedObject obj) {
        if (state==State.BELLYSLIDE) {
            if(obj instanceof PhysicsObject){
                PhysicsObject pobj = (PhysicsObject)obj;
                if(pobj.state!=State.STAGGER){
                    if(facing_direction){
                        setUncontrollable(0.2);
                        pobj.setStagger(0.3);
                        pobj.x_velocity = -300;
                        pobj.y_velocity = -120;
                    }else{
                        setUncontrollable(0.2);
                        pobj.setStagger(0.3);
                        pobj.x_velocity = 300;
                        pobj.y_velocity = -120;
                    }
                }
            }
        }
    }

    @Override
    public void draw(byte[] p) {
    }

    @Override
    public String toString() {
        return "Player [facing_direction=" + (facing_direction ? "RIGHT" : "LEFT") + ", groundCollision="
                + groundCollision + ", jumpCount="
                + jumpCount + ", x_velocity=" + x_velocity + ", y_velocity=" + y_velocity + ", x=" + getX() + ", y="
                + getY() + "]";
    }

    @Override
    public Transform getSpriteTransform() {
        return facing_direction?Transform.HORIZONTAL:Transform.NONE;
    }

    @Override
    public boolean rightKeyHeld() {
        return state!=State.ATTACK2&&state!=State.ATTACK3&&KeyHeld(Action.MOVE_RIGHT);
    }

    @Override
    public boolean leftKeyHeld() {
        return state!=State.ATTACK2&&state!=State.ATTACK3&&KeyHeld(Action.MOVE_LEFT);
    }

    public double getYVelocity() {
        return y_velocity;
    }

    @Override
    public boolean isFriendlyObject() {
        return true;
    }
    
}
