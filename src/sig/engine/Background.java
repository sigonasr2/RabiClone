package sig.engine;

import java.io.File;

public class Background extends Sprite{

    public static Sprite BACKGROUND_1 = new Background(0.02,new File(new File("..","backgrounds"),"back1.gif"));
    public static Sprite BACKGROUND_2 = new Background(0.02,new File(new File("..","backgrounds"),"back2.gif"));
    public static Sprite BACKGROUND_3 = new Background(0.05,new File(new File("..","backgrounds"),"back3.gif"));

    double scrollAmt; //How many pixels to scroll the background per pixel of offset provided.

    Background(double scrollAmt,File filename) {
        super(filename);
        this.scrollAmt=scrollAmt;
    }
    
}
