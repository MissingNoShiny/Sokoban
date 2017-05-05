
import java.awt.Graphics;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class InfoPanel extends JPanel{
	
	private static final long serialVersionUID = 1L;
	
	private Game game;
	
	private Grid grid;
	
	private JLabel fps;
	
	private JLabel movesCount;
	
	private JLabel pushesCount;
	
	public InfoPanel(Game game, Grid grid) {
		this.game = game;
		this.grid = grid;
		setLayout(new GridLayout(10, 1));
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
