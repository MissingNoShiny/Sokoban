
import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class DisplayLevel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4992322428530946741L;

	/**
	 * A Grid object containing a matrix containing the data of a level.
	 */
	private Grid grid;
	
	Map<Component, Image> sprites = new HashMap<Component, Image>();
	
	//Solution temporaire pour que le sprite du player se dessine au dessus du sprite precedent
	Map<Direction, Image> playerSprites = new HashMap<Direction, Image>();
	
	/**
	 * TODO
	 * @param grid The Grid object to get data from
	 */
	public DisplayLevel(Grid grid) {
		this.grid = grid;
		addToMap(sprites, Component.GROUND, "../resources/ground.png");
		addToMap(sprites, Component.CRATE, "../resources/crate.png");
		addToMap(sprites, Component.WALL, "../resources/wall.png");
		addToMap(sprites, Component.GOAL, "../resources/goal.png");
		addToMap(sprites, Component.CRATE_ON_GOAL, "../resources/crateOnGoal.png");
		addToMap(playerSprites, Direction.UP, "../resources/playerUp.png");
		addToMap(playerSprites, Direction.RIGHT, "../resources/playerRight.png");
		addToMap(playerSprites, Direction.DOWN, "../resources/playerDown.png");
		addToMap(playerSprites, Direction.LEFT, "../resources/playerLeft.png");
	}
	
	public void addToMap(Map<Component, Image> map, Component comp, String nameResource) {
		Image sprite = null;
		try {
		    sprite = ImageIO.read(new File(nameResource));
		} catch (IOException e) {
		}
		map.put(comp, sprite);
	}
	
	public void addToMap(Map<Direction, Image> map, Direction dir, String nameResource) {
		Image sprite = null;
		try {
		    sprite = ImageIO.read(new File(nameResource));
		} catch (IOException e) {
		}
		map.put(dir, sprite);
	}
	
	public void paintComponent(Graphics g) {
		int i, j;
		int midX = this.getWidth()/2;
		int midY = this.getHeight()/2;
		int x0 = midX - (grid.getWidth()*64)/2;
		int y0 = midY - (grid.getHeight()*64)/2;
		
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setStroke(new BasicStroke(20));
		g2.drawRect(x0 - 10, y0 - 10, (grid.getWidth()*64) + 20, (grid.getHeight()*64) + 20);
		setBackground(Game.BLEU_CLAIR);
		
		for (j = 0; j < grid.getHeight(); j++) {
			for (i = 0; i < grid.getWidth(); i++) {
				g.drawImage(sprites.get(grid.getComponentAt(i, j)), x0 + i*64, y0 + j*64, null);
				if (grid.player.getX() == i && grid.player.getY() == j) 
					g.drawImage(playerSprites.get(grid.player.getDirection()), x0 + i*64, y0 + j*64, null);
			}
		}
		
		//Creer bouton bact to menu

	}
}
