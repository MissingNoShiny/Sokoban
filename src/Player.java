
public class Player extends Crate {
	
	Direction direction;
	
	/*
	*public Player() {
		super(0, 0);
	}
	*/
	
	public Player(int xInput, int yInput, Grid grid) {
		super(xInput, yInput);
		grid.placeOnGrid(xInput, yInput, Component.PLAYER);
		direction = Direction.DOWN;
	}
	
	@Override
	public boolean canMove(Grid grid, Direction dir) {
		boolean test = true;
		switch (dir) {
		case UP:
			if (y-1 < 0 || grid.getComponentAt(x, y-1).equals(Component.WALL))       
				test = false;
			else if (grid.getComponentAt(x, y-1).equals(Component.CRATE)) 
				test = grid.getCrateAt(x, y-1).canMove(grid, Direction.UP);
			direction = Direction.UP;
			break;
		case RIGHT:
			if (x+1 >= grid.getWidth() || grid.getComponentAt(x+1, y).equals(Component.WALL))
				test = false;
			else if (grid.getComponentAt(x+1, y).equals(Component.CRATE)) 
				test = grid.getCrateAt(x+1, y).canMove(grid, Direction.RIGHT);
			direction = Direction.RIGHT;
			break;
		case DOWN:
			if (y+1 >= grid.getHeight() || grid.getComponentAt(x, y+1).equals(Component.WALL))
				test = false;
			else if (grid.getComponentAt(x, y+1).equals(Component.CRATE)) 
				test = grid.getCrateAt(x, y+1).canMove(grid, Direction.DOWN);
			direction = Direction.DOWN;
			break;
		case LEFT:
			if (x-1 < 0 || grid.getComponentAt(x-1, y).equals(Component.WALL))
				test = false;
			else if (grid.getComponentAt(x-1, y).equals(Component.CRATE)) 
				test = grid.getCrateAt(x-1, y).canMove(grid, Direction.LEFT);
			direction = Direction.LEFT;
			break;
		}
		return test;
	}
	
	@Override
	public void moveUp(Grid grid) {
		if (grid.getComponentAt(x, y-1).equals(Component.CRATE))
				grid.getCrateAt(x, y-1).moveUp(grid);
		grid.placeOnGrid(x, y, Component.GROUND);
		grid.placeOnGrid(x, y-1, Component.PLAYER);		
		y -= 1;
	}
	
	@Override
	public void moveRight(Grid grid) {
		if (grid.getComponentAt(x+1, y).equals(Component.CRATE))
			grid.getCrateAt(x+1, y).moveRight(grid);
		grid.placeOnGrid(x, y, Component.GROUND);
		grid.placeOnGrid(x+1, y, Component.PLAYER);		
		x += 1;
	}
	
	@Override
	public void moveDown(Grid grid) {
		if (grid.getComponentAt(x, y+1).equals(Component.CRATE))
			grid.getCrateAt(x, y+1).moveDown(grid);
		grid.placeOnGrid(x, y, Component.GROUND);
		grid.placeOnGrid(x, y+1, Component.PLAYER);		
		y += 1;
	}
	
	@Override
	public void moveLeft(Grid grid) {
		if (grid.getComponentAt(x-1, y).equals(Component.CRATE))
			grid.getCrateAt(x-1, y).moveLeft(grid);
		grid.placeOnGrid(x, y, Component.GROUND);
		grid.placeOnGrid(x-1, y, Component.PLAYER);		
		x -= 1;
	}

}
