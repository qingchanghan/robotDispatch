package robotDispatch;

public class Map {
	
	public char[][] map;
	public Point entrance;
	public Point exit;
	public Point waitingPoint;
	
	public Map(char[][] map) {
		this.map = map;
		judge();
		for(int i = 0; i < map.length; i++) {
			for(int j = 0; j < map[i].length; j++) {
				if(map[i][j] == 'I')
					entrance = new Point(i, j);
				else if(map[i][j] == 'E')
					exit = new Point(i, j);
			}
		}
		setWaitingPoint();
	}
	
	public boolean judge() {
		//�жϵ�ͼ�Ƿ���ȷ
	}
	
	public void setWaitingPoint() {
		//���û����˵ȴ���
	}
}
