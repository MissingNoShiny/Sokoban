package be.ac.umons.info.sokoban;

import java.awt.Color;

public class Options implements java.io.Serializable {
	
	private static final long serialVersionUID = 880731819964339511L;

	private static String fontName = "century";

	private static int fontSize = 40;
	
	private static int buttonFontSize = 70;
	
	private static Color buttonColor = Color.orange;
	
	private static Color backgroundColor = new Color(135, 206, 250);

	private static boolean playerArrowsShown = false;
	
	private static String textureDir = "classic";
	
	private String fontNameSave;
	
	private int fontSizeSave;
	
	private int buttonFontSizeSave;
	
	private Color buttonColorSave;
	
	private Color backgroundColorSave;
	
	private boolean playerArrowsShownSave;
	
	private String textureDirSave;
	
	public Options() {

	}

	public void save() {
		fontNameSave = fontName;
		fontSizeSave = fontSize;
		buttonFontSizeSave = buttonFontSize;
		buttonColorSave = buttonColor;
		backgroundColorSave = backgroundColor;
		playerArrowsShownSave = playerArrowsShown;
		textureDirSave = textureDir;
	}
	
	public void load() {
		fontName = fontNameSave;
		fontSize = fontSizeSave;
		buttonFontSize = buttonFontSizeSave;
		buttonColor = buttonColorSave;
		backgroundColor = backgroundColorSave;
		playerArrowsShown = playerArrowsShownSave;
		textureDir = textureDirSave;
	}
	
	public static String getFontName() {
		return fontName;
	}

	public static void setFontName(String fontName) {
		Options.fontName = fontName;
	}

	public static int getFontSize() {
		return fontSize;
	}

	public static void setFontSize(int fontSize) {
		Options.fontSize = fontSize;
	}

	public static int getButtonFontSize() {
		return buttonFontSize;
	}

	public static void setButtonFontSize(int buttonFontSize) {
		Options.buttonFontSize = buttonFontSize;
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
