package be.ac.umons.info.sokoban.gui;

import javax.swing.JFrame;

public class DefaultFrame extends JFrame {

	private static final long serialVersionUID = 235522330751682721L;
	
	public DefaultFrame(String title, int width, int height) {
		super(title);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setSize(width, height);
		setLocationRelativeTo(null); 
		setResizable(false);
	}
}
