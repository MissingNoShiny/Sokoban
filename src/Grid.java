
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
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
	 * The array containing the list of the crates of the level.
	 */
	private ArrayList <Crate> crateList; 
	
	//private ArrayList <Goal> goals; 
	
	private Player player;
	
	private MovementTracker tracker;
	/**
	 * Creates an object containing an empty matrix of specified width and height.
	 * @param width The width of the matrix
	 * @param height The height of the matrix
	 */
	//Peut etre ajouter instanciation du player dans le constructeur
	public Grid(int width, int height) {
		matrix = new Component[height][width];
		crateList = new ArrayList<Crate>(0);
		this.width = width;
		this.height = height;
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
		String comp;
		for (int i = 0; i < crateList.size(); i++) {
			comp = getComponentAt(crateList.get(i).getX(), crateList.get(i).getY()).getName();
			if  (! comp.equals("CrateOnGoal")) {
				test= false;
			}
		}
		return test;
	}
	
	public void setPlayer (int x, int y) {
		player = new Player (this, x, y);
		tracker = new MovementTracker(player);
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public void addCrate(int x, int y) {
		crateList.add(new Crate(this, x, y));
	}
	

	public boolean hasCrateAt (int x, int y) {
		String nameComponent = getComponentAt(x, y).getName();
		return (nameComponent == "Crate" || nameComponent == "CrateOnGoal");
	}
	
	//Il faudrait faire en sorte d'obliger à utiliser hasCrateAt avant ceci
	public Crate getCrateAt(int x, int y) {
		return (Crate) getComponentAt(x, y);
	}
	
	public ArrayList<Crate> getCrateList() {
		return crateList;
	}
	
	public MovementTracker getTracker() {
		return tracker;
	}
	
	/*
	 * Attention, le fill ne remplit pas tous le tableau avec des instances differentes d'une même classe, mais avec
	 * la même objet a chaque fois. Toutes les cases pointent vers le même objet.
	 */
	public void fill(Component component) {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++)
				matrix[j][i] = component;
		}
	}
	

	public void placeBlanks() {
		boolean[][] isFlooded = new boolean[height][width];
		findGrounds(isFlooded, player.getX(), player.getY());
		Component blank = new Blank();
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++)
				if (matrix[j][i].getName().equals("Ground") && !isFlooded[j][i])
					matrix[j][i] = blank;
		}
	}
	
	private void findGrounds(boolean[][] isFlooded, int x, int y) {
		if (isFlooded[y][x] == false && !matrix[y][x].getName().equals("Wall")){
			isFlooded[y][x] = true;
			if (x > 0)
				findGrounds(isFlooded, x-1, y);
			if (x < width-1)
				findGrounds(isFlooded, x+1, y);
			if (y > 0)
				findGrounds(isFlooded, x, y-1);
			if (y < height-1)
				findGrounds(isFlooded, x, y+1);
		}
	}
}
