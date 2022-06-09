package sig.engine;

public class Point {
	int x,y;

	public Point(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void set(int x,int y) {
		setX(x);setY(y);
	}

	public void update(int x,int y) {
		set(x,y);
	}

	@Override
	public java.lang.String toString() {
		return "Point(" + x + "," + y + ")";
	}
}
