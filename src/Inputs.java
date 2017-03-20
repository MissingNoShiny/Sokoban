
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Inputs implements KeyListener, MouseListener {

	private final long KEY_SPEED_CAP = 50;
	
	Game game;
	
	long lastKey = 0;
	
	
	public Inputs(Game game) {
		this.game = game;
	}
	
	//Keyoard
	
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

	
	
	//Mouse
	
	@Override
	public void mouseClicked(MouseEvent e) {
		game.mouseClicked(e);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	

}
