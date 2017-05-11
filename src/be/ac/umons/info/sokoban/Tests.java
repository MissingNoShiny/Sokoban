package be.ac.umons.info.sokoban;

import java.io.File;
import java.io.IOException;

/**
 * A class used to test other classes in this package.
 * @author Vincent Larcin
 */
public class Tests {

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
