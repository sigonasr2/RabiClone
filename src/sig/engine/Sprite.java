package sig.engine;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public enum Sprite{

    NANA(new File("..","3x.png")),
    ;



    BufferedImage img;
    int height;
    int width;
    int[] bi_array;
    
    Sprite(File filename){
        try {
            BufferedImage img = ImageIO.read(filename);
            this.width=img.getWidth();
            this.height=img.getHeight();
            this.bi_array = new int[width*height];
            img.getRGB(0,0,width,height,this.bi_array,0,width);
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

    public int[] getBi_array() {
        return bi_array;
    }

    public void setBi_array(int[] bi_array) {
        this.bi_array = bi_array;
    };

}