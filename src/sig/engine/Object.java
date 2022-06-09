package sig.engine;

import java.awt.event.MouseEvent;
import sig.DrawLoop;

public abstract class Object implements GameEntity{
    double x,y;
    Sprite spr;
    Panel panel;
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

    public void drawBackground(byte[] p) {
    }

    public void drawOverlay(byte[] p) {
    }

    protected void Draw(byte[] canvas, int index, PaletteColor col, Alpha alpha) {
        DrawLoop.Draw(canvas, index, (byte)col.ordinal(), alpha);
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

	protected void Draw_Animated_Sprite(double x, double y, AnimatedSprite sprite, double frameIndex){
        DrawLoop.Draw_Animated_Sprite(x,y,sprite,frameIndex);
    }

    protected void Draw_Animated_Sprite(double x, double y, AnimatedSprite sprite, double frameIndex, Transform transform){
        DrawLoop.Draw_Animated_Sprite(x,y,sprite,frameIndex,transform);
    }

	protected void Draw_Text(double x, double y, StringBuilder string, Font font){
        DrawLoop.Draw_Text(x,y,string,font);
    }

	protected void Draw_Text_Ext(double x, double y, StringBuilder string, Font font, Alpha alpha, PaletteColor col){
        DrawLoop.Draw_Text_Ext(x,y,string,font,alpha,col);
    }

	protected void Draw_Sprite_Partial(double x, double y, double xOffset, double yOffset, double w, double h, Sprite sprite, double frame_index, Transform transform){
        DrawLoop.Draw_Sprite_Partial(x,y,xOffset,yOffset,w,h,sprite,frame_index,transform);
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

	protected void KeyPressed(Action a) {
	}

	protected void KeyReleased(Action a) {
	}

	protected void MousePressed(MouseEvent e) {
	}

	protected void MouseReleased(MouseEvent e) {
	}

    protected boolean MouseHeld(int mb) {
		return panel.MOUSE.getOrDefault(mb,false);
	}

    /**
     * @param scrolled -1 is UP, 1 is DOWN
     * @return
     */
    protected void MouseScrolled(MouseScrollValue scrolled) {
	}
}
