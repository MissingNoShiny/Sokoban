package be.ac.umons.info.sokoban.grid;

/**
 * A class used to manage a player.
 * @author Vincent Larcin, Joachim Sneessens
 */
public class Player extends Position {
	
	/**
	 * The Direction the player is facing.
	 */
	private Direction direction;
	
	/**
	 * Creates a new player in specified grid at specified coordinates.
	 * @param grid The grid the player will be in
	 * @param x The X-coordinate of the player
	 * @param y The Y-coordinate of the player
	 */
	Player(Grid grid, int x, int y) {
		super(grid, x, y);
		direction = Direction.DOWN;
	}
	
	/**
	 * Sets the Direction of the player.
	 * @param direction The new Direction
	 */
	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	
	/**
	 * Gets the Direction of the player
	 * @return the Direction of the player
	 */
	public Direction getDirection() {
		return direction;
	}
	
	/**
	 * Sets the X-coordinate of the object to a new value.
	 * @param inputX The new X-coordinate
	 */
	void setNewCoordinates(int inputX, int inputY) {
		try {
			if (!grid.getComponentAt(inputX, inputY).canBePassedThrough())
				throw new IllegalArgumentException();
		} catch (NullPointerException e) {
			throw new IllegalArgumentException();
		}
		x = inputX;
		y = inputY;
	}
	
	/**
	 * Checks if the player can move in the Direction it's currently facing.
	 * @return true if the player can move, false else
	 */
	public boolean canMove() {
		return canMove(direction);
	}
	
	/**
	 * Checks if the player can move is specified Direction.
	 * @param dir The Direction to move the player in
	 * @return true if the player can move, false else
	 */
	public boolean canMove(Direction dir) {
		
		boolean test = false;
		
		Point p = getNextPoint(this, dir);
		int newX = p.getX();
		int newY = p.getY();
		
		if (newX < grid.getWidth() && newX >= 0 && newY < grid.getHeight() && newY >= 0) {
			if (grid.getComponentAt(newX, newY).canBePassedThrough()) {
				test = true;
			}
			else if (grid.hasCrateAt(newX, newY)) 
				test = grid.getCrateAt(newX, newY).canMove(dir);
		}
		return test;
	}
	
	/**
	 * Moves the player in the Direction it's currently facing.
	 */
	public void move() {
		move(direction, true);
	}
	
	/**
	 * Moves the player in the Direction it's currently facing.
	 * @param isTracked Whether the move is tracked by the movementTracker
	 */
	public void move(boolean isTracked) {
		move(direction, isTracked);
	}
	
	/**
	 * Moves the player in specified Direction.
	 * @param dir The Direction to move the player in
	 * @param isTracked Whether the move is tracked by the movementTracker
	 */
	public void move(Direction dir, boolean isTracked) {
		if (canMove(dir)) {
			Point p = getNextPoint(this, dir);
			int newX = p.getX();
			int newY = p.getY();
			
			if (grid.hasCrateAt(newX, newY)) {
				Crate crate = grid.getCrateAt(newX, newY);
				crate.move(dir);
				if (isTracked)
					grid.getTracker().addPush(dir);
			}
			else if (isTracked)
				grid.getTracker().addMove(dir);
			x = newX;
			y = newY;	
		}
	}
	
	/**
	 * Moves the player in a specified Direction and pulls the crate at the opposite.
	 * @param dir The Direction to move the Crate and the player in
	 */
	void pullCrate(Direction dir) {
		switch (dir) {
		case UP:
			pullCrateUp();
			break;
		case RIGHT:
			pullCrateRight();
			break;
		case DOWN:
			pullCrateDown();
			break;
		case LEFT:
			pullCrateLeft();
		}
		grid.getTracker().addMove(dir);
	}
	
	/**
	 * Moves up the player and the crate below it.
	 */
	void pullCrateUp() {
		grid.getCrateAt(x, y+1).move(Direction.UP);
		y--;
	}
	
	/**
	 * Moves right the player and the crate to its left.
	 */
	void pullCrateRight() {
		grid.getCrateAt(x-1, y).move(Direction.RIGHT);
		x++;
	}
	
	/**
	 * Moves down the player and the crate above it.
	 */
	void pullCrateDown() {
		grid.getCrateAt(x, y-1).move(Direction.DOWN);
		y++;
	}
	
	/**
	 * Moves left the player and the crate to its right.
	 */
	void pullCrateLeft() {
		grid.getCrateAt(x+1, y).move(Direction.LEFT);
		x--;
	}
	
	@Override
	public String getName() {
		return "Player" + direction.name();
	}
}
