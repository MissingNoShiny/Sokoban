
import java.awt.Image;
import java.awt.image.BufferedImage;

public class Player extends Position {
	
	int oldX, oldY;
	Image sprite;
	
	public Player() {
		super(0, 0, new BufferedImage(16, 16, BufferedImage.TYPE_INT_RGB));
	}
	
	public Player(int xInput, int yInput, Image img) {
		super(xInput, yInput, img);
	}
	
	public void moveUp() {
		y -= 1;
	}
	
	public void moveDown() {
		y += 1;
	}	
	
	public void moveRight() {
		x += 1;
	}
	
	public void moveLeft() {
		x -= 1;
	}
	
	//Pour détecter si le joueur a changé de position entre deux exécutions de la fonction
	public boolean hasMoved() {
		if (x == oldX && y == oldY)
			return false;
		oldX = x;
		oldY = y;
		return true;
	}
}
