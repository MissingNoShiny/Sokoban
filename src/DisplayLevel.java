
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class DisplayLevel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4992322428530946741L;
	
	/*
	 * J'ai du faire de displaygrid un attribut de displayLevel, pour lui donner le focus. Il faudra trouver 
	 * une meilleure solution
	 */
	public DisplayGrid displayGrid;
	
	/**
	 * TODO
	 * @param grid The Grid object to get data from
	 * @param game
	 */
	
	public DisplayLevel(Grid grid, Game game) {
		setFocusable(true);
		setOpaque(false);
		setVisible(true);
		setLayout(new BorderLayout());
		
		displayGrid = new DisplayGrid(grid);
		add(displayGrid, BorderLayout.CENTER);
		//displayGrid.requestFocusInWindow();
		
		JPanel buttonsPanel = new JPanel(){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				setBackground(Game.BLEU_CLAIR);
			}
		};
		
		buttonsPanel.setOpaque(true);
		add(buttonsPanel, BorderLayout.EAST);
		System.out.println(getWidth());
		buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.PAGE_AXIS));
		
		Button backToMenuButton = new Button("Back to menu", Color.orange, 50);
		backToMenuButton.addMouseListener(new ButtonListener(backToMenuButton) {
			@Override
			public void mouseReleased(MouseEvent e) {
				game.loadMenu();
				grid.saveGrid("level1_saved", game);
			}
		});
		buttonsPanel.add(backToMenuButton);
		
		Button undoButton = new Button("Undo", Color.orange, 50);
		undoButton.addMouseListener(new ButtonListener(undoButton) {
			@Override
			public void mouseReleased(MouseEvent e) {
				grid.getMovementTracker().undo();
			}
		});
		buttonsPanel.add(undoButton);
		
		Button resetButton = new Button("Reset", Color.orange, 50);
		resetButton.addMouseListener(new ButtonListener(resetButton) {
			@Override
			public void mouseReleased(MouseEvent e) {
				grid.getMovementTracker().reset();
			}
		});
		buttonsPanel.add(resetButton);
	}
}
