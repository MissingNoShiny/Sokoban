
public class Crate extends Position implements Movable {

	private Component support;
	
	public Crate(int x, int y, Grid grid) {
		super(x, y);
		grid.placeComponentAt(x, y, this);
		setSupport(grid.new Ground());
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
			if (y-1 < 0 || grid.getComponentAt(x, y-1).getNameSprite().equals("Wall") || grid.hasCrateAt(x, y-1))
				test = false;
			break;
		case RIGHT:
			if (x+1 >= grid.getWidth() || grid.getComponentAt(x+1, y).getNameSprite().equals("Wall") || grid.hasCrateAt(x+1, y))
				test = false;
			break;
		case DOWN:
			if (y+1 >= grid.getHeight() || grid.getComponentAt(x, y+1).getNameSprite().equals("Wall") || grid.hasCrateAt(x, y+1))
				test = false;
			break;
		case LEFT:
			if (x-1 < 0 || grid.getComponentAt(x-1, y).getNameSprite().equals("Wall") || grid.hasCrateAt(x-1, y))
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
	public String getNameSprite () {
		if (getSupport().getNameSprite().equals("Goal"))
			return "CrateOnGoal";	
		return "Crate";
	}
}
