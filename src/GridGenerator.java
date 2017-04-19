
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
	
	public static Grid generateGrid(int width, int height, int numberCrates) {
		Grid grid;
		do {
			grid = generateRoom(width, height);
		}while(!validateRoom(grid, numberCrates));
		placeGoals(grid, numberCrates);
		placePlayer(grid);
		movePlayer(grid, 100);
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
	
	private static boolean canPlaceArround(Grid grid, int x, int y, Component[][] pattern) {
		for (int i = x-1; i < x+4; i++){
			for (int j = y-1; j < y+4; j++){
				if (i >= 0 && j >= 0 && i < grid.getWidth() && j < grid.getHeight()){
					if (! pattern[i-x+1][j-y+1].getSpriteName().equals("Blank")){
						if (! grid.getComponentAt(i, j).getSpriteName().equals("Blank")){
							if (! grid.getComponentAt(i, j).getSpriteName().equals(pattern[i-x+1][j-y+1].getSpriteName()))
								return false;
						}
					}
				}
			}
		}
		return true;
	}
	
	private static void placeArround(Grid grid, int x, int y, Component[][] pattern){
		for (int i = x-1; i < x+4; i++){
			for (int j = y-1; j < y+4; j++){
				if (i >= 0 && j >= 0 && i < grid.getWidth() && j < grid.getHeight()){
					if (! pattern[i-x+1][j-y+1].getSpriteName().equals("Blank"))
						grid.placeComponentAt(i, j, pattern[i-x+1][j-y+1]);
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
	
	
	private static Grid generateRoom(int width, int height) {
		Grid grid = new Grid(width, height, true);
		grid.fill(new Blank());
		int numberRotations;
		Component[][] pattern = new Component[patternSize][patternSize];
		Random rand = new Random();
		for (int i = 0; i < grid.getWidth(); i+=3){
			for (int j = 0; j < grid.getHeight(); j+=3){
				do {
					pattern = getPattern(rand.nextInt(17));
					numberRotations = 0;
					while (! canPlaceArround(grid, i, j, pattern) && numberRotations < 4){
						turnPattern(pattern);
						numberRotations++;
					}
				}while (! canPlaceArround(grid, i, j, pattern));
				placeArround(grid, i, j, pattern);
			}
		}
		return grid;
	}
	
	/**
	 * Je compte tous les "Ground", et je compare par rapport au nombre de "Ground" qu'on peut atteindre a
	 * partir du premier "ground" trouve en passant uniquement par des Ground.
	 * @param grid
	 * @param numberCrates
	 * @return
	 */
	private static boolean validateRoom (Grid grid, int numberCrates) {
		int numberGrounds, numberAdjacentGrounds;
		numberGrounds = countAllGrounds(grid);
		numberAdjacentGrounds = countAdjacentGrounds(grid);
		if (numberGrounds < numberCrates+3 || numberGrounds != numberAdjacentGrounds)
			return false;
		return true;
	}
	
	
	private static int countAllGrounds (Grid grid) {
		int count = 0;
		for (int i = 0; i < grid.getWidth(); i++){
			for (int j = 0; j < grid.getHeight(); j++){
				if (grid.getComponentAt(i, j).getSpriteName().equals("Ground"))
					count++;
			}
		}
		return count;
	}
	
	
	private static int countAdjacentGrounds (Grid grid) {
		int count;
		boolean[][] mat = new boolean[grid.getWidth()][grid.getHeight()];
		for (int i = 0; i < grid.getWidth(); i++){
			for (int j = 0; j < grid.getHeight(); j++){
				if (grid.getComponentAt(i, j).getSpriteName().equals("Ground")){
					count = countAdjacentGrounds(grid, mat, i, j);
					return count;	
				}
			}
		}
		return 0;
	}
	
	private static int countAdjacentGrounds (Grid grid, boolean[][] mat, int x, int y) {
		int count = 0;
		if (mat[x][y] == false && grid.getComponentAt(x, y).getSpriteName().equals("Ground")){
			count += 1;
			mat[x][y] = true;
			if (x > 0)
				count += countAdjacentGrounds(grid, mat, x-1, y);
			if (x < grid.getWidth()-1)
				count += countAdjacentGrounds(grid, mat, x+1, y);
			if (y > 0)
				count += countAdjacentGrounds(grid, mat, x, y-1);
			if (y < grid.getHeight()-1)
				count += countAdjacentGrounds(grid, mat, x, y+1);
		}
		return count;
	}
	
	private static void placeGoals(Grid grid, int number) {
		Random rand = new Random();
		int x, y, count = number;
		while (count > 0){
			x = rand.nextInt(grid.getWidth());
			y = rand.nextInt(grid.getHeight());
			if (grid.getComponentAt(x, y).getSpriteName().equals("Ground")) {
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
			if (grid.getComponentAt(x, y).getSpriteName().equals("Ground")) {
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
			}
			*/
			if (tab[player.getX()][player.getY()]!=0)
				goToPosition(grid, tab, grid.getCrateList().get(i));
			else
				System.out.println("erreur");
			System.out.println("ici");
			player.setDirection(getDirectionToPullCrate(grid, crate));
			if (player.canMove(grid, false))
				player.pullCrate(grid);
			/*intRandom = rand.nextInt(4);
			newDirection = directions[intRandom];
			grid.getPlayer().setDirection(directions[intRandom]);
			*/
		}
	}
	
	
	private static void goToPosition(Grid grid, int[][] tab, Position position) {
		while (tab[grid.getPlayer().getX()][grid.getPlayer().getY()]!=2) {
			int x = grid.getPlayer().getX();
			int y = grid.getPlayer().getY();
			if (y-1 >= 0 && tab[x][y-1]==tab[x][y]-1)
				grid.getPlayer().move(grid, Direction.UP);
			else if (x+1 < grid.getWidth() && tab[x+1][y]==tab[x][y]-1)
				grid.getPlayer().move(grid, Direction.RIGHT);
			else if (y+1 < grid.getHeight() && tab[x][y+1]==tab[x][y]-1)
				grid.getPlayer().move(grid, Direction.DOWN);
			else if (x-1 >= 0 && tab[x-1][y]==tab[x][y]-1)
				grid.getPlayer().move(grid, Direction.LEFT);
			
		}
	}
	
	private static void fillLee(Grid grid, int[][] tab, int xSource, int ySource, int xGoal, int yGoal) {
		int ind = 0;
		ArrayList<Point> list = new ArrayList<Point>(0);
		list.add(new Point(xSource, ySource));
		while(list.size()>0){
			ind++;
			int size = list.size();
			for (int i = 0; i < size; i++){
				int x = (int)list.get(i).getX();
				int y = (int)list.get(i).getY();
				tab[x][y] = ind;
				if (x==xGoal && y == yGoal)
					return;
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
			for(int i = 0; i<size; i++){
				list.remove(0);
			}
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
