package sig.engine;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.awt.image.DataBufferByte;

import javax.imageio.ImageIO;

public enum Sprite{

    NANA(new File(new File("..","sprites"),"3x.png")),
    NANA_SMALL(new File(new File("..","sprites"),"1x.gif")),
    TILE_SHEET(new File(new File("..","sprites"),"tiles.gif")),
    MAP_TILE_INFO(new File(new File("..","sprites"),"maptileinfo.gif")),
    ;



    BufferedImage img;
    int height;
    int width;
    byte[] bi_array;
    
    Sprite(File filename){
        try {
            BufferedImage img = ImageIO.read(filename);
            this.width=img.getWidth();
            this.height=img.getHeight();
            bi_array = ((DataBufferByte)(img.getRaster().getDataBuffer())).getData();
            if (this.ordinal()==3) {
                System.out.println(Arrays.toString(bi_array));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Loaded sprite for "+this+".");
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

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
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