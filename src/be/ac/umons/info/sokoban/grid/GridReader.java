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
	public static void saveGrid(Grid grid, String name) {
		String path = name + ".xsb";
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
	
	/**
	 * Gets the index of the last unlocked level of the campaign.
	 * @return The index of the last unlocked level
	 */
	public static int getMaxIndexLevel() {
		File file = new File("levels/saved/maxIndexLevel.txt");
		if (!file.exists()) 
			return 0;
		int max = 0;
		BufferedReader buff = null;
		try {
			buff = new BufferedReader(new InputStreamReader(new FileInputStream("levels/saved/maxIndexLevel.txt")));
			max = buff.read();
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
		return max;
	}
	
	/**
	 * Saves the best scores of a level and update campaign progress if necessary.
	 * @param path The path of the file to save the scores to
	 * @param levelIndex The index of the level
	 * @param moves The moves score
	 * @param pushes The pushes score
	 */
	public static void saveVictory(String path, int levelIndex, int moves, int pushes) {
		BufferedWriter buff = null;
		int bestMoves = moves + 1, bestPushes = pushes + 1;
		int[] bestScores = getBestScores(path);
		if (bestScores != null) {
			bestMoves = bestScores[0];
			bestPushes = bestScores[1];
		}
		
		if ((bestMoves > moves) || (bestPushes > pushes)) {
			try {
				buff = new BufferedWriter(new FileWriter(path));
				if (bestMoves > moves)
					buff.write(moves);
				else
					buff.write(bestMoves);
				buff.newLine();
				if (bestPushes > pushes)
					buff.write(pushes);
				else
					buff.write(bestPushes);
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

		
		// if there is a progress in the campaign
		if (levelIndex > getMaxIndexLevel()) {
			try {
				buff = new BufferedWriter(new FileWriter("levels/saved/maxIndexLevel.txt"));
				buff.write(levelIndex);
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
	}
	
	
	/**
	 * Gets the content of a score file.
	 * @param path The path of the file
	 * @return An array containing the best scores if the file exist, null else
	 */
	public static int[] getBestScores(String path) {
		File file = new File(path);
		if (!file.exists()) 
			return null;
		int moves = 0 , pushes = 0;
		BufferedReader buff = null;
		try {
			buff = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
			moves = buff.read();
			buff.readLine();
			pushes = buff.read();
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
		int[] bestScores = {moves, pushes};
		return bestScores;
	}
	
	/**
	 * Loads a .xsb file and its associated .mov file if it exists.
	 * @param path The path of the filesto load (without the .xsb or .mov extension)
	 * @param isCampaignLevel true if the level if part of the campaign, false else
	 * @return The loaded grid
	 * @throws FileNotFoundException if the path is invalid
	 * @throws InvalidFileException if the file is invalid
	 */
    public static Grid loadLevel(String path, boolean isCampaignLevel) throws FileNotFoundException, InvalidFileException {
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
     * Reads a .xsb file and translates its content into a grid
     * @param path The path of the file to read
     * @return The grid contained in the file
     * @throws FileNotFoundException if the path is invalid
     * @throws InvalidFileException if the file is invalid
     */
	private static Grid readGrid(String path) throws FileNotFoundException, InvalidFileException {
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
	 * Applies the moves in a .mov file to a specified grid.
	 * @param grid The grid to apply the moves to
	 * @param path The path of the .mov file
	 * @param acceptImpossibleMoves true if attempting invalid moves is allowed, false else
	 * @throws FileNotFoundException if the path is invalid
	 * @throws InvalidFileException if the .mov file is invalid
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
	public static void applyMovesToGrid(String gridInputPath, String movInputPath, String gridOutputPath) throws IOException, InvalidFileException {
		Grid grid = GridReader.readGrid(gridInputPath);
		applyMovesToGrid(grid, movInputPath, true);
		GridReader.saveGrid(grid, gridOutputPath);
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
