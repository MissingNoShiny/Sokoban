package be.ac.umons.info.sokoban;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Options implements java.io.Serializable {
	
	private static final long serialVersionUID = 880731819964339511L;

	public static final String fontName = "century";

	public static final Font littleFont  = new Font(fontName, 0, 40);
	
	public static final Font bigFont = new Font(fontName, 0, 70);
	
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
		try {
			FileOutputStream fileOut = new FileOutputStream("options.ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(this);
			out.close();
			fileOut.close();
			System.out.println("Options saved");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Options load() {
		Options output = new Options();
		if (new File("options.ser").exists()) {
			try {
				FileInputStream fileIn = new FileInputStream("options.ser");
				ObjectInputStream in = new ObjectInputStream(fileIn);
				output = (Options) in.readObject();
				in.close();
				fileIn.close();
				System.out.println("Options loaded");
				buttonColor = output.buttonColorSave;
				backgroundColor = output.backgroundColorSave;
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
