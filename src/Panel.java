import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
		addToMap(sprites, Component.GROUND, "Resources/ground.png");
		addToMap(sprites, Component.CRATE, "Resources/crate.png");
		addToMap(sprites, Component.WALL, "Resources/wall.png");
		addToMap(sprites, Component.PLAYER, "Resources/playerDown.png");
		addToMap(sprites, Component.GOAL, "Resources/goal.png");
		addToMap(sprites, Component.CRATE_ON_GOAL, "Resources/crateOnGoal.png");
		addToMap(sprites, Component.PLAYER_ON_GOAL, "Resources/castor.jpg");
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
		int midX = this.getWidth()/2;
		int midY = this.getHeight()/2;
		int x0 = midX - (grid.getWidth()*64)/2;
		int y0 = midY - (grid.getHeight()*64)/2;
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(10));
		g2.drawRect(x0-5, y0-5, (grid.getWidth()*64) + 10, (grid.getHeight()*64) + 10);
		for (j = 0; j < grid.getHeight(); j++) {
			for (i = 0; i < grid.getWidth(); i++) {
				if (grid.getComponentAt(i, j) != null)
					if (grid.getComponentAt(i, j) == Component.PLAYER) {
						switch (player.getDirection()) {
						case UP:
							addToMap(sprites, Component.PLAYER, "Resources/playerUp.png");
							break;
						case DOWN:
							addToMap(sprites, Component.PLAYER, "Resources/playerDown.png");
							break;
						case RIGHT:
							addToMap(sprites, Component.PLAYER, "Resources/playerRight.png");
							break;
						case LEFT:
							addToMap(sprites, Component.PLAYER, "Resources/playerLeft.png");
							break;
						}
					}
					
					g.drawImage(sprites.get(grid.getComponentAt(i, j)), x0 + i*64, y0 + j*64, null);
			}
		}
			
	}
}
