
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class InfoPanel extends JPanel{

	private static final long serialVersionUID = 6195740163330975520L;

	/**
	 * The Game that runs this panel, and from which the fps will be retrieved.
	 */
	private Game game;
	
	/**
	 * The grid of the level to display the information of.
	 */
	private Grid grid;
	
	/**
	 * The JLabel that displays the amount of rendered frames in the last second.
	 */
	private JLabel fps;
	
	/**
	 * The JLabel that displays the current moves count.
	 */
	private JLabel movesCount;
	
	
	/**
	 * The JLabel that displays the current pushes count.
	 */
	private JLabel pushesCount;
	
	public InfoPanel(Game game, Grid grid) {
		this.game = game;
		this.grid = grid;
		setBackground(Options.backGroundColor);
		fps = new DefaultLabel("", Options.backGroundColor);
		add(fps);
		movesCount = new DefaultLabel("", Options.backGroundColor);
		add(movesCount);
		pushesCount = new DefaultLabel("", Options.backGroundColor);
		add(pushesCount);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		fps.setText("FPS: " + game.getFps());
		movesCount.setText("Moves: " + grid.getTracker().getMovesCount());
		pushesCount.setText("Pushes: " + grid.getTracker().getPushesCount());
	}
}
