
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class GridReader {
	
	/**
	 * Constructor is private to prevent instantiations.
	 */
	private GridReader() {
		
	}
	
	/**
	 * Saves a grid to a .xsb file at specified path. IF that file already exists, it is overwritten
	 * @param grid The grid to save
	 * @param path The path to save the grid to (without the ".xsb" extension)
	 */
	public static void saveGrid(Grid grid, String path){
		File file = new File(path + ".xsb");
		int px = grid.getPlayer().getX();
		int py = grid.getPlayer().getY();
		BufferedWriter buff = null;
		try {
			buff = new BufferedWriter(new FileWriter(file));
			for (int j = 0; j < grid.getHeight(); j++) {
				for (int i = 0; i < grid.getWidth(); i++) {
					Component component = grid.getComponentAt(i, j);
					if (i == px && j == py) {
						switch (component.getName()) {
						case "Goal" :
							buff.write('+');
							break;
						default :
							buff.write('@');
						}
					} else {
						switch (component.getName()) {
						case "Wall" :
							buff.write('#');
							break;
						case "Crate" :
							buff.write('$');
							break;
						case "CrateOnGoal" :
							buff.write('*');
							break;
						case "Goal" :
							buff.write('.');
							break;
						case "Ground" :
							buff.write(' ');
							break;
						case "Blank" :
							buff.write(' ');
							break;
						}
					}
				}
				buff.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (buff != null) {
				try {
					buff.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Saves a grid and its move list.
	 * @param grid The grid to save
	 * @param saveName The name of the save file
	 * @throws IOException
	 */
    public static void saveGridState(Grid grid, String saveName) throws IOException{
    	grid.getTracker().saveMov("../saves/" + saveName + ".mov");
        saveGrid(grid, "../saves/"+saveName);
    }
	
    public static Grid loadGame(String path) throws IOException {
    	Grid grid = readGrid(path + ".xsb");
    	grid.getTracker().readMov(path + ".mov");
    	return grid;
    }
    
	public static Grid readGrid (String path) throws IOException {
		if (!path.endsWith(".xsb"))
			throw new IOException();
		Grid grid = null;
		BufferedReader buff = null;
		try {
			buff = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
			String ligne;
			int height = 1, width;
			width = buff.readLine().length();
			while ((ligne = buff.readLine())!=null){
				if (ligne.length() > width)
					width = ligne.length();
				height++;
			}
			grid = new Grid(width, height);
			buff.close();
			
			buff = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
			Character character;
			for (int i = 0; i < height; i++){
				int j;
				ligne = buff.readLine();
				for (j = 0; j < ligne.length(); j++) {
					character = ligne.charAt(j);
					
					switch (character) {
					case ('#') :
						grid.placeComponentAt(j, i, new Wall());
						break;
					case ('$'):
						grid.placeComponentAt(j, i, new Ground());
						grid.addCrate(j, i);
						break;
					case(' ') :
						grid.placeComponentAt(j, i, new Ground());
						break;
					case('.') :
						grid.placeComponentAt(j, i, new Goal());
						break;
					case ('@'):
						grid.placeComponentAt(j, i, new Ground());
						grid.setPlayerCoordinates(j, i);
						break;
					case ('+'):
						grid.placeComponentAt(j, i, new Goal());
						grid.setPlayerCoordinates(j, i);
						break;
					case ('*'):
						grid.placeComponentAt(j, i, new Goal());
						grid.addCrate(j, i);
						break;
					}
				}
				while(j < width) {
					grid.placeComponentAt(j, i, new Ground());
					j++;
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
		GridReader.placeBlanks(grid);
		return grid;
	}
	
	public void applyMovesToGrid(String pathIn, String pathOut, Game game, MovementTracker tracker) throws IOException {
		Grid grid = GridReader.readGrid(pathIn);
		Player player = grid.getPlayer();
		for (int i = 0; i < tracker.getMoves().size(); i++) {
			switch(tracker.getMoves().get(i)){
			case('u'):
			case('U'):
				player.setDirection(Direction.UP);
				break;
			case('r'):
			case('R'):
				player.setDirection(Direction.RIGHT);
				break;
			case('d'):
			case('D'):
				player.setDirection(Direction.DOWN);
				break;
			case('l'):
			case('L'):
				player.setDirection(Direction.LEFT);
				break;
			default :
				throw new IOException();
			}
			if (player.canMove())
				player.move();
			else 
				throw new IOException("Grid et machin pas valide");
		}
		GridReader.saveGrid(grid, pathOut);
	}
	
	/**
	 * Replaces all Ground components in a grid which can't be accessed by the player with Blank components.
	 * @param grid The grid to replace inaccessible Ground components in
	 */
	private static void placeBlanks(Grid grid) {
		boolean[][] isFlooded = new boolean[grid.getWidth()][grid.getHeight()];
		findGrounds(grid, isFlooded, grid.getPlayer().getX(), grid.getPlayer().getY());
		Component blank = new Blank();
		for (int i = 0; i < grid.getWidth(); i++) {
			for (int j = 0; j < grid.getHeight(); j++)
				if (grid.getComponentAt(i, j).getName().equals("Ground") && !isFlooded[i][j])
					grid.placeComponentAt(i, j, blank);
		}
	}
	
	/**
	 * Finds all the Ground components in a specified grid directly accessible by a Player from a specified starting location.
	 * All found grounds will then be marked true in a matrix of booleans provided by the placeBlanks() function.
	 * @param grid The grid to find the Ground components in
	 * @param isFlooded A matrix of boolean to keep track of found Ground components, which will always be provided by the placeBlanks() function.
	 * @param x The x-coordinate of the starting position
	 * @param y The y-coordinate of the starting position
	 * @see placeBlanks()
	 */
	private static void findGrounds(Grid grid, boolean[][] isFlooded, int x, int y) {
		if (isFlooded[x][y] == false && !grid.getComponentAt(x, y).getName().equals("Wall")){
			isFlooded[x][y] = true;
			if (x > 0)
				GridReader.findGrounds(grid, isFlooded, x-1, y);
			if (x < grid.getWidth()-1)
				GridReader.findGrounds(grid, isFlooded, x+1, y);
			if (y > 0)
				GridReader.findGrounds(grid, isFlooded, x, y-1);
			if (y < grid.getHeight()-1)
				GridReader.findGrounds(grid, isFlooded, x, y+1);
		}
	}
}
