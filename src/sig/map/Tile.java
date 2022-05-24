package sig.map;

public enum Tile {
    VOID(0,0,true), //File is populated by 0s by default. This represents the void.
    WALL(0,0),
    FLOOR(1,0),
    PLATFORM_LEDGE(2,0),
    INVISIBLE_WALL(0,0,true),
    ;

    final public static int TILE_WIDTH=32;
    final public static int TILE_HEIGHT=32;

    int spriteSheetX,spriteSheetY;
    boolean invisible;

    int tileWidth=TILE_WIDTH,tileHeight=TILE_HEIGHT;
    /*Tile(int spriteSheetX,int spriteSheetY,int tileWidth,int tileHeight) {
        this.spriteSheetX=spriteSheetX;
        this.spriteSheetY=spriteSheetY;
        this.tileWidth=tileWidth;
        this.tileHeight=tileHeight;
    }*/
    Tile(int spriteSheetX,int spriteSheetY,boolean invisible) {
        this.spriteSheetX=spriteSheetX;
        this.spriteSheetY=spriteSheetY;
        this.invisible=invisible;
    }
    Tile(int spriteSheetX,int spriteSheetY) {
        this(spriteSheetX, spriteSheetY, false);
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
