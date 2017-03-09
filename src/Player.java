
import javax.imageio.*;
import java.awt.image.*;

public class Player extends Position {
	
	int oldX, oldY;
	
	public Player() {
		this(0, 0, new BufferedImage(16, 16, Image));
	}
	
	public Player(int xInput, int yInput, Image img) {
		super(xInput, yInput);
		oldX = x;
		oldY = y;
		sprite = img;
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
		x -= 1;
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
