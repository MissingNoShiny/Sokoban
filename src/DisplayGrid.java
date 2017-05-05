
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


public class DisplayGrid extends JPanel implements KeyListener{
	

	private static final long serialVersionUID = -5700571976068104061L;

	private Map<String, Image> sprites = new HashMap<String, Image>();
	
	private Grid grid;
	
	private int cellSize = 64;
	
	private int borderThickness = 5;
	
	JButton buttonLeft, buttonRight, buttonUp, buttonDown;
	
	public DisplayGrid (Grid grid) {
		addKeyListener(this);
		setFocusable(true);
		setOpaque(true);
		setLayout(null);
		this.grid = grid;

		buttonLeft = new JButton();
		add(buttonLeft);
		initializeButton(buttonLeft, Direction.LEFT);
		buttonLeft.setIcon(new ImageIcon("../resources/arrowLeft.png"));
		
		buttonRight = new JButton();
		add(buttonRight);
		initializeButton(buttonRight, Direction.RIGHT);
		buttonRight.setIcon(new ImageIcon("../resources/arrowRight.png"));
		
		buttonUp = new JButton();
		add(buttonUp);
		initializeButton(buttonUp, Direction.UP);
		buttonUp.setIcon(new ImageIcon("../resources/arrowUp.png"));
		
		buttonDown = new JButton();
		add(buttonDown);
		initializeButton(buttonDown, Direction.DOWN);
		buttonDown.setIcon(new ImageIcon("../resources/arrowDown.png"));
		
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
		
		cellSize = 64;
		while (grid.getWidth()*cellSize + 2*borderThickness > getWidth() || grid.getHeight()*cellSize + 2*borderThickness > getHeight())
			cellSize --;
		int x0 = getWidth()/2 - (grid.getWidth()*cellSize)/2;
		int y0 = getHeight()/2 - (grid.getHeight()*cellSize)/2;
		
		super.paintComponent(g);
		setBackground(Options.backGroundColor);
		
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
		
		if (Options.SHOW_PLAYER_ARROWS && grid.getTracker().hasMoved()) {
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
			grid.getTracker().reset();
			grid.setPlayer(grid.getHeight(), grid.getWidth());
			break;
		case KeyEvent.VK_ENTER:
			System.out.print("Coordonnées du joueur: ");
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
		default :
			System.out.println("On a appuyé, \nComposant en (6,5) : " + grid.getComponentAt(6, 5).getName());
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
	
	public void initializeButton(JButton button, Direction direction) {
		button.setFocusable(false);
		button.setBounds(0, 0, 0, 0);
		button.setBorderPainted(false);
		button.setFocusPainted(false);
		button.setContentAreaFilled(false);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				grid.getPlayer().setDirection(direction);
				grid.getPlayer().move(direction, true);
			}
		});
	}
}
