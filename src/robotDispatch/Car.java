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
	public int inRobotId;
	public int outRobotId;
	
	public Point parkPoint;//车辆停放位置
	public ArrayList<Point> inPath;//进入路径
	public ArrayList<Point> outPath;//出去路径
	
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
	
	public int getIfCanAndMass(int time) {//判断该车在当前时间是否能进入或出去 如果已经超时返回-1 如果不能返回0 能则返回车的质量mass
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
		//System.out.println(id + " " + applyInTime + " " + applyOutTime + " " + maxWaitingTime + " " + mass);
		System.out.print(id+" ");
		if(ifGiveUp) {
			System.out.println("yes");
		} else {
			System.out.print("no " + inRobotId + " " + actualInTime + " ");
			for(int i = 0; i < inPath.size(); i++) {
				inPath.get(i).print();
				System.out.print(" ");
			}
			System.out.print(outRobotId + " " + actualOutTime);
			for(int i = 0; i < outPath.size(); i++) {
				System.out.print(" ");
				outPath.get(i).print();
			}
			System.out.println();
		}
	}
	
	public void clear() {
		currentStatus = notStarted;
		ifGiveUp = false;
		actualInTime = 0;
		actualOutTime = 0;
		inPath.clear();
		outPath.clear();
		parkPoint = null;
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
	
	public void setParkPoint(Point p) {
		parkPoint = p.copy();
	}
	
	public Point getParkPoint() {
		return parkPoint;
	}
	
	public boolean ifFinished() {
		return currentStatus == finished;
	}
	
	public void setActualInTime(int time) {
		actualInTime = time;
	}
	
	public void setActualOutTime(int time) {
		actualOutTime = time;
	}
	
	public int getT1() {
		return actualInTime - applyInTime + actualOutTime - applyOutTime;
	}
	
	public void setInRobotId(int id) {
		inRobotId = id;
	}
	
	public void setOutRobotId(int id) {
		outRobotId = id;
	}
}
