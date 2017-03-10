
/**
 * Description
 * @author Vincent Larcin, Joachim Sneessens
 */
public class Position {
	
	/**
	 * The X-coordinate of the object.
	 */
	public int x;
	
	/**
	 * The Y-coordinate of the object.
	 */
	public int y;
	
	/**
	 * Creates a position with the specified coordinates in input.
	 * @param inputX the X-coordinate of the object
	 * @param inputY the Y-coordinate of the object
	 */
	public Position (int inputX, int inputY) {
		x = inputX;
		y = inputY;
	}
	
	/**
	 * Gets the current position of the object on the X-axis.
	 * @return The X-coordinate of the object
	 */
	public int getX () {
		return x;
	}
	
	/**
	 * Gets the current position of the object on the Y-axis.
	 * @return The Y-coordinate of the object
	 */
	public int getY () {
		return y;
	}
	
	/**
	 * Sets the X-coordinate of the object to a new value.
	 * @param inputX the new X-coordinate
	 */
	public void setX(int inputX) {
		x = inputX;
	}
	
	/**
	 * Sets the Y-coordinate of the object to a new value.
	 * @param inputY the new Y-coordinate
	 */
	public void setY(int inputY) {
		y = inputY;
	}
	
	/**
	 * Compares the coordinate of the object with the coordinates of another.
	 * @param other the object to compare the coordinates with
	 * @return true if the objects have the same coordinates, false else
	 */
	public boolean equals (Position other) {
			
		if (this.x != other.x)
			return false;
		if (this.y != other.y)
			return false;
		
		return true;		
	}
}
