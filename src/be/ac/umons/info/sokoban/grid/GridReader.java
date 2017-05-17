package be.ac.umons.info.sokoban.grid;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * A class used to manage grid inputs and outputs.
 * @author Vincent Larcin, Joachim Sneessens
 */
public final class GridReader {
	
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
	public static void saveGrid(Grid grid, String name, boolean isLevel) {
		String path = name + ".xsb";
		if (isLevel)
			path = "saves/" + path;
		File file = new File(path);
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
	
	public static void saveGrid(Grid grid, String name) {
		saveGrid(grid, name, true);
	}
	
	public static boolean isLevelAlreadyWon(String path) {
		path += ".txt";
		File file = new File(path);
		if (! file.exists()) 
			return false;
		BufferedReader buff = null;
		String isDone = null;
		try {
			buff = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
			isDone = buff.readLine();
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
		if (isDone.equals("true")) {
			return true;
		}
		return false;
	}
	
	public static void saveVictory(String path, int moves, int pushes) {
		File file = new File(path + ".txt");
		BufferedWriter buff = null;
		try {
			buff = new BufferedWriter(new FileWriter(file));
			buff.write("true");
			buff.newLine();
			buff.write(moves);
			buff.newLine();
			buff.write(pushes);
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
	 * 
	 * @param path
	 * @param isCampaignLevel
	 * @return
	 * @throws FileNotFoundException
	 * @throws InvalidFileException
	 */
    public static Grid loadGame(String path, boolean isCampaignLevel) throws FileNotFoundException, InvalidFileException {
    	Grid grid = readGrid(path + ".xsb");
    	try {
        	if (isCampaignLevel) {
        		String levelName = path.substring(path.lastIndexOf("/"));
        		String parentPath = path.substring(0, path.lastIndexOf("/"));
        		applyMovesToGrid(grid, parentPath + "/saved" + levelName + ".mov", false);
        	}
        	else
        		applyMovesToGrid(grid, path + ".mov", false);
    	} catch (FileNotFoundException e) {
    		
    	}
    	return grid;
    }
    
    /**
     * 
     * @param path
     * @return
     * @throws FileNotFoundException
     * @throws InvalidFileException
     */
	private static Grid readGrid (String path) throws FileNotFoundException, InvalidFileException {
		File file = new File(path);
		if (! file.exists())
			throw new FileNotFoundException("File not found");
		Grid grid = null;
		BufferedReader buff = null;
		try {
			buff = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
			String ligne;
			int height = 0, width = 0;
			while ((ligne = buff.readLine())!=null){
				if (ligne.length() > width)
					width = ligne.length();
				height++;
			}
			grid = new Grid(width, height);
			buff.close();
			
			if (height < 2 || width < 2)
				throw new InvalidFileException("File represent a too little level.");
			
			buff = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
			char character;
			int playersAmount = 0;
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
						playersAmount++;
						break;
					case ('+'):
						grid.placeComponentAt(j, i, new Goal());
						grid.setPlayerCoordinates(j, i);
						playersAmount++;
						break;
					case ('*'):
						grid.placeComponentAt(j, i, new Goal());
						grid.addCrate(j, i);
						break;
					default:
						throw new InvalidFileException("File contain illegal character.");
					}
				}
				while(j < width) {
					grid.placeComponentAt(j, i, new Ground());
					j++;
				}
			}
			if (playersAmount != 1)
				throw new InvalidFileException("Level must have one player.");
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
	
	/**
	 * 
	 * @param grid
	 * @param path
	 * @throws FileNotFoundException
	 * @throws InvalidFileException
	 */
	private static void applyMovesToGrid(Grid grid, String path, boolean acceptImpossibleMoves) throws FileNotFoundException, InvalidFileException {
		grid.getTracker().readMov(path);
		Player player = grid.getPlayer();
		for (int i = 0; i < grid.getTracker().getMoves().size(); i++) {
			switch(grid.getTracker().getMoves().get(i)){
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
				throw new InvalidFileException("The specified file contain illegal character");
			}
			if (player.canMove())
				player.move(false);
			else if (!acceptImpossibleMoves)
				throw new InvalidFileException("Corrupted file");
		}
	}
	
	/**
	 * Applies the specified moves to the specified grid, and saves the resulting grid.
	 * @param gridInputPath The path of the input .xsb file containing the grid
	 * @param movInputPath The path of the input .mov file containing the moves
	 * @throws IOException If any of the paths are invalid
	 * @throws InvalidFileException
	 */
	public static void applyMovesToGrid(String gridInputPath, String movInputPath) throws IOException, InvalidFileException {
		Grid grid = GridReader.readGrid(gridInputPath);
		String gridName = gridInputPath.split("[.]")[0];
		String gridOutputPath = gridName + "_output";
		applyMovesToGrid(grid, movInputPath, true);
		GridReader.saveGrid(grid, gridOutputPath, false);
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
