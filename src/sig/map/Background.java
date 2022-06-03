package sig.map;

import sig.engine.Sprite;

public enum Background {
    BACKGROUND1(Sprite.BACKGROUND1,0.02),
    BACKGROUND2(Sprite.BACKGROUND2,0.02),
    BACKGROUND3(Sprite.BACKGROUND3,0.05);

    Sprite background;
    double scrollSpd; //Amount of pixels to move the background based on pixel offset provided.

    Background(Sprite background,double scrollSpd) {
        this.background=background;
        this.scrollSpd=scrollSpd;
    }

    public byte[] getPixels() {
        return background.getBi_array();
    }

    public int getWidth() {
        return background.getWidth();
    }

    public int getHeight() {
        return background.getHeight();
    }

    public double getScrollSpeed() {
        return scrollSpd;
    }
}
