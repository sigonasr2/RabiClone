package sig.map;

public enum Tile {
    VOID(0,0,true,CollisionType.NONE), //File is populated by 0s by default. This represents the void.
    WALL(0,0,CollisionType.SOLID),
    FLOOR(1,0,CollisionType.SOLID),
    PLATFORM_LEDGE(2,0,CollisionType.SOLID),
    INVISIBLE_WALL(0,0,true,CollisionType.NONE),
    HIGHLIGHTED_TILE(3,0,CollisionType.NONE),
    SMALL_SOLID_LEFT(0,1,CollisionType.SOLID),
    SMALL_SOLID_RIGHT(1,1,CollisionType.SOLID),
    BIG_SOLID_LEFT1(2,1,CollisionType.SOLID),
    BIG_SOLID_LEFT2(3,1,CollisionType.SOLID),
    BIG_SOLID_RIGHT1(0,2,CollisionType.SOLID),
    BIG_SOLID_RIGHT2(1,2,CollisionType.SOLID),
    ;

    final public static int TILE_WIDTH=32;
    final public static int TILE_HEIGHT=32;
    final public static int TILE_SCREEN_COUNT_X=Map.MAP_WIDTH/TILE_WIDTH;
    final public static int TILE_SCREEN_COUNT_Y=Map.MAP_HEIGHT/TILE_HEIGHT;

    int spriteSheetX,spriteSheetY;
    boolean invisible;
    CollisionType collision;

    public boolean isInvisible() {
        return invisible;
    }
    public void setInvisible(boolean invisible) {
        this.invisible = invisible;
    }
    public CollisionType getCollision() {
        return collision;
    }
    public void setCollision(CollisionType collision) {
        this.collision = collision;
    }
    int tileWidth=TILE_WIDTH,tileHeight=TILE_HEIGHT;
    /*Tile(int spriteSheetX,int spriteSheetY,int tileWidth,int tileHeight) {
        this.spriteSheetX=spriteSheetX;
        this.spriteSheetY=spriteSheetY;
        this.tileWidth=tileWidth;
        this.tileHeight=tileHeight;
    }*/
    Tile(int spriteSheetX,int spriteSheetY,boolean invisible, CollisionType collision) {
        this.spriteSheetX=spriteSheetX;
        this.spriteSheetY=spriteSheetY;
        this.invisible=invisible;
        this.collision=collision;
    }
    Tile(int spriteSheetX,int spriteSheetY, CollisionType collision) {
        this(spriteSheetX, spriteSheetY, false, collision);
    }

    public int getSpriteSheetX() {
        return spriteSheetX;
    }
    public void setSpriteSheetX(int spriteSheetX) {
        this.spriteSheetX = spriteSheetX;
    }
    public int getSpriteSheetY() {
        return spriteSheetY;
    }
    public void setSpriteSheetY(int spriteSheetY) {
        this.spriteSheetY = spriteSheetY;
    }
    public int getTileWidth() {
        return tileWidth;
    }
    public void setTileWidth(int tileWidth) {
        this.tileWidth = tileWidth;
    }
    public int getTileHeight() {
        return tileHeight;
    }
    public void setTileHeight(int tileHeight) {
        this.tileHeight = tileHeight;
    }
}
