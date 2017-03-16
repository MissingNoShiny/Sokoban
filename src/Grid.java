
/**
 * Description
 * @author Vincent Larcin, Joachim Sneessens
 */
public class Grid {
	
	/**
	 * The matrix containing the data of a level.
	 */
	Component[][] matrix;
	
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
	 * Creates an object containing an empty matrix of specified height and width.
	 * @param height The height of the matrix
	 * @param width The width of the matrix
	 */
	public Grid(int height, int width) {
		matrix = new Component[height][width];
		this.height = height;
		this.width = width;
	}
	
	/*Pour que l'attribut matrix de l'objet Grid ne serve plus qu'à l'affichage, j'ai remplacé les positions de la 
	 matrix par des Component. C'est mieux à certains égards par contre ca implique de créer une liste des caisses.
	 Je ne sais pas s'il est préférable de créer une liste de taille fixe ou bien d'implémenter une liste de taille 
	 variable à laquelle on ajoutera les caisses une à une. Pour aujourd'hui, pour aller plus vite, je laisse fixe.
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
	
	public void placeOnGrid(int x, int y, Component comp) {
		matrix[y][x] = comp;
	}
	
	public Crate getCrateAt(int x, int y) {
		for (int i = 0; i < crates.length; i++) {
			if (x == crates[i].getX() && y == crates[i].getY())
				return crates[i];
		}
		System.out.println("Caisse pas trouvée");
		return null; //Cette ligne n'existe que pour contenter Eclipse, concrètement on appelle cette méthode que lorsqu'on est sur qu'il y a une caisse aux x et y donnés
	}
	
	public void fill(Component component) {
		int i, j;
		for (j = 0; j < height; j++) {
			for (i = 0; i < width; i++)
				matrix[i][j] = component;
		}
	}

	
	
	/*
	public static Grid readGrid (String name) {
		FileInputStream flux = new FileInputStream (name);
		InputStreamReader read = new InputStreamReader(flux); 
		BufferedReader buff=new BufferedReader(read);
		String ligne;
		int height = 0, width;
		try {
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
						grid.placeOnGrid(i, j, Component.WALL);
						break;
					case ("$"):
						grid.placeOnGrid(i, j, Component.CRATE); //Appeler methode addCrate
						break;
					case(" ") :
						grid.placeOnGrid(i, j, Component.GROUND);
						break;
					case(".") :
						grid.placeOnGrid(i, j, Component.GOAL);
						break;
					case ("@"):
						grid.placeOnGrid(i, j, Component.PLAYER);//Creer Player à ce moment
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if(reader !=null){reader.close();}
		 	}
			return grid;
		}
	}*/
	
	
	/*public boolean isFree(int posX, int posY) {
		if ((posX < height) && (posY < width)) {
			if !(getComponentAt(posX, posY).equals(Component.WALL))
				return true;
		}	
		return false;
	}*/
}
