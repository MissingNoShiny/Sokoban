package be.ac.umons.info.sokoban;

/**
 * The four directions that describe the orientation of a Player or Component.
 * @author Vincent Larcin, Joachim Sneessens
 */
public enum Direction {
	UP,
	RIGHT,
	DOWN,
	LEFT;
	
	
	public static Point associateDirectionToNewPoint(int x, int y, Direction dir) {
		switch (dir) {
		case UP:
			y--; 
			break;
		case RIGHT:
			x++;
			break;
		case DOWN:
			y++;
			break;
		case LEFT:
			x--;
			break;
		}
		return new Point(x, y);
	}
}
