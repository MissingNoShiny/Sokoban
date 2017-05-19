package be.ac.umons.info.sokoban.gui;

import java.io.IOException;

import be.ac.umons.info.sokoban.grid.GridReader;
import be.ac.umons.info.sokoban.grid.InvalidFileException;

public class ConsoleMode {
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
