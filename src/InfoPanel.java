import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class InfoPanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Grid grid;
	
	JLabel movesCount;
	
	JLabel pushesCount;
	
	public InfoPanel(Grid grid) {
		this.grid = grid;
		movesCount = new JLabel();
		movesCount.setHorizontalAlignment(JLabel.CENTER);
		movesCount.setFont(new Font("arial", 0, 40));
		add(movesCount);
		pushesCount = new JLabel();
		pushesCount.setHorizontalAlignment(JLabel.CENTER);
		pushesCount.setFont(new Font("arial", 0, 40));
		add(pushesCount);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		setBackground(Game.BLEU_CLAIR);
		movesCount.setText("Moves: "+grid.getMovementTracker().getMovesCount());
		pushesCount.setText("Pushes: " + grid.getMovementTracker().getPushesCount());
	}
}
