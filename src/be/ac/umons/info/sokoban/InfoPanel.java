package be.ac.umons.info.sokoban;

import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class InfoPanel extends JPanel{

	private static final long serialVersionUID = 6195740163330975520L;
	
	/**
	 * The grid of the level to display the information of.
	 */
	private Grid grid;
	
	/**
	 * The JLabel that displays the current moves count.
	 */
	private JLabel movesCount;
	
	
	/**
	 * The JLabel that displays the current pushes count.
	 */
	private JLabel pushesCount;
	
	public InfoPanel(Grid grid) {
		this.grid = grid;
		setBackground(Options.getBackgroundColor());
		movesCount = new DefaultLabel("", Options.getBackgroundColor());
		add(movesCount);
		pushesCount = new DefaultLabel("", Options.getBackgroundColor());
		add(pushesCount);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		movesCount.setText("Moves: " + grid.getTracker().getMovesCount());
		pushesCount.setText("Pushes: " + grid.getTracker().getPushesCount());
	}
}
