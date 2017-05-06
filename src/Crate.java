
public class Crate extends Position {
	
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
	
	public Component getSupport() {
		return support;
	}
	
	public boolean canMove(Direction dir) {
		
		Point p = Direction.assocyDirectionToNewPoint(x, y, dir);
		int newX = p.getX();
		int newY = p.getY();
		
		if (newX < 0 || newX >= grid.getWidth() || newY < 0 || newY >= grid.getHeight() || !grid.getComponentAt(newX, newY).canBePassedThrough())
			return false;
			
		return true;
	}
	
	public boolean canBePulled(Direction dir) {
		if (canMove(dir)) {
			Point p = Direction.assocyDirectionToNewPoint(x, y, dir);
			p = Direction.assocyDirectionToNewPoint(p.getX(), p.getY(), dir);
			int newX = p.getX();
			int newY = p.getY();
			if (newX >= 0 && newX < grid.getWidth() && newY >= 0 && newY < grid.getHeight() && grid.getComponentAt(newX, newY).canBePassedThrough())
				return true;
		}
		return false;
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

	@Override
	public boolean canBePassedThrough() {
		return false;
	}
}
