
import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;

public class Button extends JButton{
	
	
	private static final long serialVersionUID = -3219033588828073912L;

	private Color color;

	
	public Button (String text, Color color, Font font) {
		super(text);
		setFocusable(false);
		setFont(font);
		setColor(color);
		setBackground(color);
	}
	
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
}
