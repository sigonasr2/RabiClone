package sig.objects;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import sig.RabiClone;
import sig.engine.Alpha;
import sig.engine.Font;
import sig.engine.MouseScrollValue;
import sig.engine.PaletteColor;
import sig.engine.Panel;
import sig.engine.Sprite;
import sig.map.Map;
import sig.map.Tile;

public class EditorRenderer extends LevelRenderer{

    Tile selectedTile = Tile.WALL;

    final StringBuilder tempText = new StringBuilder("Hello World!\n\n  Test newlines!\nTest\nTest2\nTest3");
    int frameCount=0;
    StringBuilder frameCheck = new StringBuilder("I am blue! Frame:").append(frameCount);

    public EditorRenderer(Panel panel) {
        super(panel);
    }

   @Override 
    public void update(double updateMult) {
        int right = KeyHeld(KeyEvent.VK_RIGHT)||KeyHeld(KeyEvent.VK_D)?1:0;
        int left = KeyHeld(KeyEvent.VK_LEFT)||KeyHeld(KeyEvent.VK_A)?1:0;
        int up = KeyHeld(KeyEvent.VK_UP)||KeyHeld(KeyEvent.VK_W)?1:0;
        int down = KeyHeld(KeyEvent.VK_DOWN)||KeyHeld(KeyEvent.VK_S)?1:0;
        /*if (right-left!=0) {
            setX(getX()+(right-left)*512*updateMult);
        }
        if (up-down!=0) {
            setY(getY()+(down-up)*512*updateMult);
        }*/
        boolean left_mb = MouseHeld(MouseEvent.BUTTON1);
        boolean middle_mb = MouseHeld(MouseEvent.BUTTON2);
        boolean right_mb = MouseHeld(MouseEvent.BUTTON3);

        if(left_mb){
            RabiClone.CURRENT_MAP.ModifyTile(RabiClone.p.highlightedSquare.getX(), RabiClone.p.highlightedSquare.getY(), selectedTile);
        }
        if(KeyHeld(KeyEvent.VK_CONTROL)&&KeyHeld(KeyEvent.VK_S)){
            System.out.println("Saving map");
            Map.SaveMap(RabiClone.CURRENT_MAP);
            System.out.println("Map saved");
        }
    }

    @Override
    protected void MouseScrolled(MouseScrollValue scrolled) {
        int up = scrolled==MouseScrollValue.UP?1:0;
        int down = scrolled==MouseScrollValue.DOWN?1:0;
        int tempIndex = selectedTile.ordinal()+down-up;
        int selectedIndex = tempIndex<0?Tile.values().length-1:tempIndex%Tile.values().length;
        selectedTile = Tile.values()[selectedIndex];
        System.out.println(selectedTile);
    }

    @Override
    public void draw(byte[] p) {
        super.draw(p);
        Draw_Text(32,16,tempText,Font.PROFONT_12);
        Draw_Text_Ext(104,137,new StringBuilder("I am blue! Frame:").append(frameCount++),Font.PROFONT_12,Alpha.ALPHA0,PaletteColor.SLATE_BLUE);
    }

    @Override
    protected void drawMapTileForEditorMode(int x, int y) {
        if (x==RabiClone.p.highlightedSquare.getX()&&y==RabiClone.p.highlightedSquare.getY()) {
            DrawTransparentTile(x*Tile.TILE_WIDTH-this.getX(),y*Tile.TILE_HEIGHT-this.getY(),selectedTile,Alpha.ALPHA160);
        }
    }    
}
