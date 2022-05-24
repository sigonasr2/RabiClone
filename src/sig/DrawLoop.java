package sig;

import sig.engine.Panel;
import sig.engine.Sprite;

public class DrawLoop {
    public static Panel panel;
    public static void drawGame(Panel panel) {
        DrawLoop.panel=panel;
        int[] p = panel.pixel;
        
		for (int y=0;y<RabiClone.BASE_HEIGHT;y++) {
			for (int x=0;x<RabiClone.BASE_WIDTH;x++) {
        		p[y*RabiClone.BASE_WIDTH+x]=(16<<16)+(16<<8)+16;//RGB
        	}
        }
		
		for (int i=0;i<RabiClone.OBJ.size();i++) {
			RabiClone.OBJ.get(i).draw(p);
		}
    }

	public static void Draw_Sprite(double x, double y, Sprite sprite){
		Draw_Sprite_Partial(x,y,0,0,sprite.getWidth(),sprite.getHeight(),sprite);
	}

	public static void Draw_Sprite_Partial(double x, double y, double xOffset, double yOffset, double w, double h, Sprite sprite){
		int[] p = panel.pixel;
		for(int X=(int)xOffset;X<(int)(w+xOffset);X++){
			for(int Y=(int)yOffset;Y<(int)(h+yOffset);Y++){
				if (X+x-xOffset<0||Y+y-yOffset<0||X-xOffset+x>=RabiClone.BASE_WIDTH||Y-yOffset+y>=RabiClone.BASE_HEIGHT) {
					continue;
				} else {
					int index = (Y-(int)yOffset+(int)y)*RabiClone.BASE_WIDTH+X-(int)xOffset+(int)x;
					if (index<0||index>=p.length||p[index]==sprite.getBi_array()[Y*sprite.getWidth()+X]) {
						continue;
					} else {
						Draw(p,index,sprite.getBi_array()[Y*sprite.getWidth()+X],true);
						//Draw(p,index,sprite.getBi_array()[Y*sprite.getWidth()+X],false);
					}
				}
			}	
		}
	}

	public static void Draw(int[] canvas,int index, int col,boolean transparency) {
		if (!transparency) {
			canvas[index]=col;
			return;
		}
 		int alpha = col>>>24;
		if (alpha==0) {
			return;}
		 else
		if (alpha==255) {
			canvas[index]=col;
		} else {
			float ratio=alpha/255f;
			int prev_col=canvas[index];
			int prev_r=(prev_col&0xFF);
			int prev_g=(prev_col&0xFF00)>>>8;
			int prev_b=(prev_col&0xFF0000)>>>16;
			int r=(col&0xFF);
			int g=(col&0xFF00)>>>8;
			int b=(col&0xFF0000)>>>16;

			int new_r=(int)(ratio*r+(1-ratio)*prev_r);
			int new_g=(int)(ratio*g+(1-ratio)*prev_g);
			int new_b=(int)(ratio*b+(1-ratio)*prev_b);
			
			canvas[index]=new_r+(new_g<<8)+(new_b<<16)+(col&0xFF000000);
		}
	}
}
