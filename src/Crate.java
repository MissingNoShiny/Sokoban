public class Crate extends Position {

	public Crate(int x, int y) {
		super(x, y);
	}
	
	public Crate(int x, int y, Grid grid) {
		super(x, y);
		grid.placeOnGrid(x, y, Component.CRATE);
	}
	
	public boolean canMove(Grid grid, Direction dir) {
		boolean test = true;
		switch (dir) {
		case UP:
			if (y-1 < 0 || grid.getComponentAt(x, y-1).equals(Component.WALL) || grid.getComponentAt(x, y-1).equals(Component.CRATE))
				test = false;
			break;
		case RIGHT:
			if (x+1 >= grid.getWidth() || grid.getComponentAt(x+1, y).equals(Component.WALL) || grid.getComponentAt(x+1, y).equals(Component.CRATE))
				test = false;
			break;
		case DOWN:
			if (y+1 >= grid.getHeight() || grid.getComponentAt(x, y+1).equals(Component.WALL) || grid.getComponentAt(x, y+1).equals(Component.CRATE))
				test = false;
			break;
		case LEFT:
			if (x-1 < 0 || grid.getComponentAt(x-1, y).equals(Component.WALL) || grid.getComponentAt(x-1, y).equals(Component.CRATE))
				test = false;
			break;
		}
		return test;
	}
	
	public void moveUp(Grid grid) {
		grid.placeOnGrid(x, y, Component.GROUND);
		grid.placeOnGrid(x, y-1, Component.CRATE);		
		y -= 1;
	}
	
	public void moveRight(Grid grid) {
		grid.placeOnGrid(x, y, Component.GROUND);
		grid.placeOnGrid(x+1, y, Component.CRATE);		
		x += 1;
	}
	
	public void moveDown(Grid grid) {
		grid.placeOnGrid(x, y, Component.GROUND);
		grid.placeOnGrid(x, y+1, Component.CRATE);		
		y += 1;
	}
	
	public void moveLeft(Grid grid) {
		grid.placeOnGrid(x, y, Component.GROUND);
		grid.placeOnGrid(x-1, y, Component.CRATE);		
		x -= 1;
	}
}
