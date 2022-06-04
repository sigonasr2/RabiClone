package sig.objects;

import sig.RabiClone;
import sig.engine.Alpha;
import sig.engine.AnimatedObject;
import sig.engine.Object;
import sig.engine.Panel;
import sig.engine.Sprite;
import sig.map.Background;
import sig.map.Map;
import sig.map.Tile;

public class LevelRenderer extends Object{

    public LevelRenderer(Panel panel) {
        super(panel);
        this.setSprite(Sprite.TILE_SHEET);
    }

   @Override 
    public void update(double updateMult) {}

    @Override
    public void draw(byte[] p) {
        for (int y=(int)(this.getY()/Tile.TILE_HEIGHT);y<(int)(RabiClone.BASE_HEIGHT/Tile.TILE_HEIGHT+this.getY()/Tile.TILE_HEIGHT+1);y++) {
            if (y<0||y>Map.MAP_HEIGHT) {
                continue;
            }
            for (int x=(int)(0+this.getX()/Tile.TILE_WIDTH);x<(int)(RabiClone.BASE_WIDTH/Tile.TILE_WIDTH+this.getX()/Tile.TILE_WIDTH+1);x++) {
                if (x<0||x>Map.MAP_WIDTH) {
                    continue;
                }
                if (RabiClone.CURRENT_MAP.getTile(x,y)!=Tile.VOID) {
                    DrawTile(x*Tile.TILE_WIDTH-this.getX(),y*Tile.TILE_HEIGHT-this.getY(),RabiClone.CURRENT_MAP.getTile(x,y));
                    //System.out.println((x*Tile.TILE_WIDTH+(this.getX()%Tile.TILE_WIDTH) )+","+(y*Tile.TILE_HEIGHT+(this.getY()%Tile.TILE_HEIGHT)));
                }
            }
        }
        if (RabiClone.player!=null) {
            Draw_Animated_Object(RabiClone.player);
        }
    }

    @Override
    public void drawBackground(byte[] p) {
        int screenX = (int)(getX())/Tile.TILE_WIDTH;
        int screenY = (int)(getY())/Tile.TILE_HEIGHT;
        Background targetBackground = RabiClone.CURRENT_MAP.getBackground(screenX, screenY);
        for (int y=0;y<RabiClone.BASE_HEIGHT;y++) {
            for (int x=0;x<RabiClone.BASE_WIDTH;x++) {
                int index = y*RabiClone.BASE_WIDTH+x;
                p[index] = targetBackground.getPixels()[
                    ((y+(int)(getY()*targetBackground.getScrollSpeed()))%targetBackground.getHeight())*targetBackground.getWidth()+((x+(int)(getX()*targetBackground.getScrollSpeed()))%targetBackground.getWidth())
                ];
            }
        }
    }

    protected void drawMapTileForEditorMode(int x, int y) {}

    /**
     * Draws an object where its sprite is centered among its position and drawn relative to the camera position.
     * @param object
     */
    protected void Draw_Object(Object object) {
        super.Draw_Sprite(object.getX()-this.getX()-object.getSprite().getWidth()/2, Math.round(object.getY()-this.getY()-object.getSprite().getHeight()/2), object.getSprite());
    }

    protected void Draw_Animated_Object(AnimatedObject object) {
        super.Draw_Animated_Sprite(object.getX()-this.getX()-object.getAnimatedSpr().getWidth()/2, Math.round(object.getY()-this.getY()-object.getAnimatedSpr().getHeight()/2), object.getAnimatedSpr(), object.getCurrentFrame());
    }

    private void DrawTile(double x, double y, Tile tile) {
        Draw_Sprite_Partial(x,y, tile.getSpriteSheetX()*tile.getTileWidth(), tile.getSpriteSheetY()*tile.getTileHeight(), tile.getTileWidth(), tile.getTileHeight(), getSprite(), 0);
    }

    protected void DrawTransparentTile(double x, double y, Tile tile, Alpha alpha) {
        Draw_Sprite_Partial_Ext(x,y, tile.getSpriteSheetX()*tile.getTileWidth(), tile.getSpriteSheetY()*tile.getTileHeight(), tile.getTileWidth(), tile.getTileHeight(), getSprite(), alpha);
    }
    
}
