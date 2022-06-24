package sig.objects;

import java.awt.event.KeyEvent;

import sig.RabiClone;
import sig.engine.Action;
import sig.engine.Alpha;
import sig.engine.Font;
import sig.engine.Key;
import sig.engine.PaletteColor;
import sig.engine.Panel;
import sig.engine.Sprite;
import sig.engine.Transform;
import sig.engine.objects.AnimatedObject;
import sig.engine.objects.Object;
import sig.engine.String;
import sig.map.Background;
import sig.map.DataTile;
import sig.map.Map;
import sig.map.Tile;
import sig.objects.actor.PhysicsObject;
import sig.objects.actor.RenderedObject;
import sig.objects.actor.State;
import sig.utils.TimeUtils;

public class LevelRenderer extends Object{

    final static long staggerJitterWaitTime=TimeUtils.millisToNanos(200);

    long staggerTimer=0;
    int staggerOffsetX=2;

    public LevelRenderer(Panel panel) {
        super(panel);
        this.setSprite(Sprite.TILE_SHEET);
    }

   @Override 
    public void update(double updateMult) {
        for (int y=(int)(this.getY()/Tile.TILE_HEIGHT)-RabiClone.EVENT_BOUNDARY_RANGE;y<(int)(RabiClone.BASE_HEIGHT/Tile.TILE_HEIGHT+this.getY()/Tile.TILE_HEIGHT+1+RabiClone.EVENT_BOUNDARY_RANGE);y++) {
            if (y<0||y>Map.MAP_HEIGHT) {
                continue;
            }
            for (int x=(int)(0+this.getX()/Tile.TILE_WIDTH)-RabiClone.EVENT_BOUNDARY_RANGE;x<(int)(RabiClone.BASE_WIDTH/Tile.TILE_WIDTH+this.getX()/Tile.TILE_WIDTH+1+RabiClone.EVENT_BOUNDARY_RANGE);x++) {
                if (x<0||x>Map.MAP_WIDTH) {
                    continue;
                }
                if (RabiClone.CURRENT_MAP.getDataTileValue(x,y)<8192&&RabiClone.CURRENT_MAP.getDataTile(x,y)!=DataTile.NULL) {
                    if (!RabiClone.CURRENT_MAP.getDataTile(x,y).perform(x*Tile.TILE_WIDTH,y*Tile.TILE_HEIGHT)) {
                        RabiClone.CURRENT_MAP.ModifyDataTile(x, y, DataTile.NULL);
                    }
                }
            }
        }
        if (RabiClone.TIME-staggerTimer>staggerJitterWaitTime) {
            staggerOffsetX*=-1;
        }
    }

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
        for (int i=0;i<RabiClone.OBJ.size();i++) {
            Object o = RabiClone.OBJ.get(i);
            if (o instanceof RenderedObject) {
                if (o instanceof AnimatedObject) {
                    Draw_Animated_Object((AnimatedObject)o,o.getSpriteTransform());
                } else {
                    Draw_Object(o,o.getSpriteTransform());
                }
            }
        }
        if (RabiClone.player!=null) {
            Draw_Text(4,4,new String(RabiClone.player.getYVelocity()),Font.PROFONT_12);
            Draw_Text(4,4+Font.PROFONT_12.getGlyphHeight(),new String(RabiClone.scaleTime),Font.PROFONT_12);
        }
    }

    @SuppressWarnings("unused")
    private void RenderCollisionGrid(byte[] p) {
        for (int y=0;y<RabiClone.BASE_HEIGHT;y++) {
            for (int x=0;x<RabiClone.BASE_WIDTH;x++) {
                if (RabiClone.COLLISION[((int)getY()+y)*RabiClone.BASE_WIDTH*Tile.TILE_WIDTH+((int)getX()+x)]) {
                    p[y*RabiClone.BASE_WIDTH+x]=(byte)PaletteColor.CRIMSON.ordinal();
                }
            }
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

    @Override
    public void drawOverlay(byte[] p) {
        //RenderCollisionGrid(p);
        //RenderPlayerCollisionGrid(p);
    }

    @SuppressWarnings("unused")
    private void RenderPlayerCollisionGrid(byte[] p) {
        for (int y=0;y<RabiClone.BASE_HEIGHT;y++) {
            for (int x=0;x<RabiClone.BASE_WIDTH;x++) {
                if (RabiClone.PLAYER_COLLISION[y*RabiClone.BASE_WIDTH+x]) {
                    p[y*RabiClone.BASE_WIDTH+x]=(byte)PaletteColor.MANTIS.ordinal();
                }
            }
        }
    }

    /**
     * Draws an object where its sprite is centered among its position and drawn relative to the camera position.
     * @param object
     */
    protected void Draw_Object(Object object) {
        super.Draw_Sprite(object.getX()-this.getX()-object.getSprite().getWidth()/2, Math.round(object.getY()-this.getY()-object.getSprite().getHeight()/2), object.getSprite());
    }

    protected void Draw_Object(Object object, Transform transform) {
        super.Draw_Sprite(object.getX()-this.getX()-object.getSprite().getWidth()/2, Math.round(object.getY()-this.getY()-object.getSprite().getHeight()/2), object.getSprite(), transform);
    }

    protected void Draw_Animated_Object(AnimatedObject object) {
        Draw_Animated_Object(object,Transform.NONE);
    }

    protected void Draw_Animated_Object(AnimatedObject object, Transform transform){
        if (object instanceof PhysicsObject) {
            PhysicsObject po = (PhysicsObject)object;
            super.Draw_Animated_Sprite(object.getX()-this.getX()-object.getAnimatedSpr().getWidth()/2+(po.state==State.STAGGER?staggerOffsetX:0), Math.round(object.getY()-this.getY()-object.getAnimatedSpr().getHeight()/2), object.getAnimatedSpr(), object.getCurrentFrame(), transform);
        } else {
            super.Draw_Animated_Sprite(object.getX()-this.getX()-object.getAnimatedSpr().getWidth()/2, Math.round(object.getY()-this.getY()-object.getAnimatedSpr().getHeight()/2), object.getAnimatedSpr(), object.getCurrentFrame(), transform);
        }
    }

    private void DrawTile(double x, double y, Tile tile) {
        Draw_Sprite_Partial(x,y, tile.getSpriteSheetX()*tile.getTileWidth(), tile.getSpriteSheetY()*tile.getTileHeight(), tile.getTileWidth(), tile.getTileHeight(), getSprite(), 0, Transform.NONE);
    }

    protected void DrawTransparentTile(double x, double y, Tile tile, Alpha alpha) {
        Draw_Sprite_Partial_Ext(x,y, tile.getSpriteSheetX()*tile.getTileWidth(), tile.getSpriteSheetY()*tile.getTileHeight(), tile.getTileWidth(), tile.getTileHeight(), getSprite(), alpha, Transform.NONE);
    }

    @Override
    @SuppressWarnings("incomplete-switch")
    public void KeyPressed(Action a) {
        switch(a) {
            case LEVEL_EDITOR:{
                RabiClone.OBJ.clear();
                RabiClone.ResetGame();
                Map.LoadMap(RabiClone.CURRENT_MAP);
                RabiClone.OBJ.add(RabiClone.level_renderer = new EditorRenderer(RabiClone.p));
            }break;
        }
        if (Key.isKeyHeld(KeyEvent.VK_F3)) {
            RabiClone.OBJ.clear();
            RabiClone.ResetGame();
            RabiClone.OBJ.add(RabiClone.control_settings_menu = new ConfigureControls(RabiClone.p));
        }
    }
    
    @Override
    public Transform getSpriteTransform() {
        return Transform.NONE;
    }
}
