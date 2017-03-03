
public class Player {
	
	int x, y;
	
	public Player() {
		this(0, 0);
	}
	
	public Player(int xInput, int yInput) {
		x = xInput;
		y = yInput;
	}
	
	//Déplacements
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
	
}
