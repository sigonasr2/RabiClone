package sig.objects;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;

import sig.RabiClone;
import sig.engine.Alpha;
import sig.engine.Font;
import sig.engine.MouseScrollValue;
import sig.engine.PaletteColor;
import sig.engine.Panel;
import sig.engine.Sprite;
import sig.map.Background;
import sig.map.Map;
import sig.map.Tile;
import sig.map.View;
import sig.map.Type;

public class EditorRenderer extends LevelRenderer{

    Tile selectedTile = Tile.WALL;

    StringBuilder messageLog = new StringBuilder();
    final static long MESSAGE_TIME = 5000;
    long last_message_log = System.currentTimeMillis();

    final static StringBuilder HELP_TEXT = new StringBuilder("(F3) Toggle Camera  (F4) Toggle Map Type  (F5) Toggle Background");

    final static char CAMERA_SPD = 512;

    public EditorRenderer(Panel panel) {
        super(panel);
        setX(3.5*Tile.TILE_WIDTH);
        setY(3.5*Tile.TILE_HEIGHT);
        AddMessage(PaletteColor.YELLOW_GREEN,"Level editing mode",PaletteColor.NORMAL," started.");
    }

    private void AddMessage(Object...s) {
        messageLog.append('\n');
        for (int i=0;i<s.length;i++) {
            if (s[i] instanceof String) {
                messageLog.append((String)s[i]);
            } else if (s[i] instanceof PaletteColor) {
                messageLog.append((PaletteColor)s[i]);
            }
        }
        last_message_log = System.currentTimeMillis();
    }

   @Override 
    public void update(double updateMult) {
        int right = KeyHeld(KeyEvent.VK_RIGHT)||KeyHeld(KeyEvent.VK_D)?1:0;
        int left = KeyHeld(KeyEvent.VK_LEFT)||KeyHeld(KeyEvent.VK_A)?1:0;
        int up = KeyHeld(KeyEvent.VK_UP)||KeyHeld(KeyEvent.VK_W)?1:0;
        int down = KeyHeld(KeyEvent.VK_DOWN)||KeyHeld(KeyEvent.VK_S)?1:0;
        if (right-left!=0) {
            setX(getX()+(right-left)*CAMERA_SPD*updateMult);
        }
        if (up-down!=0) {
            setY(getY()+(down-up)*CAMERA_SPD*updateMult);
        }
        boolean left_mb = MouseHeld(MouseEvent.BUTTON1);
        boolean middle_mb = MouseHeld(MouseEvent.BUTTON2);
        boolean right_mb = MouseHeld(MouseEvent.BUTTON3);

        if(left_mb){
            RabiClone.CURRENT_MAP.ModifyTile(RabiClone.p.highlightedSquare.getX(), RabiClone.p.highlightedSquare.getY(), selectedTile);
        }
        if(KeyHeld(KeyEvent.VK_CONTROL)&&KeyHeld(KeyEvent.VK_S)){
            AddMessage("Saving map...");
            try {
                Map.SaveMap(RabiClone.CURRENT_MAP);
                AddMessage(RabiClone.CURRENT_MAP.toString()," has been saved successfully.");
            } catch (IOException e) {
                e.printStackTrace();
                AddMessage(PaletteColor.RED,"Map failed to save: ",e.getLocalizedMessage());
            }
        }
        updateMessageLog();
    }

    private void updateMessageLog() {
        if (messageLog.length()>0) {
            if (System.currentTimeMillis()-last_message_log>MESSAGE_TIME) {
                last_message_log=System.currentTimeMillis();
                int secondMarker = messageLog.indexOf("\n",messageLog.indexOf("\n")+1);
                messageLog.replace(messageLog.indexOf("\n"), secondMarker==-1?messageLog.length():secondMarker, "");
            }
        }
    }

    @Override
    protected void MouseScrolled(MouseScrollValue scrolled) {
        int up = scrolled==MouseScrollValue.UP?1:0;
        int down = scrolled==MouseScrollValue.DOWN?1:0;
        int tempIndex = selectedTile.ordinal()+down-up;
        int selectedIndex = tempIndex<0?Tile.values().length-1:tempIndex%Tile.values().length;
        selectedTile = Tile.values()[selectedIndex];
    }

    @Override
    public void draw(byte[] p) {
        super.draw(p);
        for (int y=(int)(this.getY()/Tile.TILE_HEIGHT);y<(int)(RabiClone.BASE_HEIGHT/Tile.TILE_HEIGHT+this.getY()/Tile.TILE_HEIGHT+1);y++) {
            if (y<0||y>Map.MAP_HEIGHT) {
                continue;
            }
            for (int x=(int)(0+this.getX()/Tile.TILE_WIDTH);x<(int)(RabiClone.BASE_WIDTH/Tile.TILE_WIDTH+this.getX()/Tile.TILE_WIDTH+1);x++) {
                if (x<0||x>Map.MAP_WIDTH) {
                    continue;
                }
                drawMapTileForEditorMode(x,y);
            }
        }
        for (int y=(int)(this.getY()/Tile.TILE_HEIGHT);y<(int)(RabiClone.BASE_HEIGHT/Tile.TILE_HEIGHT+this.getY()/Tile.TILE_HEIGHT+1);y++) {
            if (y<0||y>Map.MAP_HEIGHT) {
                continue;
            }
            for (int x=(int)(0+this.getX()/Tile.TILE_WIDTH);x<(int)(RabiClone.BASE_WIDTH/Tile.TILE_WIDTH+this.getX()/Tile.TILE_WIDTH+1);x++) {
                if (x<0||x>Map.MAP_WIDTH) {
                    continue;
                }
                drawTileGrid(p,x,y);
            }
        }
        Draw_Text(4,0,messageLog,Font.PROFONT_12);
        Draw_Text(4,RabiClone.BASE_HEIGHT-Font.PROFONT_12.getGlyphHeight()-4,HELP_TEXT,Font.PROFONT_12);
    }

    private void drawTileGrid(byte[] p, int x, int y) {
        if (x%Tile.TILE_SCREEN_COUNT_X==0&&y%Tile.TILE_SCREEN_COUNT_Y==0) {
            int xpos=(int)(x*Tile.TILE_WIDTH-getX());
            int ypos=(int)(y*Tile.TILE_HEIGHT-getY());
            Draw_Text(xpos+2,ypos+2,
                new StringBuilder("View:").append(PaletteColor.EMERALD).append(RabiClone.CURRENT_MAP.getView(x,y).ordinal())
                .append(PaletteColor.NORMAL).append("\nType:").append(PaletteColor.MIDNIGHT_BLUE).append(RabiClone.CURRENT_MAP.getType(x,y).ordinal())
                .append(PaletteColor.NORMAL).append("\nBackground:").append(PaletteColor.GRAPE).append(RabiClone.CURRENT_MAP.getBackground(x,y).ordinal())
            ,Font.PROFONT_12);
        }
        if (x%Tile.TILE_SCREEN_COUNT_X==0) {
            for (int yy=0;yy<Tile.TILE_HEIGHT;yy++) {
                int ypos=(int)(y*Tile.TILE_HEIGHT-getY()+yy);
                int xpos=(int)(x*Tile.TILE_WIDTH-getX());
                int index=ypos*Map.MAP_WIDTH+xpos;
                if (index<0||index>=p.length) {
                    continue;
                }
                Draw(p,index,PaletteColor.BLACK,Alpha.ALPHA0);
            }
        }
        if (y%Tile.TILE_SCREEN_COUNT_Y==0) {
            for (int xx=0;xx<Tile.TILE_HEIGHT;xx++) {
                int ypos=(int)(y*Tile.TILE_HEIGHT-getY());
                int xpos=(int)(x*Tile.TILE_WIDTH-getX()+xx);
                int index=ypos*Map.MAP_WIDTH+xpos;
                if (xpos<0||xpos>=Map.MAP_WIDTH||ypos<0||ypos>=Map.MAP_HEIGHT||index<0||index>=p.length) {
                    continue;
                }
                Draw(p,index,PaletteColor.BLACK,Alpha.ALPHA0);
            }
        }
    }

    @Override
    protected void drawMapTileForEditorMode(int x, int y) {
        if (x==RabiClone.p.highlightedSquare.getX()&&y==RabiClone.p.highlightedSquare.getY()) {
            double tileX = x*Tile.TILE_WIDTH-this.getX();
            double tileY = y*Tile.TILE_HEIGHT-this.getY();
            DrawTransparentTile(tileX,tileY,selectedTile,Alpha.ALPHA160);
            Draw_Text(tileX+2,tileY-Font.PROFONT_12.getGlyphHeight()-2,new StringBuilder(selectedTile.toString()),Font.PROFONT_12);
            Draw_Text_Ext(tileX+2,tileY+Tile.TILE_HEIGHT+2,new StringBuilder(RabiClone.CURRENT_MAP.getTile(x,y).toString()),Font.PROFONT_12,Alpha.ALPHA0,PaletteColor.CRIMSON);
        }
    }

    @Override
    protected void KeyPressed(int key) {
        int screenX = (int)(RabiClone.MOUSE_POS.getX()+getX())/Tile.TILE_WIDTH;
        int screenY = (int)(RabiClone.MOUSE_POS.getY()+getY())/Tile.TILE_HEIGHT;
        switch (key) {
            case KeyEvent.VK_F3:{
                RabiClone.CURRENT_MAP.setView(screenX,screenY,View.values()[(RabiClone.CURRENT_MAP.getView(screenX, screenY).ordinal()+1)%View.values().length]);
            }break;
            case KeyEvent.VK_F4:{
                RabiClone.CURRENT_MAP.setType(screenX,screenY,Type.values()[(RabiClone.CURRENT_MAP.getType(screenX, screenY).ordinal()+1)%Type.values().length]);
            }break;
            case KeyEvent.VK_F5:{
                RabiClone.CURRENT_MAP.setBackground(screenX,screenY,Background.values()[(RabiClone.CURRENT_MAP.getBackground(screenX, screenY).ordinal()+1)%Background.values().length]);
            }break;
        }
    }    
}
