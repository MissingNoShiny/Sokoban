
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Display extends JFrame {
	
	public static void main(String[] args) {
		
		Grid grid = new Grid(5, 5);		
		
		BufferedImage img = null;
		try {
		    img = ImageIO.read(new File("resources/banana.gif"));
		} catch (IOException e) {
		}
		
		Player player = new Player(0, 0, img);
		grid.placeOnGrid(player.getX(), player.getY(), player);
		
		Display fenetre = new Display(32*5, 32*5);
		Panel p = new Panel(grid);
		fenetre.setContentPane(p);
		
		//Pour une raison que j'ignore, si le print juste en dessous est supprim� ou si des commentaires sont ins�r�s
		//entre une des 3 lignes qui suivent, �a ne fonctionne plus. Tr�s bizarre.
		Dimension size = fenetre.getContentPane().getSize();
		System.out.println(size.width + " " + size.height);
		fenetre.setSize(2 * fenetre.getWidth() - size.width, 2 * fenetre.getHeight() - size.height);
		
		fenetre.addKeyListener(new KeyListener() {
			
			@Override
			public void keyPressed(KeyEvent e) { 
				int input = e.getKeyCode();
				switch (input){
				case KeyEvent.VK_ENTER:
					System.out.print("Coordonn�es du joueur: ");
					System.out.println(player.getX() + ", " + player.getY());
					break;
				case KeyEvent.VK_UP:
					System.out.println("UP");
					if (grid.moveUp(player.getX(), player.getY()))
						player.moveUp();
					break;
				case KeyEvent.VK_DOWN :
					System.out.println("DOWN");
					if (grid.moveDown(player.getX(), player.getY()))
						player.moveDown();
					break;
				case KeyEvent.VK_RIGHT:
					System.out.println("RIGHT");
					if (grid.moveRight(player.getX(), player.getY()))
						player.moveRight();
					break;
				case KeyEvent.VK_LEFT:
					System.out.println("LEFT");
					if (grid.moveLeft(player.getX(), player.getY()))
						player.moveLeft();
					break;
				case KeyEvent.VK_M:
					System.out.println("Le joueur a boug�: " + player.hasMoved());
					break;
				default :
				}
				p.revalidate();
				p.repaint();
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
	
}
