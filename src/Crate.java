
public class Crate extends Position implements Component, Movable {

	private Component support;
	
	/**
	 * Warning : toujours placer la caisse sur une case de grid contenant déjà une instance
	 * @param x
	 * @param y
	 * @param grid
	 */
	public Crate(int x, int y, Grid grid) {
		super(x, y);
		setSupport(grid.getComponentAt(x, y));
		grid.placeComponentAt(x, y, this);
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
	
	/**
	 * Ajout de la methode moveTo
	 * 
	 * D'abord je dirige le pointeur du tableau de la case actuelle vers le component support de la caisse
	 * Ensuite je dirige le pointeur de support vers le component de la case visee
	 * Finalement je dirige le pointeur de la case du dessus vers la caisse
	 * 
	 * L'idee est d'ajouter cette methode a l'interface movable
	 * Pour ca il faut adapter les move du Player. Pas evident
	 * Peut etre ensuite faire en sorte que position implemente movable et faire passer les moveUp, etc. dans position
	 */
	
	public void moveTo(Grid grid, int x, int y) {
		grid.placeComponentAt(getX(), getY(), getSupport());
		setSupport(grid.getComponentAt(x, y));
		setX(x);
		setY(y);
		grid.placeComponentAt(x, y, this);
	}
	
	public void moveUp(Grid grid) {
		moveTo(grid, getX(), getY()-1);
	}
	
	public void moveRight(Grid grid) {
		moveTo(grid, getX()+1, getY());
	}
	
	public void moveDown(Grid grid) {
		moveTo(grid, getX(), getY()+1);
	}
	
	public void moveLeft(Grid grid) {
		moveTo(grid, getX()-1, getY());
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
