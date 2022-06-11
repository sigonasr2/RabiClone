package sig.engine;

public class Rectangle {
    int x,y,w,h;

    public Rectangle(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public int getX() {
        return x;
    }

    public int getX2() {
        return x+w;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public int getY2() {
        return y+h;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return w;
    }

    public void setWidth(int w) {
        this.w = w;
    }

    public int getHeight() {
        return h;
    }

    public void setHeight(int h) {
        this.h = h;
    }

    @Override
    public java.lang.String toString() {
        return new StringBuilder("Rectangle(x=").append(x).append(",")
            .append("y=").append(y).append(",")
            .append("w=").append(w).append(",")
            .append("h=").append(h).append(")").toString();
    }
    
}
