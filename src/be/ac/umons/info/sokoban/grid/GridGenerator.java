package be.ac.umons.info.sokoban.grid;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

/**
 * A class used to generate random levels.
 * @author Vincent Larcin, Joachim Sneessens
 */
public final class GridGenerator {
	
	private static class InvalidDispositionException extends Exception {
		
		private static final long serialVersionUID = -6319481640977055123L;
		
	}
	
	/**
	 * Constructor is private to prevent instantiations.
	 */
	private GridGenerator() {
		
	}
	
	/**
	 * patternSize The size of the side of a pattern, in cells.
	 */
	private final static int patternSize = 5;
	
	/**
	 * The number of times a player will attempt to pull the same crate.
	 */
	private final static int playerTriesOnSameCrate = 2;
	
	/**
	 * Generates a Grid.
	 * @param width The width of the grid to generate
	 * @param height The height of the grid to generate
	 * @param cratesAmount The amount of crates to place in the grid
	 * @param difficulty An approximation of the difficulty of the grid (the higher this number is, the more moves will be done during generation)
	 * @return The generated Grid
	 * @throws IllegalArgumentException If the parameters are out of set bounds
	 */
	public static Grid generateGrid(int width, int height, int cratesAmount, int difficulty) throws IllegalArgumentException{
		if (width < 6 || height < 6 || width > 25 || height > 25 || cratesAmount < 2 || cratesAmount > ((width-2)*(height-2))/5+2 || difficulty > 20)
			throw new IllegalArgumentException();
		
		Boolean validGoalsDisposition;
		Grid grid;
		final int seuilMaxIterations = 8;
		
		do {
			int numberIterations = 0;
			validGoalsDisposition = false;
			do {
				grid = generateEmptyGrid(width, height);
			}while(!areGroundsConnected(grid));
			while (!validGoalsDisposition && numberIterations < seuilMaxIterations) {
				validGoalsDisposition = true;
				try {
				placeGoals(grid, cratesAmount);
				placePlayer(grid);
				movePlayer(grid, cratesAmount*15*(int)Math.pow((difficulty+5),2/3));
				}catch (InvalidDispositionException e) {
					//System.out.println("disposition invalide");
					validGoalsDisposition = false;
				}
				numberIterations++;
			}			
		} while(!validGoalsDisposition);
		removeUselessWall(grid);
		//if you want to see the moves in the generator, delete this line
		grid.getTracker().empty();
		return grid;
	}
	
	/**
	 * Generates a grid containing only ground and wall Components.
	 * @param width The width of the grid
	 * @param height The height of the grid
	 * @return The generated empty grid
	 */
	private static Grid generateEmptyGrid(int width, int height) {
		Grid grid = new Grid(width, height);
		grid.fill(new Blank());
		int step = patternSize/2+1;
		int numberRotations;
		Component[][] pattern = new Component[patternSize][patternSize];
		Random rand = new Random();
		for (int i = 2; i < grid.getWidth(); i+=step){
			for (int j = 2; j < grid.getHeight(); j+=step){
				do {
					pattern = getPattern(rand.nextInt(17));
					numberRotations = rand.nextInt(4);
					for (int k = 0; k < numberRotations; k++)
						rotatePattern(pattern);
				}while (! canPlacePattern(grid, i, j, pattern));
				placePattern(grid, i, j, pattern);
			}
		}
		removeDeadEnds(grid);
		surroundGridWithWalls(grid);
		return grid;
	}
	
	/**
	 * Gets the pattern of corresponding specified index.
	 * @param numberPattern The index of the pattern
	 * @return The pattern of specified index
	 */
	private static Component[][] getPattern(int numberPattern){
		Component[][] tab = new Component[patternSize][patternSize];
		BufferedReader buff = null;
		try {
			buff = new BufferedReader(new InputStreamReader(new FileInputStream("patterns/pattern"+ Integer.toString(numberPattern) + ".txt")));
			String ligne;
			Character character;
			for (int i = 0; i < patternSize; i++){
				ligne = buff.readLine();
				for (int j = 0; j < patternSize; j++) {
					character = ligne.charAt(j);
					switch(character){
					case ' ':
						tab[j][i] = new Ground();
						break;
					case '#':
						tab[j][i] = new Wall();
						break;
					case 'B':
						tab[j][i] = new Blank();
						break;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (buff !=null) {
				try {
					buff.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
	 	}
		return tab;
	}
	
	/**
	 * Checks if a pattern can be placed in a grid at a specified position.
	 * @param grid The grid to attempt to place the pattern in
	 * @param x The X-coordinate of the position
	 * @param y The Y-coordinate of the position
	 * @param pattern The pattern to place
	 * @return true if the pattern can be placed, false else
	 */
	private static boolean canPlacePattern(Grid grid, int x, int y, Component[][] pattern) {
		int half = patternSize/2;
		for (int i = x-half; i <= x+half; i++){
			for (int j = y-half; j <= y+half; j++){
				if (i >= 0 && j >= 0 && i < grid.getWidth() && j < grid.getHeight()){
					if (! pattern[i-x+half][j-y+half].getName().equals("Blank")){
						if (! grid.getComponentAt(i, j).getName().equals("Blank")){
							if (! grid.getComponentAt(i, j).getName().equals(pattern[i-x+half][j-y+half].getName()))
								return false;
						}
					}
				}
			}
		}
		return true;
	}
	

	/**
	 * Places a pattern in a specified grid at a specified position.
	 * The pattern will be centered around the position.
	 * @param grid The grid to place the pattern in
	 * @param x The X-coordinate of the position
	 * @param y The Y-coordinate of the position
	 * @param pattern The pattern to place
	 */
	private static void placePattern(Grid grid, int x, int y, Component[][] pattern){
		int half = patternSize/2;
		for (int i = x-half; i <= x+half; i++){
			for (int j = y-half; j <= y+half; j++){
				if (i >= 0 && j >= 0 && i < grid.getWidth() && j < grid.getHeight()){
					if (! pattern[i-x+half][j-y+half].getName().equals("Blank"))
						grid.placeComponentAt(i, j, pattern[i-x+half][j-y+half]);
				}
			}
		}
	}
	
	/**
	 * Rotates a pattern 90 degrees to the right.
	 * @param pattern the pattern to rotate
	 */
	private static void rotatePattern(Component[][] pattern) {
		Component[][] tmp = new Component[patternSize][patternSize];
		for (int i = 0; i < patternSize; i++){
			for (int j = 0; j < patternSize; j++)
				tmp[i][j] = pattern[patternSize-1-j][i];
		}
	}
	
	/**
	 * Checks if all ground Components in a grid are connected.
	 * @param grid The grid to check the ground of
	 * @return true if the ground Components are all connected, false else
	 */
	private static boolean areGroundsConnected (Grid grid) {
		int numberGrounds, numberAdjacentGrounds;
		numberGrounds = countAllGrounds(grid);
		numberAdjacentGrounds = countAccessibleGrounds(grid);
		return numberGrounds == numberAdjacentGrounds;
	}
	
	/**
	 * Counts all the ground Components in a grid.
	 * @param grid The grid to count the ground Components of
	 * @return The amount of ground Components the grid contains
	 */
	private static int countAllGrounds (Grid grid) {
		int count = 0;
		for (int i = 0; i < grid.getWidth(); i++){
			for (int j = 0; j < grid.getHeight(); j++){
				if (grid.getComponentAt(i, j).getName().equals("Ground"))
					count++;
			}
		}
		return count;
	}
	
	/**
	 * Finds a ground Component.
	 * Counts all the other ground Components that can be accessed from found ground.
	 * @param grid The grid to count ground Components in
	 * @return The amount of ground Components that can be accessed
	 */
	private static int countAccessibleGrounds (Grid grid) {
		int count;
		boolean[][] mat = new boolean[grid.getWidth()][grid.getHeight()];
		for (int i = 0; i < grid.getWidth(); i++){
			for (int j = 0; j < grid.getHeight(); j++){
				if (grid.getComponentAt(i, j).getName().equals("Ground")){
					count = countAccessibleGrounds(grid, mat, i, j);
					return count;	
				}
			}
		}
		return 0;
	}
	
	/**
	 * Recursively counts all ground Components that can be accessed from a starting position.
	 * @param grid The grid to count ground Components in
	 * @param mat A boolean matrix used to avoid counting the same Component twice
	 * @param x The X-coordinate of the starting position
	 * @param y The Y-coordinate of the starting position
	 * @return The amount of ground Components that can be accessed
	 */
	private static int countAccessibleGrounds (Grid grid, boolean[][] mat, int x, int y) {
		int count = 0;
		if (mat[x][y] == false && grid.getComponentAt(x, y).getName().equals("Ground")){
			count += 1;
			mat[x][y] = true;
			if (x > 0)
				count += countAccessibleGrounds(grid, mat, x-1, y);
			if (x < grid.getWidth()-1)
				count += countAccessibleGrounds(grid, mat, x+1, y);
			if (y > 0)
				count += countAccessibleGrounds(grid, mat, x, y-1);
			if (y < grid.getHeight()-1)
				count += countAccessibleGrounds(grid, mat, x, y+1);
		}
		return count;
	}
	
	/**
	 * Removes all dead ends in a blank grid
	 * @param grid The grid to remove the dead ends of
	 */
	private static void removeDeadEnds(Grid grid) {
		for (int i = 1; i < grid.getWidth()-1; i++){
			for (int j = 1; j < grid.getHeight()-1; j++) {
				if (grid.getComponentAt(i, j).getName().equals("Ground")){
					if (countAdjacentWall(grid, i, j) > 2){
						int xCenterPattern = ((i-1)/3)*3+2;
						int yCenterPattern = ((j-1)/3)*3+2;
						for (int k = xCenterPattern-1; k <= xCenterPattern+1; k++) {
							for (int l = yCenterPattern-1; l <= yCenterPattern+1; l++) {
								if (k > 0 && k < grid.getWidth() && l > 0 && l < grid.getHeight()) 
									grid.placeComponentAt(k, l, new Blank());
							}
						}
						Component[][] pattern;
						Random rand = new Random();
						do {
							pattern = getPattern(rand.nextInt(17));
							int numberRotations = rand.nextInt(4);
							for (int k = 0; k < numberRotations; k++)
								rotatePattern(pattern);
						}while (! canPlacePattern(grid, xCenterPattern, yCenterPattern, pattern));
						placePattern(grid, xCenterPattern, yCenterPattern, pattern);
						if (xCenterPattern > 2)
							i = xCenterPattern - 2;
						else
							i = 1;
						if (yCenterPattern > 2)
							j = yCenterPattern - 2;
						else
							j = 1;
					}
				}
			}
		}
	}
	
	/**
	 * Counts the wall Components surrounding a cell at specified coordinates in a specified grid.
	 * @param grid The grid containing the cell
	 * @param x The x-coordinate of the cell
	 * @param y The y-coordinate of the cell
	 * @return The number of wall Components surrounding the cell
	 */
	private static int countAdjacentWall(Grid grid, int x, int y) {
		int count = 0;
		if (x+1 > grid.getWidth()-2 || grid.getComponentAt(x+1, y).getName().equals("Wall"))
			count++;
		if (x-1 < 1 || grid.getComponentAt(x-1, y).getName().equals("Wall"))
			count++;
		if (y+1 > grid.getHeight()-2 || grid.getComponentAt(x, y+1).getName().equals("Wall"))
			count++;
		if (y-1 < 1 || grid.getComponentAt(x, y-1).getName().equals("Wall"))
			count++;
		return count;
	}
	
	/**
	 * Surrounds specified grid with wall Components.
	 * @param grid The grid to surround with wall Components
	 */
	private static void surroundGridWithWalls(Grid grid) {
		Wall wall = new Wall();
		for (int i = 0; i < grid.getWidth(); i++){
			grid.placeComponentAt(i, 0, wall);
			grid.placeComponentAt(i, grid.getHeight()-1, wall);
		}
		for (int i = 1; i < grid.getHeight()-1; i++){
			grid.placeComponentAt(0, i, wall);
			grid.placeComponentAt(grid.getWidth()-1, i, wall);
		}
	}
	
	/**
	 * Removes the walls which are useless to gameplay in a grid.
	 * @param grid The grid to remove the useless walls of
	 */
	private static void removeUselessWall(Grid grid) {
		boolean[][] mat = new boolean[grid.getWidth()][grid.getHeight()];
		detectUselessWalls(grid, mat, grid.getPlayer().getX(), grid.getPlayer().getY());
		for (int i = 0; i < grid.getWidth(); i++){
			for (int j = 0; j < grid.getHeight(); j++){
				if (grid.getComponentAt(i, j).getName().equals("Wall") && mat[i][j]==false){
					int adjacentBorderWallsCount = 0;
					if (i-1 >= 0 && mat[i-1][j] == true)
						adjacentBorderWallsCount++;
					if (i+1 < grid.getWidth() && mat[i+1][j] == true)
						adjacentBorderWallsCount++;
					if (j-1 >= 0 && mat[i][j-1] == true)
						adjacentBorderWallsCount++;
					if (j+1 < grid.getHeight() && mat[i][j+1] == true)
						adjacentBorderWallsCount++;
					if (adjacentBorderWallsCount < 2)
						grid.placeComponentAt(i, j, new Blank());
				}
			}
		}
	}
	
	/**
	 * Recursively detects the useless walls in a grid by flooding from specified position.
	 * @param grid The grid to detect useless walls in.
	 * @param mat A boolean matrix used to avoid detecting the same Component twice
	 * @param x The X-coordinate of the starting position
	 * @param y The Y-coordinate of the starting position
	 */
	private static void detectUselessWalls(Grid grid, boolean[][] mat, int x, int y) {
		if (mat[x][y] == false) {
			mat[x][y] = true;
			if (!grid.getComponentAt(x, y).getName().equals("Wall")) {
				if (x > 0)
					detectUselessWalls(grid, mat, x-1, y);
				if (x+1 < grid.getWidth())
					detectUselessWalls(grid, mat, x+1, y);
				if (y > 0)
					detectUselessWalls(grid, mat, x, y-1);
				if (y+1 < grid.getHeight())
					detectUselessWalls(grid, mat, x, y+1);
			}
		}
	}
	
	/**
	 * Removes all existing crates and goals from the grid.
	 * Place at random valid locations the goals. Above each goal is placed an crate.
	 * The location is not valid if the cannot be pulled in at least one direction.
	 * @param grid
	 * @param number The number of goals/crates to be placed
	 * @throws InvalidDispositionException If it's impossible placing crates in the specified grid.
	 */
	private static void placeGoals(Grid grid, int number) throws InvalidDispositionException {
		if (!grid.getCrateList().isEmpty()) {
			for (int i = 0; i < grid.getWidth(); i++) {
				for (int j = 0; j < grid.getHeight(); j++) {
					String name = grid.getComponentAt(i, j).getName();
					if (name.equals("Crate") || name.equals("CrateOnGoal") || name.equals("Goal"))
						grid.placeComponentAt(i, j, new Ground());
				}
			}
			grid.getCrateList().clear();
		}
		Random rand = new Random();
		int x, y, nbPlacedCrates = 0, nbIterations = 0;
		while (nbPlacedCrates < number){
			x = rand.nextInt(grid.getWidth());
			y = rand.nextInt(grid.getHeight());
			if (grid.getComponentAt(x, y).getName().equals("Ground")) {
				grid.placeComponentAt(x, y, new Goal());
				grid.addCrate(x, y);
				Crate crate = grid.getCrateAt(x, y);
				if (crate.canBePulled(Direction.UP)||crate.canBePulled(Direction.RIGHT) || crate.canBePulled(Direction.DOWN) || crate.canBePulled(Direction.LEFT)){
					nbPlacedCrates++;
					nbIterations = 0;
				}
				else {
					grid.removeCrate(nbPlacedCrates);
					grid.placeComponentAt(x, y, new Ground());
				}
				nbIterations++;
			}
			if (nbIterations > 500)
				throw new InvalidDispositionException();
		}
	}
	
	/**
	 * Finds a random valid starting location for the player of a grid.
	 * Sets the player coordinates to found location.
	 * @param grid The grid to find a valid location for the player of
	 */
	private static void placePlayer(Grid grid) {
		Random rand = new Random();
		boolean isOnValidPlace = false;
		int x, y;
		while (!isOnValidPlace){
			x = rand.nextInt(grid.getWidth());
			y = rand.nextInt(grid.getHeight());
			if (grid.getComponentAt(x, y).getName().equals("Ground")) {
				grid.setPlayerCoordinates(x, y);
				isOnValidPlace = true;
			}
		}
	}
	
	/**
	 * Moves a player to generate a random starting situation from a finished level.
	 * <p>The player will try to change its Direction a specified number of times, but if it can't
	 * and at least 80% of the Crates have been moved, the number will be considered reached.
	 * @param grid The grid to move the player of
	 * @param numberMoves The number of direction changes the player will try to reach
	 * @throws InvalidDispositionException If the player can't be moved enough in the current situation
	 */
	private static void movePlayer(Grid grid, int numberMoves) throws InvalidDispositionException {
		Random rand = new Random();
		Crate crate = null;
		int i, iDep ,indexPrecCrate = -1, oldDirection = -1, newDirection;
		Player player = grid.getPlayer();
		int[][] tab = new int[grid.getWidth()][grid.getHeight()];
		ArrayList<Crate> crateList = grid.getCrateList();
		boolean[] movedCratesList = new boolean[crateList.size()];
		Direction[] directions = {Direction.UP, Direction.RIGHT, Direction.DOWN, Direction.LEFT};
		while (grid.getTracker().getDirectionChangesCount()<numberMoves) {
			do {
				iDep = rand.nextInt(crateList.size());
			} while (iDep == indexPrecCrate);
			int numberIterations = 0;
			i = iDep;
			boolean hasFoundPossibleCrate = false;
			//Loop on all non-moved crates
			while (!hasFoundPossibleCrate && numberIterations <= (crateList.size()-getMovedCratesAmount(movedCratesList))) {
				if (movedCratesList[i] == false && i != indexPrecCrate) {
					crate = crateList.get(i);
					for (int j = 0; j < 4; j++) {
						hasFoundPossibleCrate = canPlayerPullCrate(grid, tab, crate, directions[j]);
						if (hasFoundPossibleCrate) {
							oldDirection = j;
							break;
						}
					}
				}
				if (!hasFoundPossibleCrate) {
					i = (i+1)%crateList.size();
					numberIterations++;
				}
			} 
			//Loop on all moved crates
			numberIterations = 0;
			i = iDep;
			while (!hasFoundPossibleCrate && numberIterations <= getMovedCratesAmount(movedCratesList)) {
				if (movedCratesList[i] == true && i != indexPrecCrate) {
					crate = crateList.get(i);
					for (int j = 0; j < 4; j++) {
						hasFoundPossibleCrate = canPlayerPullCrate(grid, tab, crate, directions[j]);
						if (hasFoundPossibleCrate) {
							oldDirection = j;
							break;
						}
					}
				}
				if (!hasFoundPossibleCrate) {
					i = (i+1)%crateList.size();
					numberIterations++;
				}
			} 

			if (hasFoundPossibleCrate) {
				indexPrecCrate = i%crateList.size();
				goToSource(grid, tab);
				if (grid.getTracker().getDirectionChangesCount()<numberMoves) {
					movedCratesList[i] = true;
					player.pullCrate(directions[oldDirection]);
					int trialsNumber = 0;
					while (trialsNumber < playerTriesOnSameCrate) {
						do {
							newDirection = rand.nextInt(4);
						} while (newDirection == (oldDirection+2)%4);
						trialsNumber++;
						if (canPlayerPullCrate(grid, tab, crate, directions[newDirection])) {
							goToSource(grid, tab);
							player.pullCrate(directions[newDirection]);
							oldDirection = newDirection;
							trialsNumber = 0;
						}
					}
				}
			}
			else {
				if (getMovedCratesAmount(movedCratesList) > crateList.size()*4/5)
					break;
				else {
					grid.getTracker().empty();
					throw new InvalidDispositionException();
				}
			}
		}		
		if (getMovedCratesAmount(movedCratesList) <= crateList.size()*3/4){
			//System.out.println("pas assez caisses bougees");
			grid.getTracker().empty();
			throw new InvalidDispositionException();
		}
		player.setDirection(Direction.DOWN);
	}
	
	/**
	 * Finds the shortest path from a position to another.
	 * @param grid The grid to find the path in
	 * @param tab An int matrix used to determine the path
	 * @param xSource The X-coordinate of the ending position
	 * @param ySource The Y-coordinate of the ending position
	 * @param xGoal	The X-coordinate of the starting position
	 * @param yGoal The Y-coordinate of the starting position
	 */
	private static void fillLee(Grid grid, int[][] tab, int xSource, int ySource, int xGoal, int yGoal) {
		boolean goalIsReach = false;
		int ind = 0, deb = 0;
		ArrayList<Point> list = new ArrayList<Point>(0);
		list.add(new Point(xSource, ySource));
		while(!goalIsReach && deb!=list.size() && ind < 30){
			ind++;
			int size = list.size();
			for (int i = deb; i < size; i++){
				int x = list.get(i).getX();
				int y = list.get(i).getY();
				tab[x][y] = ind;
				if (x == xGoal && y == yGoal) {
					goalIsReach = true;
					break;
				}
				if (y-1 >= 0 && tab[x][y-1] == 0)
					if (grid.getComponentAt(x, y-1).canBePassedThrough())
						list.add(new Point(x, y-1));
					else
						tab[x][y-1] = -1;
				if (x+1 < grid.getWidth() && tab[x+1][y] == 0)
					if (grid.getComponentAt(x+1, y).canBePassedThrough())
						list.add(new Point(x+1, y));
					else
						tab[x+1][y] = -1;
				if (y+1 < grid.getHeight() && tab[x][y+1] == 0)
					if (grid.getComponentAt(x, y+1).canBePassedThrough())
						list.add(new Point(x, y+1));
					else
						tab[x][y+1] = -1;
				if (x-1 >= 0 && tab[x-1][y] == 0)
					if (grid.getComponentAt(x-1, y).canBePassedThrough())
						list.add(new Point(x-1, y));
					else
						tab[x-1][y] = -1;
			}
			deb=size;
		}
	}
	
	/**
	 * Moves the player of a grid to the source of a filling.
	 * @param grid The grid to move the player in
	 * @param tab A int matrix obtained through filling
	 */
	private static void goToSource(Grid grid, int[][] tab) {
		Player player = grid.getPlayer();
		while (tab[player.getX()][player.getY()]!=1) {
			int x = player.getX();
			int y = player.getY();
			if (y-1 >= 0 && tab[x][y-1]==tab[x][y]-1)
				player.move(Direction.UP, true);
			else if (x+1 < grid.getWidth() && tab[x+1][y]==tab[x][y]-1)
				player.move(Direction.RIGHT, true);
			else if (y+1 < grid.getHeight() && tab[x][y+1]==tab[x][y]-1)
				player.move(Direction.DOWN, true);
			else if (x-1 >= 0 && tab[x-1][y]==tab[x][y]-1)
				player.move(Direction.LEFT, true);
		}
	}
	
	/**
	 * Return true if a crate can be pulled by a player and the player can pull it from where he currently is.
	 * This method have border effects on the tab.
	 * @param grid
	 * @param tab
	 * @param crate
	 * @param direction
	 * @return
	 */
	private static boolean canPlayerPullCrate(Grid grid, int[][] tab, Crate crate, Direction dir) {
		cleanTab(tab);
		if (crate.canBePulled(dir)){
			Point p = Point.getNextPoint(crate, dir);
			int x = p.getX();
			int y = p.getY();
			fillLee(grid, tab, x, y, grid.getPlayer().getX(), grid.getPlayer().getY());
			if (tab[grid.getPlayer().getX()][grid.getPlayer().getY()]!=0)
				return true;
		}
		return false;
	}
	
	/**
	 * Replaces the all elements of the tab by 0
	 * @param tab The matrix to replace all the elements of
	 */
	private static void cleanTab(int[][] tab) {
		int width = tab.length;
		int height = tab[0].length;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++)
				tab[i][j]=0;
		}
	}
	
	/**
	 * Counts the number of occurrences of true in an array of booleans representing if a Crate has moved or not.
	 * @param movedCrates The array of booleans
	 * @return The number of occurrences of true in the array
	 */
	private static int getMovedCratesAmount(boolean[] movedCrates) {
		int movedCratesAmount = 0;
		for (int i = 0; i < movedCrates.length; i++) {
			if (movedCrates[i] == true)
				movedCratesAmount++;
		}
		return movedCratesAmount;
	}
}
