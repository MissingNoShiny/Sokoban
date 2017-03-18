
public interface Movable {
	public boolean canMove(Grid grid, Direction dir);
	public void moveUp(Grid grid);
	public void moveRight(Grid grid);
	public void moveDown(Grid grid);
	public void moveLeft(Grid grid);
}
