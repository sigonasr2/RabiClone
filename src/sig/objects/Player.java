package sig.objects;

import sig.RabiClone;
import sig.engine.Action;
import sig.engine.AnimatedObject;
import sig.engine.Panel;
import sig.engine.Sprite;
import sig.map.CollisionType;
import sig.map.Map;
import sig.map.Tile;
import sig.map.View;
import sig.objects.actor.State;

public class Player extends AnimatedObject{
    final static double GRAVITY = 1300;
    final static double NORMAL_FRICTION = 6400;
    final static double NORMAL_JUMP_VELOCITY = -300;
    final static boolean LEFT = false;
    final static boolean RIGHT = true;
    final static int jump_fall_AnimationWaitTime = 200;
    final static int slide_AnimationWaitTime = 100;
    final static int slide_duration = 700;

    final static double WALKING_SPEED_LIMIT = 164;

    double y_acceleration = GRAVITY;
    double y_acceleration_limit = 100;
    double x_acceleration = 0;
    double x_acceleration_limit = 100;
    double x_velocity = 0;
    double x_velocity_limit = 246;
    double y_velocity = 5;
    double y_velocity_limit = 500;
    double sliding_velocity = 164;
    double sliding_acceleration = 120;

    double horizontal_drag = 2000;
    double horizontal_friction = NORMAL_FRICTION;
    double horizontal_air_drag = 800;
    double horizontal_air_friction = 180;

    double jump_velocity = NORMAL_JUMP_VELOCITY;

    int maxJumpCount=2;
    int jumpCount=maxJumpCount;
    State state = State.IDLE;
    State prvState = state;

    final double viewBoundaryX=RabiClone.BASE_WIDTH/3;
    final double viewBoundaryY=RabiClone.BASE_HEIGHT/3;
    View lastCameraView = View.FIXED;

    boolean groundCollision = false;
    boolean spacebarReleased = true;
    boolean facing_direction = RIGHT;

    long spacebarPressed = System.currentTimeMillis();
    long jump_slide_fall_StartAnimationTimer = -1;
    long slide_time = -1;
    int jumpHoldTime = 150;

    final static int slideBufferTime = 200;
    long slidePressed = -1;

    public Player(Panel panel) {
        super(Sprite.ERINA,5,panel);
        setX(RabiClone.BASE_WIDTH/2-getAnimatedSpr().getWidth()/2);
        setY(RabiClone.BASE_HEIGHT*(2/3d)-getAnimatedSpr().getHeight()/2);
    }


    @Override
    public void update(double updateMult) {
        super.update(updateMult);
        handleMovementPhysics(updateMult);
        handleCameraRoomMovement();

        switch(state){
            case ATTACK:
                break;
            case FALLING:
                if(prvState!=State.FALLING){
                    jump_slide_fall_StartAnimationTimer = System.currentTimeMillis();
                    setAnimatedSpr(Sprite.ERINA_JUMP_FALL1);
                }
                if(System.currentTimeMillis()-jump_slide_fall_StartAnimationTimer>jump_fall_AnimationWaitTime){
                    setAnimatedSpr(Sprite.ERINA_JUMP_FALL);
                    jump_slide_fall_StartAnimationTimer=-1;
                }
                break;
            case IDLE:
                if (System.currentTimeMillis()-slidePressed<=slideBufferTime) {
                    performSlide();
                    break;
                }

                jump_velocity = NORMAL_JUMP_VELOCITY;
                horizontal_friction = NORMAL_FRICTION;
                jump_slide_fall_StartAnimationTimer=-1;

                if(x_velocity!=0){
                    setAnimatedSpr(Sprite.ERINA_WALK);
                }
                else{
                    setAnimatedSpr(Sprite.ERINA);
                }
                break;
            case JUMP:
                if(prvState==State.SLIDE){
                    //jump_velocity=-500;
                }
                if(jump_slide_fall_StartAnimationTimer==-1){
                    jump_slide_fall_StartAnimationTimer = System.currentTimeMillis();
                    setAnimatedSpr(Sprite.ERINA_JUMP_RISE1);
                }
                if(System.currentTimeMillis()-jump_slide_fall_StartAnimationTimer>jump_fall_AnimationWaitTime){
                    setAnimatedSpr(Sprite.ERINA_JUMP_RISE);
                }
                break;
            case SLIDE:
                horizontal_friction=0;
                if(jump_slide_fall_StartAnimationTimer==-1){
                    jump_slide_fall_StartAnimationTimer = System.currentTimeMillis();
                    setAnimatedSpr(Sprite.ERINA_SLIDE1);
                }
                if(System.currentTimeMillis()-jump_slide_fall_StartAnimationTimer>slide_AnimationWaitTime){
                    setAnimatedSpr(Sprite.ERINA_SLIDE);
                }
                if(System.currentTimeMillis()-slide_time>slide_duration){
                    if(KeyHeld(Action.MOVE_LEFT)){
                        facing_direction=LEFT;
                    }
                    if(KeyHeld(Action.MOVE_RIGHT)){
                        facing_direction=RIGHT;
                    }
                    state=State.IDLE;
                }
                if (KeyHeld(Action.MOVE_LEFT)&&!KeyHeld(Action.MOVE_RIGHT)) {
                    if (facing_direction==LEFT&&x_velocity>-sliding_velocity*1.5||
                        facing_direction==RIGHT&&x_velocity>sliding_velocity*0.5) {
                            x_velocity-=sliding_acceleration*updateMult;
                        }
                } else
                if (KeyHeld(Action.MOVE_RIGHT)&&!KeyHeld(Action.MOVE_LEFT)) {
                    if (facing_direction==LEFT&&x_velocity<-sliding_velocity*0.5||
                        facing_direction==RIGHT&&x_velocity<sliding_velocity*1.5) {
                            x_velocity+=sliding_acceleration*updateMult;
                        }
                }
                break;
            case STAGGER:
                break;
            case UNCONTROLLABLE:
                break;
            default:
                break;
        }
        prvState = state;
        if (KeyHeld(Action.JUMP)&&System.currentTimeMillis()-spacebarPressed<jumpHoldTime) {
            y_velocity=jump_velocity;
        }
    }


    @Override
    protected void KeyReleased(Action a) {
        if (a==Action.JUMP) {
            spacebarPressed=0;
            spacebarReleased=true;
        }
        if(state!=State.SLIDE){
            if((a==Action.MOVE_LEFT)&&(KeyHeld(Action.MOVE_RIGHT))){
                facing_direction=RIGHT;
            } else
            if((a==Action.MOVE_RIGHT)&&(KeyHeld(Action.MOVE_LEFT))){
                facing_direction=LEFT;
            }
        }
    }

    @Override
    @SuppressWarnings("incomplete-switch")
    protected void KeyPressed(Action a) {
        switch(state){
            case ATTACK:
                break;
            case IDLE:
                if(a==Action.SLIDE||a==Action.FALL){
                    performSlide();
                }
                break;
            case FALLING:
                if (a==Action.SLIDE||a==Action.FALL) {
                    slidePressed=System.currentTimeMillis();
                    //System.out.println("Queue up slide.");
                }
            case JUMP:
                if(jumpCount>0 && spacebarReleased && (a==Action.JUMP)){
                    jumpCount=0;
                    y_velocity = jump_velocity;
                    spacebarReleased=false;
                    spacebarPressed=System.currentTimeMillis();
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
        if (groundCollision) {
            if (spacebarReleased&&(a==Action.JUMP)&&jumpCount>0) {
                state = State.JUMP;
                jumpCount--;
                y_velocity = jump_velocity;
                spacebarReleased=false;
                spacebarPressed=System.currentTimeMillis();
                //System.out.println("Jump");
            }
        }
        if(state!=State.SLIDE){
            switch(a){
                case MOVE_LEFT:
                    facing_direction=LEFT;
                break;
                case MOVE_RIGHT:
                    facing_direction=RIGHT;
                break;
            }   
        }
    }


    private void performSlide() {
        slide_time = System.currentTimeMillis();
        if(facing_direction){
            x_velocity=sliding_velocity;
        }
        else{
            x_velocity=-sliding_velocity;
        }
        state=State.SLIDE;
    }


    private void handleCameraRoomMovement() {
        int tileX = (int)(getX())/Tile.TILE_WIDTH;
        int tileY = (int)(getY())/Tile.TILE_HEIGHT;
        double newX=RabiClone.level_renderer.getX(),newY=RabiClone.level_renderer.getY(); //By default we use a fixed view.
        View currentView = RabiClone.CURRENT_MAP.getView(tileX, tileY);
        if (currentView!=lastCameraView) {
            lastCameraView=currentView;
            newX=(tileX/Tile.TILE_SCREEN_COUNT_X)*Map.MAP_WIDTH;
            newY=(tileY/Tile.TILE_SCREEN_COUNT_Y)*Map.MAP_HEIGHT;
        } else
        switch (currentView) {
            case LIGHT_FOLLOW:
                if (getX()-RabiClone.level_renderer.getX()<viewBoundaryX) {
                    newX=getX()-viewBoundaryX;
                    int newTileX = (int)(newX)/Tile.TILE_WIDTH;
                    View newView = RabiClone.CURRENT_MAP.getView(newTileX, tileY);
                    if (newView!=currentView) {
                        newX=(tileX/Tile.TILE_SCREEN_COUNT_X)*Map.MAP_WIDTH;
                    }
                } else if (getX()-RabiClone.level_renderer.getX()>RabiClone.BASE_WIDTH-viewBoundaryX) {
                    newX=getX()-(RabiClone.BASE_WIDTH-viewBoundaryX);
                    int newTileX = (int)(getX()+viewBoundaryX)/Tile.TILE_WIDTH;
                    View newView = RabiClone.CURRENT_MAP.getView(newTileX, tileY);
                    if (newView!=currentView) {
                        newX=(tileX/Tile.TILE_SCREEN_COUNT_X)*Map.MAP_WIDTH;
                    }
                }
                if (getY()-RabiClone.level_renderer.getY()<viewBoundaryY) {
                    newY=getY()-viewBoundaryY;
                    int newTileY = (int)(getY()-viewBoundaryY)/Tile.TILE_HEIGHT;
                    View newView = RabiClone.CURRENT_MAP.getView(tileX, newTileY);
                    if (newView!=currentView) {
                        newY=(tileY/Tile.TILE_SCREEN_COUNT_Y)*Map.MAP_HEIGHT;
                    }
                } else if (getY()-RabiClone.level_renderer.getY()>RabiClone.BASE_HEIGHT-viewBoundaryY) {
                    newY=getY()-(RabiClone.BASE_HEIGHT-viewBoundaryY);
                    int newTileY = (int)(getY()+viewBoundaryY)/Tile.TILE_HEIGHT;
                    View newView = RabiClone.CURRENT_MAP.getView(tileX, newTileY);
                    if (newView!=currentView) {
                        newY=(tileY/Tile.TILE_SCREEN_COUNT_Y)*Map.MAP_HEIGHT;
                    }
                }
                break;
            case LIGHT_HORIZONTAL_FOLLOW:
                if (getX()-RabiClone.level_renderer.getX()<viewBoundaryX) {
                    newX=getX()-viewBoundaryX;
                    int newTileX = (int)(newX)/Tile.TILE_WIDTH;
                    View newView = RabiClone.CURRENT_MAP.getView(newTileX, tileY);
                    if (newView!=currentView) {
                        newX=(tileX/Tile.TILE_SCREEN_COUNT_X)*Map.MAP_WIDTH;
                    }
                } else if (getX()-RabiClone.level_renderer.getX()>RabiClone.BASE_WIDTH-viewBoundaryX) {
                    newX=getX()-(RabiClone.BASE_WIDTH-viewBoundaryX);
                    int newTileX = (int)(getX()+viewBoundaryX)/Tile.TILE_WIDTH;
                    View newView = RabiClone.CURRENT_MAP.getView(newTileX, tileY);
                    if (newView!=currentView) {
                        newX=(tileX/Tile.TILE_SCREEN_COUNT_X)*Map.MAP_WIDTH;
                    }
                }
                newY=(tileY/Tile.TILE_SCREEN_COUNT_Y)*Map.MAP_HEIGHT;
                break;
            case LIGHT_VERTICAL_FOLLOW:
                newX=(tileX/Tile.TILE_SCREEN_COUNT_X)*Map.MAP_WIDTH;
                if (getY()-RabiClone.level_renderer.getY()<viewBoundaryY) {
                    newY=getY()-viewBoundaryY;
                    int newTileY = (int)(getY()-viewBoundaryY)/Tile.TILE_HEIGHT;
                    View newView = RabiClone.CURRENT_MAP.getView(tileX, newTileY);
                    if (newView!=currentView) {
                        newY=(tileY/Tile.TILE_SCREEN_COUNT_Y)*Map.MAP_HEIGHT;
                    }
                } else if (getY()-RabiClone.level_renderer.getY()>RabiClone.BASE_HEIGHT-viewBoundaryY) {
                    newY=getY()-(RabiClone.BASE_HEIGHT-viewBoundaryY);
                    int newTileY = (int)(getY()+viewBoundaryY)/Tile.TILE_HEIGHT;
                    View newView = RabiClone.CURRENT_MAP.getView(tileX, newTileY);
                    if (newView!=currentView) {
                        newY=(tileY/Tile.TILE_SCREEN_COUNT_Y)*Map.MAP_HEIGHT;
                    }
                }
                break;
            case FIXED:
                break;
            default:
                break;
        }
        RabiClone.level_renderer.setX(newX<0?0:newX);
        RabiClone.level_renderer.setY(newY<0?0:newY);
    }


    private void handleMovementPhysics(double updateMult) {
        int right = KeyHeld(Action.MOVE_RIGHT)?1:0;
        int left = KeyHeld(Action.MOVE_LEFT)?1:0;
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
        for(int i=0;i<4;i++){
            double check_distance_x = (displacement_x/4)*(i+1);

            Tile checked_tile_top_right = RabiClone.CURRENT_MAP.getTile((int)(getX()+getAnimatedSpr().getWidth()/2-4+check_distance_x)/Tile.TILE_WIDTH, (int)(getY()+getAnimatedSpr().getHeight()/2)/Tile.TILE_HEIGHT);
            Tile checked_tile_top_left = RabiClone.CURRENT_MAP.getTile((int)(getX()-getAnimatedSpr().getWidth()/2+4+check_distance_x)/Tile.TILE_WIDTH, (int)(getY()+getAnimatedSpr().getHeight()/2)/Tile.TILE_HEIGHT);
            if(checked_tile_top_right.getCollision()==CollisionType.BLOCK||checked_tile_top_left.getCollision()==CollisionType.BLOCK){
                //System.out.println(checked_tile_top_right.getCollision()+"//"+checked_tile_top_left.getCollision());
                if (checked_tile_top_right.getCollision()==CollisionType.BLOCK) {
                    setX(((int)(getX()-getAnimatedSpr().getWidth()/2)/Tile.TILE_WIDTH)*Tile.TILE_WIDTH+Tile.TILE_WIDTH/2+3+check_distance_x);
                } else {
                    setX(((int)(getX()+getAnimatedSpr().getWidth())/Tile.TILE_WIDTH)*Tile.TILE_WIDTH-Tile.TILE_WIDTH/2-3+check_distance_x);
                }
                x_acceleration = 0;
                x_velocity = 0;
                sideCollision=true;
            }
        }
        if (y_velocity==0) {
            Tile checked_tile_bottom_right = RabiClone.CURRENT_MAP.getTile((int)(getX()+getAnimatedSpr().getWidth()/2-4)/Tile.TILE_WIDTH, (int)(getY()+getAnimatedSpr().getHeight()/2+1)/Tile.TILE_HEIGHT);
            Tile checked_tile_bottom_left = RabiClone.CURRENT_MAP.getTile((int)(getX()-getAnimatedSpr().getWidth()/2+4)/Tile.TILE_WIDTH, (int)(getY()+getAnimatedSpr().getHeight()/2+1)/Tile.TILE_HEIGHT);
            if (!(checked_tile_bottom_right.getCollision()==CollisionType.BLOCK||checked_tile_bottom_left.getCollision()==CollisionType.BLOCK)) {
                groundCollision=false;
            } else {
                groundCollision=true;
                jumpCount=maxJumpCount;
            }
        } else {
            //System.out.println(x_velocity);
            //System.out.println(((int)(getX()+getAnimatedSpr().getWidth()/2+displacement_x)/Tile.TILE_WIDTH)+"//"+((int)(getY()+getAnimatedSpr().getHeight()/2)/Tile.TILE_HEIGHT));
            boolean collisionOccured=false;
            for(int i=0;i<4;i++){
                double check_distance_y = (displacement_y/4)*(i+1);
                Tile checked_tile_bottom_right = RabiClone.CURRENT_MAP.getTile((int)(getX()+getAnimatedSpr().getWidth()/2-4)/Tile.TILE_WIDTH, (int)(getY()+getAnimatedSpr().getHeight()/2+check_distance_y)/Tile.TILE_HEIGHT);
                Tile checked_tile_bottom_left = RabiClone.CURRENT_MAP.getTile((int)(getX()-getAnimatedSpr().getWidth()/2+4)/Tile.TILE_WIDTH, (int)(getY()+getAnimatedSpr().getHeight()/2+check_distance_y)/Tile.TILE_HEIGHT);
                //System.out.println((int)getX()/Tile.TILE_WIDTH);
                if(checked_tile_bottom_right.getCollision()==CollisionType.BLOCK||checked_tile_bottom_left.getCollision()==CollisionType.BLOCK){
                    setY((getY()-check_distance_y));
                    y_acceleration = 0;
                    y_velocity = 0;
                    groundCollision=true;
                    collisionOccured=true;
                    state = State.IDLE;
                    break;
                }
            }
            if (!collisionOccured) {
                groundCollision=false;
            }
        }
        if (!groundCollision){
            this.setY(this.getY()+displacement_y);
            y_acceleration = GRAVITY;
            if(y_velocity>0){
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


    private void handleKeyboardMovement(double updateMult, int movement, double friction, double drag) {
        if (movement!=0&&Math.abs(x_velocity)<WALKING_SPEED_LIMIT) {
            x_acceleration=drag*(movement);
        } else {
            if (x_velocity!=0) {
                x_velocity=x_velocity>0
                    ?x_velocity-friction*updateMult>0
                        ?x_velocity-friction*updateMult
                        :0
                    :x_velocity+friction*updateMult<0
                        ?x_velocity+friction*updateMult
                        :0;
            }
            x_acceleration=0;
        }
    }

    @Override
    public void draw(byte[] p) {}


    @Override
    public String toString() {
        return "Player [facing_direction=" + (facing_direction?"RIGHT":"LEFT") + ", groundCollision=" + groundCollision + ", jumpCount="
                + jumpCount + ", x_velocity=" + x_velocity + ", y_velocity=" + y_velocity + ", x=" + getX() + ", y=" + getY() + "]";
    }
    
}
