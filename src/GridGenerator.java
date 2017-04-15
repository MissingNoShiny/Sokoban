
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

public class GridGenerator {
	
	private final static int patternSize = 5;
	
	public static Grid generateGrid(int width, int height, int numberCrates) {
		Grid grid;
		do {
			grid = generateRoom(width, height);
		}while(!validateRoom(grid, numberCrates));
		placeGoals(grid, numberCrates);
		placePlayer(grid);
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
						tab[i][j] = new Ground();
						break;
					case '#':
						tab[i][j] = new Wall();
						break;
					case 'B':
						tab[i][j] = new Blank();
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
			if (i >= 0 && y-1 >= 0 && i < grid.getWidth() && y-1 < grid.getHeight()){
				if (! pattern[i-x+1][0].getSpriteName().equals("Blank")){
					if (!grid.getComponentAt(i, y-1).getSpriteName().equals(pattern[i-x+1][0]))
						return false;
				}
			}
		}
		for (int j = y; j < y+4; j++){
			if (x-1 >= 0 && j >= 0 && x-1 < grid.getWidth() && j < grid.getHeight()){
				if (! pattern[0][0].getSpriteName().equals("Blank")){
					if (!grid.getComponentAt(x-1, j).getSpriteName().equals(pattern[0][j-y+1]))
						return false;
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
		int numberRotations;
		Component[][] pattern = new Component[patternSize][patternSize];
		Random rand = new Random();
		for (int i = 0; i <= grid.getWidth(); i+=3){
			for (int j = 0; j <= grid.getHeight(); j+=3){
				do {
					pattern = getPattern(rand.nextInt(17));
					numberRotations = 0;
					while (!canPlaceArround(grid, i, j, pattern) && numberRotations < 4){
						turnPattern(pattern);
						numberRotations++;
					}
				}while (!canPlaceArround(grid, i, j, pattern));
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
	
	/*
	 * Il serait surement intéressant de fusionner cette methode avec celle d'au dessus.
	 */
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
}
