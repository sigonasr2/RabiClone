package sig.engine;

public class Color {
	int r,g,b,a;
	
	final static public Color BLACK = new Color(0,0,0);
	final static public Color RED = new Color(204,0,0);
	final static public Color GREEN = new Color(78,154,6);
	final static public Color YELLOW = new Color(196,160,0);
	final static public Color BLUE = new Color(114,159,207);
	final static public Color MAGENTA = new Color(117,80,123);
	final static public Color CYAN = new Color(6,152,154);
	final static public Color WHITE = new Color(211,215,207);
	final static public Color BRIGHT_BLACK = new Color(85,87,83);
	final static public Color BRIGHT_RED = new Color(239,41,41);
	final static public Color BRIGHT_GREEN = new Color(138,226,52);
	final static public Color BRIGHT_YELLOW = new Color(252,233,79);
	final static public Color BRIGHT_BLUE = new Color(50,175,255);
	final static public Color BRIGHT_MAGENTA = new Color(173,127,168);
	final static public Color BRIGHT_CYAN = new Color(52,226,226);
	final static public Color BRIGHT_WHITE = new Color(255,255,255);

	public Color(int r, int g, int b) {
		this(r,g,b,255);
	}

	public Color(int r, int g, int b,int a) {
		super();
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
	
	public int getColor() {
		return (a<<24)+(r<<16)+(g<<8)+b;
	}
}
