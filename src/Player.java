
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
	
	//Déplacements
	public void move(int dir){ //1 pour Up, 2 pour Right, 3 pour Down, 4 pour Left
		switch (dir) {
		case 1:  //A chaque fois, un test pour voir si le mouvement est possible est nécessaire
			this.moveUp();
			break;
		case 2:
			this.moveRight();
			break;
		case 3:
			this.moveDown();
			break;
		case 4:
			this.moveLeft();
			break;
		default :
			break; // Un pass serait peut être plus approprié
		}
	
	}
	/*public void possibleMove(int dir, qqch qui permette de voir si le mvt est possible){
	 * 
	 * } */
	
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
	
	//Pour détecter si le joueur a bougé entre deux exécutions de la fonction
	public boolean hasMoved() {
		if (x == oldX && y == oldY)
			return false;
		oldX = x;
		oldY = y;
		return true;
	}
}
