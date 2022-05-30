package sig;

import sig.engine.Alpha;
import sig.engine.Panel;
import sig.engine.Sprite;

public class DrawLoop {
    public static Panel panel;
    public static void drawGame(Panel panel) {
        DrawLoop.panel=panel;
        byte[] p = panel.pixel;
        
		for (int y=0;y<RabiClone.BASE_HEIGHT;y++) {
			for (int x=0;x<RabiClone.BASE_WIDTH;x++) {
        		p[y*RabiClone.BASE_WIDTH+x]=0;//RGB
        	}
        }
		
		for (int i=0;i<RabiClone.OBJ.size();i++) {
			RabiClone.OBJ.get(i).draw(p);
		}
		
		for (int i=0;i<RabiClone.OBJ.size();i++) {
			RabiClone.OBJ.get(i).drawOverlay(p);
		}
    }

	public static void Draw_Sprite(double x, double y, Sprite sprite){
		Draw_Sprite_Partial(x,y,0,0,sprite.getWidth(),sprite.getHeight(),sprite);
	}

	public static void Draw_Sprite_Partial(double x, double y, double xOffset, double yOffset, double w, double h, Sprite sprite){
		byte[] p = panel.pixel;
		for(int X=(int)xOffset;X<(int)(w+xOffset);X++){
			for(int Y=(int)yOffset;Y<(int)(h+yOffset);Y++){
				if (X+x-xOffset<0||Y+y-yOffset<0||X-xOffset+x>=RabiClone.BASE_WIDTH||Y-yOffset+y>=RabiClone.BASE_HEIGHT) {
					continue;
				} else {
					int index = (Y-(int)yOffset+(int)y)*RabiClone.BASE_WIDTH+X-(int)xOffset+(int)x;
					if (index<0||index>=p.length||p[index]==sprite.getBi_array()[Y*sprite.getWidth()+X]) {
						continue;
					} else {
						Draw(p,index,sprite.getBi_array()[Y*sprite.getWidth()+X],Alpha.ALPHA0);	
						//Draw(p,index,sprite.getBi_array()[Y*sprite.getWidth()+X],false);
					}
				}
			}	
		}
	}

	public static void Draw_Sprite_Partial_Ext(double x, double y, double xOffset, double yOffset, double w, double h, Sprite sprite, Alpha alpha){
		byte[] p = panel.pixel;
		for(int X=(int)xOffset;X<(int)(w+xOffset);X++){
			for(int Y=(int)yOffset;Y<(int)(h+yOffset);Y++){
				if (X+x-xOffset<0||Y+y-yOffset<0||X-xOffset+x>=RabiClone.BASE_WIDTH||Y-yOffset+y>=RabiClone.BASE_HEIGHT) {
					continue;
				} else {
					int index = (Y-(int)yOffset+(int)y)*RabiClone.BASE_WIDTH+X-(int)xOffset+(int)x;
					if (index<0||index>=p.length||p[index]==sprite.getBi_array()[Y*sprite.getWidth()+X]) {
						continue;
					} else {
						Draw(p,index,sprite.getBi_array()[Y*sprite.getWidth()+X],alpha);	
					}
				}
			}	
		}
	}

	public static void Draw(byte[] canvas,int index, byte col, Alpha alpha) {
		canvas[index]=(byte)(((int)(col)&0xff)+(alpha.ordinal()*(32)));
	}
}
