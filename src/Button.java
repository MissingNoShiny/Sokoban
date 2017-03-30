import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JButton;


public class Button extends JButton{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3219033588828073912L;

	private Color color;
	
		
	public Button (String text, Color color, int fontSize) {
		super(text);
		setFocusable(false);
		setFont(new Font("arial", 0, fontSize));
		setColor(color);
		setBackground(color);
	}
	
	public Button (String text) {
		this(text, Color.orange, 70);
	}
	
	public Button (String text, Color color, int fontSize, int preferredHeight, int preferredWidth) {
		this(text, color, fontSize);
		setPreferredSize(new Dimension(preferredWidth, preferredHeight));		
	}
	
	public Button (String text, int preferredHeight, int preferredWidth) {
		this(text);
		setPreferredSize(new Dimension(preferredWidth, preferredHeight));
	}
	
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
}
