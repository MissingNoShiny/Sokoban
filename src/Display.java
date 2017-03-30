
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Display extends JFrame {
	
	/**
	 * TODO
	 */
	private JPanel p;
	
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
	 * Changes the JPanel currently displayed.
	 * @param p the JPanel to display
	 */
	public void setPanel(JPanel p) {
		this.p = p;
		setContentPane(p);
		refresh();
	}
	
	/**
	 * Updates the current JPanel.
	 */
	public void refresh() {
		p.revalidate();
		p.repaint();
	}
	
}
