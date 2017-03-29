
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

public class DisplayLevel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4992322428530946741L;
	
	/**
	 * TODO
	 * @param grid The Grid object to get data from
	 */
	public DisplayLevel(Grid grid, Game game) {
		setFocusable(true);
		setLayout(new BorderLayout());
		
		DisplayGrid displayGrid = new DisplayGrid(grid);
		add(displayGrid, BorderLayout.CENTER);
		//displayGrid.requestFocusInWindow();
		
		Button backToMenuButton = new Button("Back to menu", Color.orange, 50);
		backToMenuButton.addMouseListener(new ButtonListener(backToMenuButton) {
			@Override
			public void mouseReleased(MouseEvent e) {
				game.loadMenu();
				grid.saveGrid("level1_saved", game);
			}
		});
		add(backToMenuButton, BorderLayout.EAST);
		
	}
}
