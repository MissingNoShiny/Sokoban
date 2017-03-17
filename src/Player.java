
public class Player extends Crate {
	
	private Direction direction;
	
	/*
	*public Player() {
		super(0, 0);
	}
	*/
	
	public Player(int xInput, int yInput) {
		super(xInput, yInput);
		direction = Direction.DOWN;
	}
	
	public void setDirection(Direction direction) {
		this.direction = direction;
	}
	
	public Direction getDirection() {
		return direction;
	}
	
	
	//Dans les fonctions canMove, utilisation de variables locales x et y (donc qui ne sont pas les attributs)
	@Override
	public boolean canMove(Grid grid, Direction dir) {
		int x = getX(), y = getY();
		boolean test = true;
		switch (dir) {
		case UP:
			if (y-1 < 0 || grid.getComponentAt(x, y-1).equals(Component.WALL))       
				test = false;
			else if (grid.hasCrateAt(x, y-1)) 
				test = grid.getCrateAt(x, y-1).canMove(grid, Direction.UP);
			break;
		case RIGHT:
			if (x+1 >= grid.getWidth() || grid.getComponentAt(x+1, y).equals(Component.WALL))
				test = false;
			else if (grid.hasCrateAt(x+1, y)) 
				test = grid.getCrateAt(x+1, y).canMove(grid, Direction.RIGHT);
			break;
		case DOWN:
			if (y+1 >= grid.getHeight() || grid.getComponentAt(x, y+1).equals(Component.WALL))
				test = false;
			else if (grid.hasCrateAt(x, y+1)) 
				test = grid.getCrateAt(x, y+1).canMove(grid, Direction.DOWN);
			break;
		case LEFT:
			if (x-1 < 0 || grid.getComponentAt(x-1, y).equals(Component.WALL))
				test = false;
			else if (grid.hasCrateAt(x-1, y)) 
				test = grid.getCrateAt(x-1, y).canMove(grid, Direction.LEFT);
			break;
		}
		return test;
	}
	
	public void move(Grid grid) {
		if (canMove(grid, direction)) {
			switch(direction) {
			case UP:
				moveUp(grid);
				break;
			case RIGHT:
				moveRight(grid);
				break;
			case DOWN:
				moveDown(grid);
				break;
			case LEFT:
				moveLeft(grid);
				break;
			}
		}

	}
	
	@Override
	public void moveUp(Grid grid) {
		if (grid.hasCrateAt(getX(), getY()-1))
				grid.getCrateAt(getX(), getY()-1).moveUp(grid);		
		setY(getY()-1);
	}
	
	@Override
	public void moveRight(Grid grid) {
		if (grid.hasCrateAt(getX()+1, getY()))
			grid.getCrateAt(getX()+1, getY()).moveRight(grid);		
		setX(getX()+1);
	}
	
	@Override
	public void moveDown(Grid grid) {
		if (grid.hasCrateAt(getX(), getY()+1))
			grid.getCrateAt(getX(), getY()+1).moveDown(grid);		
		setY(getY()+1);
	}
	
	@Override
	public void moveLeft(Grid grid) {
		if (grid.hasCrateAt(getX()-1, getY()))
			grid.getCrateAt(getX()-1, getY()).moveLeft(grid);	
		setX(getX()-1);
	}
	
	
	//Je sais, c'est con d'avoir deux methodes inutiles. A ameliorer. Surement en les suprimant aussi pour les caisses. A voir
	@Override
	public void letPlaceFree (Grid grid) {
	}
	
	@Override
	public void placeOnGrid(int x, int y, Grid grid) {
	}

}
