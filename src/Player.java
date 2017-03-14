
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Player extends Crate {
	
	int oldX, oldY;
	
	public Player() {
		super(0, 0);
	}
	
	public Player(int xInput, int yInput) {
		super(xInput, yInput);
	}
	
	@Override
	public void setSprite() {
		try {
		    sprite = ImageIO.read(new File("resources/banana.gif"));
		} catch (IOException e) {
		}
	}
	
	//Pour détecter si le joueur a change de position entre deux executions de la fonction
	public boolean hasMoved() {
		if (x == oldX && y == oldY)
			return false;
		oldX = x;
		oldY = y;
		return true;
	}
}
