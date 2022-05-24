package sig.objects;

import java.awt.event.KeyEvent;
import sig.RabiClone;
import sig.engine.Object;
import sig.engine.Panel;
import sig.engine.Sprite;
import sig.map.Tile;

public class LevelRenderer extends Object{

    public LevelRenderer(Panel panel) {
        super(panel);
        this.setSprite(Sprite.TILE_SHEET);
    }

    @Override
    public void update(double updateMult) {
        int right = KeyHeld(KeyEvent.VK_RIGHT)?1:0;
        int left = KeyHeld(KeyEvent.VK_LEFT)?1:0;
        int up = KeyHeld(KeyEvent.VK_UP)?1:0;
        int down = KeyHeld(KeyEvent.VK_DOWN)?1:0;
        if (right-left!=0) {
            setX(getX()+(right-left)*256*updateMult);
        }
        if (up-down!=0) {
            setY(getY()+(down-up)*256*updateMult);
        }
    }

    @Override
    public void draw(int[] p) {
        for (int y=(int)(0+this.getY()/Tile.TILE_HEIGHT);y<(int)(RabiClone.BASE_HEIGHT/Tile.TILE_HEIGHT+this.getY()/Tile.TILE_HEIGHT+1);y++) {
            for (int x=(int)(0+this.getX()/Tile.TILE_WIDTH);x<(int)(RabiClone.BASE_WIDTH/Tile.TILE_WIDTH+this.getX()/Tile.TILE_WIDTH+1);x++) {
                if (RabiClone.CURRENT_MAP.getTile(x,y)!=Tile.VOID) {
                    DrawTile(x*Tile.TILE_WIDTH-this.getX(),y*Tile.TILE_HEIGHT-this.getY(),RabiClone.CURRENT_MAP.getTile(x,y));
                    //System.out.println((x*Tile.TILE_WIDTH+(this.getX()%Tile.TILE_WIDTH) )+","+(y*Tile.TILE_HEIGHT+(this.getY()%Tile.TILE_HEIGHT)));
                }
            }
        }
    }

    private void DrawTile(double x, double y, Tile tile) {
        Draw_Sprite_Partial(x,y, tile.getSpriteSheetX()*tile.getTileWidth(), tile.getSpriteSheetY()*tile.getTileHeight(), tile.getTileWidth(), tile.getTileHeight(), getSprite());
    }
    
}
