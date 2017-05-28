package robotDispatch;

public class Map {
	
	public char[][] map;
	public int w;
	public int h;
	public boolean ifValid;
	public Point entrance;
	public Point exit;
	public Point waitingPoint;
	
	public Map(int w, int h, char[][] map) {
		this.w = w;
		this.h = h;
		this.map = map;
		ifValid = judge();
		if(ifValid) {
			for(int i = 0; i < w; i++) {
				for(int j = 0; j < h; j++) {
					if(map[i][j] == 'I')
						entrance = new Point(i, j);
					else if(map[i][j] == 'E')
						exit = new Point(i, j);
				}
			}
			setWaitingPoint();
		}
	}
	
	public static boolean judge() {
		//�жϵ�ͼ�Ƿ���ȷ
		return true;
	}
	
	public static void setWaitingPoint() {
		//���û����˵ȴ���
	}
	
	public void print() {
		System.out.println(w + " " + h);
		for(int i = 0; i < w; i++) {
			for(int j = 0; j < h; j++) {
				System.out.print(map[i][j] + " ");
			}
			System.out.println();
		}
		entrance.print();
		exit.print();
	}
}
