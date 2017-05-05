
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
	public Grid(int width, int height) {
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
	 */
	public Component getComponentAt(int x, int y) {
		return matrix[x][y];
	}
	
	/**
	 * Places a new Component at specified position in the matrix
	 * @param x The X-coordinate of the cell to set a new Component
	 * @param y The Y-coordinate of the cell to set a new Component
	 * @param comp  The Component to set at the specified cell
	 */
	public void placeComponentAt(int x, int y, Component comp) {
		matrix[x][y] = comp;
	}
	
	/**
	 * Checks if the grid is in a won state.
	 * @return true if the grid is in a won state, false else
	 */
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
	
	/**
	 * Moves the player to specified coordinates.
	 * @param x The new x-coordinate of the player
	 * @param y The new y-coordinate of the player
	 */
	public void setPlayerCoordinates(int x, int y) {
		player.setX(x);
		player.setY(y);
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
	public void addCrate(int x, int y) {
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
	public boolean hasCrateAt (int x, int y) {
		if (x >= width || y >=height)
			return false;
		String nameComponent = getComponentAt(x, y).getName();
		return (nameComponent == "Crate" || nameComponent == "CrateOnGoal");
	}
	
	/**
	 * Gets a reference to a crate Component using its coordinates. This method is only used by other methods, and will only be executed if there is a crate at specified coordinates.
	 * @param x The x-coordinate of the crate
	 * @param y The y-coordinate of the crate
	 * @return The crate at specified coordinates
	 */
	public Crate getCrateAt(int x, int y) {
		return (Crate) getComponentAt(x, y);
	}
	
	/**
	 * Gets the list of crate Components contained in the matrix.
	 * @return the list of crate Components contained in the matrix
	 */
	public ArrayList<Crate> getCrateList() {
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
	public void fill(Component component) {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++)
				matrix[i][j] = component;
		}
	}
	
	public boolean frozenDeadlockDetector(Crate lastMovedCrate) {
		int x = lastMovedCrate.getX();
		int y = lastMovedCrate.getY();
		boolean[] booleanContainer = new boolean[1];
		booleanContainer[0] = false;
		boolean xAxis = false, yAxis = false;
		ArrayList<Crate> testedCrates = new ArrayList<Crate>();
		testedCrates.add(lastMovedCrate);
		if (
				x == 0 
				|| (hasCrateAt(x - 1, y) && frozenDeadlockDetector(getCrateAt(x - 1, y), booleanContainer, testedCrates, 0))
				|| getComponentAt(x - 1, y).getName().equals("Wall")
				|| x == getWidth() - 1
				|| (hasCrateAt(x + 1, y) && frozenDeadlockDetector(getCrateAt(x + 1, y), booleanContainer, testedCrates, 0))
				|| getComponentAt(x + 1, y).getName().equals("Wall")
				)
			xAxis = true;
		if (
				y == 0 
				|| (hasCrateAt(x, y - 1) && frozenDeadlockDetector(getCrateAt(x, y - 1), booleanContainer, testedCrates, 1))
				|| getComponentAt(x, y - 1).getName().equals("Wall")
				|| y == getHeight() - 1
				|| (hasCrateAt(x, y + 1) && frozenDeadlockDetector(getCrateAt(x, y + 1), booleanContainer, testedCrates, 1))
				|| getComponentAt(x, y + 1).getName().equals("Wall")
				)
			yAxis = true;
		if (xAxis && yAxis && lastMovedCrate.getName().equals("Crate"))
			booleanContainer[0] = true;
		return booleanContainer[0];
	}
	
	private boolean frozenDeadlockDetector(Crate crate, boolean[] booleanContainer, ArrayList<Crate> testedCrates, int frozenAxis) {
		int x = crate.getX();
		int y = crate.getY();
		boolean xAxis = false, yAxis = false;
		testedCrates.add(crate);
		if (
				frozenAxis == 0
				| x == 0 
				| (hasCrateAt(x - 1, y) && (testedCrates.contains(getCrateAt(x - 1, y)) || frozenDeadlockDetector(getCrateAt(x - 1, y), booleanContainer, testedCrates, 0)))
				| getComponentAt(x - 1, y).getName().equals("Wall")
				| x == getWidth() - 1
				| (hasCrateAt(x + 1, y) && (testedCrates.contains(getCrateAt(x + 1, y)) || frozenDeadlockDetector(getCrateAt(x + 1, y), booleanContainer, testedCrates, 0)))
				| getComponentAt(x + 1, y).getName().equals("Wall")
				)
			xAxis = true;
		if (
				frozenAxis == 1
				| y == 0 
				| (hasCrateAt(x, y - 1) && (testedCrates.contains(getCrateAt(x, y - 1)) || frozenDeadlockDetector(getCrateAt(x, y - 1), booleanContainer, testedCrates, 1)))
				| getComponentAt(x, y - 1).getName().equals("Wall")
				| y == getHeight() - 1
				| (hasCrateAt(x, y + 1) && (testedCrates.contains(getCrateAt(x, y + 1)) || frozenDeadlockDetector(getCrateAt(x, y + 1), booleanContainer, testedCrates, 1)))
				| getComponentAt(x, y + 1).getName().equals("Wall")
				)
			yAxis = true;
		if (xAxis && yAxis) {
			if (crate.getName().equals("Crate"))
				booleanContainer[0] = true;
			return true;
		}
		return false;
	}
}
