package be.ac.umons.info.sokoban.grid;

/**
 * A substitute to awt's Point using int instead of double.
 * @author Vincent Larcin, Joachim Sneessens
 */
public class Point {
	
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
}
