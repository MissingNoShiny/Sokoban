
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
	 * Le probleme avec les fonctions moveBack et pullCrate actuelles, c'est qu'elles rendent en partie inutile le switch
	 * du movementTracker
	 * Peut etre serait il preferable de separer ces fonction en 4 fonctions distinctes
	 */
	public void moveBack(Grid grid, Direction dir) {
		int newX = getX();
		int newY = getY();
		switch (dir) {
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
	
	public void pullCrate (Grid grid, Direction dir) {
		int newX = getX();
		int newY = getY();
		int crateX = getX();
		int crateY = getY();
		Direction crateDir = null;
		switch (dir) {
		case UP:
			newY++; 
			crateY--;
			crateDir = Direction.DOWN;
			break;
		case RIGHT:
			newX--;
			crateX++;
			crateDir = Direction.LEFT;
			break;
		case DOWN:
			newY--;
			crateY++;
			crateDir = Direction.UP;
			break;
		case LEFT:
			newX++;
			crateX--;
			crateDir = Direction.RIGHT;
			break;
		}
		
		grid.getCrateAt(crateX, crateY).move(crateDir);
		setX(newX);
		setY(newY);
	}
	
	@Override
	public String getSpriteName() {
		return "Player" + direction.name();
	}
}
