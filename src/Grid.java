import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Description
 * @author Vincent Larcin, Joachim Sneessens
 */
public class Grid {
	
	/**
	 * The matrix containing the data of a level.
	 */
	Component[][] matrix;
	
	/**
	 * The array containing the Crates of the level.
	 */
	Crate[] crates; 
	
	/**
	 * The height of the matrix.
	 */
	private int height;
	
	/**
	 * The width of the matrix.
	 */
	private int width;
	
	/**
	 * Creates an object containing an empty matrix of specified width and height.
	 * @param width The width of the matrix
	 * @param height The height of the matrix
	 */
	public Grid(int width, int height) {
		matrix = new Component[height][width];
		this.height = height;
		this.width = width;
	}
	
	/*Pour que l'attribut matrix de l'objet Grid ne serve plus qu'à l'affichage, j'ai remplace les positions de la 
	 matrix par des Component. C'est mieux à certains égards par contre ca implique de creer une liste des caisses.
	 Je ne sais pas s'il est preferable de creer une liste de taille fixe ou bien d'implementer une liste de taille 
	 variable a� laquelle on ajoutera les caisses une à une. Pour aujourd'hui, pour aller plus vite, je laisse fixe.
	*/
	public void setNumberCrates(int numberCrates) {
		crates = new Crate [numberCrates];
	}
	
	public void addCrate(int x, int y) {
		int i = 0;
		while (crates[i] != null)
			i += 1;
		if (i < crates.length)
			crates[i] = new Crate(x, y, this);
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
	public Component getComponentAt(int x, int y) {
		return matrix[y][x];
	}
	
	/**
	 * Place a new Component at specified position in the matrix
	 * @param x The X-coordinate of the cell to set a new Component
	 * @param y The Y-coordinate of the cell to set a new Component
	 * @param comp  The Component to set at the specified cell
	 */
	public void placeComponentAt(int x, int y, Component comp) {
		matrix[y][x] = comp;
	}
	
	public boolean isWon(){
		boolean test = true;
		Component comp;
		for (int i = 0; i < crates.length; i++) {
			comp = getComponentAt(crates[i].getX(), crates[i].getY());
			if  (! comp.equals(Component.CRATE_ON_GOAL)) {
				test= false;
			}
		}
		return test;
	}
	
	public boolean hasCrateAt (int x, int y) {
		boolean test = false;
		for (int i = 0; i < crates.length; i++) {
			if (x == crates[i].getX() && y == crates[i].getY())
				test = true;
		}
		return test;
	}
	
	public Crate getCrateAt(int x, int y) {
		for (int i = 0; i < crates.length; i++) {
			if (x == crates[i].getX() && y == crates[i].getY())
				return crates[i];
		}
		System.out.println("Caisse pas trouvee");
		return null; //Cette ligne n'existe que pour contenter Eclipse, concrètement on appelle cette méthode que lorsqu'on est sur qu'il y a une caisse aux x et y donnés
	}
	
	public void fill(Component component) {
		int i, j;
		for (j = 0; j < width; j++) {
			for (i = 0; i < height; i++)
				matrix[i][j] = component;
		}
	}
	
	
	public static Grid readGrid (String path) {
		Grid grid = null;
		BufferedReader buff = null;
		try {
			buff = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
			String ligne;
			int height = 1, width;
			width = buff.readLine().length();
			while ((ligne = buff.readLine())!=null){
				height++;
			}
			grid = new Grid(width, height);
			buff.close();
			
			buff = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
			Character character;
			System.out.println(width);
			for (int i = 0; i < height; i++){
				ligne = buff.readLine();
				for (int j = 0; j < width; j++) {
					character = ligne.charAt(j);
					switch (character) {
					case ('#') :
						grid.placeComponentAt(j, i, Component.WALL);
						break;
					case ('$'):
						grid.placeComponentAt(j, i, Component.CRATE); //Appeler methode addCrate
						break;
					case(' ') :
						grid.placeComponentAt(j, i, Component.GROUND);
						break;
					case('.') :
						grid.placeComponentAt(j, i, Component.GOAL);
						break;
					case ('@'):
						grid.placeComponentAt(j, i, Component.PLAYER);//Creer Player a� ce moment
						break;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(buff !=null)
				try {
					buff.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
	 	}
		return grid;
	}
	
	
	/*public boolean isFree(int posX, int posY) {
		if ((posX < height) && (posY < width)) {
			if !(getComponentAt(posX, posY).equals(Component.WALL))
				return true;
		}	
		return false;
	}*/
}
