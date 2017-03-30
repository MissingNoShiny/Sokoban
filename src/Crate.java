
public class Crate extends Position {
	
	private Grid grid;
	
	private Component support;
	
	/**
	 * Warning : toujours placer la caisse sur une case de grid contenant déjà une instance
	 * @param x
	 * @param y
	 * @param grid
	 */
	public Crate(int x, int y, Grid grid) {
		super(x, y);
		this.grid = grid;
		setSupport(grid.getComponentAt(x, y));
		grid.placeComponentAt(x, y, this);
	}
	
	public Component getSupport() {
		return support;
	}

	public void setSupport(Component support) {
		this.support = support;
	}
	
	public boolean canMove(Direction dir) {
		boolean test = true;
		int x = getX(), y = getY();
		switch (dir) {
		case UP:
			if (y-1 < 0 || !grid.getComponentAt(x, y-1).canGoTrough())
				test = false;
			break;
		case RIGHT:
			if (x+1 >= grid.getWidth() || !grid.getComponentAt(x+1, y).canGoTrough())
				test = false;
			break;
		case DOWN:
			if (y+1 >= grid.getHeight() || !grid.getComponentAt(x, y+1).canGoTrough())
				test = false;
			break;
		case LEFT:
			if (x-1 < 0 || !grid.getComponentAt(x-1, y).canGoTrough())
				test = false;
			break;
		}
		return test;
	}
	
	/*
	 * D'abord je dirige le pointeur du tableau de la case actuelle vers le component support de la caisse
	 * Ensuite je dirige le pointeur de support vers le component de la case visee
	 * Finalement je dirige le pointeur de la case du dessus vers la caisse
	 * 
	 * L'idee est d'ajouter cette methode a l'interface movable
	 * Pour ca il faut adapter les move du Player. Pas evident
	 * Peut etre ensuite faire en sorte que position implemente movable et faire passer les moveUp, etc. dans position
	 */
	
	public void moveTo(int x, int y) {
		grid.placeComponentAt(getX(), getY(), getSupport());
		setSupport(grid.getComponentAt(x, y));
		setX(x);
		setY(y);
		grid.placeComponentAt(x, y, this);
	}
	
	public void move(Direction dir) {
		switch (dir) {
		case UP:
			moveTo(getX(), getY()-1);
			break;
		case RIGHT:
			moveTo(getX()+1, getY());
			break;
		case DOWN:
			moveTo(getX(), getY()+1);
			break;
		case LEFT:
			moveTo(getX()-1, getY());
			break;
		}
	}
	
	@Override
	public String getSpriteName () {
		if (getSupport().getSpriteName().equals("Goal"))
			return "CrateOnGoal";	
		return "Crate";
	}

	@Override
	public boolean canGoTrough() {
		return false;
	}
}
