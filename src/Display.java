
import javax.swing.JFrame;

public class Display extends JFrame {
	
	public Display() {
		super("Sokoban");
		setSize(600, 400);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		
		Panel p = new Panel();
		
		setVisible(true);
	}
}
