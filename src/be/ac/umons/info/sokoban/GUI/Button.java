package be.ac.umons.info.sokoban.gui;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;

public class Button extends JButton {
	
	private static final long serialVersionUID = -3219033588828073912L;

	public Button (String text, Color color, Font font) {
		super(text);
		setFocusable(false);
		setFont(font);
		setBackground(color);
	}
}
