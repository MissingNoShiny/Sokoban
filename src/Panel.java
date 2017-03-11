import java.awt.Graphics;

import javax.swing.JPanel;

public class Panel extends JPanel {
	
	Grid grid;
	
	int i, j;
	
	public Panel(Grid grid) {
		this.grid = grid;
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
