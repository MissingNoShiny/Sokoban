package be.ac.umons.info.sokoban.gui;

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

import be.ac.umons.info.sokoban.grid.Direction;
import be.ac.umons.info.sokoban.grid.Grid;

/**
 * A class used to display a Grid.
 * @author Vincent Larcin, Joachim Sneessens
 */
public class GridDisplay extends JPanel {
	

	private static final long serialVersionUID = -5700571976068104061L;
	
	private class ArrowButton extends JButton {

		private static final long serialVersionUID = 4616710500625962373L;
		
		public ArrowButton(final Direction dir, String resourcePath) {
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
			setIcon(new ImageIcon("resources/default/" + resourcePath));
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
	private int cellSize;
	
	/**
	 * The thickness of the border around the level, in pixels.
	 */
	private int borderThickness;
	
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
	ArrowButton buttonUp, buttonRight, buttonDown, buttonLeft;
	
	public GridDisplay (Grid gridInput) {
		grid = gridInput;
		
		addKeyListener(new KeyListener() {
			
			@Override
			public void keyPressed(KeyEvent e) { 
				int input = e.getKeyCode();
				switch (input){
				case KeyEvent.VK_R:
					grid.getTracker().reset();
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
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				
			}

			@Override
			public void keyTyped(KeyEvent e) {
				
			}
		});
		
		setFocusable(true);
		setOpaque(true);
		setLayout(null);

		buttonUp = new ArrowButton(Direction.UP, "arrowUp.png");
		add(buttonUp);

		buttonRight = new ArrowButton(Direction.RIGHT, "arrowRight.png");
		add(buttonRight);
		
		buttonDown = new ArrowButton(Direction.DOWN, "arrowDown.png");
		add(buttonDown);
		
		buttonLeft = new ArrowButton(Direction.LEFT, "arrowLeft.png");
		add(buttonLeft);
		
		updateMap();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		setBackground(Options.getBackgroundColor());
		
		cellSize = 64;
		while (grid.getWidth()*cellSize + 2*borderThickness > getWidth() || grid.getHeight()*cellSize + 2*borderThickness > getHeight())
			cellSize --;
		if (Options.getBorderThickness() > cellSize)
			borderThickness = cellSize;
		else
			borderThickness = Options.getBorderThickness();
		x0 = getWidth()/2 - (grid.getWidth()*cellSize)/2;
		y0 = getHeight()/2 - (grid.getHeight()*cellSize)/2;
		updateArrowButtonsIconSize();

			
		Graphics2D g2d = (Graphics2D) g;
		g2d.setStroke(new BasicStroke(borderThickness));
		g2d.setColor(Color.DARK_GRAY);
		
		for (int i = 0; i < grid.getWidth(); i++){
			for (int j = 0; j < grid.getHeight(); j++) {
				g.drawImage(sprites.get(grid.getComponentTypeAt(i, j)), x0 + i*cellSize, y0 + j*cellSize, cellSize, cellSize, null);

				if (borderThickness > 0)
					drawBorder(i, j, g2d);
			}
		}
		g.drawImage(sprites.get(grid.getPlayer().getName()), x0 + grid.getPlayer().getX()*cellSize, y0 + grid.getPlayer().getY()*cellSize, cellSize, cellSize, null);
		
		if (grid.getTracker().hasMoved()) {
			updateArrowButtonsLocation();
			updateArrowButtonsVisibility();
			if (grid.isWon())
				((LevelDisplay) getParent()).displayVictoryScreen();
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
			if (new File("resources/" + Options.getTextureDir() + "/" + resourceName).exists())
				sprite = ImageIO.read(new File("resources/" + Options.getTextureDir() + "/" + resourceName));
			else
				sprite = ImageIO.read(new File("resources/default/" + resourceName));
		} catch (IOException e) {
		}
		sprites.put(componentName, sprite);
	}
	
	/**
	 * Reloads all the textures.
	 */
	public void updateMap() {
		addToMap("Ground", "ground.png");
		addToMap("Crate", "crate.png");
		addToMap("Wall", "wall.png");
		addToMap("Goal", "goal.png");
		addToMap("CrateOnGoal", "crateOnGoal.png");
		addToMap("PlayerUP", "playerUp.png");
		addToMap("PlayerRIGHT", "playerRight.png");
		addToMap("PlayerDOWN", "playerDown.png");
		addToMap("PlayerLEFT", "playerLeft.png");
	}
		
	/**
	 * Draws a border next to the cell at specified coordinates if that cell is at the border of the playing area.
	 * Draws a square in the corner of a cell if it contains a blank Component next to a cell at the border of the playing area to allow border continuity.
	 * @param x The X-coordinate of the cell
	 * @param y The Y-coordinate of the cell
	 * @param g2d The Graphics2D object to draw the border with
	 */
	public void drawBorder(int x, int y, Graphics2D g2d) {
		if (!grid.getComponentTypeAt(x, y).equals("Blank")) {
			if (x == 0)
				g2d.drawLine(x0 + x*cellSize - borderThickness/2, y0 + y*cellSize - borderThickness/2, x0 + x*cellSize - borderThickness/2, y0 + (y+1)*cellSize + borderThickness/2);
			if (x == grid.getWidth() - 1)
				g2d.drawLine(x0 + (x+1)*cellSize + borderThickness/2, y0 + y*cellSize - borderThickness/2, x0 + (x+1)*cellSize + borderThickness/2, y0 + (y+1)*cellSize + borderThickness/2);
			if (y == 0)
				g2d.drawLine(x0 + x*cellSize - borderThickness/2, y0 + y*cellSize - borderThickness/2, x0 + (x+1)*cellSize + borderThickness/2, y0 + y*cellSize - borderThickness/2);
			if (y == grid.getHeight() - 1)
				g2d.drawLine(x0 + x*cellSize - borderThickness/2, y0 + (y+1)*cellSize + borderThickness/2, x0 + (x+1)*cellSize + borderThickness/2, y0 + (y+1)*cellSize + borderThickness/2);
		} else {
			if (x > 0 && !grid.getComponentTypeAt(x - 1, y).equals("Blank")) {
				g2d.drawLine(x0 + x*cellSize + borderThickness/2, y0 + y*cellSize + borderThickness/2, x0 + x*cellSize + borderThickness/2, y0 + (y+1)*cellSize - borderThickness/2);
				if (y > 0 && grid.getComponentTypeAt(x, y - 1).equals("Blank"))
					g2d.drawLine(x0 + x*cellSize + borderThickness/2, y0 + y*cellSize - borderThickness/2, x0 + x*cellSize + borderThickness/2, y0 + y*cellSize - borderThickness/2);
			}
			if (x + 1 < grid.getWidth() && !grid.getComponentTypeAt(x + 1, y).equals("Blank")) {
				g2d.drawLine(x0 + (x+1)*cellSize - borderThickness/2, y0 + y*cellSize + borderThickness/2, x0 + (x+1)*cellSize - borderThickness/2, y0 + (y+1)*cellSize - borderThickness/2);
				if (y + 1 < grid.getHeight() && grid.getComponentTypeAt(x, y + 1).equals("Blank"))
					g2d.drawLine(x0 + (x+1)*cellSize - borderThickness/2, y0 + (y+1)*cellSize + borderThickness/2, x0 + (x+1)*cellSize - borderThickness/2, y0 + (y+1)*cellSize + borderThickness/2);
			}
			if (y > 0 && !grid.getComponentTypeAt(x, y - 1).equals("Blank")) {
				g2d.drawLine(x0 + x*cellSize + borderThickness/2, y0 + y*cellSize + borderThickness/2, x0 + (x+1)*cellSize - borderThickness/2, y0 + y*cellSize + borderThickness/2);
				if (x + 1 < grid.getWidth() && grid.getComponentTypeAt(x + 1, y).equals("Blank"))
					g2d.drawLine(x0 + (x+1)*cellSize + borderThickness/2, y0 + y*cellSize + borderThickness/2, x0 + (x+1)*cellSize + borderThickness/2, y0 + y*cellSize + borderThickness/2);
			}
			if (y + 1 < grid.getHeight() && !grid.getComponentTypeAt(x, y + 1).equals("Blank")) {
				g2d.drawLine(x0 + x*cellSize + borderThickness/2, y0 + (y+1)*cellSize - borderThickness/2, x0 + (x+1)*cellSize - borderThickness/2, y0 + (y+1)*cellSize - borderThickness/2);
				if (x > 0 && grid.getComponentTypeAt(x - 1, y).equals("Blank"))
					g2d.drawLine(x0 + x*cellSize - borderThickness/2, y0 + (y+1)*cellSize - borderThickness/2, x0 + x*cellSize - borderThickness/2, y0 + (y+1)*cellSize - borderThickness/2);
			}
		}
	}
	
	/**
	 * Updates the location of the arrow buttons.
	 */
	private void updateArrowButtonsLocation() {
		buttonLeft.setBounds(x0 + (grid.getPlayer().getX()-1)*cellSize, y0 + grid.getPlayer().getY()*cellSize, cellSize, cellSize);
		buttonRight.setBounds(x0 + (grid.getPlayer().getX()+1)*cellSize, y0 + grid.getPlayer().getY()*cellSize, cellSize, cellSize);
		buttonUp.setBounds(x0 + grid.getPlayer().getX()*cellSize, y0 + (grid.getPlayer().getY()-1)*cellSize, cellSize, cellSize);
		buttonDown.setBounds(x0 + grid.getPlayer().getX()*cellSize, y0 + (grid.getPlayer().getY()+1)*cellSize, cellSize, cellSize);
	}
	
	/**
	 * Updates the visibility of the arrow buttons.
	 */
	private void updateArrowButtonsVisibility() {
		buttonLeft.setVisible(Options.arePlayerArrowsShown() && grid.getPlayer().canMove(Direction.LEFT));
		buttonRight.setVisible(Options.arePlayerArrowsShown() && grid.getPlayer().canMove(Direction.RIGHT));
		buttonUp.setVisible(Options.arePlayerArrowsShown() && grid.getPlayer().canMove(Direction.UP));
		buttonDown.setVisible(Options.arePlayerArrowsShown() && grid.getPlayer().canMove(Direction.DOWN));
	}
	
	/**
	 * Adapts the icon of the arrow buttons to the cell size.
	 */
	private void updateArrowButtonsIconSize() {
		buttonUp.setIcon(new ImageIcon((((ImageIcon) buttonUp.getIcon()).getImage().getScaledInstance(cellSize, cellSize, Image.SCALE_SMOOTH))));
		buttonRight.setIcon(new ImageIcon((((ImageIcon) buttonRight.getIcon()).getImage().getScaledInstance(cellSize, cellSize, Image.SCALE_SMOOTH))));
		buttonDown.setIcon(new ImageIcon((((ImageIcon) buttonDown.getIcon()).getImage().getScaledInstance(cellSize, cellSize, Image.SCALE_SMOOTH))));
		buttonLeft.setIcon(new ImageIcon((((ImageIcon) buttonLeft.getIcon()).getImage().getScaledInstance(cellSize, cellSize, Image.SCALE_SMOOTH))));
	}
}
