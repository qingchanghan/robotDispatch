package robotDispatch;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Map {
	
	public char[][] map;
	public int w;
	public int h;
	public boolean ifValid;
	public Point entrance;
	public Point exit;
	public Point waitingPoint;
	public int averageLength;
	
	public short[][] pathLength;
	public char[][] pathDir;
	
	public int parkAmount;
	public int currentPark;
	public ArrayList<ParkPoint> parkList;
	
	public Map(int w, int h, char[][] map) {
		this.w = w;
		this.h = h;
		this.map = map;
		parkAmount = 0;
		currentPark = 0;
		parkList = new ArrayList<>();
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
				if(map[i][j] == 'P'){
					parkAmount++;
					parkList.add(new ParkPoint(i, j));
				}
			}
		}
		if(entrance.equal1(exit)) {
			return false;
		}
		if(!ifBorder(entrance) || !ifBorder(exit))
			return false;
		
		pathLength = new short[w*h][w*h];
		pathDir = new char[w*h][w*h];
		
		for(int i = 0; i < w; i++) {
			for(int j = 0; j < h; j++) {
				for(int l = 0; l < w; l++) {
					for(int k = 0; k < h; k++) {
						if(map[i][j] == 'B' || map[l][k] == 'B') {
							pathLength[i*h+j][l*h+k] = -1;
							pathLength[l*h+k][i*h+j] = -1;
						}
					}
				}
			}
		}
		
		//用BFS判断从入口和出口能否到达每个车位
		for(int i = 0; i  < w; i++)
			for(int j = 0; j < h; j++) {
				if(map[i][j] != 'B')
					bfs(i, j);
			}
		
		int v = entrance.getX()*h + entrance.getY(), u = exit.getX()*h + exit.getY();
		for(int i = 0; i < parkList.size(); i++) {
			Point p = parkList.get(i);
			int x = p.getX(), y = p.getY();
			if(map[x][y] == 'P' && (pathLength[v][x*h+y] == 0 || pathLength[u][x*h+y] == 0)) {
				return false;
			}
		}
		
		return true;
	}
	
	private void bfs(int x, int y) {
		Queue<Point> queue = new LinkedList<>();
		boolean[][] visited = new boolean[w][h];
		
		visited[x][y] = true;
		for(int i = 0; i < w; i++) {
			for(int j = 0; j < h; j++) {
				if(map[i][j] == 'B')
					visited[i][j] = true;
			}
		}
		queue.offer(new Point(x, y));
		while(!queue.isEmpty()) {
			Point p = queue.remove();
			int x1 = p.getX(), y1 = p.getY();
			if(x1-1 >= 0 && ifConnected(x1, y1, x1-1, y1) && !visited[x1-1][y1]) {
				visited[x1-1][y1] = true;
				queue.offer(new Point(x1-1, y1));
				pathLength[x*h+y][(x1-1)*h+y1] = (short)(pathLength[x*h+y][x1*h+y1] + 1);
				if(x == x1 && y == y1) {
					pathDir[x*h+y][(x1-1)*h+y1] = 'u';
				} else {
					pathDir[x*h+y][(x1-1)*h+y1] = pathDir[x*h+y][x1*h+y1];
				}
			}
			if(y1-1 >= 0 && ifConnected(x1, y1, x1, y1-1) && !visited[x1][y1-1]) {
				visited[x1][y1-1] = true;
				queue.offer(new Point(x1, y1-1));
				pathLength[x*h+y][x1*h+y1-1] = (short)(pathLength[x*h+y][x1*h+y1] + 1);
				if(x == x1 && y == y1) {
					pathDir[x*h+y][x1*h+y1-1] = 'l';
				} else {
					pathDir[x*h+y][x1*h+y1-1] = pathDir[x*h+y][x1*h+y1];
				}
			}
			if(x1+1 < w && ifConnected(x1, y1, x1+1, y1) && !visited[x1+1][y1]) {
				visited[x1+1][y1] = true;
				queue.offer(new Point(x1+1, y1));
				pathLength[x*h+y][(x1+1)*h+y1] = (short)(pathLength[x*h+y][x1*h+y1] + 1);
				if(x == x1 && y == y1) {
					pathDir[x*h+y][(x1+1)*h+y1] = 'd';
				} else {
					pathDir[x*h+y][(x1+1)*h+y1] = pathDir[x*h+y][x1*h+y1];
				}
			}
			if(y1+1 < h && ifConnected(x1, y1, x1, y1+1) && !visited[x1][y1+1]) {
				visited[x1][y1+1] = true;
				queue.offer(new Point(x1, y1+1));
				pathLength[x*h+y][x1*h+y1+1] = (short)(pathLength[x*h+y][x1*h+y1] + 1);
				if(x == x1 && y == y1) {
					pathDir[x*h+y][x1*h+y1+1] = 'r';
				} else {
					pathDir[x*h+y][x1*h+y1+1] = pathDir[x*h+y][x1*h+y1];
				}
			}
			
			boolean flag = false;
			for(int i = 0; i < w; i++) {
				for(int j = 0; j < h; j++) {
					if(!visited[i][j]) {
						flag = true;
						break;
					}
				}
				if(flag)
					break;
			}
			if(!flag)
				break;
		}
	}
	
	private boolean ifConnected(int x, int y, int x1, int y1) {
		if(map[x][y] == 'B' || map[x1][y1] == 'B')
			return false;
		if(map[x][y] == 'P' && map[x1][y1] == 'P')
			return false;
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
		int i, min = -1, minNum = 1;
		Point minPoint = null;
		for(i = 0; i < w*h; i++) {
			int x = i / h, y = i % h;
			if(map[x][y] != 'X')
				continue;
			int sum = 0, j, num = 0;
			for(j = 0; j < w*h; j++) {
				int x1 = j / h, y1 = j % h;
				if(i == j || map[x1][y1] == 'B' || map[x1][y1] == 'X') {
					continue;
				}
				if(pathLength[i][j] == 0) {
					j = -1;
					break;
				}
				sum += pathLength[i][j];
				num++;
			}
			if(j == -1)
				continue;
			if(min == -1) {
				min = sum;
				minPoint = new Point(x, y);
				minNum = num;
			}
			else if(min > sum) {
				min = sum;
				minPoint = new Point(x, y);
				minNum = num;
			}
		}
		waitingPoint = minPoint;
		averageLength = min / minNum;
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
		waitingPoint.print();
		System.out.println(averageLength);
	}
	
	public boolean getIfValid() {
		return ifValid;
	}
	
	public Point getWaitingPoint() {
		return waitingPoint;
	}
	
	public int getAverageLength() {
		return averageLength;
	}
	
	public Point getEntrance() {
		return entrance;
	}
	
	public Point getExit() {
		return exit;
	}
	
	public char getDirection(Point p1, Point p2) {
		return pathDir[p1.getX()*h+p1.getY()][p2.getX()*h+p2.getY()];
	}
	
	public boolean ifParkFull() {
		return parkAmount == currentPark;
	}
	
	public int getPathLength(Point p1, Point p2) {
		return pathLength[p1.getX()*h+p1.getY()][p2.getX()*h+p2.getY()];
	}
	
	public int getInLength(Point p1) {
		return getPathLength(entrance, p1);
	}
	
	public ParkPoint findNearestParkPoint() {
		int min = -1, minI = -1;
		for(int i = 0; i < parkList.size(); i++) {
			if(!parkList.get(i).getIfFree())
				continue;
			int tmp = getPathLength(parkList.get(i), entrance);
			if(min == -1) {
				min = tmp;
				minI = i;
			} else if(min > tmp) {
				min = tmp;
				minI = i;
			}
		}
		currentPark++;
		ParkPoint p = parkList.get(minI);
		p.setIfFree(false);
		return p;
	}
	
	public void clear() {
		parkAmount = parkList.size();
		currentPark = 0;
		for(int i = 0; i < parkList.size(); i++)
			parkList.get(i).setIfFree(true);
	}
}
