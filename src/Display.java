
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;

public class Display extends JFrame implements KeyListener{	
	
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
		
		addKeyListener(this);
	}
	
    public void keyPressed(KeyEvent e) { 
    	int input = e.getKeyCode();
    	switch (input){
    	case KeyEvent.VK_ENTER:
        	System.out.println("Vous avez appuyé sur Enter");
        	break;
    	case KeyEvent.VK_UP:
    		System.out.println("UP");
    		break;
    	case KeyEvent.VK_RIGHT :
    		System.out.println("RIGHT");
    		break;
    	case KeyEvent.VK_DOWN:
    		System.out.println("DOWN");
    		break;
    	case KeyEvent.VK_LEFT:
    		System.out.println("LEFT");
    		break;
    	case KeyEvent.VK_J:
    		System.out.println("J");
    		break;
    	default :
    	}
    }
    
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
