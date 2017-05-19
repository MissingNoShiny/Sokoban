package be.ac.umons.info.sokoban.gui;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Options implements Serializable {
	
	private static final long serialVersionUID = 880731819964339511L;
	
	public static final Color DEFAULT_BUTTON_COLOR = Color.orange;
	
	public static final Color DEFAULT_BACKGROUND_COLOR = new Color(135, 206, 250);
	
	public static final int DEFAULT_BORDER_THICKNESS = 5;
	
	public static final String DEFAULT_TEXTURE_DIR = "default";
	
	public static String fontName = "century";

	public static final Font smallFont  = new Font(fontName, 0, 40);
	
	public static final Font bigFont = new Font(fontName, 0, 70);
	
	public static final Font titleFont = new Font(fontName, 0, 100);
	
	private static Color buttonColor = DEFAULT_BUTTON_COLOR;
	
	private static Color backgroundColor = DEFAULT_BACKGROUND_COLOR;
	
	private static int borderThickness = DEFAULT_BORDER_THICKNESS;

	private static boolean playerArrowsShown = true;
	
	private static String textureDir = DEFAULT_TEXTURE_DIR;

	private Color buttonColorSave;
	
	private Color backgroundColorSave;
	
	private int borderThicknessSave;
	
	private boolean playerArrowsShownSave;
	
	private String textureDirSave;
	
	/**
	 * Creates a new Options object with the default values.
	 */
	public Options() {

	}

	/**
	 * Saves the current options to a file
	 */
	public void save() {
		buttonColorSave = buttonColor;
		backgroundColorSave = backgroundColor;
		borderThicknessSave = borderThickness;
		playerArrowsShownSave = playerArrowsShown;
		textureDirSave = textureDir;
		try {
			FileOutputStream fileOut = new FileOutputStream("options.ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(this);
			out.close();
			fileOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Loads a saved Options object or creates a new one if none exists.
	 * @return The loaded Options object, or a new one if none could be loaded.
	 */
	public static Options load() {
		Options output = new Options();
		if (new File("options.ser").exists()) {
			try {
				FileInputStream fileIn = new FileInputStream("options.ser");
				ObjectInputStream in = new ObjectInputStream(fileIn);
				output = (Options) in.readObject();
				in.close();
				fileIn.close();
				buttonColor = output.buttonColorSave;
				backgroundColor = output.backgroundColorSave;
				borderThickness = output.borderThicknessSave;
				playerArrowsShown = output.playerArrowsShownSave;
				textureDir = output.textureDirSave;
			} catch (IOException i) {
				i.printStackTrace();
			} catch (ClassNotFoundException c) {
				System.out.println("Options class not found");
				c.printStackTrace();
			}
		}
		return output;

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

	public static int getBorderThickness() {
		return borderThickness;
	}

	public static void setBorderThickness(int borderThickness) {
		Options.borderThickness = borderThickness;
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
