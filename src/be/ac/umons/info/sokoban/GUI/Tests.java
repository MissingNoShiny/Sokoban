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
	 * Constructor is private to prevent instantiations.
	 */
	private Tests() {
		
	}
	
	/**
	 * Executes all the tests it can find in the test folder.
	 * @param args Unused
	 */
	public static void main(String[] args) {
		File folder = new File("tests");
		File[] fileList = folder.listFiles();
		for (File file1 : fileList) {
			if (file1.getName().endsWith(".xsb")) {
				File file2 = new File(file1.getPath().split("[.]")[0] + ".mov");
				if (file2.exists()) {
					try {
						GridReader.applyMovesToGrid(file1.getPath(), file2.getPath());
					} catch (IOException e) {
						e.printStackTrace();
					} catch (InvalidFileException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	/**
	 * Deletes all the outputs from the test folder.
	 */
	public static void cleanFolder() {
		File folder = new File("tests");
		File[] fileList = folder.listFiles();
		for (File file : fileList) {
			if (file.getName().endsWith("_output.xsb")) {
				file.delete();
			}
		}
	}
}
