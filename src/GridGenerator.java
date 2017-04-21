
import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public final class GridGenerator {
	
	private GridGenerator() {
		
	}
	
	private final static int patternSize = 5;
	//patternSize doit etre un impair
	
	public static Grid generateGrid(int width, int height, int numberCrates) {
		Grid grid;
		do {
			grid = generateRoom(width, height);
		}while(!isGroundConnected(grid, numberCrates));
		placeGoals(grid, numberCrates);
		placePlayer(grid);
		removeUselessWall(grid);
		movePlayer(grid, 100);
		//grid.getTracker().empty();
		return grid;
	}
	
	
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
		discardDeadEnds(grid);
		//removeBigRectangleGround(grid);
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
	
	private static void discardDeadEnds(Grid grid) {
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
		flood(grid, mat, grid.getPlayer().getX(), grid.getPlayer().getY());
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
	
	private static void flood(Grid grid, boolean[][] mat, int x, int y) {
		if (mat[x][y] == false) {
			mat[x][y] = true;
			if (!grid.getComponentAt(x, y).getName().equals("Wall")) {
				if (x > 0)
					flood(grid, mat, x-1, y);
				if (x+1 < grid.getWidth())
					flood(grid, mat, x+1, y);
				if (y > 0)
					flood(grid, mat, x, y-1);
				if (y+1 < grid.getHeight())
					flood(grid, mat, x, y+1);
			}
		}
	}
	
	private static void placeGoals(Grid grid, int number) {
		Random rand = new Random();
		int x, y, count = number;
		while (count > 0){
			x = rand.nextInt(grid.getWidth());
			y = rand.nextInt(grid.getHeight());
			if (grid.getComponentAt(x, y).getName().equals("Ground")) {
				count--;
				grid.placeComponentAt(x, y, new Goal());
				grid.addCrate(x, y);
			}
		}
	}
	
	
	private static void placePlayer(Grid grid) {
		Random rand = new Random();
		boolean test = false;
		int x, y;
		while (!test){
			x = rand.nextInt(grid.getWidth());
			y = rand.nextInt(grid.getHeight());
			if (grid.getComponentAt(x, y).getName().equals("Ground")) {
				grid.setPlayer(x, y);
				test = true;
			}
		}
	}
	
	private static void movePlayer(Grid grid, int numberMoves) {
		Random rand = new Random();
		Crate crate;
		Player player = grid.getPlayer();
		int intRandom;
		Direction newDirection;
		int[][] tab = new int[grid.getWidth()][grid.getHeight()];
		ArrayList<Crate> crateList = grid.getCrateList();
		//Pour ne pas devoir faire un switch
		Direction[] directions = {Direction.UP, Direction.RIGHT, Direction.DOWN, Direction.LEFT};
		for (int i = 0; i < 1; i++) {
			crate = crateList.get(i);
			fillLee(grid, tab, crate.getX(), crate.getY(), player.getX(), player.getY());	
			/*
			while (tab[player.getX()][player.getY()]!=0); {
				System.out.println("erreur");
				//Trouver moyen de changer de caisse
				fillLee(grid, tab, crate.getX(), crate.getY(), player.getX(), player.getY());	
			}
			*/
			if (tab[player.getX()][player.getY()]!=0)
				goToPosition(grid, tab, grid.getCrateList().get(i));
			else
				System.out.println("erreur");
			player.setDirection(getDirectionToPullCrate(grid, crate));
			if (player.canMove(false))
				player.pullCrate();
			/*intRandom = rand.nextInt(4);
			newDirection = directions[intRandom];
			grid.getPlayer().setDirection(directions[intRandom]);
			*/
			player.setDirection(Direction.DOWN);
		}
	}
	
	
	private static void goToPosition(Grid grid, int[][] tab, Position position) {
		while (tab[grid.getPlayer().getX()][grid.getPlayer().getY()]!=2) {
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
		while(!goalIsReach && deb!=list.size()){
			ind++;
			int size = list.size();
			for (int i = deb; i < size; i++){
				int x = (int)list.get(i).getX();
				int y = (int)list.get(i).getY();
				tab[x][y] = ind;
				if (x==xGoal && y == yGoal) {
					goalIsReach = true;
					break;
				}
				if (y-1>=0 && tab[x][y-1] == 0)
					if (grid.getComponentAt(x, y-1).canGoTrough())
						list.add(new Point(x, y-1));
					else
						tab[x][y-1] = -1;
				if (x+1<grid.getWidth() && tab[x+1][y] == 0)
					if (grid.getComponentAt(x+1, y).canGoTrough())
						list.add(new Point(x+1, y));
					else
						tab[x+1][y] = -1;
				if (y+1<grid.getHeight() && tab[x][y+1]==0)
					if (grid.getComponentAt(x, y+1).canGoTrough())
						list.add(new Point(x, y+1));
					else
						tab[x][y+1] = -1;
				if (x-1>=0 && tab[x-1][y] == 0)
					if (grid.getComponentAt(x-1, y).canGoTrough())
						list.add(new Point(x-1, y));
					else
						tab[x-1][y] = -1;
			}
			deb=size;
		}
	}
	
	private static Direction getDirectionToPullCrate(Grid grid, Crate crate) {
		int x = grid.getPlayer().getX();
		int y = grid.getPlayer().getY();
		int crateX = crate.getX();
		int crateY = crate.getY();
		if (y-1 >= 0 && x == crateX && y-1 == crateY)
			return Direction.DOWN;
		if (x+1 < grid.getWidth() && x+1 == crateX && y == crateY)
			return Direction.LEFT;
		if (y+1 < grid.getHeight() && x == crateX && y+1 == crateY)
			return Direction.UP;
		if (x-1 >= 0 && x-1 == crateX && y == crateY)
			return Direction.RIGHT;
		//Il faudra mettre le return Direction.Right dans le else, mais attendons que ca fonctionne
		else{
			System.out.println("Ca foire");
			return null;
		}

	}
}
