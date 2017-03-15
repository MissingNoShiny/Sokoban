
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;

public class Display extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6826804177182624245L;

	public static void main(String[] args) {
		
		Grid grid = new Grid(10, 10);		
		
		grid.fill(Component.GROUND);
		
		grid.setNumberCrates(3);
		grid.addCrate(2, 3);
		grid.addCrate(5, 4);
		grid.addCrate(2, 2);
		
		Player player = new Player(0, 0, grid);
		grid.placeOnGrid(player.getY(), player.getY(), Component.PLAYER);
		
		grid.placeOnGrid(5,5,Component.WALL);
		grid.placeOnGrid(6,7,Component.WALL);
		grid.placeOnGrid(9,9,Component.WALL);
		
		Display window = new Display(32*grid.getWidth(), 32*grid.getHeight());
		Panel p = new Panel(grid, player);
		window.setContentPane(p);
		p.setPreferredSize(window.getSize());
		window.setLocationRelativeTo(null);
		window.pack();
		window.paintPanel(p);
		
		
		window.addKeyListener(new KeyListener() {
			
			@Override
			public void keyPressed(KeyEvent e) { 
				int input = e.getKeyCode();
				switch (input){
				case KeyEvent.VK_ENTER:
					System.out.print("Coordonnées du joueur: ");
					System.out.println(player.getX() + ", " + player.getY());
					break;
				case KeyEvent.VK_UP:
					System.out.println("UP");
					if (player.canMove(grid, Direction.UP))
						player.moveUp(grid);
					break;
				case KeyEvent.VK_DOWN :
					System.out.println("DOWN");
					if (player.canMove(grid, Direction.DOWN))
						player.moveDown(grid);
					break;
				case KeyEvent.VK_RIGHT:
					System.out.println("RIGHT");
					if (player.canMove(grid, Direction.RIGHT))
						player.moveRight(grid);
					break;
				case KeyEvent.VK_LEFT:
					System.out.println("LEFT");
					if (player.canMove(grid, Direction.LEFT))
						player.moveLeft(grid);
					break;
				case KeyEvent.VK_0:
					grid.placeOnGrid(4,0, Component.WALL);
				default :
					System.out.println("On a appuyé");
				}
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
	
	public Display(int width, int height) {
		super("Sokoban");
		setSize(width, height);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);
	}
	
	public void paintPanel(Panel p) {
		p.revalidate();
		p.repaint();
	}
	
}
