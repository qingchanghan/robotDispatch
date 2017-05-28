package robotDispatch;

import java.util.ArrayList;

public class Car {
	
	public int id;
	
	public int applyInTime;
	public int applyOutTime;
	public int maxWaitingTime;
	public int mass;
	
	public boolean ifGiveUp;
	
	public int actualInTime;
	public int actualOutTime;
	
	public ArrayList<Point> inPath;
	public ArrayList<Point> outPath;
	
	public Car(int id, int t1, int t2, int maxTime, int mass) {
		this.id = id;
		this.applyInTime = t1;
		this.applyOutTime = t2;
		this.maxWaitingTime = maxTime;
		this.mass = mass;
	}
	
	public void printInfo() {
		System.out.println(id + " " + applyInTime + " " + applyOutTime + " " + maxWaitingTime + " " + mass);
	}
	
}
