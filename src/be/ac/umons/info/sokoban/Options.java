package be.ac.umons.info.sokoban;

import java.awt.Color;
import java.awt.Font;

public class Options implements java.io.Serializable {
	
	private static final long serialVersionUID = 880731819964339511L;

	public static final String fontName = "century";

	public static final Font littleFont  = new Font(fontName, 0, 40);
	
	public static final Font bigFont = new Font(fontName, 0, 40);
	
	private static Color buttonColor = Color.orange;
	
	private static Color backgroundColor = new Color(135, 206, 250);

	private static boolean playerArrowsShown = false;
	
	private static String textureDir = "classic";
	
	private Color buttonColorSave;
	
	private Color backgroundColorSave;
	
	private boolean playerArrowsShownSave;
	
	private String textureDirSave;
	
	public Options() {

	}

	public void save() {
		buttonColorSave = buttonColor;
		backgroundColorSave = backgroundColor;
		playerArrowsShownSave = playerArrowsShown;
		textureDirSave = textureDir;
	}
	
	public void load() {
		buttonColor = buttonColorSave;
		backgroundColor = backgroundColorSave;
		playerArrowsShown = playerArrowsShownSave;
		textureDir = textureDirSave;
	}

	public static Color getButtonColor() {
		return buttonColor;
	}

	public static void setButtonColor(Color buttonColor) {
		Options.buttonColor = buttonColor;
	}

	public static Color getBackgroundColor() {
		return backgroundColor;
	}

	public static void setBackgroundColor(Color backgroundColor) {
		Options.backgroundColor = backgroundColor;
	}

	public static boolean arePlayerArrowsShown() {
		return playerArrowsShown;
	}

	public static void setPlayerArrowsShown(boolean playerArrowsShown) {
		Options.playerArrowsShown = playerArrowsShown;
	}

	public static String getTextureDir() {
		return textureDir;
	}

	public static void setTextureDir(String textureDir) {
		Options.textureDir = textureDir;
	}
}
