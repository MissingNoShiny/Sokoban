
public class Player extends Position {
	
	private Direction direction;
	
	public Player(int xInput, int yInput) {
		super(xInput, yInput);
		direction = Direction.DOWN;
	}
	
	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	
	public Direction getDirection() {
		return direction;
	}
	
	public boolean canMove(Grid grid, boolean canPushCrate) {
		return canMove(grid, canPushCrate, direction);
	}
	
	/**
	 * @param grid
	 * @param dir
	 */
	public boolean canMove(Grid grid, boolean canPushCrate, Direction dir) {
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
			if (grid.getComponentAt(newX, newY).canGoTrough()) {
				test = true;
			}

			else if (canPushCrate && grid.hasCrateAt(newX, newY)) 
				test = grid.getCrateAt(newX, newY).canMove(dir);

		}
		return test;
	}
	
	public void move(Grid grid) {
		move(grid, direction);
	}
	
	/**
	 * 
	 * @param grid
	 * @param dir
	 */
	public void move(Grid grid, Direction dir) {
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
		if (grid.hasCrateAt(newX, newY)){
			grid.getCrateAt(newX, newY).move(dir);
			grid.getMovementTracker().addPush();
		}
		else
			grid.getMovementTracker().addMove();
		setX(newX);
		setY(newY);
	}
	
	public void moveBackUp(Grid grid) {
		setY(y-1);
	}
	
	public void moveBackRight(Grid grid) {
		setX(x+1);
	}
	
	public void moveBackDown(Grid grid) {
		setY(y+1);
	}
	
	public void moveBackLeft(Grid grid) {
		setX(x-1);
	}
	
	public void pullCrate(Grid grid) {
		pullCrate(grid, direction);
	}
	
	public void pullCrate(Grid grid, Direction dir) {
		switch (dir){
		case UP:
			pullCrateUp(grid);
			break;
		case RIGHT:
			pullCrateRight(grid);
			break;
		case DOWN:
			pullCrateDown(grid);
			break;
		case LEFT:
			pullCrateLeft(grid);
		}
	}
	/**
	 * The crate is under the player, and the player go up and pull the crate
	 * ->En realite, je bouge la caisse puis le joueur. C'est pour ne pas devoir adapter les coordonnees a l'avance.
	 * @param grid
	 */
	public void pullCrateUp(Grid grid) {
		grid.getCrateAt(x, y+1).move(Direction.UP);
		setY(y-1);
	}
	
	public void pullCrateRight(Grid grid) {
		grid.getCrateAt(x-1, y).move(Direction.RIGHT);
		setX(x+1);
	}
	
	public void pullCrateDown(Grid grid) {
		grid.getCrateAt(x, y-1).move(Direction.DOWN);
		setY(y+1);
	}
	
	public void pullCrateLeft(Grid grid) {
		grid.getCrateAt(x+1, y).move(Direction.LEFT);
		setX(x-1);
	}
	
	@Override
	public String getSpriteName() {
		return "Player" + direction.name();
	}
}
