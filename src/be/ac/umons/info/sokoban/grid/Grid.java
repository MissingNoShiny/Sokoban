package be.ac.umons.info.sokoban.grid;

import java.util.ArrayList;

/**
 * A class used to manage the data of a level.
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
	
	/**
	 * The Player of this grid.
	 */
	private Player player;
	
	/**
	 * The MovementTracker of this grid.
	 */
	private MovementTracker tracker;
	
	/**
	 * Creates an object containing an empty matrix of specified width and height.
	 * @param width The width of the matrix
	 * @param height The height of the matrix
	 */
	Grid(int width, int height) {
		this.width = width;
		this.height = height;
		matrix = new Component[width][height];
		crateList = new ArrayList<Crate>(0);
		player = new Player (this, 1, 1);
		tracker = new MovementTracker(player);
	}
	
	/**
	 * Gets the height of the matrix.
	 * @return The height of the matrix
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * Gets the width of the matrix.
	 * @return The width of the matrix
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * Gets the content of the matrix at specified position.
	 * @param x The X-coordinate of the cell to get data from
	 * @param y The y-coordinate of the cell to get data from
	 * @return The Component contained in specified cell
	 * @throws IllegalArgumentException
	 */
	Component getComponentAt(int x, int y) {
		if (x < 0 || x >= width || y < 0 || y >= height)
			throw new IllegalArgumentException();
		return matrix[x][y];
	}
	
	public String getComponentTypeAt(int x, int y) {
		return getComponentAt(x, y).getName();
	}
	
	/**
	 * Places a new Component at specified position in the matrix
	 * @param x The X-coordinate of the cell to set a new Component
	 * @param y The Y-coordinate of the cell to set a new Component
	 * @param comp  The Component to set at the specified cell
	 * @throws IllegalArgumentException
	 */
	void placeComponentAt(int x, int y, Component comp) throws IllegalArgumentException {
		if (x < 0 || x >= width || y < 0 || y >= height)
			throw new IllegalArgumentException();
		matrix[x][y] = comp;
	}
	
	/**
	 * Checks if the grid is in a won state.
	 * @return true if the grid is in a won state, false else
	 */
	public boolean isWon(){
		String comp;
		for (int i = 0; i < crateList.size(); i++) {
			comp = getComponentAt(crateList.get(i).getX(), crateList.get(i).getY()).getName();
			if  (! comp.equals("CrateOnGoal"))
				return false;
		}
		return true;
	}
	
	/**
	 * Moves the player to specified coordinates.
	 * @param x The new X-coordinate of the player
	 * @param y The new Y-coordinate of the player
	 */
	void setPlayerCoordinates(int x, int y) {
		player.setNewCoordinates(x, y);
	}
	
	/**
	 * Gets the player of this grid.
	 * @return The player of this grid
	 */
	public Player getPlayer() {
		return player;
	}
	
	/**
	 * Adds a crate Component at specified location.
	 * @param x The x-coordinate of the crate
	 * @param y The y-coordinate of the crate
	 */
	void addCrate(int x, int y) {
		crateList.add(new Crate(this, x, y));
	}
	
	/**
	 * Removes a crate Component from the grid. This method is only used when generating a level.
	 * @param ind The index of the crate in the crateList
	 */
	public void removeCrate(int ind) {
		Crate crate = crateList.get(ind);
		matrix[crate.getX()][crate.getY()] = crate.getSupport();
		crateList.remove(ind);
	}
	
	/**
	 * Checks if the Component at specified coordinates is a Crate.
	 * @param x The x-coordinate of the cell to check
	 * @param y The y-coordinate of the cell to check
	 * @return true if the coordinates are inside the grid and the Component is a Crate, false else
	 */
	boolean hasCrateAt (int x, int y) {
		if (x < 0 || x >= width || y < 0 || y >=height)
			throw new IllegalArgumentException();
		return (getComponentTypeAt(x, y).equals("Crate") || getComponentTypeAt(x, y).equals("CrateOnGoal"));
	}
	
	/**
	 * Gets a reference to a crate Component using its coordinates. This method is only used by other methods, and will only be executed if there is a crate at specified coordinates.
	 * @param x The x-coordinate of the crate
	 * @param y The y-coordinate of the crate
	 * @return The crate at specified coordinates
	 */
	Crate getCrateAt(int x, int y) {
		return (Crate) getComponentAt(x, y);
	}
	
	/**
	 * Gets the list of crate Components contained in the matrix.
	 * @return the list of crate Components contained in the matrix
	 */
	ArrayList<Crate> getCrateList() {
		return crateList;
	}
	
	/**
	 * Gets the MovementTracker of this grid.
	 * @return The MovementTracker of this grid
	 */
	public MovementTracker getTracker() {
		return tracker;
	}
	
	/**
	 * Fills the grid with an instance of a chosen Component.
	 * @param component The component to fill the grid with an instance of
	 */
	void fill(Component component) {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++)
				matrix[i][j] = component;
		}
	}
}
