package be.ac.umons.info.sokoban.gui;

import java.awt.Color;
import javax.swing.JLabel;

/**
 * A JLabel used in multiple classes
 * @author Vincent Larcin, Joachim Sneessens
 */
public class DefaultLabel extends JLabel {

	private static final long serialVersionUID = -2925126198019609830L;
	
	/**
	 * Creates a Label of specified color with specified text.
	 * @param text The text of the Label
	 * @param color The color of the Label
	 */
	public DefaultLabel(String text, Color color) {
		super(text);
		setBackground(color);
		setOpaque(true);
		setHorizontalAlignment(JLabel.CENTER);
		setFont(Options.smallFont);
	}
	
	/**
	 * Creates a Label of the default color with specified text.
	 * @param text The text of the Label
	 */
	public DefaultLabel(String text) {
		this(text, Options.getButtonColor());
	}
}
