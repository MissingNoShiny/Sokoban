
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.JPanel;


public class DisplayGrid extends JPanel implements KeyListener{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -5700571976068104061L;

	private Map<String, Image> sprites = new HashMap<String, Image>();
	
	private Grid grid;
	
	private int cellSize = 64;
	
	private int borderThickness = 5;
	
	public DisplayGrid (Grid grid) {
		addKeyListener(this);
		setFocusable(true);
		setOpaque(true);
		this.grid = grid;
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
	
	public void paintComponent(Graphics g) {

		while (grid.getWidth()*cellSize + 4*borderThickness > this.getWidth() || grid.getHeight()*cellSize + 4*borderThickness > this.getHeight())
			cellSize --;
		int midX = this.getWidth()/2;
		int midY = this.getHeight()/2;
		int x0 = midX - (grid.getWidth()*cellSize)/2;
		int y0 = midY - (grid.getHeight()*cellSize)/2;
		
		super.paintComponent(g); //J'ai compris a quoi servait cette ligne (sans elle le setBackground ne fonctionne pas), mais pas reelelement ce qu'elle faisait
		setBackground(Game.BLEU_CLAIR);
		
		Graphics2D g2d = (Graphics2D) g;
		g2d.setStroke(new BasicStroke(borderThickness));
		g2d.setColor(Color.DARK_GRAY);
		
		for (int i = 0; i < grid.getWidth(); i++){
			for (int j = 0; j < grid.getHeight(); j++) {
				if (!grid.getComponentAt(i, j).getName().equals("Blank")) {
					g.drawImage(sprites.get(grid.getComponentAt(i, j).getName()), x0 + i*cellSize, y0 + j*cellSize, cellSize, cellSize, null);

					if (i == 0)
						g2d.drawLine(x0 + i*cellSize - borderThickness/2, y0 + j*cellSize - borderThickness/2, x0 + i*cellSize - borderThickness/2, y0 + (j+1)*cellSize + borderThickness/2);
					if (i == grid.getWidth() - 1)
						g2d.drawLine(x0 + (i+1)*cellSize + borderThickness/2, y0 + j*cellSize - borderThickness/2, x0 + (i+1)*cellSize + borderThickness/2, y0 + (j+1)*cellSize + borderThickness/2);
					if (j == 0)
						g2d.drawLine(x0 + i*cellSize - borderThickness/2, y0 + j*cellSize - borderThickness/2, x0 + (i+1)*cellSize + borderThickness/2, y0 + j*cellSize - borderThickness/2);
					if (j == grid.getHeight() - 1)
						g2d.drawLine(x0 + i*cellSize - borderThickness/2, y0 + (j+1)*cellSize + borderThickness/2, x0 + (i+1)*cellSize + borderThickness/2, y0 + (j+1)*cellSize + borderThickness/2);
				} else {
					if (i > 0 && !grid.getComponentAt(i - 1, j).getName().equals("Blank")) {
						g2d.drawLine(x0 + i*cellSize + borderThickness/2, y0 + j*cellSize + borderThickness/2, x0 + i*cellSize + borderThickness/2, y0 + (j+1)*cellSize - borderThickness/2);
						if (j > 0 && grid.getComponentAt(i, j - 1).getName().equals("Blank"))
							g2d.drawLine(x0 + i*cellSize + borderThickness/2, y0 + j*cellSize - borderThickness/2, x0 + i*cellSize + borderThickness/2, y0 + j*cellSize - borderThickness/2);
					}
					if (i + 1 < grid.getWidth() && !grid.getComponentAt(i + 1, j).getName().equals("Blank")) {
						g2d.drawLine(x0 + (i+1)*cellSize - borderThickness/2, y0 + j*cellSize + borderThickness/2, x0 + (i+1)*cellSize - borderThickness/2, y0 + (j+1)*cellSize - borderThickness/2);
						if (j + 1 < grid.getHeight() && grid.getComponentAt(i, j + 1).getName().equals("Blank"))
							g2d.drawLine(x0 + (i+1)*cellSize - borderThickness/2, y0 + (j+1)*cellSize + borderThickness/2, x0 + (i+1)*cellSize - borderThickness/2, y0 + (j+1)*cellSize + borderThickness/2);
					}
					if (j > 0 && !grid.getComponentAt(i, j - 1).getName().equals("Blank")) {
						g2d.drawLine(x0 + i*cellSize + borderThickness/2, y0 + j*cellSize + borderThickness/2, x0 + (i+1)*cellSize - borderThickness/2, y0 + j*cellSize + borderThickness/2);
						if (i + 1 < grid.getWidth() && grid.getComponentAt(i + 1, j).getName().equals("Blank"))
							g2d.drawLine(x0 + (i+1)*cellSize + borderThickness/2, y0 + j*cellSize + borderThickness/2, x0 + (i+1)*cellSize + borderThickness/2, y0 + j*cellSize + borderThickness/2);
					}
					if (j + 1 < grid.getHeight() && !grid.getComponentAt(i, j + 1).getName().equals("Blank")) {
						g2d.drawLine(x0 + i*cellSize + borderThickness/2, y0 + (j+1)*cellSize - borderThickness/2, x0 + (i+1)*cellSize - borderThickness/2, y0 + (j+1)*cellSize - borderThickness/2);
						if (i > 0 && grid.getComponentAt(i - 1, j).getName().equals("Blank"))
							g2d.drawLine(x0 + i*cellSize - borderThickness/2, y0 + (j+1)*cellSize - borderThickness/2, x0 + i*cellSize - borderThickness/2, y0 + (j+1)*cellSize - borderThickness/2);
					}
				}
			}
		}
		g.drawImage(sprites.get(grid.getPlayer().getName()), x0 + grid.getPlayer().getX()*cellSize, y0 + grid.getPlayer().getY()*cellSize, cellSize, cellSize, null);
	}
	
	public void addToMap(Map<String, Image> map, String comp, String nameResource) {
		Image sprite = null;
		try {
		    sprite = ImageIO.read(new File(nameResource));
		} catch (IOException e) {
		}
		map.put(comp, sprite);
	
	}
	
	@Override
	public void keyPressed(KeyEvent e) { 
		int input = e.getKeyCode();
		switch (input){
		case KeyEvent.VK_R:
			grid.getTracker().reset(grid);
			break;
		case KeyEvent.VK_ENTER:
			System.out.print("Coordonn�es du joueur: ");
			System.out.println(grid.getPlayer().getX() + ", " + grid.getPlayer().getY());
			System.out.println(grid.getPlayer().getDirection());
			break;
		case KeyEvent.VK_UP:
			grid.getPlayer().setDirection(Direction.UP);
			if (grid.getPlayer().canMove(true))
				grid.getPlayer().move();
			break;
		case KeyEvent.VK_DOWN :
			grid.getPlayer().setDirection(Direction.DOWN);
			if (grid.getPlayer().canMove(true))
				grid.getPlayer().move();
			break;
		case KeyEvent.VK_RIGHT:
			grid.getPlayer().setDirection(Direction.RIGHT);
			if (grid.getPlayer().canMove(true))
				grid.getPlayer().move();
			break;
		case KeyEvent.VK_LEFT:
			grid.getPlayer().setDirection(Direction.LEFT);
			if (grid.getPlayer().canMove(true))
				grid.getPlayer().move();
			break;
		default :
			System.out.println("On a appuy�, \nComposant en (6,5) : " + grid.getComponentAt(6, 5).getName());
		}
		


		if (grid.isWon())
			System.out.println("Vivent les castors!");
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}
}
