package be.ac.umons.info.sokoban;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * A class used to display a Grid.
 * @author Vincent Larcin, Joachim Sneessens
 */
public class GridDisplay extends JPanel implements KeyListener{
	

	private static final long serialVersionUID = -5700571976068104061L;
	
	private class ArrowButton extends JButton {

		private static final long serialVersionUID = 4616710500625962373L;
		
		public ArrowButton(Direction dir, String resourcePath) {
			setFocusable(false);
			setBounds(0, 0, 0, 0);
			setBorderPainted(false);
			setFocusPainted(false);
			setContentAreaFilled(false);
			addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					grid.getPlayer().setDirection(dir);
					grid.getPlayer().move(dir, true);
				}
			});
			setIcon(new ImageIcon(resourcePath));
		}
	}
	/**
	 * The map that contains the sprite associated to each Component.
	 */
	private Map<String, Image> sprites = new HashMap<String, Image>();
	
	/**
	 * The grid to display.
	 */
	private Grid grid;
	
	/**
	 * The size of each cell of the grid, in pixels.
	 */
	private int cellSize = 64;
	
	/**
	 * The thickness of the border around the level, in pixels.
	 */
	private int borderThickness = 5;
	
	/**
	 * The X-coordinate of the upper left corner of the grid.
	 */
	private int x0;
	
	/**
	 * The Y-coordinate of the upper left corner of the grid.
	 */
	private int y0;
	
	/**
	 * The arrow buttons that allows the user to move the player using a mouse or touch screen.
	 */
	ArrowButton buttonLeft, buttonRight, buttonUp, buttonDown;
	
	private LevelDisplay displayLevel;
	
	public GridDisplay (Grid grid, LevelDisplay displayLevel) {
		addKeyListener(this);
		setFocusable(true);
		setOpaque(true);
		setLayout(null);
		this.grid = grid;
		this.displayLevel = displayLevel;

		add(new ArrowButton(Direction.UP, "resources/arrowUp.png"));
		
		add(new ArrowButton(Direction.RIGHT, "resources/arrowRight.png"));
		
		add(new ArrowButton(Direction.DOWN, "resources/arrowDown.png"));
		
		add(new ArrowButton(Direction.LEFT, "resources/arrowLeft.png"));
		
		updateMap();
	}
	
	public void paintComponent(Graphics g) {	
		
		cellSize = 64;
		while (grid.getWidth()*cellSize + 2*borderThickness > getWidth() || grid.getHeight()*cellSize + 2*borderThickness > getHeight())
			cellSize --;
		x0 = getWidth()/2 - (grid.getWidth()*cellSize)/2;
		y0 = getHeight()/2 - (grid.getHeight()*cellSize)/2;
		
		super.paintComponent(g);
		setBackground(Options.getBackgroundColor());
		
		Graphics2D g2d = (Graphics2D) g;
		g2d.setStroke(new BasicStroke(borderThickness));
		g2d.setColor(Color.DARK_GRAY);
		
		for (int i = 0; i < grid.getWidth(); i++){
			for (int j = 0; j < grid.getHeight(); j++) {
				g.drawImage(sprites.get(grid.getComponentAt(i, j).getName()), x0 + i*cellSize, y0 + j*cellSize, cellSize, cellSize, null);

				if (borderThickness > 0)
					drawBorder(i, j, g2d);
			}
		}
		g.drawImage(sprites.get(grid.getPlayer().getName()), x0 + grid.getPlayer().getX()*cellSize, y0 + grid.getPlayer().getY()*cellSize, cellSize, cellSize, null);
		
		if (grid.getTracker().hasMoved()) {
			if (Options.arePlayerArrowsShown()) 
				updateButtons();
			if (grid.isWon())
				displayLevel.displayVictoryScreen();
		}
	}
	
	/**
	 * Adds a sprite for specified component in the sprite map. The specified sprite must exist in the resources directory.
	 * @param componentName The name of the component to add a sprite for
	 * @param resourceName The name of the file that contains the sprite
	 */
	private void addToMap(String componentName, String resourceName) {
		Image sprite = null;
		try {
		    sprite = ImageIO.read(new File(resourceName));
		} catch (IOException e) {
		}
		sprites.put(componentName, sprite);
	}
	
	public void updateMap() {
		String path = "resources/" + Options.getTextureDir() + "/";
		addToMap("Ground", path + "ground.png");
		addToMap("Crate", path + "crate.png");
		addToMap("Wall", path + "wall.png");
		addToMap("Goal", path + "goal.png");
		addToMap("CrateOnGoal", path + "crateOnGoal.png");
		addToMap("PlayerUP", path + "playerUp.png");
		addToMap("PlayerRIGHT", path + "playerRight.png");
		addToMap("PlayerDOWN", path + "playerDown.png");
		addToMap("PlayerLEFT", path + "playerLeft.png");
	}
	
	@Override
	public void keyPressed(KeyEvent e) { 
		int input = e.getKeyCode();
		switch (input){
		case KeyEvent.VK_R:
			grid.getTracker().reset();
			grid.setPlayerCoordinates(grid.getHeight(), grid.getWidth());
			break;
		case KeyEvent.VK_ENTER:
			System.out.println(grid.getPlayer().getX() + ", " + grid.getPlayer().getY());
			System.out.println(grid.getPlayer().getDirection());
			break;
		case KeyEvent.VK_UP:
			grid.getPlayer().setDirection(Direction.UP);
			if (grid.getPlayer().canMove())
				grid.getPlayer().move();
			break;
		case KeyEvent.VK_DOWN :
			grid.getPlayer().setDirection(Direction.DOWN);
			if (grid.getPlayer().canMove())
				grid.getPlayer().move();
			break;
		case KeyEvent.VK_RIGHT:
			grid.getPlayer().setDirection(Direction.RIGHT);
			if (grid.getPlayer().canMove())
				grid.getPlayer().move();
			break;
		case KeyEvent.VK_LEFT:
			grid.getPlayer().setDirection(Direction.LEFT);
			if (grid.getPlayer().canMove())
				grid.getPlayer().move();
			break;
		case KeyEvent.VK_G:
			Options.setButtonColor(Color.green);
			break;
		case KeyEvent.VK_O:
			Options.setButtonColor(Color.orange);
			break;
		case KeyEvent.VK_1:
			Options.setTextureDir("classic");
			updateMap();
			break;
		case KeyEvent.VK_2:
			Options.setTextureDir("test");
			updateMap();
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}
	
	/**
	 * Draws a border next to the cell at specified coordinates if that cell is at the border of the playing area.
	 * Draws a square in the corner of a cell if it contains a blank Component next to a cell at the border of the playing area to allow border continuity.
	 * @param x The X-coordinate of the cell
	 * @param y The Y-coordinate of the cell
	 * @param g2d The Graphics2D object to draw the border with
	 */
	public void drawBorder(int x, int y, Graphics2D g2d) {
		if (!grid.getComponentAt(x, y).getName().equals("Blank")) {
			if (x == 0)
				g2d.drawLine(x0 + x*cellSize - borderThickness/2, y0 + y*cellSize - borderThickness/2, x0 + x*cellSize - borderThickness/2, y0 + (y+1)*cellSize + borderThickness/2);
			if (x == grid.getWidth() - 1)
				g2d.drawLine(x0 + (x+1)*cellSize + borderThickness/2, y0 + y*cellSize - borderThickness/2, x0 + (x+1)*cellSize + borderThickness/2, y0 + (y+1)*cellSize + borderThickness/2);
			if (y == 0)
				g2d.drawLine(x0 + x*cellSize - borderThickness/2, y0 + y*cellSize - borderThickness/2, x0 + (x+1)*cellSize + borderThickness/2, y0 + y*cellSize - borderThickness/2);
			if (y == grid.getHeight() - 1)
				g2d.drawLine(x0 + x*cellSize - borderThickness/2, y0 + (y+1)*cellSize + borderThickness/2, x0 + (x+1)*cellSize + borderThickness/2, y0 + (y+1)*cellSize + borderThickness/2);
		} else {
			if (x > 0 && !grid.getComponentAt(x - 1, y).getName().equals("Blank")) {
				g2d.drawLine(x0 + x*cellSize + borderThickness/2, y0 + y*cellSize + borderThickness/2, x0 + x*cellSize + borderThickness/2, y0 + (y+1)*cellSize - borderThickness/2);
				if (y > 0 && grid.getComponentAt(x, y - 1).getName().equals("Blank"))
					g2d.drawLine(x0 + x*cellSize + borderThickness/2, y0 + y*cellSize - borderThickness/2, x0 + x*cellSize + borderThickness/2, y0 + y*cellSize - borderThickness/2);
			}
			if (x + 1 < grid.getWidth() && !grid.getComponentAt(x + 1, y).getName().equals("Blank")) {
				g2d.drawLine(x0 + (x+1)*cellSize - borderThickness/2, y0 + y*cellSize + borderThickness/2, x0 + (x+1)*cellSize - borderThickness/2, y0 + (y+1)*cellSize - borderThickness/2);
				if (y + 1 < grid.getHeight() && grid.getComponentAt(x, y + 1).getName().equals("Blank"))
					g2d.drawLine(x0 + (x+1)*cellSize - borderThickness/2, y0 + (y+1)*cellSize + borderThickness/2, x0 + (x+1)*cellSize - borderThickness/2, y0 + (y+1)*cellSize + borderThickness/2);
			}
			if (y > 0 && !grid.getComponentAt(x, y - 1).getName().equals("Blank")) {
				g2d.drawLine(x0 + x*cellSize + borderThickness/2, y0 + y*cellSize + borderThickness/2, x0 + (x+1)*cellSize - borderThickness/2, y0 + y*cellSize + borderThickness/2);
				if (x + 1 < grid.getWidth() && grid.getComponentAt(x + 1, y).getName().equals("Blank"))
					g2d.drawLine(x0 + (x+1)*cellSize + borderThickness/2, y0 + y*cellSize + borderThickness/2, x0 + (x+1)*cellSize + borderThickness/2, y0 + y*cellSize + borderThickness/2);
			}
			if (y + 1 < grid.getHeight() && !grid.getComponentAt(x, y + 1).getName().equals("Blank")) {
				g2d.drawLine(x0 + x*cellSize + borderThickness/2, y0 + (y+1)*cellSize - borderThickness/2, x0 + (x+1)*cellSize - borderThickness/2, y0 + (y+1)*cellSize - borderThickness/2);
				if (x > 0 && grid.getComponentAt(x - 1, y).getName().equals("Blank"))
					g2d.drawLine(x0 + x*cellSize - borderThickness/2, y0 + (y+1)*cellSize - borderThickness/2, x0 + x*cellSize - borderThickness/2, y0 + (y+1)*cellSize - borderThickness/2);
			}
		}
	}
	
	/**
	 * Updates the position of the arrow buttons around the player.
	 */
	private void updateButtons() {
		buttonLeft.setBounds(x0 + (grid.getPlayer().getX()-1)*cellSize, y0 + grid.getPlayer().getY()*cellSize, cellSize, cellSize);
		buttonRight.setBounds(x0 + (grid.getPlayer().getX()+1)*cellSize, y0 + grid.getPlayer().getY()*cellSize, cellSize, cellSize);
		buttonUp.setBounds(x0 + grid.getPlayer().getX()*cellSize, y0 + (grid.getPlayer().getY()-1)*cellSize, cellSize, cellSize);
		buttonDown.setBounds(x0 + grid.getPlayer().getX()*cellSize, y0 + (grid.getPlayer().getY()+1)*cellSize, cellSize, cellSize);
		
		buttonLeft.setVisible(grid.getPlayer().canMove(Direction.LEFT));
		buttonRight.setVisible(grid.getPlayer().canMove(Direction.RIGHT));
		buttonUp.setVisible(grid.getPlayer().canMove(Direction.UP));
		buttonDown.setVisible(grid.getPlayer().canMove(Direction.DOWN));
	}
}
