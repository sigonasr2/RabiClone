package sig.engine;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.image.DataBufferByte;

import javax.imageio.ImageIO;

public class Sprite{

    //NANA(new File(new File("..","sprites"),"3x.png")),
    public static Sprite NANA_SMALL = new Sprite(new File(new File("..","sprites"),"1x.gif"));
    public static Sprite TILE_SHEET = new Sprite(new File(new File("..","sprites"),"tiles.gif"));
    public static Sprite MAP_TILE_INFO = new Sprite(new File(new File("..","sprites"),"maptileinfo.gif"));
    public static Sprite PROFONT = new Sprite(new File(new File("..","sprites"),"Profont.gif"));
    public static Sprite BACKGROUND1 = new Sprite(new File(new File("..","backgrounds"),"back1.gif"));
    public static Sprite BACKGROUND2 = new Sprite(new File(new File("..","backgrounds"),"back2.gif"));
    public static Sprite BACKGROUND3 = new Sprite(new File(new File("..","backgrounds"),"back3.gif"));
    public static AnimatedSprite ERINOAH = new AnimatedSprite(new File(new File("..","sprites"),"erinoah.gif"),48,48);
    public static AnimatedSprite ERINA = new AnimatedSprite(new File(new File("..","sprites"),"erina.gif"),32,32);
    public static AnimatedSprite ERINA_WALK = new AnimatedSprite(new File(new File("..","sprites"),"erina_walk.gif"),32,32);
    public static AnimatedSprite ERINA_JUMP_RISE1 = new AnimatedSprite(new File(new File("..","sprites"),"erina_jump_rise1.gif"),32,32);
    public static AnimatedSprite ERINA_JUMP_RISE = new AnimatedSprite(new File(new File("..","sprites"),"erina_jump_rise.gif"),32,32);
    public static AnimatedSprite ERINA_JUMP_FALL1 = new AnimatedSprite(new File(new File("..","sprites"),"erina_jump_fall1.gif"),32,32);
    public static AnimatedSprite ERINA_JUMP_FALL = new AnimatedSprite(new File(new File("..","sprites"),"erina_jump_fall.gif"),32,32);
    public static AnimatedSprite ERINA_SLIDE1 = new AnimatedSprite(new File(new File("..","sprites"),"erina_slide1.gif"),32,32);
    public static AnimatedSprite ERINA_SLIDE = new AnimatedSprite(new File(new File("..","sprites"),"erina_slide.gif"),32,32);
    public static AnimatedSprite KNIFE_SWING = new AnimatedSprite(new File(new File("..","sprites"),"knife-swing.gif"),32,32);



    BufferedImage img;
    int height;
    int width;
    byte[] bi_array;
    
    Sprite(File filename){
        try {
            BufferedImage img = ImageIO.read(filename);
            this.width=img.getWidth();
            this.height=img.getHeight();
            
            if (filename.getName().endsWith(".png")) {
                System.out.println("  WARNING! PNGs are not officially supported! Will attempt to convert anyways.");
                bi_array=new byte[width*height];
                for (int y=0;y<height;y++) {
                    for (int x=0;x<width;x++) {
                        int col = img.getRGB(x, y);
                        int r = (col&0xff0000)>>>16;
                        int g = (col&0xff00)>>>8;
                        int b = col&0xff;
                        boolean found=false;
                        for (int c=0;c<Panel.generalPalette.length;c+=3) {
                            if (Panel.generalPalette[c]==(byte)r&&
                            Panel.generalPalette[c+1]==(byte)g&&
                            Panel.generalPalette[c+2]==(byte)b) {
                                System.out.println("  Matched color index["+((byte)(c/3))+"]");
                                bi_array[y*width+x]=(byte)(c/3);
                                found=true;
                                break;
                            }
                            if ((byte)0xff==(byte)r&&(byte)0x0==(byte)g&&(byte)0xff==(byte)b) {
                                System.out.println("  Matched color index[32]");
                                bi_array[y*width+x]=32;
                                found=true;
                                break;
                            }
                        }
                        if (!found) {
                            System.out.println(" COLOR NOT FOUND! USING TRANSPARENCY BY DEFAULT. ("+r+","+g+","+b+")");
                            bi_array[y*width+x]=32;
                        }
                    }
                }
            } else {
                bi_array = ((DataBufferByte)(img.getRaster().getDataBuffer())).getData();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Loaded sprite for "+filename+".");
    }

    public BufferedImage getImg() {
        return img;
    }

    public void setImg(BufferedImage img) {
        this.img = img;
    }

    public int getHeight() {
        return height;
    }

    public int getCanvasHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getCanvasWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public byte[] getBi_array() {
        return bi_array;
    }

    public void setBi_array(byte[] bi_array) {
        this.bi_array = bi_array;
    };

}