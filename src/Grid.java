
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
	 * @return The Component contained in specified cell
	 */
	public Component getComponentAt(int x, int y) {
		return matrix[x][y];
	}
	
	/**
	 * Place a new Component at specified position in the matrix
	 * @param x The X-coordinate of the cell to set a new Component
	 * @param y The Y-coordinate of the cell to set a new Component
	 * @param comp  The Component to set at the specified cell
	 */
	public void placeComponentAt(int x, int y, Component comp) {
		matrix[x][y] = comp;
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
		player.setX(x);
		player.setY(y);
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
				matrix[i][j] = component;
		}
	}
	
	public boolean frozenDeadLockDetector(Crate lastMovedCrate) {
		int x = lastMovedCrate.getX();
		int y = lastMovedCrate.getY();
		boolean[] booleanContainer = new boolean[1];
		booleanContainer[0] = false;
		boolean xAxis = false, yAxis = false;
		ArrayList<Crate> testedCrates = new ArrayList<Crate>();
		testedCrates.add(lastMovedCrate);
		if (
				x == 0 
				|| (hasCrateAt(x - 1, y) && frozenDeadLockDetector(getCrateAt(x - 1, y), booleanContainer, 0))
				|| getComponentAt(x - 1, y).getName().equals("Wall")
				|| x == getWidth() - 1
				|| (hasCrateAt(x + 1, y) && frozenDeadLockDetector(getCrateAt(x + 1, y), booleanContainer, 0))
				|| getComponentAt(x + 1, y).getName().equals("Wall")
				)
			xAxis = true;
		if (
				y == 0 
				|| (hasCrateAt(x, y - 1) && frozenDeadLockDetector(getCrateAt(x, y - 1), booleanContainer, 1))
				|| getComponentAt(x, y - 1).getName().equals("Wall")
				|| y == getHeight() - 1
				|| (hasCrateAt(x, y + 1) && frozenDeadLockDetector(getCrateAt(x, y + 1), booleanContainer, 1))
				|| getComponentAt(x, y + 1).getName().equals("Wall")
				)
			yAxis = true;
		if (xAxis && yAxis && lastMovedCrate.getName().equals("Crate"))
			booleanContainer[0] = true;
		return booleanContainer[0];
	}
	
	private boolean frozenDeadLockDetector(Crate crate, boolean[] booleanContainer, int frozenAxis) {
		int x = crate.getX();
		int y = crate.getY();
		boolean xAxis = false, yAxis = false;
		ArrayList<Crate> testedCrates = new ArrayList<Crate>();
		testedCrates.add(crate);
		if (
				frozenAxis == 0
				|| x == 0 
				|| (hasCrateAt(x - 1, y) && !testedCrates.contains(getCrateAt(x - 1, y)) && frozenDeadLockDetector(getCrateAt(x - 1, y), booleanContainer, 0))
				|| getComponentAt(x - 1, y).getName().equals("Wall")
				|| x == getWidth() - 1
				|| (hasCrateAt(x + 1, y) && !testedCrates.contains(getCrateAt(x + 1, y)) && frozenDeadLockDetector(getCrateAt(x + 1, y), booleanContainer, 0))
				|| getComponentAt(x + 1, y).getName().equals("Wall")
				)
			xAxis = true;
		if (
				frozenAxis == 1
				|| y == 0 
				|| (hasCrateAt(x, y - 1) && !testedCrates.contains(getCrateAt(x, y - 1)) && frozenDeadLockDetector(getCrateAt(x, y - 1), booleanContainer, 1))
				|| getComponentAt(x, y - 1).getName().equals("Wall")
				|| y == getHeight() - 1
				|| (hasCrateAt(x, y + 1) && !testedCrates.contains(getCrateAt(x, y + 1)) && frozenDeadLockDetector(getCrateAt(x, y + 1), booleanContainer, 1))
				|| getComponentAt(x, y + 1).getName().equals("Wall")
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
