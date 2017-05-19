package be.ac.umons.info.sokoban.gui;

import java.io.File;
import java.io.IOException;

import be.ac.umons.info.sokoban.grid.GridReader;
import be.ac.umons.info.sokoban.grid.InvalidFileException;

/**
 * A class used to execute all the tests made for the package. 
 * @author Vincent Larcin, Joachim Sneessens
 */
public final class Tests {

	/**
	 * The folder this class
	 */
	private static final String TEST_FOLDER = "tests";
	
	/**
	 * Constructor is private to prevent instantiations.
	 */
	private Tests() {
		
	}
	
	/**
	 * Executes all the tests it can find in the test folder.
	 * @param args Unused
	 */
	public static void main(String[] args) {
		File folder = new File(TEST_FOLDER);
		File[] fileList = folder.listFiles();
		for (File file1 : fileList) {
			if (file1.getName().endsWith(".xsb")) {
				File file2 = new File(file1.getPath().split("[.]")[0] + ".mov");
				if (file2.exists()) {
					try {
						String gridOutputPath = file1.getPath().split("[.]")[0] + "_output";
						GridReader.applyMovesToGrid(file1.getPath(), file2.getPath(), gridOutputPath);
					} catch (IOException e) {
						e.printStackTrace();
					} catch (InvalidFileException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
