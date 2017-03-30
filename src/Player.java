
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
	
	/**
	 * @param grid
	 * @param dir
	 */
	public boolean canMove(Grid grid, Direction dir) {
		int newX = getX(), newY = getY();
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
		
		if (newX < grid.getHeight() && newX > 0 && newY < grid.getWidth() && newY > 0) {
			if (grid.getComponentAt(newX, newY).canGoTrough()) 
				test = true;
			else if (grid.hasCrateAt(newX, newY)) 
				test = grid.getCrateAt(newX, newY).canMove(dir);
		}
		return test;
	}
	
	public void move(Grid grid) {
		if (canMove(grid, direction)) {
			int newX = getX();
			int newY = getY();
			switch (direction) {
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
				grid.getCrateAt(newX, newY).move(direction);
				grid.getMovementTracker().addPush();
			}
			else
				grid.getMovementTracker().addMove();
			setX(newX);
			setY(newY);
		}
	}
	
	/*
	 * Les fonctions moveBack et pullCrate sont facilement fusionnables, si il faut vraiment
	 */
	public void moveBack(Grid grid) {
		int newX = getX();
		int newY = getY();
		switch (direction) {
		case UP:
			newY++;
			break;
		case RIGHT:
			newX--;
			break;
		case DOWN:
			newY--;
			break;
		case LEFT:
			newX++;
			break;
		}
		setX(newX);
		setY(newY);
	}
	
	public void pullCrate (Grid grid) {
		int newX = getX();
		int newY = getY();
		int crateX = getX();
		int crateY = getY();
		Direction dir = null;
		switch (direction) {
		case UP:
			newY++; 
			crateY--;
			dir = Direction.DOWN;
			break;
		case RIGHT:
			newX--;
			crateX++;
			dir = Direction.LEFT;
			break;
		case DOWN:
			newY--;
			crateY++;
			dir = Direction.UP;
			break;
		case LEFT:
			newX++;
			crateX--;
			dir = Direction.RIGHT;
			break;
		}
		
		grid.getCrateAt(crateX, crateY).move(dir);
		setX(newX);
		setY(newY);
	}
	
	@Override
	public String getSpriteName() {
		return "Player" + direction.name();
	}
}
