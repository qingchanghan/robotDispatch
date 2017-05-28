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
			setWaitingPoint();
		}
	}
	
	public boolean judge() {
		//判断地图是否正确
		boolean flag1 = true, flag2 = true;
		for(int i = 0; i < w; i++) {
			for(int j = 0; j < h; j++) {
				if(map[i][j] == 'I') {
					if(flag1) {
						entrance = new Point(i, j);
					} else {
						return false;
					}
				}
				if(map[i][j] == 'E') {
					if(flag2) {
						exit = new Point(i, j);
					} else {
						return false;
					}
				}
				if(map[i][j] == 'P' && !ifAreaValid(i, j))
					return false;
			}
		}
		if(entrance.equal1(exit)) {
			return false;
		}
		if(!ifBorder(entrance) || !ifBorder(exit))
			return false;
		
		//用BFS判断从入口和出口能否到达每个车位
//		boolean[][] flag = new boolean[w][h];
//		for(int i = 0; i < w; i++)
//			for(int j = 0; j < h; j++)
//				if()
		
		
		return true;
	}
	
	public boolean ifAreaValid(int x, int y) {
		int num = 0;
		if(x-1 >= 0 && map[x-1][y] == 'X')
			num++;
		if(y-1 >= 0 && map[x][y-1] == 'X')
			num++;
		if(x+1 < w && map[x+1][y] == 'X')
			num++;
		if(y+1 < h && map[x][y+1] == 'X')
			num++;
		if(num == 1)
			return true;
		else
			return false;
	}
	
	public boolean ifBorder(Point p) {
		int x = p.getX();
		int y = p.getY();
		if(x != 0 && x != w-1 && y != 0 && y != h-1)
			return false;
		else
			return true;
	}
	
	public void setWaitingPoint() {
		//设置机器人等待点
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
	
	public boolean getIfValid() {
		return ifValid;
	}
}
