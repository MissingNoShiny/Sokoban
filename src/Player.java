
public class Player {
	
	int x, y, oldX, oldY;
	
	public Player() {
		this(0, 0);
	}
	
	public Player(int xInput, int yInput) {
		x = xInput;
		y = yInput;
		oldX = x;
		oldY = y;		
	}
	
	//Accesseurs
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	//D�placements
	public void moveUp() {
		y += 1;
	}
	
	public void moveDown() {
		y -= 1;
	}
	
	public void moveRight() {
		x += 1;
	}
	
	public void moveLeft() {
		y -= 1;
	}
	
	//Pour d�tecter si le joueur a boug� entre deux ex�cutions de la fonction
	public boolean hasMoved() {
		if (x == oldX && y == oldY)
			return false;
		oldX = x;
		oldY = y;
		return true;
	}
}
