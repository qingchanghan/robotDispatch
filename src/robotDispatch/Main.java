package robotDispatch;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class Main {
	
	public static Map map;
	public static Car[] cars;
	public static int k, p, a, b, w, h, N;

	public static void main(String[] args) throws Exception {
		
		input();
		//print();
		
		if(map.getIfValid()) {
			System.out.println("YES");
			dispatch();
			//dispatchNum(1, true);
		} else {
			System.out.println("NO");
		}
	}
	
	private static void input() throws Exception {
//		String fileInPath = "D:\\java_files\\resource\\1.txt";
// 		File f = new File(fileInPath);
// 		Scanner cin = new Scanner(new FileInputStream(f));
	
		Scanner cin = new Scanner(System.in);
		
		k = cin.nextInt();
		p = cin.nextInt();
		a = cin.nextInt();
		b = cin.nextInt();
		w = cin.nextInt();
		h = cin.nextInt();
		
		char[][] mapMatrix = new char[w][h];
		cin.nextLine();
		for(int i = 0; i < w; i++) {
			String str = cin.nextLine();
			char[] chars = str.toCharArray();
			int l = 0;
			for(int j = 0; j < h; j++) {
				while(!ifMapCh(chars[l]))
					l++;
				mapMatrix[i][j] = chars[l];
				l++;
			}
		}
		map = new Map(w, h, mapMatrix);
		
		N = cin.nextInt();
		cars = new Car[N];
		for(int i = 0; i < N; i++) {
			int id, t1, t2, maxTime, mass;
			id = cin.nextInt();
			t1 = cin.nextInt();
			t2 = cin.nextInt();
			maxTime = cin.nextInt();
			mass = cin.nextInt();
			Car car1 = new Car(id, t1, t2, maxTime, mass);
			cars[i] = car1;
		}
		cin.close();
	}
	
	private static void print() {
		System.out.println(k + " " + p + " " + a + " " + b);
		map.print();
		for(int i = 0; i < N; i++)
			cars[i].printInfo();
	}
	
	private static boolean ifMapCh(char ch) {
		if(ch == 'X' || ch == 'B' || ch == 'P' || ch == 'E' || ch == 'I')
			return true;
		else
			return false;
	}
	
	private static void dispatch() {
		int max = map.getAverageLength() < N ? map.getAverageLength() : N;
		int min = -1, z = 0, minM = 0;
		for(int m = 1; m <= max; m++) {
			z = dispatchNum(m, false);
			if(min == -1) {
				min = z;
				minM = m;
			} else if(z < min) {
				min = z;
				minM = m;
			}
			clearCars();
			map.clear();
		}
		z = dispatchNum(minM, true);
		for(int i = 0; i < cars.length; i++)
			cars[i].printInfo();
	}

	private static int dispatchNum(int m, boolean ifPrint) {
		int time = 0;
		Robot[] robots = new Robot[m];
		for(int i = 0; i < m; i++) {
			robots[i] = new Robot(i, map.getEntrance(), map.getExit(), map.getWaitingPoint());
		}
		while(true) {
			for(int i = 0; i < m; i++) {
				robots[i].oneStep(map.getDirection(robots[i].currentPoint, robots[i].getTargetPoint()), time);
			}
			
			ArrayList<Car> requestList = new ArrayList<>();
			for(int j = 0; j < cars.length; j++) {
				if(cars[j].getIfGiveUp())
					continue;
				int result = cars[j].getIfCanAndMass(time);
				if(result > 0) {
					requestList.add(cars[j]);
				}
			}
			Comparator<Car> c1 = new Comparator<Car>() {  
	            @Override  
	            public int compare(Car o1, Car o2) {  
	                return ((Integer)o1.getMass()).compareTo((Integer)o2.getMass());
	            }  
	        };        
			Collections.sort(requestList, c1);//将车辆按质量从大到小排列
			
			ArrayList<Robot> freeList = null;
			if(!requestList.isEmpty()) {
				freeList = new ArrayList<>();
				for(int j = 0; j < m; j++)
					if(robots[j].getIfFree())
						freeList.add(robots[j]);
			}
			
			for(int i = 0; i < requestList.size(); i++) {
				if(freeList.isEmpty())
					break;
				Car car = requestList.get(i);
				if(car.getCurrentStatus() == Car.notStarted && map.ifParkFull())
					continue;
				if(car.getCurrentStatus() == Car.notStarted) {
					int min = -1, minJ = -1;
					for(int j = 0; j < freeList.size(); j++) {
						int tmp = map.getInLength(freeList.get(j).getCurrentPoint());
						if(min == -1) {
							min = tmp;
							minJ = j;
						} else if(min > tmp) {
							min = tmp;
							minJ = j;
						}
					}
					ParkPoint targetPark = map.findNearestParkPoint();
					freeList.get(minJ).assign(car, true, targetPark, time);
					freeList.remove(minJ);
				} else if(car.getCurrentStatus() == Car.parking) {
					int min = -1, minJ = -1;
					for(int j = 0; j < freeList.size(); j++) {
						int tmp = map.getPathLength(freeList.get(j).getCurrentPoint(), car.getParkPoint());
						if(min == -1) {
							min = tmp;
							minJ = j;
						} else if(min > tmp) {
							min = tmp;
							minJ = j;
						}
					}
					freeList.get(minJ).assign(car, false, car.getParkPoint(), time);
					freeList.remove(minJ);
				}
			}
			
			boolean flag = true;
			for(int i = 0; i < cars.length; i++)
				if(!cars[i].ifFinished()) {
					flag = false;
					break;
				}
			if(flag)
				break;
			time++;
		}
		
		int z = 0, t = 0, t1 = 0, t2 = 0, W = 0;
		for(int i = 0; i < cars.length; i++) {
			Car car = cars[i];
			if(car.ifGiveUp)
				t2++;
			else
				t1 += car.getT1();
		}
		t2 *= p;
		t = b*t1 + t2;
		for(int i = 0; i < robots.length; i++) {
			W += robots[i].getWork();
		}
		z = a*m + t + k*W;
		if(ifPrint)
			System.out.println(m + " " + t + " " + k*W);
		return z;
	}
	
	private static void clearCars() {
		for(int i = 0; i < cars.length; i++)
			cars[i].clear();
	}
}
