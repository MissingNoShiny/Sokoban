
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

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
	 * The height of the matrix.
	 */
	private int height;
	
	/**
	 * The width of the matrix.
	 */
	private int width;
	
	/**
	 * The array containing the Crates of the level.
	 */
	private ArrayList <Crate> crates; 
	
	private ArrayList <Goal> goals; 
	
	//Attention, quand player est private, tout foire
	Player player;
	
	/**
	 * Creates an object containing an empty matrix of specified width and height.
	 * @param width The width of the matrix
	 * @param height The height of the matrix
	 */
	//Peut etre ajouter instanciation du player dans le constructeur
	public Grid(int width, int height) {
		matrix = new Component[height][width];
		crates = new ArrayList<Crate>(0);
		goals = new ArrayList<Goal>(0);
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
		for (int i = 0; i < crates.size(); i++) {
			comp = getComponentAt(crates.get(i).getX(), crates.get(i).getY());
			if  (! comp.equals(Component.CRATE_ON_GOAL)) {
				test= false;
			}
		}
		return test;
	}
	
	public void setPlayer (int x, int y) {
		player = new Player (x, y);
	}
	
	public void addCrate(int x, int y) {
		crates.add(new Crate(x, y, this));
	}
	
	public void addGoal(int x, int y) {
		goals.add(new Goal(x, y));
	}

	public boolean hasCrateAt (int x, int y) {
		for (int i = 0; i < crates.size(); i++) {
			if (x == crates.get(i).getX() && y == crates.get(i).getY())
				return true;
		}
		return false;
	}
	
	public Crate getCrateAt(int x, int y) {
		for (int i = 0; i < crates.size(); i++) {
			if (x == crates.get(i).getX() && y == crates.get(i).getY())
				return crates.get(i);
		}
		return null; //Cette ligne n'existe que pour contenter Eclipse, concretement on appelle cette methode que
		//lorsqu'on est sur qu'il y a une caisse aux x et y donnes
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
						grid.addCrate(j, i);
						break;
					case(' ') :
						grid.placeComponentAt(j, i, Component.GROUND);
						break;
					case('.') :
						grid.addGoal(j, i);
						grid.placeComponentAt(j, i, Component.GOAL);
						break;
					case ('@'):
						grid.placeComponentAt(j, i, Component.GROUND);
						grid.setPlayer(j, i);
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
