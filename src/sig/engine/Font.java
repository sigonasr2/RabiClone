package sig.engine;

public enum Font {
    PROFONT_12((byte)7,(byte)14,Sprite.PROFONT),
    ;

    byte glyphWidth,glyphHeight;
    int glyphCountX,glyphCountY;
    Rectangle[] charBounds;
    Sprite spr;

    Font(byte glyphWidth, byte glyphHeight, Sprite spr) {
        this.glyphWidth=glyphWidth;
        this.glyphHeight=glyphHeight;
        this.glyphCountX=spr.width/glyphWidth;
        this.glyphCountY=spr.height/glyphHeight;
        this.charBounds = new Rectangle[256];
        for (int y=0;y<glyphCountY;y++) {
            for (int x=0;x<glyphCountX;x++) {
                if (y*glyphCountX+x<256) {
                    charBounds[y*glyphCountX+x]=new Rectangle(x*glyphWidth,y*glyphHeight,glyphWidth,glyphHeight);
                }
            }
        }
        this.spr=spr;
    }

    public Rectangle getCharacterBounds(char c) {
        return charBounds[c];
    }

    public byte getGlyphWidth() {
        return glyphWidth;
    }

    public byte getGlyphHeight() {
        return glyphHeight;
    }
    public Rectangle getCharInfo(char c) {
        return charBounds[c];
    }

    public Sprite getSprite() {
        return spr;    
    }
}
