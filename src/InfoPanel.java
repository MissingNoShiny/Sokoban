import java.awt.Font;
import java.awt.Graphics;

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
		fps = new JLabel();
		fps.setHorizontalAlignment(JLabel.CENTER);
		fps.setFont(new Font(Menu.fontName, 0, 50));
		add(fps);
		movesCount = new JLabel();
		movesCount.setHorizontalAlignment(JLabel.CENTER);
		movesCount.setFont(new Font(Menu.fontName, 0, 50));
		add(movesCount);
		pushesCount = new JLabel();
		pushesCount.setHorizontalAlignment(JLabel.CENTER);
		pushesCount.setFont(new Font(Menu.fontName, 0, 50));
		add(pushesCount);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		setBackground(Game.BLEU_CLAIR);
		fps.setText("FPS: " + game.getFps());
		movesCount.setText("Moves: " + grid.getTracker().getMovesCount());
		pushesCount.setText("Pushes: " + grid.getTracker().getPushesCount());
	}
}
