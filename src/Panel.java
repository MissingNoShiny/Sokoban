import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
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
	
	Map<Component, Image> sprites = new HashMap<Component, Image>();
	
	/**
	 * TODO
	 * @param grid The Grid object to get data from
	 */
	public Panel(Grid grid, Player player) {
		this.grid = grid;
		this.player = player;
		this.addToMap(sprites, Component.GROUND, "Resources/ground.png");
		this.addToMap(sprites, Component.CRATE, "Resources/crate.png");
		this.addToMap(sprites, Component.WALL, "Resources/wall.jpg");
		this.addToMap(sprites, Component.PLAYER, "Resources/banana.gif"); //il faudra changer cette maniere d'ajouter des elements au dico
	}
	

	public void addToMap(Map<Component, Image> map, Component comp, String nameResource) {
		Image sprite = null;
		try {
		    sprite = ImageIO.read(new File(nameResource));
		} catch (IOException e) {
		}
		map.put(comp, sprite);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (j = 0; j < grid.getHeight(); j++) {
			for (i = 0; i < grid.getWidth(); i++) {
				if (grid.getComponentAt(i, j) != null)
					g.drawImage(sprites.get(grid.getComponentAt(i, j)), i*32, j*32, null);
			}
		}
			
	}
}
