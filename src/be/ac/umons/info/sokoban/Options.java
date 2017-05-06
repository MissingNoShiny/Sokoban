package be.ac.umons.info.sokoban;

import java.awt.Color;
import java.awt.Font;

public class Options {
	
	public static final String fontName = "century";
	
	public static final Font defaultFont = new Font(fontName, 0, 40);
	
	public static final Font menuButtonsFont = new Font(fontName, 0, 70);
	
	public static Color buttonsColor = Color.orange;
	
	public static Color backGroundColor = new Color(135, 206, 250);

	public static boolean SHOW_PLAYER_ARROWS = false;
	
	/**
	 * Pour le moment je rends l'instanciation impossible. A l'avenir c'est lors de l'instanciation que l'on chargera 
	 * les parametres precedents.
	 */
	private Options() {
		
	}
}
