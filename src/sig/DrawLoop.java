package sig;

import java.util.regex.Pattern;

import sig.engine.Alpha;
import sig.engine.AnimatedSprite;
import sig.engine.Font;
import sig.engine.PaletteColor;
import sig.engine.Panel;
import sig.engine.Rectangle;
import sig.engine.Sprite;
import sig.engine.String;
import sig.engine.Transform;

public class DrawLoop {
    public static Panel panel;
    public static void drawGame(Panel panel) {
        DrawLoop.panel=panel;
        byte[] p = panel.pixel;
        
		for (int y=0;y<RabiClone.BASE_HEIGHT;y++) {
			for (int x=0;x<RabiClone.BASE_WIDTH;x++) {
        		p[y*RabiClone.BASE_WIDTH+x]=(byte)RabiClone.BACKGROUND_COLOR.ordinal();//RGB
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

	public static void Draw_Text(double x, double y, String s, Font f) {
		Draw_Text_Ext(x,y,s,f,Alpha.ALPHA0,PaletteColor.NORMAL);
	}

	public static void Draw_Text_Ext(double x, double y, String s, Font f, Alpha alpha, PaletteColor col) {
		java.lang.String finalS = s.toString();
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
				Draw_Sprite_Partial_Ext(x+i*f.getGlyphWidth()-xOffset, y+yOffset, f.getCharInfo(finalS.charAt(i)).getX(), f.getCharInfo(finalS.charAt(i)).getY(), f.getCharInfo(finalS.charAt(i)).getWidth(), f.getCharInfo(finalS.charAt(i)).getHeight(), f.getSprite(), 0,alpha,currentCol,Transform.NONE);
				charCount++;
			}
		}
	}

	public static void Draw_Line(byte[] canvas,int x1,int y1,int x2,int y2,byte col,Alpha alpha) {
        int x,y,dx,dy,dx1,dy1,px,py,xe,ye;
        dx=x2-x1;dy=y2-y1;
        dx1=Math.abs(dx);dy1=Math.abs(dy);
        px=2*dy1-dx1;py=2*dx1-dy1;
        if (dy1<=dx1) {
            if (dx>=0) {
                x=x1;y=y1;xe=x2-1;
            } else {
                x=x2-1;y=y2-1;xe=x1;
            }
            Draw(canvas,y*RabiClone.BASE_WIDTH+x,col,alpha);
            while (x<xe) {
                x=x+1;
                if (px<0) {
                    px=px+2*dy1;
                } else {
                    if ((dx<0&&dy<0)||(dx>0&&dy>0)) {
                        y=y+1;
                    } else {
                        y=y-1;
                    }
                    px=px+2*(dy1-dx1);
                }
				Draw(canvas,y*RabiClone.BASE_WIDTH+x,col,alpha);
            }
        } else {
            if (dy>=0) {
                x=x1;y=y1;ye=y2-1;
            } else {
                x=x2-1;y=y2-1;ye=y1;
            }
            Draw(canvas,y*RabiClone.BASE_WIDTH+x,col,alpha);
            while (y<ye) {
                y=y+1;
                if (py<=0) {
                    py=py+2*dx1;
                } else {
                    if ((dx<0&&dy<0)||(dx>0&&dy>0)) {
                        x=x+1;
                    } else {
                        x=x-1;
                    }
                    py=py+2*(dx1-dy1);
                }
				Draw(canvas,y*RabiClone.BASE_WIDTH+x,col,alpha);
            }
        }
    }
    
    public static void Fill_Rect(byte[] p,byte col,double x,double y,double w,double h) {
    	for (int xx=0;xx<w;xx++) {
        	for (int yy=0;yy<h;yy++) {
				if (x+xx>=0&&y+yy>=0&&x+xx<RabiClone.BASE_WIDTH&&y+yy<RabiClone.BASE_HEIGHT) {
					int index = ((int)y+yy)*RabiClone.BASE_WIDTH+(int)x+xx;
					Draw(p,index,col, Alpha.ALPHA0);
				}
        	}	
    	}
    }

	public static void Draw_Sprite(double x, double y, Sprite sprite){
		Draw_Sprite_Partial(x,y,0,0,sprite.getWidth(),sprite.getHeight(),sprite,0,Alpha.ALPHA0,Transform.NONE );
	}

	public static void Draw_Animated_Sprite(double x, double y, AnimatedSprite sprite, double frameIndex){
		Rectangle frameRectangle=sprite.getFrame((int)frameIndex);
		Draw_Sprite_Partial(x,y,frameRectangle.getX(),frameRectangle.getY(),frameRectangle.getWidth(),frameRectangle.getHeight(),sprite,frameIndex,Alpha.ALPHA0,Transform.NONE);
	}

	public static void Draw_Sprite(double x, double y, Sprite sprite, Transform transform){
		Draw_Sprite_Partial(x,y,0,0,sprite.getWidth(),sprite.getHeight(),sprite,0,Alpha.ALPHA0,transform);
	}
	public static void Draw_Animated_Sprite(double x, double y, AnimatedSprite sprite, double frameIndex,Alpha alpha,Transform transform){
		Rectangle frameRectangle=sprite.getFrame((int)frameIndex);
		Draw_Sprite_Partial(x,y,frameRectangle.getX(),frameRectangle.getY(),frameRectangle.getWidth(),frameRectangle.getHeight(),sprite,frameIndex, alpha, transform);
	}

	public static void Draw_Sprite_Partial(double x, double y, double xOffset, double yOffset, double w, double h, Sprite sprite, double frame_index, Alpha alpha, Transform transform){
		Draw_Sprite_Partial_Ext(x,y,xOffset,yOffset,w,h,sprite,frame_index,alpha,PaletteColor.NORMAL,transform);
	}

	public static void Draw_Sprite_Partial_Ext(double x, double y, double xOffset, double yOffset, double w, double h, Sprite sprite, Alpha alpha, Transform transform){
		Draw_Sprite_Partial_Ext(x, y, xOffset, yOffset, w, h, sprite, 0, alpha, PaletteColor.NORMAL, transform);
	}

	public static void Draw_Animated_Sprite_Partial_Ext(double x, double y, double xOffset, double yOffset, double w, double h, AnimatedSprite sprite, double frameIndex, Alpha alpha, Transform transform){
		Rectangle frameRectangle=sprite.getFrame((int)frameIndex);
		Draw_Sprite_Partial_Ext(x, y, frameRectangle.getX(), frameRectangle.getY(), frameRectangle.getWidth(), frameRectangle.getHeight(), sprite, 0, alpha, PaletteColor.NORMAL, transform);
	}

	public static void Draw_Sprite_Partial_Ext(double x, double y, double xOffset, double yOffset, double w, double h, Sprite sprite, double frame_index, Alpha alpha, PaletteColor col, Transform transform){
		boolean horizontal = transform==Transform.HORIZONTAL||transform==Transform.HORIZ_VERTIC;
		boolean vertical = transform==Transform.VERTICAL||transform==Transform.HORIZ_VERTIC;
		byte[] p = panel.pixel;
		int transparentTotalCount=alpha.getB();
		int transparentRunCount=0;
		for(int X=(int)xOffset;X<(int)(w+xOffset);X++){
			for(int Y=(int)yOffset;Y<(int)(h+yOffset);Y++){
				if (alpha!=Alpha.ALPHA0) {
					if (transparentRunCount++==transparentTotalCount) {
						transparentRunCount=0;
						continue;
					} else 
					if (transparentRunCount>alpha.getA()) {
						continue;
					}
				}
				if (X+x-xOffset<0||Y+y-yOffset<0||X-xOffset+x>=RabiClone.BASE_WIDTH||Y-yOffset+y>=RabiClone.BASE_HEIGHT) {
					continue;
				} else {
					int index = 
						((vertical?
						sprite.getHeight()-(Y-(int)yOffset):
						(Y-(int)yOffset))
					+(int)y)*RabiClone.BASE_WIDTH+
						(horizontal?
						sprite.getWidth()-(X-(int)xOffset):
						(X-(int)xOffset))
					+(int)x;

					if (sprite.getBi_array()[Y*sprite.getCanvasWidth()+X]==32||index<0||index>=p.length) {
						continue;
					} else {
						Draw(p,index,(col==PaletteColor.NORMAL)?sprite.getBi_array()[Y*sprite.getCanvasWidth()+X]:(byte)col.ordinal(),alpha);	
					}
				}
			}	
			transparentRunCount--;
		}
	}

	public static void Draw(byte[] canvas,int index, byte col, Alpha alpha) {
		canvas[index]=(byte)(((int)(col)&0xff)+(alpha.ordinal()*(32)));
	}
}
