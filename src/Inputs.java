
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Inputs implements KeyListener {

	private final long KEY_SPEED_CAP = 50;
	
	Game game;
	
	long lastKey = 0;
	
	
	public Inputs(Game game) {
		this.game = game;
	}
	
	//Keyboard
	
	@Override
	public void keyPressed(KeyEvent e) {
		if (System.currentTimeMillis() - lastKey > KEY_SPEED_CAP) {
			game.keyPressed(e);
			lastKey = System.currentTimeMillis();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}
}
