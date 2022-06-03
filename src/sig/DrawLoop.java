package sig;

import java.util.regex.Pattern;

import sig.engine.Alpha;
import sig.engine.Font;
import sig.engine.PaletteColor;
import sig.engine.Panel;
import sig.engine.Sprite;

public class DrawLoop {
    public static Panel panel;
    public static void drawGame(Panel panel) {
        DrawLoop.panel=panel;
        byte[] p = panel.pixel;
        
		for (int y=0;y<RabiClone.BASE_HEIGHT;y++) {
			for (int x=0;x<RabiClone.BASE_WIDTH;x++) {
        		p[y*RabiClone.BASE_WIDTH+x]=(byte)PaletteColor.DARK_ORCHID.ordinal();//RGB
        	}
        }
		
		for (int i=0;i<RabiClone.OBJ.size();i++) {
			RabiClone.OBJ.get(i).drawBackground(p);
		}
		
		for (int i=0;i<RabiClone.OBJ.size();i++) {
			RabiClone.OBJ.get(i).draw(p);
		}
		
		for (int i=0;i<RabiClone.OBJ.size();i++) {
			RabiClone.OBJ.get(i).drawOverlay(p);
		}
    }

	public static void Draw_Text(double x, double y, StringBuilder s, Font f) {
		Draw_Text_Ext(x,y,s,f,Alpha.ALPHA0,PaletteColor.NORMAL);
	}

	public static void Draw_Text_Ext(double x, double y, StringBuilder s, Font f, Alpha alpha, PaletteColor col) {
		String finalS = s.toString();
		int charCount=0;
		int yOffset=0;
		int xOffset=0;
		PaletteColor currentCol = col;
		for (int i=0;i<finalS.length();i++) {
			if (finalS.charAt(i)==(char)26&&i<finalS.length()-1) {
				byte nextCol=Byte.parseByte(finalS.substring(i+1, finalS.indexOf(' ',i+1)));
				if (nextCol>=PaletteColor.values().length||nextCol<0) {
					throw new ArrayIndexOutOfBoundsException("Chosen color %"+nextCol+" is not in range (Min:0, Max: "+(PaletteColor.values().length-1)+")");
				} else {
					currentCol=PaletteColor.values()[nextCol];
				}
				finalS=finalS.replaceFirst(Pattern.quote(Character.valueOf((char)26)+Byte.toString(nextCol)+" "),"");
				i--;
			} else
			if (finalS.charAt(i)=='\n') {
				xOffset+=(charCount+1)*f.getGlyphWidth();
				yOffset+=f.getGlyphHeight();
				charCount=0;
			} else {
				Draw_Sprite_Partial_Ext(x+i*f.getGlyphWidth()-xOffset, y+yOffset, f.getCharInfo(finalS.charAt(i)).getX(), f.getCharInfo(finalS.charAt(i)).getY(), f.getCharInfo(finalS.charAt(i)).getWidth(), f.getCharInfo(finalS.charAt(i)).getHeight(), f.getSprite(),alpha,currentCol);
				charCount++;
			}
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
					if (index<0||index>=p.length||sprite.getBi_array()[Y*sprite.getWidth()+X]==32||p[index]==sprite.getBi_array()[Y*sprite.getWidth()+X]) {
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
		Draw_Sprite_Partial_Ext(x, y, xOffset, yOffset, w, h, sprite, alpha, PaletteColor.NORMAL);
	}

	public static void Draw_Sprite_Partial_Ext(double x, double y, double xOffset, double yOffset, double w, double h, Sprite sprite, Alpha alpha, PaletteColor col){
		byte[] p = panel.pixel;
		for(int X=(int)xOffset;X<(int)(w+xOffset);X++){
			for(int Y=(int)yOffset;Y<(int)(h+yOffset);Y++){
				if (X+x-xOffset<0||Y+y-yOffset<0||X-xOffset+x>=RabiClone.BASE_WIDTH||Y-yOffset+y>=RabiClone.BASE_HEIGHT) {
					continue;
				} else {
					int index = (Y-(int)yOffset+(int)y)*RabiClone.BASE_WIDTH+X-(int)xOffset+(int)x;
					if (index<0||index>=p.length||sprite.getBi_array()[Y*sprite.getWidth()+X]==32||p[index]==sprite.getBi_array()[Y*sprite.getWidth()+X]) {
						continue;
					} else {
						Draw(p,index,col==PaletteColor.NORMAL?sprite.getBi_array()[Y*sprite.getWidth()+X]:(byte)col.ordinal(),alpha);	
					}
				}
			}	
		}
	}

	public static void Draw(byte[] canvas,int index, byte col, Alpha alpha) {
		canvas[index]=(byte)(((int)(col)&0xff)+(alpha.ordinal()*(32)));
	}
}
