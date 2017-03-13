import java.io.*;

public class Grid {
	
	Position[][] matrix;
	int height, width;
	
	public Grid(int height, int width) {
		matrix = new Position[height][width];
		this.height = height;
		this.width = width;
		
	}
	public int getHeight() {
		return height;
	}
	
	public int getWidth() {
		return width;
	}
	
	public Position getPositionAt(int x, int y) {
		return matrix[y][x];
	}
	
	public void placeOnGrid(int x, int y, Position p) {
		matrix[y][x] = p;
	}
	
	public boolean moveUp(int x, int y) {
		if (matrix[y][x] != null && y!= 0 && matrix[y-1][x] == null) {
			matrix[y-1][x] = matrix[y][x];
			matrix[y][x] = null;
			return true;
		}
		return false;
	}
	
	public boolean moveDown(int x, int y) {
		if (matrix[y][x] != null && y!= height-1 && matrix[y+1][x] == null) {
			matrix[y+1][x] = matrix[y][x];
			matrix[y][x] = null;
			return true;
		}
		return false;
	}
	
	public boolean moveRight(int x, int y) {
		if (matrix[y][x] != null && x!= width-1 && matrix[y][x+1] == null) {
			matrix[y][x+1] = matrix[y][x];
			matrix[y][x] = null;
			return true;
		}
		return false;
	}
	
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
