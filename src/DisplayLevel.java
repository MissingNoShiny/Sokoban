
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseEvent;
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
		setLayout(new BorderLayout());
		
		displayGrid = new DisplayGrid(grid);
		add(displayGrid, BorderLayout.CENTER);
		//displayGrid.requestFocusInWindow();
		
		Button backToMenuButton = new Button("Back to menu", Color.orange, 50, game.getWindow().getHeight(), game.getWindow().getWidth()/6);
		backToMenuButton.addMouseListener(new ButtonListener(backToMenuButton) {
			@Override
			public void mouseReleased(MouseEvent e) {
				game.loadMenu();
				grid.saveGrid("level1_saved", game);
			}
		});
		add(backToMenuButton, BorderLayout.EAST);
		
		Button undoButton = new Button("Undo", Color.orange, 50, game.getWindow().getHeight(), game.getWindow().getWidth()/6);
		undoButton.addMouseListener(new ButtonListener(undoButton) {
			@Override
			public void mouseReleased(MouseEvent e) {
				grid.getMovementTracker().undo();
			}
		});
		add(undoButton, BorderLayout.WEST);
	}
}
