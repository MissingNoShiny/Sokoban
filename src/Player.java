
public class Player extends Position {
	
	private Direction direction;
	
	public Player(Grid grid, int x, int y) {
		super(grid, x, y);
		direction = Direction.DOWN;
	}
	
	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	
	public Direction getDirection() {
		return direction;
	}
	
	public boolean canMove() {
		return canMove(direction);
	}
	
	/**
	 * @param grid
	 * @param dir
	 */
	public boolean canMove(Direction dir) {
		int newX = x, newY = y;
		boolean test = false;
		
		switch (dir) {
		case UP:
			newY--; 
			break;
		case RIGHT:
			newX++;
			break;
		case DOWN:
			newY++;
			break;
		case LEFT:
			newX--;
			break;
		}
		
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
	
	/**
	 * 
	 * @param grid
	 * @param dir
	 */
	public void move(Direction dir, boolean isTracked) {
		int newX = getX(), newY = getY();
		switch (dir) {
		case UP:
			newY--; 
			break;
		case RIGHT:
			newX++;
			break;
		case DOWN:
			newY++;
			break;
		case LEFT:
			newX--;
			break;
		}
		if (grid.hasCrateAt(newX, newY)) {
			Crate crate = grid.getCrateAt(newX, newY);
			crate.move(dir);
			if (grid.frozenDeadlockDetector(crate))
				System.out.println("deadlock");
			if (isTracked)
			grid.getTracker().addPush(dir);
		}
		else if (isTracked)
			grid.getTracker().addMove(dir);
		setX(newX);
		setY(newY);
	}
	
	public void pullCrate() {
		pullCrate(direction, true);
	}
	
	public void pullCrate(Direction dir, boolean isTracked) {
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
	public void pullCrateUp() {
		grid.getCrateAt(x, y+1).move(Direction.UP);
		y--;
	}
	
	public void pullCrateRight() {
		grid.getCrateAt(x-1, y).move(Direction.RIGHT);
		x++;
	}
	
	public void pullCrateDown() {
		grid.getCrateAt(x, y-1).move(Direction.DOWN);
		y++;
	}
	
	public void pullCrateLeft() {
		grid.getCrateAt(x+1, y).move(Direction.LEFT);
		x--;
	}
	
	@Override
	public String getName() {
		return "Player" + direction.name();
	}
}
