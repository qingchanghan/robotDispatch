package robotDispatch;

public class Robot {
	
	public int id;
	public Point currentPoint;
	
	public short currentStatus;
	public static final short start = 0;
	public static final short fromWaitingToEntrance = 1;
	public static final short fromEntranceToPark = 2;
	public static final short fromParkToWaiting = 3;
	public static final short fromWaitingToPark = 4;
	public static final short fromParkToExit = 5;
	public static final short fromExitToWaiting = 6;
	public static final short waiting = 7;
	
	public boolean ifFree;
	
	public Car carryCar;
	public Point targetPoint;
	public Point waitingPoint;
	public int weight;
	public int currentWork;
	public Point entrance;
	public Point exit;
	
	public Robot(int id, Point entrance, Point exit, Point waitingPoint) {
		this.id = id;
		currentPoint = entrance.copy();
		this.entrance = entrance;
		this.exit = exit;
		currentStatus = start;
		this.waitingPoint = waitingPoint;
		setIfFree();
		targetPoint = entrance.copy();
		currentWork = 0;
		weight = 0;
	}
	
	public void setIfFree() {
		if(currentStatus == start || currentStatus == fromParkToWaiting || currentStatus == fromExitToWaiting || currentStatus == waiting)
			ifFree = true;
		else
			ifFree = false;
	}
	
	public void oneStep(char direction, int time) {
		if(currentStatus != start && currentStatus != waiting) {
			currentPoint.setByDirection(direction);
			switch(currentStatus) {
			case fromEntranceToPark:
				carryCar.addInPath(currentPoint.copy());
				currentWork += weight;
				break;
			case fromParkToExit:
				carryCar.addOutPath(currentPoint.copy());
				currentWork += weight;
				break;
			default:
				break;
			}
			if(currentPoint.equal1(targetPoint)) {
				switch(currentStatus) {
				case fromWaitingToEntrance:
					currentStatus = fromEntranceToPark;
					carryCar.setStatus(Car.comingIn);
					carryCar.addInPath(currentPoint.copy());
					targetPoint = carryCar.getParkPoint().copy();
					carryCar.setActualInTime(time);
					break;
				case fromEntranceToPark:
					currentStatus = fromParkToWaiting;
					carryCar.setStatus(Car.parking);
					targetPoint = waitingPoint.copy();
					break;
				case fromParkToWaiting:
					currentStatus = waiting;
					break;
				case fromWaitingToPark:
					currentStatus = fromParkToExit;
					carryCar.setStatus(Car.goOut);
					carryCar.addOutPath(currentPoint.copy());
					targetPoint = exit.copy();
					break;
				case fromParkToExit:
					currentStatus = fromExitToWaiting;
					carryCar.setStatus(Car.finished);
					carryCar.setActualOutTime(time);
					targetPoint = waitingPoint.copy();
					break;
				case fromExitToWaiting:
					currentStatus = waiting;
					break;
				default:
					break;
				}
				setIfFree();
			}
		}
	}
	
	public void assign(Car c, boolean flag, Point targetPark, int time) {//flag为true表示进入 false为出去
		carryCar = c;
		weight = c.getMass();
		if(flag) {
			c.setParkPoint(targetPark);
			c.setInRobotId(id);
			if(this.currentPoint.equal1(entrance)) {
				this.currentStatus = fromEntranceToPark;
				this.targetPoint = targetPark.copy();
				c.setStatus(Car.comingIn);
				c.setActualInTime(time);
				c.addInPath(currentPoint.copy());
			} else {
				this.currentStatus = fromWaitingToEntrance;
				this.targetPoint = entrance.copy();
				c.setStatus(Car.waitingRobotIn);
			}
		} else {
			c.setOutRobotId(id);
			if(currentPoint.equal1(targetPark)) {
				this.currentStatus = fromParkToExit;
				this.targetPoint = exit.copy();
				c.setStatus(Car.goOut);
				c.addOutPath(currentPoint.copy());
			} else {
				this.currentStatus = fromWaitingToPark;
				this.targetPoint = targetPark.copy();
				c.setStatus(Car.waitingRobotOut);
			}
		}
		setIfFree();
	}
	
	public Point getCurrentPoint() {
		return currentPoint;
	}
	
	public Point getTargetPoint() {
		return targetPoint;
	}
	
	public int getWork() {
		return currentWork;
	}
	
	public boolean getIfFree() {
		return ifFree;
	}
}
