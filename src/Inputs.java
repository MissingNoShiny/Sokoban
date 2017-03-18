import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Inputs implements KeyListener {

	Game game;
	
	long lastKey = 0;
	
	public Inputs(Game game) {
		this.game = game;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if (System.currentTimeMillis() - lastKey > 50) {
			game.keyPressed(e);
			lastKey = System.currentTimeMillis();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		game.keyReleased(e);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		game.keyTyped(e);
	}
	
	

}
