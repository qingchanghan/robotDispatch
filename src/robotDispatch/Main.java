package robotDispatch;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class Main {
	
	public static Map map;
	public static Car[] cars;
	public static int k, p, a, b, w, h, N;
	

	public static void main(String[] args) throws Exception {
		
		input();
		print();
		
		if(map.getIfValid()) {
			System.out.println("YES");
			dispatchNum(1);
		} else {
			System.out.println("NO");
		}
		
	}
	
	private static void input() throws Exception {
		String fileInPath = "D:\\java_files\\resource\\test.txt";
 		File f = new File(fileInPath);
 		Scanner cin = new Scanner(new FileInputStream(f));
	
		//Scanner cin = new Scanner(System.in);
		
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
		int min = 0, z = 0, minM = 0;
		for(int m = 1; m <= max; m++) {
			z = dispatchNum(m);
			if(min == 0) {
				min = z;
				minM = m;
			} else if(z < min) {
				min = z;
				minM = m;
			}
		}
		clearCars();
		z = dispatchNum(minM);
		System.out.println(minM);
	}

	private static int dispatchNum(int m) {
		int time = 0;
		Robot[] robots = new Robot[m];
		for(int i = 0; i < m; i++) {
			robots[i] = new Robot(i, map.getEntrance(), map.getWaitingPoint());
		}
		while(true) {
			for(int i = 0; i < m; i++) {
				robots[i].oneStep(map.getDirection(robots[i].currentPoint, robots[i].getTargetPoint()));
			}
			
			ArrayList<Car> requestList = new ArrayList<>();
			for(int j = 0; j < cars.length; j++) {
				if(cars[j].getIfGiveUp())
					continue;
				int result = cars[j].getIfCanInAndMass(time);
				if(result > 0) {
					requestList.add(cars[j]);
				}
			}
			Comparator<Car> c1 = new Comparator<Car>() {  
	            @Override  
	            public int compare(Car o1, Car o2) {  
	                // TODO Auto-generated method stub  
	                if(o1.getMass() < o2.getMass())  
	                    return 1; 
	                else return -1;
	            }  
	        };        
			requestList.sort(c1);
			for(int i = 0; i < requestList.size(); i++) {
				requestList.get(i).printInfo();
			}
			
			for(int i = 0; i < requestList.size(); i++) {
				Car car = requestList.get(i);
				if(car.getCurrentStatus() == Car.notStarted && map.ifParkFull())
					continue;
				ArrayList<Robot> freeList = new ArrayList<>();
				for(int j = 0; j < m; j++)
					if(robots[j].ifFree)
						freeList.add(robots[j]);
				if(car.getCurrentStatus() == Car.notStarted) {
					
				} else if(car.getCurrentStatus() == Car.parking) {
					
				}
			}
		}
		return 0;
		
		
		
		
	}
	
	private static void clearCars() {
		for(int i = 0; i < cars.length; i++)
			cars[i].clear();
	}
}
