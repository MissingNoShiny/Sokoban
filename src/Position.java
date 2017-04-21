
/**
 * Description
 * @author Vincent Larcin, Joachim Sneessens
 */
public abstract class Position extends Component{
	
	/**
	 * The X-coordinate of the object.
	 */
	protected int x;
	
	/**
	 * The Y-coordinate of the object.
	 */
	protected int y;
	
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
	
	/**
	 * Gets the sprite of the object.
	 * @return The sprite of the object
	 */

	/**
	 * Sets the X-coordinate of the object to a new value.
	 * @param inputX The new X-coordinate
	 */
	public void setX(int inputX) {
		x = inputX;
	}
	
	/**
	 * Sets the Y-coordinate of the object to a new value.
	 * @param inputY The new Y-coordinate
	 */
	public void setY(int inputY) {
		y = inputY;
	}
	
	/*
	public boolean isAdjacentTo(Position other) {
		int xGap = Math.abs(x - other.getX());
		int yGap = Math.abs(y - other.getY());
		if (xGap == 0 && yGap == 1)
			return true;
		if (yGap == 1 && yGap == 0)
			return true;
		return false;
	}
	*/
}
