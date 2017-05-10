package be.ac.umons.info.sokoban;

public class InvalidFileException extends Exception {
	
	/*
	 * J'aurais aime mettre extends IOException mais alors les InvalidFileException sont catch dans le readGrid
	 */
	
	public InvalidFileException(String string) {
		super(string);
	}

	private static final long serialVersionUID = -8917708189191418966L;

}
