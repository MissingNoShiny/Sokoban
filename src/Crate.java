public class Crate extends Position implements Movable {

	public Crate(int x, int y) {
		super(x, y);
	}
	
	public Crate(int x, int y, Grid grid) {
		super(x, y);
		placeOnGrid(x, y, grid);
	}
	
	public boolean canMove(Grid grid, Direction dir) {
		boolean test = true;
		int x = getX(), y = getY();
		switch (dir) {
		case UP:
			if (y-1 < 0 || grid.getComponentAt(x, y-1).equals(Component.WALL) || grid.hasCrateAt(x, y-1))
				test = false;
			break;
		case RIGHT:
			if (x+1 >= grid.getWidth() || grid.getComponentAt(x+1, y).equals(Component.WALL) || grid.hasCrateAt(x+1, y))
				test = false;
			break;
		case DOWN:
			if (y+1 >= grid.getHeight() || grid.getComponentAt(x, y+1).equals(Component.WALL) || grid.hasCrateAt(x, y+1))
				test = false;
			break;
		case LEFT:
			if (x-1 < 0 || grid.getComponentAt(x-1, y).equals(Component.WALL) || grid.hasCrateAt(x-1, y))
				test = false;
			break;
		}
		return test;
	}
	
	public void moveUp(Grid grid) {
		letPlaceFree(grid);
		placeOnGrid(getX(), getY()-1, grid);		
		setY(getY()-1);
	}
	
	public void moveRight(Grid grid) {
		letPlaceFree(grid);
		placeOnGrid(getX()+1, getY(), grid);		
		setX(getX()+1);
	}
	
	public void moveDown(Grid grid) {
		letPlaceFree(grid);
		placeOnGrid(getX(), getY()+1, grid);		
		setY(getY()+1);
	}
	
	public void moveLeft(Grid grid) {
		letPlaceFree(grid);
		placeOnGrid(getX()-1, getY(), grid);		
		setX(getX()-1);
	}
	
	public void letPlaceFree(Grid grid) {
		if (grid.getComponentAt(getX(), getY()).equals(Component.CRATE_ON_GOAL))
			grid.placeComponentAt(getX(), getY(), Component.GOAL);
		else
			grid.placeComponentAt(getX(), getY(), Component.GROUND);
	}
	
	public void placeOnGrid(int x, int y, Grid grid) {
		if (grid.getComponentAt(x, y) == Component.GOAL)
			grid.placeComponentAt(x, y, Component.CRATE_ON_GOAL);
		else
			grid.placeComponentAt(x,  y, Component.CRATE);
	}
}
