
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;

public class Display extends JFrame {	
	
	public static void main(String[] args) {
		Display fenetre = new Display();
	}
	
	public Display() {
		super("Sokoban");
		setSize(600, 400);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		
		Panel p = new Panel();
		
		setVisible(true);
		
		addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) { 
				int input = e.getKeyCode();
				switch (input){
				case KeyEvent.VK_ENTER:
					System.out.println("Coordonnées du joueur: ");
					System.out.print(play.getX() + ", " + play.getY());
					break;
				case KeyEvent.VK_UP:
					System.out.println("UP");
					play.moveUp();
					break;
				case KeyEvent.VK_RIGHT :
					System.out.println("RIGHT");
					play.moveRight();
					break;
				case KeyEvent.VK_DOWN:
					System.out.println("DOWN");
					play.moveDown();
					break;
				case KeyEvent.VK_LEFT:
					System.out.println("LEFT");
					play.moveLeft();
					break;
				case KeyEvent.VK_M:
					System.out.println("Le joueur a bougé: " + play.hasMoved())
					break;
				default :
				}
			}
			    
			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}
			
		}
		);
	}
	
}
