package robotDispatch;

public class ParkPoint extends Point {

	public boolean ifFree;//³µÎ»ÊÇ·ñ¿ÕÏÐ
	
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
