
public class Crate extends Position implements Component, Movable {

	private Component support;
	
	public Crate(int x, int y, Grid grid) {
		super(x, y);
		grid.placeComponentAt(x, y, this);
		setSupport(new Ground());
	}
	
	public Component getSupport() {
		return support;
	}

	public void setSupport(Component support) {
		this.support = support;
	}
	
	public boolean canMove(Grid grid, Direction dir) {
		boolean test = true;
		int x = getX(), y = getY();
		switch (dir) {
		case UP:
			if (y-1 < 0 || grid.getComponentAt(x, y-1).cannotGoTrough())
				test = false;
			break;
		case RIGHT:
			if (x+1 >= grid.getWidth() || grid.getComponentAt(x+1, y).cannotGoTrough())
				test = false;
			break;
		case DOWN:
			if (y+1 >= grid.getHeight() || grid.getComponentAt(x, y+1).cannotGoTrough())
				test = false;
			break;
		case LEFT:
			if (x-1 < 0 || grid.getComponentAt(x-1, y).cannotGoTrough())
				test = false;
			break;
		}
		return test;
	}
	
	/**
	 * D'abord je dirige le pointeur du tableau de la case actuelle vers le component support de la caisse
	 * Ensuite je dirige le pointeur de support vers le component de la case du dessus
	 * Finalement je dirige le pointeur de la case du dessus vers la caisse
	 */
	public void moveUp(Grid grid) {
		grid.placeComponentAt(getX(), getY(), getSupport());
		setSupport(grid.getComponentAt(getX(), getY()-1));
		setY(getY()-1);
		grid.placeComponentAt(getX(), getY(), this);
	}
	
	public void moveRight(Grid grid) {
		grid.placeComponentAt(getX(), getY(), getSupport());
		setSupport(grid.getComponentAt(getX()+1, getY()));
		setX(getX()+1);
		grid.placeComponentAt(getX(), getY(), this);
	}
	
	public void moveDown(Grid grid) {
		grid.placeComponentAt(getX(), getY(), getSupport());
		setSupport(grid.getComponentAt(getX(), getY()+1));
		setY(getY()+1);
		grid.placeComponentAt(getX(), getY(), this);
	}
	
	public void moveLeft(Grid grid) {
		grid.placeComponentAt(getX(), getY(), getSupport());
		setSupport(grid.getComponentAt(getX()-1, getY()));
		setX(getX()-1);
		grid.placeComponentAt(getX(), getY(), this);
	}
	
	@Override
	public String getSpriteName () {
		if (getSupport().getSpriteName().equals("Goal"))
			return "CrateOnGoal";	
		return "Crate";
	}

	@Override
	public boolean cannotGoTrough() {
		return true;
	}
}
