import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;


public class Button extends JButton{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3219033588828073912L;

	private Color color;
	
	public Button (String text, Color color) {
		super(text);
		setFocusable(false);
		setFont(new Font("arial", 0, 70));
		setSize(200,200);
		setColor(color);
		setBackground(color);
	}
	
	public Button (String name) {
		this(name, Color.orange);
	}
	
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
}
