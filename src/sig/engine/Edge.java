package sig.engine;

public class Edge {
	Point a,b;
	int min_y;
	int max_y;
	int min_x;
	int max_x;
	double x_of_min_y;
	double inverse_slope;
	public Edge(Point a, Point b) {
		super();
		this.a = a;
		this.b = b;
		min_y=Math.min(a.y, b.y);
		max_y=Math.max(a.y, b.y);
		min_x=Math.min(a.x, b.x);
		max_x=Math.max(a.x, b.x);
		if (a.y==min_y) {
			x_of_min_y=a.x;
		} else {
			x_of_min_y=b.x;
		}
		
		inverse_slope=(double)(a.x-b.x)/(a.y-b.y);
	}
	@Override
	public String toString() {
		return "Edge [a=" + a + ", b=" + b + ", min_y=" + min_y + ", max_y=" + max_y + ", min_x=" + min_x + ", max_x="
				+ max_x + ", x_of_min_y=" + x_of_min_y + ", inverse_slope=" + inverse_slope + "]";
	}
	
}
