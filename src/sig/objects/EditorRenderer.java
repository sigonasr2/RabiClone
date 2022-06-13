package sig.objects;

import java.awt.event.MouseEvent;
import java.io.IOException;

import sig.RabiClone;
import sig.engine.Action;
import sig.engine.Alpha;
import sig.engine.Font;
import sig.engine.MouseScrollValue;
import sig.engine.PaletteColor;
import sig.engine.Panel;
import sig.engine.String;
import sig.map.Background;
import sig.map.DataTile;
import sig.map.Map;
import sig.map.Tile;
import sig.map.View;
import sig.map.Type;

public class EditorRenderer extends LevelRenderer{

    Tile selectedTile = Tile.WALL;
    DataTile selectedDataTile = DataTile.BUN1;

    String messageLog = new String();
    final static long MESSAGE_TIME = 5000;
    long last_message_log = System.currentTimeMillis();

    final static String HELP_TEXT = new String("(F3) Toggle Camera  (F4) Toggle Map Type  (F5) Toggle Background");

    final static char CAMERA_SPD = 512;

    boolean dataTileView=false;

    public EditorRenderer(Panel panel) {
        super(panel);
        setX(3.5*Tile.TILE_WIDTH);
        setY(3.5*Tile.TILE_HEIGHT);
        AddMessage(PaletteColor.YELLOW_GREEN,"Level editing mode",PaletteColor.NORMAL," started.");
    }

    private void AddMessage(Object...s) {
        messageLog.append('\n');
        messageLog.append(s);
        last_message_log = System.currentTimeMillis();
    }

   @Override 
    public void update(double updateMult) {
        int right = KeyHeld(Action.MOVE_RIGHT)?1:0;
        int left = KeyHeld(Action.MOVE_LEFT)?1:0;
        int up = KeyHeld(Action.JUMP)?1:0;
        int down = KeyHeld(Action.FALL)?1:0;
        if (right-left!=0) {
            setX(Math.max(0,getX()+(right-left)*CAMERA_SPD*updateMult));
        }
        if (up-down!=0) {
            setY(Math.max(0,getY()+(down-up)*CAMERA_SPD*updateMult));
        }
        boolean left_mb = MouseHeld(MouseEvent.BUTTON1);
        // boolean middle_mb = MouseHeld(MouseEvent.BUTTON2);
        // boolean right_mb = MouseHeld(MouseEvent.BUTTON3);

        if(left_mb){
            int tileX = (int)(RabiClone.MOUSE_POS.getX()+getX())/Tile.TILE_WIDTH;
            int tileY = (int)(RabiClone.MOUSE_POS.getY()+getY())/Tile.TILE_HEIGHT;
            if (dataTileView) {
                RabiClone.CURRENT_MAP.ModifyDataTile(tileX, tileY, selectedDataTile);
            } else {
                RabiClone.CURRENT_MAP.ModifyTile(tileX, tileY, selectedTile);
            }
        }
        if(KeyHeld(Action.SLIDE)&&KeyHeld(Action.FALL)){
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
    public void MouseScrolled(MouseScrollValue scrolled) {
        int up = scrolled==MouseScrollValue.UP?1:0;
        int down = scrolled==MouseScrollValue.DOWN?1:0;
        if (dataTileView) {
            int tempIndex = selectedDataTile.ordinal()+down-up;
            int selectedIndex = tempIndex<0?DataTile.values().length-1:tempIndex%DataTile.values().length;
            selectedDataTile = DataTile.values()[selectedIndex];
        } else {
            int tempIndex = selectedTile.ordinal()+down-up;
            int selectedIndex = tempIndex<0?Tile.values().length-1:tempIndex%Tile.values().length;
            selectedTile = Tile.values()[selectedIndex];
        }
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
                if (dataTileView) {
                    drawMapTileForDataTileMode(p,x,y);
                    if (RabiClone.CURRENT_MAP.getDataTile(x, y)!=DataTile.NULL) {
                        DrawDataTile(p,x*Tile.TILE_WIDTH-this.getX(),y*Tile.TILE_HEIGHT-this.getY(),RabiClone.CURRENT_MAP.getDataTile(x, y));
                    }
                } else {
                    drawMapTileForEditorMode(x,y);
                }
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
        Draw_Rect(p,PaletteColor.YELLOW,2,0,messageLog.getBounds(Font.PROFONT_12).getX(),messageLog.getBounds(Font.PROFONT_12).getY());
        Draw_Text(4,0,messageLog,Font.PROFONT_12);
        Draw_Text(4,RabiClone.BASE_HEIGHT-Font.PROFONT_12.getGlyphHeight()-4,HELP_TEXT,Font.PROFONT_12);
    }

    private void drawTileGrid(byte[] p, int x, int y) {
        if (x%Tile.TILE_SCREEN_COUNT_X==0&&y%Tile.TILE_SCREEN_COUNT_Y==0) {
            int xpos=(int)(x*Tile.TILE_WIDTH-getX());
            int ypos=(int)(y*Tile.TILE_HEIGHT-getY());
            Draw_Text(xpos+2,ypos+2,
                new String("View:").append(PaletteColor.EMERALD).append(RabiClone.CURRENT_MAP.getView(x,y))
                .append(PaletteColor.NORMAL).append("\nType:").append(PaletteColor.MIDNIGHT_BLUE).append(RabiClone.CURRENT_MAP.getType(x,y))
                .append(PaletteColor.NORMAL).append("\nBackground:").append(PaletteColor.GRAPE).append(RabiClone.CURRENT_MAP.getBackground(x,y))
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

    protected void drawMapTileForEditorMode(int x, int y) {
        int tilerX = (int)(RabiClone.MOUSE_POS.getX()+getX())/Tile.TILE_WIDTH;
        int tilerY = (int)(RabiClone.MOUSE_POS.getY()+getY())/Tile.TILE_HEIGHT;
        if (x==tilerX&&y==tilerY) {
            double tileX = x*Tile.TILE_WIDTH-this.getX();
            double tileY = y*Tile.TILE_HEIGHT-this.getY();
            DrawTransparentTile(tileX,tileY,selectedTile,Alpha.ALPHA160);
            Draw_Text(tileX+2,tileY-Font.PROFONT_12.getGlyphHeight()-2,new String(selectedTile.toString()),Font.PROFONT_12);
            Draw_Text_Ext(tileX+2,tileY+Tile.TILE_HEIGHT+2,new String(RabiClone.CURRENT_MAP.getTile(x,y).toString()),Font.PROFONT_12,Alpha.ALPHA0,PaletteColor.CRIMSON);
        }
    }

    protected void drawMapTileForDataTileMode(byte[] p, int x, int y) {
        int tilerX = (int)(RabiClone.MOUSE_POS.getX()+getX())/Tile.TILE_WIDTH;
        int tilerY = (int)(RabiClone.MOUSE_POS.getY()+getY())/Tile.TILE_HEIGHT;
        if (x==tilerX&&y==tilerY) {
            double tileX = x*Tile.TILE_WIDTH-this.getX();
            double tileY = y*Tile.TILE_HEIGHT-this.getY();
            DrawTransparentDataTile(p,tileX,tileY,selectedDataTile,PaletteColor.GRAPE);
            Draw_Text_Ext(tileX+2,tileY+Tile.TILE_HEIGHT+2,selectedDataTile.getDescription(),Font.PROFONT_12,Alpha.ALPHA0,PaletteColor.CRIMSON);
        }
    }

    protected void DrawTransparentDataTile(byte[] p, double x, double y, DataTile tile,PaletteColor col) {
        Draw_Rect(p,col,x,y,Tile.TILE_WIDTH,Tile.TILE_HEIGHT);
        Draw_Text_Ext(x+2,y+2,new String(tile.toString()),Font.PROFONT_12,Alpha.ALPHA0,PaletteColor.WHITE);
    }

    protected void DrawDataTile(byte[] p, double x, double y, DataTile tile) {
        DrawTransparentDataTile(p,x,y,tile,PaletteColor.MIDNIGHT_BLUE);
    }

    @Override
    @SuppressWarnings("incomplete-switch")
    public void KeyPressed(Action a) {
        int tileX = (int)(RabiClone.MOUSE_POS.getX()+getX())/Tile.TILE_WIDTH;
        int tileY = (int)(RabiClone.MOUSE_POS.getY()+getY())/Tile.TILE_HEIGHT;
        switch (a) {
            case PLAY_GAME:{
                RabiClone.OBJ.remove(RabiClone.level_renderer);
                RabiClone.OBJ.add(RabiClone.level_renderer = new LevelRenderer(RabiClone.p));
                RabiClone.StartGame();
            }break;
            case LEVEL_EDITOR:{
                dataTileView=!dataTileView;
            }break;
            case EDITOR_SET_VIEW:{
                RabiClone.CURRENT_MAP.setView(tileX,tileY,View.values()[(RabiClone.CURRENT_MAP.getView(tileX, tileY).ordinal()+1)%View.values().length]);
            }break;
            case EDITOR_SET_TYPE:{
                RabiClone.CURRENT_MAP.setType(tileX,tileY,Type.values()[(RabiClone.CURRENT_MAP.getType(tileX, tileY).ordinal()+1)%Type.values().length]);
            }break;
            case EDITOR_SET_BACKGROUND:{
                RabiClone.CURRENT_MAP.setBackground(tileX,tileY,Background.values()[(RabiClone.CURRENT_MAP.getBackground(tileX, tileY).ordinal()+1)%Background.values().length]);
            }break;
        }
    } 

    
}
