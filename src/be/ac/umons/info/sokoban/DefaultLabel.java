package be.ac.umons.info.sokoban;

import java.awt.Color;
import javax.swing.JLabel;

public class DefaultLabel extends JLabel {

	private static final long serialVersionUID = -2925126198019609830L;
	
	public DefaultLabel(String string, Color color) {
		super(string);
		setBackground(color);
		setOpaque(true);
		setHorizontalAlignment(JLabel.CENTER);
		setFont(Options.smallFont);
	}
	
	public DefaultLabel(String string) {
		this(string, Options.getButtonColor());
	}
}
