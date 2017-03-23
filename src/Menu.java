import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

public class Menu extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5237335232850181080L;
	
	/*Je ne sais pas si c'est une bonne solution, je pensais faire un enum pour lister chaque page et
	 *rendre le panel en fonction de la page actuelle.
	 * 
	 * Il me semble que lorsque le menu est affiche, il faudrait que le playing n'existe pas, et inversement,
	 * que lorsqu'on joue, le menu n'existe pas. Donc il n'y aurait pas cette idee que l'un et l'autre coexistent et 
	 * que l'on rende le panel en fonction de ce dont on a besoin. 
	 * 
	 * Il faudrait plus, pour ma part, que lorsqu'un panel en a fini, il se ferme lui-meme et appelle -toujours lui meme
	 * le menu.
	 */
	
	private Game game;
	
	public Menu(Game game) {
		this.game = game;
		GridLayout gl = new GridLayout(5,1);
		gl.setVgap(3);
		setLayout(gl);
		Button playButton = new Button("Play");
		playButton.addMouseListener(new ButtonListener(playButton) {
			@Override
			public void mousePressed(MouseEvent e) {
				game.loadLevel("../levels/level1.txt");
			}
		});
		add(playButton);
		Button quitButton = new Button("Quit");
		quitButton.addMouseListener(new ButtonListener(quitButton) {
			@Override
			public void mousePressed(MouseEvent e) {
				game.stop();
			}
		});
		add(quitButton);
	}
	
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
			
		}
	}
}
