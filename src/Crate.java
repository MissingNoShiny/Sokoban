
public class Crate extends Position {
	
	private Component support;
	
	/**
	 * Warning : toujours placer la caisse sur une case de grid contenant déjà une instance
	 * @param x
	 * @param y
	 * @param grid
	 */
	public Crate(Grid grid, int x, int y) {
		super(grid, x, y);
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
	
	public boolean canBePulled(Direction dir) {
		boolean test = false;
		if (canMove(dir)) {
			switch (dir) {
			case UP:
				if (y-2 >= 0 && grid.getComponentAt(x, y-2).canGoTrough())
					test = true;
				break;
			case RIGHT:
				if (x+2 < grid.getWidth() && grid.getComponentAt(x+2, y).canGoTrough())
					test = true;
				break;
			case DOWN:
				if (y+2 < grid.getHeight() && grid.getComponentAt(x, y+2).canGoTrough())
					test = true;
				break;
			case LEFT:
				if (x-2 >= 0 && grid.getComponentAt(x-2, y).canGoTrough())
					test = true;
				break;
			}
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
		grid.placeComponentAt(x, y, getSupport());
		setSupport(grid.getComponentAt(newX, newY));
		setX(newX);
		setY(newY);
		grid.placeComponentAt(newX, newY, this);
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
