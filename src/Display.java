
import java.awt.event.KeyListener;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;

public class Display extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6826804177182624245L;
	
	public static final Color BLEU_CLAIR = new Color(135, 206, 250);

	public static void main(String[] args) {
		
		Grid grid = new Grid(17, 10);		
		
		grid.fill(Component.GROUND);
		
		grid.placeComponentAt(6, 5, Component.GOAL);
		grid.placeComponentAt(2, 4, Component.GOAL);
		
		grid.placeComponentAt(1, 3, Component.WALL);
		grid.placeComponentAt(1, 4, Component.WALL);
		grid.placeComponentAt(1, 5, Component.WALL);
		grid.placeComponentAt(2, 5, Component.WALL);
		grid.placeComponentAt(5, 5, Component.WALL);
		
		grid.setNumberCrates(2);;
		grid.addCrate(5, 8);
		grid.addCrate(6, 7);
		
		Player player = new Player(5, 4, grid);
		
		/*
		Grid grid = Grid.readGrid("levels\\level1.txt");
		
		Player player = new Player(4, 4, grid);
		*/
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Display window = new Display(screenSize);
		Panel p = new Panel(grid, player);
		window.setContentPane(p);
		p.setBackground(BLEU_CLAIR);
		window.paintPanel(p);
			
		window.addKeyListener(new KeyListener() {
			
			@Override
			public void keyPressed(KeyEvent e) { 
				int input = e.getKeyCode();
				switch (input){
				case KeyEvent.VK_ENTER:
					System.out.print("Coordonnées du joueur: ");
					System.out.println(player.getX() + ", " + player.getY());
					System.out.println(player.getDirection());
					break;
				case KeyEvent.VK_UP:
					player.setDirection(Direction.UP);
					player.move(grid);
					break;
				case KeyEvent.VK_DOWN :
					player.setDirection(Direction.DOWN);
					player.move(grid);
					break;
				case KeyEvent.VK_RIGHT:
					player.setDirection(Direction.RIGHT);
					player.move(grid);
					break;
				case KeyEvent.VK_LEFT:
					player.setDirection(Direction.LEFT);
					player.move(grid);
					break;
				case KeyEvent.VK_0:
					grid.placeComponentAt(4,0, Component.GOAL);
				default :
					System.out.println("On a appuyé, \nComposant en (6,5) :" + grid.getComponentAt(6, 5));
				}
				if (grid.isWon())
					System.out.println("Vive les castors");
				window.paintPanel(p);
			}
			    
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {	
			}			
		}
		);
	}
	
	public Display(Dimension dimension) {
		super("Sokoban");
		setSize((int) dimension.getWidth(), (int) dimension.getHeight());
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);
	}
	
	/**
	 * Updates a panel.
	 * @param p The panel to update
	 */
	public void paintPanel(Panel p) {
		p.revalidate();
		p.repaint();
	}
	
}
