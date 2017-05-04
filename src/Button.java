
import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;

public class Button extends JButton{
	
	
	private static final long serialVersionUID = -3219033588828073912L;

	/**
	 * The original color of the button.
	 */
	private Color color;
	
	public Button (String text, Color color, Font font) {
		super(text);
		setFocusable(false);
		setFont(font);
		setColor(color);
		setBackground(color);
	}
	
	/**
	 * Gets the original color of the button.
	 * @return the original color of the button
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Sets the color of the button.
	 * @param color The new color of the button
	 */
	public void setColor(Color color) {
		this.color = color;
	}
}
