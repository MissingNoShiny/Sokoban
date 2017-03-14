import java.awt.Graphics;

import javax.swing.JPanel;

public class Panel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4992322428530946741L;

	/**
	 * A grid object containing a matrix containing the data of a level.
	 */
	Grid grid;
	
	Player player;
	
	int i;

	int j;
	
	/**
	 * TODO
	 * @param grid The Grid object to get data from
	 */
	public Panel(Grid grid, Player player) {
		this.grid = grid;
		this.player = player;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (j = 0; j < grid.getHeight(); j++) {
			for (i = 0; i < grid.getWidth(); i++) {
				if (grid.getPositionAt(i, j) != null)
					g.drawImage(grid.getPositionAt(i, j).getSprite(), i*32, j*32, null);
			}
		}
			
	}
}
