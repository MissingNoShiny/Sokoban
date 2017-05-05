
import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public final class GridGenerator {
	
	private static class InvalidDispositionException extends Exception {

		private static final long serialVersionUID = 1L;
		
	}
	
	/**
	 * 
	 */
	private GridGenerator() {
		
	}
	
	/**
	 * patternSize must be an impair int
	 */
	private final static int patternSize = 5;
	
	/**
	 * 
	 * @param width
	 * @param height
	 * @param numberCrates
	 * @param difficulty
	 * @return
	 * @throws IllegalArgumentException
	 */
	public static Grid generateGrid(int width, int height, int numberCrates, int difficulty) throws IllegalArgumentException{
		if (width < 6 || height < 6 || width > 30 || height > 30 || numberCrates < 2 || numberCrates >= ((width-2)*(height-2))/4 || difficulty > 20)
			throw new IllegalArgumentException();
		
		Boolean validGoalsDisposition;
		Grid grid;
		final int seuilMaxIterations = 10;
		
		do {
			int numberIterations = 0;
			validGoalsDisposition = false;
			do {
				grid = generateRoom(width, height);
			}while(!isGroundConnected(grid, numberCrates));
			System.out.println("Room generee");
			while (!validGoalsDisposition && numberIterations < seuilMaxIterations) {
				validGoalsDisposition = true;
				try {
				placeGoals(grid, numberCrates);
				placePlayer(grid);
				movePlayer(grid, difficulty*10);
				}catch (InvalidDispositionException e) {
					System.out.println("exception catch");
					validGoalsDisposition = false;
				}
				numberIterations++;
			}			
		} while(!validGoalsDisposition);
		System.out.println("Room generee et tout place");
		removeUselessWall(grid);
		//grid.getTracker().empty();
		return grid;
	}
	
	/**
	 * Generate an empty room 
	 * @param width
	 * @param height
	 * @return
	 */
	private static Grid generateRoom(int width, int height) {
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
						turnPattern(pattern);
				}while (! canplaceHere(grid, i, j, pattern));
				placeHere(grid, i, j, pattern);
			}
		}
		removeDeadEnds(grid);
		surroundGridWithWalls(grid);
		return grid;
	}
	
	private static Component[][] getPattern(int numberPattern){
		Component[][] tab = new Component[patternSize][patternSize];
		BufferedReader buff = null;
		try {
			buff = new BufferedReader(new InputStreamReader(new FileInputStream("../patterns/pattern"+ Integer.toString(numberPattern) + ".txt")));
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
	

	private static boolean canplaceHere(Grid grid, int x, int y, Component[][] pattern) {
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
	

	private static void placeHere(Grid grid, int x, int y, Component[][] pattern){
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
	
	private static void turnPattern(Component[][] pattern) {
		Component[][] tmp = new Component[patternSize][patternSize];
		for (int i = 0; i < patternSize; i++){
			for (int j = 0; j < patternSize; j++)
				tmp[i][j] = pattern[patternSize-1-j][i];
		}
	}
	
	/**
	 * Je compte tous les "Ground", et je compare par rapport au nombre de "Ground" qu'on peut atteindre a
	 * partir du premier "ground" trouve en passant uniquement par des Ground.
	 * @param grid
	 * @param numberCrates
	 * @return
	 */
	private static boolean isGroundConnected (Grid grid, int numberCrates) {
		int numberGrounds, numberAdjacentGrounds;
		numberGrounds = countAllGrounds(grid);
		numberAdjacentGrounds = countAccessibleGrounds(grid);
		if (numberGrounds < numberCrates+3 || numberGrounds != numberAdjacentGrounds)
			return false;
		return true;
	}
	
	
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
	 * 
	 * @param grid
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
								turnPattern(pattern);
						}while (! canplaceHere(grid, xCenterPattern, yCenterPattern, pattern));
						placeHere(grid, xCenterPattern, yCenterPattern, pattern);
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
	 * If the player exist not yet, the method create one. In the other case, the coordinates of him are just changed.
	 * @param grid
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
	
	private static void movePlayer(Grid grid, int numberMoves) throws InvalidDispositionException {
		Random rand = new Random();
		Crate crate = null;
		int i = 0, indexPrecCrate = -1, oldDirection = -1, newDirection;
		Player player = grid.getPlayer();
		int[][] tab = new int[grid.getWidth()][grid.getHeight()];
		ArrayList<Crate> crateList = grid.getCrateList();
		Direction[] directions = {Direction.UP, Direction.RIGHT, Direction.DOWN, Direction.LEFT};
		while (grid.getTracker().getDistanceTraveled()<numberMoves) {
			do {
				i = rand.nextInt(crateList.size());
			} while (i == indexPrecCrate);
			int numberIterations = 0;
			boolean hasFoundPossibleCrate = false;
			while (!hasFoundPossibleCrate && numberIterations <= crateList.size()) {
				crate = crateList.get(i);
				for (int j = 0; j < 4; j++) {
					hasFoundPossibleCrate = isPossiblePullCrate(grid, tab, crate, directions[j]);
					if (hasFoundPossibleCrate) {
						oldDirection = j;
						break;
					}
				}
				if (!hasFoundPossibleCrate) {
					i = (i+1)%crateList.size();
					if (i == indexPrecCrate)
						i = (i+1)%crateList.size();
					numberIterations++;
				}
			} 
			if (hasFoundPossibleCrate) {
				indexPrecCrate = i%crateList.size();
				goToSource(grid, tab);
				if (grid.getTracker().getDistanceTraveled()<numberMoves) {
					player.pullCrate(directions[oldDirection], true);
					do {
						newDirection = rand.nextInt(4);
					} while (newDirection == (oldDirection+2)%4);
					while (isPossiblePullCrate(grid, tab, crate, directions[newDirection])) {
						goToSource(grid, tab);
						player.pullCrate(directions[newDirection], true);
						oldDirection = newDirection;
						do {
							newDirection = rand.nextInt(4);
						} while (newDirection == (oldDirection+2)%4);
					}
				}
			}
			else {
				grid.getTracker().empty();
				throw new InvalidDispositionException();
			}
		}
		//player.setDirection(Direction.DOWN);
	}
	
	
	private static void goToSource(Grid grid, int[][] tab) {
		while (tab[grid.getPlayer().getX()][grid.getPlayer().getY()]!=1) {
			int x = grid.getPlayer().getX();
			int y = grid.getPlayer().getY();
			if (y-1 >= 0 && tab[x][y-1]==tab[x][y]-1)
				grid.getPlayer().move(Direction.UP, true);
			else if (x+1 < grid.getWidth() && tab[x+1][y]==tab[x][y]-1)
				grid.getPlayer().move(Direction.RIGHT, true);
			else if (y+1 < grid.getHeight() && tab[x][y+1]==tab[x][y]-1)
				grid.getPlayer().move(Direction.DOWN, true);
			else if (x-1 >= 0 && tab[x-1][y]==tab[x][y]-1)
				grid.getPlayer().move(Direction.LEFT, true);
		}
	}
	
	private static void fillLee(Grid grid, int[][] tab, int xSource, int ySource, int xGoal, int yGoal) {
		boolean goalIsReach = false;
		int ind = 0, deb = 0;
		ArrayList<Point> list = new ArrayList<Point>(0);
		list.add(new Point(xSource, ySource));
		while(!goalIsReach && deb!=list.size() && ind < 30){
			ind++;
			int size = list.size();
			for (int i = deb; i < size; i++){
				int x = (int)list.get(i).getX();
				int y = (int)list.get(i).getY();
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
	 * Attention cette fonction a des effets de bord ET retourne qqch
	 * @param grid
	 * @param tab
	 * @param crate
	 * @param direction
	 * @return
	 */
	private static boolean isPossiblePullCrate(Grid grid, int[][] tab, Crate crate, Direction direction) {
		cleanTab(tab, grid.getWidth(), grid.getHeight());
		if (crate.canBePulled(direction)){
			int x = crate.getX();
			int y = crate.getY();
			switch(direction){
			case UP:
				y--;
				break;
			case RIGHT:
				x++;
				break;
			case DOWN:
				y++;
				break;
			case LEFT:
				x--;
			}
			fillLee(grid, tab, x, y, grid.getPlayer().getX(), grid.getPlayer().getY());
			if (tab[grid.getPlayer().getX()][grid.getPlayer().getY()]!=0)
				return true;
		}
		return false;
	}
	
	private static void cleanTab(int[][] tab, int width, int height) {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++)
				tab[i][j]=0;
		}
	}
}
