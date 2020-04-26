package chensley.da.mapgen;


//Used to defining bounding points of area
public class Point {
	private final int x;
	private final int y;
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int x() { return x; }
	public int y() { return y; }
}