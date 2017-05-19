package be.ac.umons.info.sokoban.gui;

import java.io.IOException;

import be.ac.umons.info.sokoban.grid.GridReader;
import be.ac.umons.info.sokoban.grid.InvalidFileException;

/**
 * A class used to perform moves on a grid without using a gui.
 * @author Vincent Larcin
 */
public class ConsoleMode {
	
	/**
	 * Takes a xsb file and a mov file in input.
	 * Applies the moves in the .mov file to the grid in the .xsb file.
	 * Saves the result to a specified .xsb output file.
	 * @param args The console arguments, which must follow this structure:
	 * <p>- args[0]: The path to the input .xsb file
	 * <p>- args[1]: The path to the input .mov file
	 * <p>- args[2]: The path to the output .xsb file (without the .xsb extension)
	 */
	public static void main(String[] args) {
		if (args.length != 3)
			throw new IllegalArgumentException();
		try {
			GridReader.applyMovesToGrid(args[0], args[1], args[2]);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidFileException e) {
			e.printStackTrace();
		}
	}
}
