package be.ac.umons.info.sokoban.grid;

class Crate extends Position {
	
	private Component support;
	
	/**
	 * 
	 * @param grid
	 * @param x
	 * @param y
	 * @throws IllegalArgumentException
	 */
	public Crate(Grid grid, int x, int y) throws IllegalArgumentException {
		super(grid, x, y);
		try {
			if (!grid.getComponentAt(x, y).canBePassedThrough())
				throw new IllegalArgumentException();
		} catch (NullPointerException e) {
			throw new IllegalArgumentException();
		}
		support = grid.getComponentAt(x, y);
		grid.placeComponentAt(x, y, this);
	}
	
	/**
	 * Gets the current support of the Crate.
	 * @return The support of the Crate
	 */
	public Component getSupport() {
		return support;
	}
	
	/**
	 * Checks if the Crate can move in specified Direction.
	 * @param dir The Direction to check for
	 * @return true if the Crate can move in that Direction, false else
	 */
	public boolean canMove(Direction dir) {
		Point p = getNextPoint(this, dir);
		int newX = p.getX();
		int newY = p.getY();
		
		if (newX < 0 || newX >= grid.getWidth() || newY < 0 || newY >= grid.getHeight() || !grid.getComponentAt(newX, newY).canBePassedThrough())
			return false;
			
		return true;
	}
	
	/**
	 * Checks if the Crate can be pulled in specified Direction.
	 * @param dir The Direction to check for
	 * @return true if the Crate can be pulled in that Direction, false else
	 */
	public boolean canBePulled(Direction dir) {
		if (canMove(dir)) {
			Point p = getNextPoint(this, dir);
			p = getNextPoint(p, dir);
			int newX = p.getX();
			int newY = p.getY();
			if (newX >= 0 && newX < grid.getWidth() && newY >= 0 && newY < grid.getHeight() && grid.getComponentAt(newX, newY).canBePassedThrough())
				return true;
		}
		return false;
	}
	
	
	/**
	 * Moves the Crate in specified Direction.
	 * @param dir The Direction to move the Crate in
	 */
	public void move(Direction dir) {
		int newX = x, newY = y;
		switch (dir) {
		case UP:
			newY--;
			break;
		case RIGHT:
			newX++;;
			break;
		case DOWN:
			newY++;
			break;
		case LEFT:
			newX--;
			break;
		}
		grid.placeComponentAt(x, y, support);
		support = grid.getComponentAt(newX, newY);
		x = newX;
		y = newY;
		grid.placeComponentAt(newX, newY, this);
	}
	
	@Override
	public String getName() {
		if (support.getName().equals("Goal"))
			return "CrateOnGoal";	
		return "Crate";
	}
}
