package robotDispatch;

import java.util.ArrayList;

public class Car {
	
	public int id;
	
	public int applyInTime;
	public int applyOutTime;
	public int maxWaitingTime;
	public int mass;
	
	public boolean ifGiveUp;
	
	public short currentStatus;
	public static final short notStarted = 0;
	public static final short waitingOut = 1;
	public static final short waitingRobotIn = 2;
	public static final short comingIn = 3;
	public static final short parking = 4;
	public static final short waitingRobotOut = 5;
	public static final short goOut = 6;
	public static final short finished = 7;
	
	public int actualInTime;
	public int actualOutTime;
	
	public Point parkPoint;
	public ArrayList<Point> inPath;
	public ArrayList<Point> outPath;
	
	public Car(int id, int t1, int t2, int maxTime, int mass) {
		this.id = id;
		this.applyInTime = t1;
		this.applyOutTime = t2;
		this.maxWaitingTime = maxTime;
		this.mass = mass;
		this.currentStatus = notStarted;
		inPath = new ArrayList<>();
		outPath = new ArrayList<>();
	}
	
	public int getIfCanInAndMass(int time) {//判断该车在当前时间是否能进入 如果已经超时返回-1 如果不能返回0 能则返回车的质量mass
		if(currentStatus == notStarted) {
			if(time > applyInTime + maxWaitingTime) {
				ifGiveUp = true;
				currentStatus = finished;
				return -1;
			} else if(time >= applyInTime)
				return mass;
			else
				return 0;
		} else if(currentStatus == parking) {
			if(time > applyOutTime) {
				return mass;
			} else {
				return 0;
			}
		}
		return 0;
	}
		
	
	public short getCurrentStatus() {
		return currentStatus;
	}
	
	public boolean getIfGiveUp() {//是否放弃
		return ifGiveUp;
	}
	
	public void printInfo() {
		System.out.println(id + " " + applyInTime + " " + applyOutTime + " " + maxWaitingTime + " " + mass);
	}
	
	public void clear() {
		currentStatus = notStarted;
		ifGiveUp = false;
		actualInTime = 0;
		actualOutTime = 0;
		inPath.clear();
		outPath.clear();
	}
	
	public int getMass() {
		return mass;
	}
	
	public void addInPath(Point p) {
		inPath.add(p);
	}
	
	public void addOutPath(Point p) {
		outPath.add(p);
	}
	
	public void setStatus(short s) {
		currentStatus = s;
	}
}
