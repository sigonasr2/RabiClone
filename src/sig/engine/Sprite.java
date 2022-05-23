package sig.engine;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;

import javax.imageio.ImageIO;

public enum Sprite{

    NANA(Path.of("..","3x.png")),
    ;



    BufferedImage img;
    int height;
    int width;
    int[] bi_array;
    
    Sprite(Path filename){
        try {
            BufferedImage img = ImageIO.read(filename.toFile());
            this.width=img.getWidth();
            this.height=img.getHeight();
            this.bi_array = new int[width*height];
            img.getRGB(0,0,width,height,this.bi_array,0,width);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Loaded sprite for "+this+".");
    };

}