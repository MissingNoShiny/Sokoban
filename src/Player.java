
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
	
	@Override
	public String getSpriteName() {
		return "Player" + direction.name();
	}
}
