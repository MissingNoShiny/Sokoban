import java.io.*;

/**
 * Description
 * @author Vincent Larcin, Joachim Sneessens
 */
public class Grid {
	
	/**
	 * The matrix containing the data of a level.
	 */
	Position[][] matrix;
	
	/**
	 * The height of the matrix.
	 */
	int height;
	
	/**
	 * The width of the matrix.
	 */
	int width;
	
	/**
	 * Creates an object containing an empty matrix of specified height and width.
	 * @param height The height of the matrix
	 * @param width The width of the matrix
	 */
	public Grid(int height, int width) {
		matrix = new Position[height][width];
		this.height = height;
		this.width = width;
	}
	
	/**
	 * Gets the height of the matrix.
	 * @return The height of the matrix
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * Gets the width of the matrix
	 * @return The width of the matrix
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * Gets the content of the matrix at specified position.
	 * @param x The X-coordinate of the cell to get data from
	 * @param y The y-coordinate of the cell to get data from
	 * @return The data contained in specified cell
	 */
	public Position getPositionAt(int x, int y) {
		return matrix[y][x];
	}
	
	
	public void placeOnGrid(int x, int y, Position p) {
		matrix[y][x] = p;
	}
	
	/**
	 * Tries to move up a Position object at specified coordinates in the matrix.
	 * @param x The X-coordinate of the object
	 * @param y The Y-coordinate of the object
	 * @return true if the move has been made successfully, false else
	 */
	public boolean moveUp(int x, int y) {
		if (matrix[y][x] != null && y!= 0 && matrix[y-1][x] == null) {
			matrix[y-1][x] = matrix[y][x];
			matrix[y][x] = null;
			return true;
		}
		return false;
	}

	/**
	 * Tries to move down a Position object at specified coordinates in the matrix.
	 * @param x The X-coordinate of the object
	 * @param y The Y-coordinate of the object
	 * @return true if the move has been made successfully, false else
	 */
	public boolean moveDown(int x, int y) {
		if (matrix[y][x] != null && y!= height-1 && matrix[y+1][x] == null) {
			matrix[y+1][x] = matrix[y][x];
			matrix[y][x] = null;
			return true;
		}
		return false;
	}

	/**
	 * Tries to move right a Position object at specified coordinates in the matrix.
	 * @param x The X-coordinate of the object
	 * @param y The Y-coordinate of the object
	 * @return true if the move has been made successfully, false else
	 */
	public boolean moveRight(int x, int y) {
		if (matrix[y][x] != null && x!= width-1 && matrix[y][x+1] == null) {
			matrix[y][x+1] = matrix[y][x];
			matrix[y][x] = null;
			return true;
		}
		return false;
	}

	/**
	 * Tries to move left a Position object at specified coordinates in the matrix.
	 * @param x The X-coordinate of the object
	 * @param y The Y-coordinate of the object
	 * @return true if the move has been made successfully, false else
	 */
	public boolean moveLeft(int x, int y) {
		if (matrix[y][x] != null && x!= 0 && matrix[y][x-1] == null) {
			matrix[y][x-1] = matrix[y][x];
			matrix[y][x] = null;
			return true;
		}
		return false;
	}


	/*public static Grid read_grid (String name) {
		FileInputStream flux = new FileInputStream (name);
		InputStreamReader read = new InputStreamReader(flux); 
		BufferedReader buff=new BufferedReader(read);
		String ligne;
		int height = 0, width;
		while ((ligne=buff.readLine())!=null){
			height++;
		}
		width = ligne.length();
		Grid grid = new Grid(height, width);
		char character;
		for (int i = 0; i < height; i++){
			ligne = buff.readLine();
			for (int j = 0; j < width; j++) {
				character = ligne[j];
				switch (character) {  //ici, une structure analogue au dictionnaire serait plus pratique qu'un switch puisque le bloc exécuté est toujours le même
				case ("#") :
					grid.placeOnGrid(i, j, wall);
					break;
				case ("$"):
					grid.placeOnGrid(i, j, "caisse");
					break;
				case(" ") :
					break;
				case(".") :
					grid.placeOnGrid(i, j, "objectif);
					break;
				case ("@"):
					grid.placeOnGrid(i, j, Player);
				}
			}
		}
		return grid;
	}
	*/
	
	/*public boolean isFree(int posX, int posY) {
		if ((posX < height) && (posY < width)) {
			//retourne un rensignement sur ce que contient la case concernee
		}
		else{
			// Erreur de OUF
		}
	}*/
}
