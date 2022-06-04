package sig.engine;

import java.io.File;

public class AnimatedSprite extends Sprite{
    int frameWidth;
    int frameHeight;
    int frames;
    Rectangle[] frames_to_Rectangle;

    AnimatedSprite(File filename, int width, int height){
        super(filename);
        this.frameWidth = width;
        this.frameHeight = height;
        frames = (this.width/width)*(this.height/height);
        frames_to_Rectangle = new Rectangle[frames];
        for(int i=0;i<frames;i++){
            int x_tile = i%(this.width/width);
            int y_tile = i/(this.width/width);
            frames_to_Rectangle[i] = new Rectangle(x_tile*width, y_tile*height, width, height);
        }
    }
    public Rectangle getFrame(int index){
        return frames_to_Rectangle[index%frames];
    }
    public int getFrame_count(){
        return frames;
    }
    public int get_original_width(){
        return this.width;
    }
        public int get_original_height(){
        return this.height;
    }
}