package sig;

import sig.engine.Panel;
import sig.engine.Sprite;

public class DrawLoop {
    public static Panel panel;
    public static void drawGame(Panel panel) {
        DrawLoop.panel=panel;
        int[] p = panel.pixel;
        
		for (int y=0;y<panel.getHeight();y++) {
			for (int x=0;x<panel.getWidth();x++) {
        		p[y*panel.getWidth()+x]=(0<<16)+(0<<8)+0;//RGB
        	}
        }
		
		for (int i=0;i<RabiClone.OBJ.size();i++) {
			RabiClone.OBJ.get(i).draw(p);
		}
    }

	public static void Draw_Sprite(double x, double y, Sprite sprite){
		int[] p = panel.pixel;
		for(int X=0;X<sprite.getHeight();X++){
			for(int Y=0;Y<sprite.getWidth();Y++){
				int index = (Y+(int)y)*panel.getWidth()+X+(int)x;
				if (index<0||index>=p.length) {
					continue;
				} else {
					Draw(p,index,sprite.getBi_array()[Y*sprite.getWidth()+X]);
				}
			}	
		}
	}

	public static void Draw(int[] canvas,int index, int col) {
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
