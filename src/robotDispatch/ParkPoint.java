package robotDispatch;

public class ParkPoint extends Point {

	public boolean ifFree;
	
	public ParkPoint(int x, int y) {
		super(x, y);
		ifFree = true;
	}
	
	public boolean getIfFree() {
		return ifFree;
	}
	
	public void setIfFree(boolean tmp) {
		ifFree = tmp;
	}

}
