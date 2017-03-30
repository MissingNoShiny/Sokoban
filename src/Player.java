
public class Player extends Position implements Movable {
	
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
	 * 
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
				test = grid.getCrateAt(newX, newY).canMove(grid, dir);
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
				grid.getCrateAt(newX, newY).move(grid, direction);					
			}
			setX(newX);
			setY(newY);
		}
	}
	
	public String getSpriteName() {
		return "Player" + direction.name();
	}
	
	public void moveUp(Grid grid) {
		if (grid.hasCrateAt(getX(), getY()-1))
				grid.getCrateAt(getX(), getY()-1).moveUp(grid);		
		setY(getY()-1);
	}
	
	public void moveRight(Grid grid) {
		if (grid.hasCrateAt(getX()+1, getY()))
			grid.getCrateAt(getX()+1, getY()).moveRight(grid);		
		setX(getX()+1);
	}
	
	public void moveDown(Grid grid) {
		if (grid.hasCrateAt(getX(), getY()+1))
			grid.getCrateAt(getX(), getY()+1).moveDown(grid);		
		setY(getY()+1);
	}
	
	public void moveLeft(Grid grid) {
		if (grid.hasCrateAt(getX()-1, getY()))
			grid.getCrateAt(getX()-1, getY()).moveLeft(grid);	
		setX(getX()-1);
	}

}
