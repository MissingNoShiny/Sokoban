package be.ac.umons.info.sokoban.grid;

/**
 * A class used to manage the player.
 * @author Vincent Larcin, Joachim Sneessens
 */
public class Player extends Position {
	
	private Direction direction;
	
	Player(Grid grid, int x, int y) {
		super(grid, x, y);
		direction = Direction.DOWN;
	}
	
	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	
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
	 * 
	 * @return
	 */
	public boolean canMove() {
		return canMove(direction);
	}
	
	/**
	 * @param grid
	 * @param dir
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
	
	public void move() {
		move(direction, true);
	}
	
	public void move(boolean isTracked) {
		move(direction, isTracked);
	}
	
	/**
	 * 
	 * @param grid
	 * @param dir
	 */
	public void move(Direction dir, boolean isTracked) {
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
	
	void pullCrate(Direction dir, boolean isTracked) {
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
	 * The crate is under the player, and the player go up and pull the crate
	 * ->En realite, je bouge la caisse puis le joueur. C'est pour ne pas devoir adapter les coordonnees a l'avance.
	 * @param grid
	 */
	void pullCrateUp() {
		grid.getCrateAt(x, y+1).move(Direction.UP);
		y--;
	}
	
	void pullCrateRight() {
		grid.getCrateAt(x-1, y).move(Direction.RIGHT);
		x++;
	}
	
	void pullCrateDown() {
		grid.getCrateAt(x, y-1).move(Direction.DOWN);
		y++;
	}
	
	void pullCrateLeft() {
		grid.getCrateAt(x+1, y).move(Direction.LEFT);
		x--;
	}
	
	@Override
	public String getName() {
		return "Player" + direction.name();
	}
}
