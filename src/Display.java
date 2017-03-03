import javax.swing.JFrame;

public class Display extends JFrame {
	
	public Display() {
		this.setTitle("Sokoban");
		this.setSize(600, 400);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Panel p = new Panel();
		
		this.setVisible(true);
	}

}
