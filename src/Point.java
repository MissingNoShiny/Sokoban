
/**
 * A substitute to awt's Point using ints instead of doubles.
 * @author Vincent Larcin, Joachim Sneessens
 */
public class Point {
	
	/*
	 * The x-coordinate of the Point
	 */
	private int x;
	
	/**
	 * The y-coordinate of the Point
	 */
	private int y;
	
	/**
	 * Creates a Point with specified coordinates.
	 * @param x The x-coordinate of the point
	 * @param y The y-coordinate of the point
	 */
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Gets the x-coordinate of the Point.
	 * @return The x-coordinate of the Point
	 */
	public int getX(){
		return x;
	}
	
	/**
	 * Gets the y-coordinate of the Point
	 * @return the y-coordinate of the Point
	 */
	public int getY(){
		return y;
	}
}
