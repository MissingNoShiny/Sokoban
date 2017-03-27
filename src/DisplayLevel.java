
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;

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
	
	Map<String, Image> sprites = new HashMap<String, Image>();
	
	/**
	 * TODO
	 * @param grid The Grid object to get data from
	 */
	public DisplayLevel(Grid grid, Game game) {
		this.grid = grid;
		setFocusable(true);
		setLayout(null);
		Button backToMenuButton = new Button("Back to menu", Color.orange, 50);
		backToMenuButton.addMouseListener(new ButtonListener(backToMenuButton) {
			@Override
			public void mousePressed(MouseEvent e) {
				game.loadMenu();
				grid.saveGrid("level1_saved", game);
			}
		});
		add(backToMenuButton, BorderLayout.EAST);
		
		addToMap(sprites, "Ground", "../resources/ground.png");
		addToMap(sprites, "Crate", "../resources/crate.png");
		addToMap(sprites, "Wall", "../resources/wall.png");
		addToMap(sprites, "Goal", "../resources/goal.png");
		addToMap(sprites, "CrateOnGoal", "../resources/crateOnGoal.png");
		addToMap(sprites, "PlayerUP", "../resources/playerUp.png");
		addToMap(sprites, "PlayerRIGHT", "../resources/playerRight.png");
		addToMap(sprites, "PlayerDOWN", "../resources/playerDown.png");
		addToMap(sprites, "PlayerLEFT", "../resources/playerLeft.png");
	}
	
	public void addToMap(Map<String, Image> map, String comp, String nameResource) {
		Image sprite = null;
		try {
		    sprite = ImageIO.read(new File(nameResource));
		} catch (IOException e) {
		}
		map.put(comp, sprite);
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
			for (i = 0; i < grid.getWidth(); i++)
				g.drawImage(sprites.get(grid.getComponentAt(i, j).getSpriteName()), x0 + i*64, y0 + j*64, null);
		}
		g.drawImage(sprites.get(grid.player.getSpriteName()), x0 + grid.player.getX()*64, y0 + grid.player.getY()*64, null);
	}
}
