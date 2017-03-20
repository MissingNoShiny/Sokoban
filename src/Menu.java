import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JPanel;

public class Menu extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5237335232850181080L;
	
	//Je ne sais pas si c'est une bonne solution, je pensais faire un enum pour lister chaque page et
	//rendre le panel en fonction de la page actuelle.
	public static enum MenuState {
		MAIN;
	}
	
	private MenuState state = MenuState.MAIN;
	
	public MenuState getState() {
		return state;
	}
	
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D) g;
		setBackground(Game.BLEU_CLAIR);
		
		if (state == MenuState.MAIN) {
			
			g2.setFont(new Font("arial", 0, 100));
			g2.drawString(Game.TITLE, getWidth()/2 - 200, getHeight()/5);
			
			Rectangle playButton = new Rectangle(4*getWidth()/10, 4*getHeight()/10, 2*getWidth()/10, getHeight()/10);
			Rectangle quitButton = new Rectangle(4*getWidth()/10, 6*getHeight()/10, 2*getWidth()/10, getHeight()/10);
			g2.setColor(Game.ORANGE);
			g2.fill(playButton);
			g2.fill(quitButton);
			g2.setColor(Game.BLACK);
			g2.setFont(new Font("arial", 0, 70));
			g2.drawString("Play", playButton.x + 130, playButton.y + 80);
			g2.drawString("Quit", quitButton.x + 130, quitButton.y + 80);
		}
	}
	
}
