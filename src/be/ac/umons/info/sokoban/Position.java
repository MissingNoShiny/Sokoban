package be.ac.umons.info.sokoban;

/**
 * A Component that has dynamic coordinates, which therefore need to be tracked.
 * @author Vincent Larcin, Joachim Sneessens
 */
public abstract class Position implements Component {
	
	/**
	 * The X-coordinate of the object.
	 */
	protected int x;
	
	/**
	 * The Y-coordinate of the object.
	 */
	protected int y;
	
	/**
	 * The Grid object which contains the position.
	 */
	protected Grid grid;
	
	/**
	 * Creates a position with the specified coordinates in input.
	 * @param inputX The X-coordinate of the object
	 * @param inputY The Y-coordinate of the object
	 */
	public Position(Grid grid, int inputX, int inputY) {
		this.grid = grid;
		x = inputX;
		y = inputY;
	}
	
	/**
	 * Gets the current position of the object on the X-axis.
	 * @return The X-coordinate of the object
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Gets the current position of the object on the Y-axis.
	 * @return The Y-coordinate of the object
	 */
	public int getY() {
		return y;
	}
}
