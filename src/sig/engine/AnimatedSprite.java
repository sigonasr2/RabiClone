package sig.engine;

import java.io.File;

public class AnimatedSprite extends Sprite{
    int originalWidth;
    int originalHeight;
    int frames;
    Rectangle[] frames_to_Rectangle;

    AnimatedSprite(File filename, int width, int height){
        super(filename);
        this.originalWidth=this.width;
        this.originalHeight=this.height;
        this.width = width;
        this.height = height;
        frames = (originalWidth/width)*(originalHeight/height);
        frames_to_Rectangle = new Rectangle[frames];
        for(int i=0;i<frames;i++){
            int x_tile = i%(originalWidth/width);
            int y_tile = i/(originalWidth/width);
            frames_to_Rectangle[i] = new Rectangle(x_tile*width, y_tile*height, width, height);
        }
    }
    public Rectangle getFrame(int index){
        return frames_to_Rectangle[index%frames];
    }
    public int getFrame_count(){
        return frames;
    }

    @Override
    public int getCanvasHeight() {
        return originalHeight;
    }

    @Override
    public int getCanvasWidth() {
        return originalWidth;
    }
    
}