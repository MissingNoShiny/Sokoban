
import java.awt.Dimension;

import javax.swing.JFrame;

public class Display extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6826804177182624245L;
	
	public Display(Dimension dimension, String title) {
		super(title);
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
