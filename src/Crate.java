
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
	
	public void moveTo(int newX, int newY) {
		grid.placeComponentAt(x, y, getSupport());
		setSupport(grid.getComponentAt(newX, newY));
		setX(newX);
		setY(newY);
		grid.placeComponentAt(newX, newY, this);
	}
	
	public void move(Direction dir) {
		switch (dir) {
		case UP:
			moveTo(x, y-1);
			break;
		case RIGHT:
			moveTo(x+1, y);
			break;
		case DOWN:
			moveTo(x, y+1);
			break;
		case LEFT:
			moveTo(x-1, y);
			break;
		}
	}
	
	@Override
	public String getName () {
		if (getSupport().getName().equals("Goal"))
			return "CrateOnGoal";	
		return "Crate";
	}

	@Override
	public boolean canGoTrough() {
		return false;
	}
}
