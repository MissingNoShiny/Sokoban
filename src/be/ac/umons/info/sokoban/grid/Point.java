package be.ac.umons.info.sokoban.grid;

/**
 * A substitute to awt's Point using int instead of double.
 * @author Vincent Larcin, Joachim Sneessens
 */
class Point {
	
	/*
	 * The X-coordinate of the Point
	 */
	protected int x;
	
	/**
	 * The Y-coordinate of the Point
	 */
	protected int y;
	
	/**
	 * Creates a Point with specified coordinates.
	 * @param x The X-coordinate of the point
	 * @param y The Y-coordinate of the point
	 */
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Gets the X-coordinate of the Point.
	 * @return The X-coordinate of the Point
	 */
	public int getX(){
		return x;
	}
	
	/**
	 * Gets the Y-coordinate of the Point
	 * @return the Y-coordinate of the Point
	 */
	public int getY(){
		return y;
	}
	
	/**
	 * Gets the Point next to a specified Point in a specified Direction.
	 * @param p The Point to start from
	 * @param dir The Direction to go in
	 * @return The next Point in specified Direction
	 */
	public static Point getNextPoint(Point p, Direction dir) {
		int x = p.getX();
		int y = p.getY();
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
