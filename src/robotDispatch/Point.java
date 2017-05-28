package robotDispatch;

public class Point {
	
	private int x;
	private int y;
	
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public void setXY(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void print() {
		System.out.println("(" + x + "," + y + ")");
	}
	
	public boolean equal1(Point p) {
		if(p.getX() == this.x && p.getY() == this.y)
			return true;
		else
			return false;
	}
}
