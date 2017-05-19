package be.ac.umons.info.sokoban.gui;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;

/**
 * A JButton used by multiple classes.
 * @author Vincent Larcin, Joachim Sneessens
 */
public class Button extends JButton {
	
	private static final long serialVersionUID = -3219033588828073912L;

	/**
	 * Creates a Button of specified color with specified text in specified font.
	 * @param text The text of the Button
	 * @param color The color of the Button
	 * @param font The font of the Button
	 */
	public Button (String text, Color color, Font font) {
		super(text);
		setFocusable(false);
		setFont(font);
		setBackground(color);
	}
}
