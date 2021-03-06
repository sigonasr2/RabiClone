package sig.engine.objects;

import java.awt.event.MouseEvent;
import sig.DrawLoop;
import sig.RabiClone;
import sig.engine.Action;
import sig.engine.Alpha;
import sig.engine.AnimatedSprite;
import sig.engine.Font;
import sig.engine.KeyBind;
import sig.engine.MouseScrollValue;
import sig.engine.PaletteColor;
import sig.engine.Panel;
import sig.engine.Rectangle;
import sig.engine.Sprite;
import sig.engine.String;
import sig.engine.Transform;

public abstract class Object implements GameEntity{
    double x,y;
    Alpha transparency = Alpha.ALPHA0;
    Sprite spr;
    Panel panel;
    protected Rectangle collisionBox;

    public Panel getPanel() {
        return panel;
    }

    public void setPanel(Panel panel) {
        this.panel = panel;
    }

    boolean markedForDeletion;

    public boolean isMarkedForDeletion() {
        return markedForDeletion;
    }

    public void setMarkedForDeletion(boolean markedForDeletion) {
        this.markedForDeletion = markedForDeletion;
    }

    protected Object(Panel panel) {
        this.panel=panel;
    }
    
    public double getX() {
        return x;
    }
    public void setX(double x) {
        this.x = x;
    }
    public double getY() {
        return y;
    }
    public void setY(double y) {
        this.y = y;
    }
    public Sprite getSprite() {
        return spr;
    }
    public void setSprite(Sprite spr) {
        this.spr = spr;
    }
    public Alpha getTransparency() {
        return transparency;
    }
    public void setTransparency(Alpha alpha) {
        this.transparency=alpha;
    }
    public void drawBackground(byte[] p) {
    }

    public void drawOverlay(byte[] p) {
    }

    protected void Draw(byte[] canvas, int index, PaletteColor col) {
        DrawLoop.Draw(canvas, index, (byte)col.ordinal());
    }

    protected void Draw_Rect(byte[] p,PaletteColor col,double x,double y,double w,double h) {
        DrawLoop.Fill_Rect(p, (byte)col.ordinal(), x, y, w, h);
    }

    protected void Draw_Line(byte[] p,double x1,double y1,double x2,double y2,PaletteColor col,Alpha alpha) {
        DrawLoop.Draw_Line(p, (int)x1, (int)y1, (int)x2, (int)y2, (byte)col.ordinal(), alpha);
    }

	protected void Draw_Sprite(double x, double y, Sprite sprite){
        DrawLoop.Draw_Sprite(x,y,sprite);
    }

	protected void Draw_Sprite(double x, double y, Sprite sprite, Transform transform){
        DrawLoop.Draw_Sprite(x,y,sprite,transform);
    }

	protected void Draw_Animated_Sprite(double x, double y, AnimatedSprite sprite, double frameIndex){
        DrawLoop.Draw_Animated_Sprite(x,y,sprite,frameIndex);
    }

    protected void Draw_Animated_Sprite(double x, double y, AnimatedSprite sprite, double frameIndex, Alpha alpha, Transform transform){
        DrawLoop.Draw_Animated_Sprite(x,y,sprite,frameIndex,alpha,transform);
    }

	protected void Draw_Text(double x, double y, String string, Font font){
        DrawLoop.Draw_Text(x,y,string,font);
    }

	protected void Draw_Text_Ext(double x, double y, String string, Font font, Alpha alpha, PaletteColor col){
        DrawLoop.Draw_Text_Ext(x,y,string,font,alpha,col);
    }

	protected void Draw_Sprite_Partial(double x, double y, double xOffset, double yOffset, double w, double h, Sprite sprite, double frame_index,Alpha alpha, Transform transform){
        DrawLoop.Draw_Sprite_Partial(x,y,xOffset,yOffset,w,h,sprite,frame_index,alpha,transform);
    }

	protected void Draw_Sprite_Partial_Ext(double x, double y, double xOffset, double yOffset, double w, double h, Sprite sprite, Alpha alpha,Transform transform){
        DrawLoop.Draw_Sprite_Partial_Ext(x,y,xOffset,yOffset,w,h,sprite,alpha,transform);
    }

	protected void Draw_Sprite_Partial_Ext(double x, double y, double xOffset, double yOffset, double w, double h, Sprite sprite, double frame_index, Alpha alpha, PaletteColor col,Transform transform){
        DrawLoop.Draw_Sprite_Partial_Ext(x,y,xOffset,yOffset,w,h,sprite,frame_index,alpha,col,transform);
    }

	protected boolean KeyHeld(Action a) {
		return KeyBind.isKeyHeld(a);
	}

	public void KeyPressed(Action a) {
	}

	public void KeyReleased(Action a) {
	}

	public void MousePressed(MouseEvent e) {
	}

	public void MouseReleased(MouseEvent e) {
	}

    public boolean isUnderwater() {
        return getY()>=RabiClone.CURRENT_MAP.getMap().getWaterLevel();
    }

    protected boolean MouseHeld(int mb) {
		return panel.MOUSE.getOrDefault(mb,false);
	}

    /**
     * @param scrolled -1 is UP, 1 is DOWN
     * @return
     */
    public void MouseScrolled(MouseScrollValue scrolled) {
	}
}
