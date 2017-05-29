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
	
	public Robot(int id, Point entrance, Point waitingPoint) {
		this.id = id;
		currentPoint = entrance;
		currentStatus = start;
		this.waitingPoint = waitingPoint;
		setIfFree();
		targetPoint = currentPoint;
		currentWork = 0;
		weight = 0;
	}
	
	public void setIfFree() {
		if(currentStatus == start || currentStatus == fromParkToWaiting || currentStatus == fromExitToWaiting || currentStatus == waiting)
			ifFree = true;
		else
			ifFree = false;
	}
	
	public void oneStep(char direction) {
		if(currentStatus != start && currentStatus != waiting) {
			currentPoint.setByDirection(direction);
			switch(currentStatus) {
			case fromEntranceToPark:
				carryCar.addInPath(currentPoint);
				currentWork += weight;
				break;
			case fromParkToExit:
				carryCar.addOutPath(currentPoint);
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
					break;
				case fromEntranceToPark:
					currentStatus = fromParkToWaiting;
					carryCar.setStatus(Car.parking);
					break;
				case fromParkToWaiting:
					currentStatus = waiting;
					break;
				case fromWaitingToPark:
					currentStatus = fromParkToExit;
					carryCar.setStatus(Car.goOut);
					break;
				case fromParkToExit:
					currentStatus = fromExitToWaiting;
					carryCar.setStatus(Car.finished);
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
	
	public Point getCurrentPoint() {
		return currentPoint;
	}
	
	public Point getTargetPoint() {
		return targetPoint;
	}
}
