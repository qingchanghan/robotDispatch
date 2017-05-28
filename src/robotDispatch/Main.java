package robotDispatch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws Exception {
		String fileInPath = "D:\\java_files\\resource\\1.txt";
 		File f = new File(fileInPath);
 		Scanner cin = new Scanner(new FileInputStream(f));
	
		//Scanner cin = new Scanner(System.in);
		int k, p, a, b, w, h, N;
		
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
		Map map = new Map(w, h, mapMatrix);
		
		N = cin.nextInt();
		Car[] cars = new Car[N];
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

}
