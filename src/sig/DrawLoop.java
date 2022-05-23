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
				p[(Y+(int)y)*panel.getWidth()+X+(int)x] = sprite.getBi_array()[Y*sprite.getWidth()+X];
			}	
		}
	}
}
