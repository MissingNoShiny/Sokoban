package be.ac.umons.info.sokoban.grid;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * A class used to track the moves of a player.
 * @author Vincent Larcin, Joachim Sneessens
 */
public class MovementTracker {

	/**
	 * The ArrayList which stores the moves made on a grid.
	 */
	private ArrayList<Character> moves;
	
	/**
	 * The grid to track the moves of.
	 */
	private Player player;
	
	/**
	 * The number of moves contained in the ArrayList moves.
	 */
	private int movesCount;
	
	/**
	 * The number of pushed contained in the ArrayList moves.
	 */
	private int pushesCount;
	
	/**
	 * A boolean value used to determine if the player has moved.
	 * @see hasMoved()
	 */
	private boolean hasMoved = true;
	
	/**
	 * Creates a MovementTracker that tracks the moves of a specified player.
	 * @param player The grid to track the moves of
	 */
	MovementTracker(Player player) {
		moves = new ArrayList<Character>();
		this.player = player;
	}

	/**
	 * Gets the current list of moves.
	 * @return The list of moves
	 */
	public ArrayList<Character> getMoves() {
		return moves;
	}
	
	/**
	 * Gets the amount of moves in the ArrayList moves.
	 * @return the amount of moves in the ArrayList moves
	 */
	public int getMovesCount() {
		return movesCount;
	}
	
	/**
	 * Gets the amount of pushed in the ArrayList moves.
	 * @return the amount of pushes in the ArrayList moves
	 */
	public int getPushesCount() {
		return pushesCount;
	}
	
	/**
	 * Saves the last move made by the player, based on his direction after said move.
	 */
	void addMove(Direction dir) {
		switch(dir) {
		case UP:
			moves.add('u');
			break;
		case DOWN:
			moves.add('d');
			break;
		case RIGHT:
			moves.add('r');
			break;
		case LEFT:
			moves.add('l');
			break;
		}
		hasMoved = true;
		movesCount += 1;
	}
	
	/**
	 * Saves the last push made by the player, based on his direction after said move.
	 */
	void addPush(Direction dir) {
		switch(dir) {
		case UP:
			moves.add('U');
			break;
		case DOWN:
			moves.add('D');
			break;
		case RIGHT:
			moves.add('R');
			break;
		case LEFT:
			moves.add('L');
			break;
		}
		hasMoved = true;
		movesCount += 1;
		pushesCount += 1;
	}
	
	/**
	 * Undoes the last move/push of the grid.
	 * @param grid
	 */
	public void undo() {
		if (moves.isEmpty())
			return;
		Character c = moves.get(moves.size()-1);
		moves.remove(moves.size()-1);
		movesCount--;
		switch(c){
		
		case('u'):
			player.move(Direction.DOWN, false);
			break;
		case('d'):
			player.move(Direction.UP, false);
			break;
		case('r'):
			player.move(Direction.LEFT, false);
			break;
		case('l'):
			player.move(Direction.RIGHT, false);
			break;
		case('U'):
			player.pullCrateDown();
			pushesCount--;
			break;	
		case('D'):
			player.pullCrateUp();
			pushesCount--;
			break;	
		case('R'):
			player.pullCrateLeft();
			pushesCount--;
			break;	
		case('L'):
			player.pullCrateRight();
			pushesCount--;
			break;	
		}
		if (moves.isEmpty()) {
			player.setDirection(Direction.DOWN);
		} else {
			c = moves.get(moves.size()-1);
			switch(c) {
			case 'u':
			case 'U':
				player.setDirection(Direction.UP);
				break;
			case 'd':
			case 'D':
				player.setDirection(Direction.DOWN);
				break;
			case 'r':
			case 'R':
				player.setDirection(Direction.RIGHT);
				break;
			case 'l':
			case 'L':
				player.setDirection(Direction.LEFT);
				break;
			}
		}
		hasMoved = true;
	}
	
	/**
	 * Undoes all moves.
	 */
	public void reset() {
		while (!moves.isEmpty())
			undo();
	}
	
	/**
	 * Saves the current moves ArrayList to a .mov file at specified path.
	 * @param path The path to save to (must end with ".mov")
	 * @throws IOException if the path is invalid.
	 */
	public void saveMov(String path) {
		if (!path.endsWith(".mov"))
			throw new IllegalArgumentException("path must end with \".mov\"");
		File file = new File(path);
		BufferedWriter buff = null;
		try {
			buff = new BufferedWriter(new FileWriter(file));
			for (int i = 0; i < moves.size(); i++)
				buff.write(moves.get(i));
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
	 * Reads a .mov file and adds its data to the moves ArrayList.
	 * @param path The path of the file
	 * @throws IOException if the path is invalid, the file not readable or if the file contains illegal characters
	 * @throws InvalidFileException 
	 */
	public void readMov(String path) throws FileNotFoundException, InvalidFileException {
		if (!path.endsWith(".mov"))
			throw new IllegalArgumentException("path must end with \".mov\"");
		
		File file = new File(path);
		if (!file.exists()) 
			throw new FileNotFoundException("File not found");
		
		BufferedReader buff = null;
		try {
			buff = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
			String moveList = buff.readLine();
			int length;
			try {
				length = moveList.length();
			} catch (NullPointerException e) {
				length = 0;
			}
			for (int i = 0; i < length; i++) {
                if (!"lurdLURD".contains(Character.toString(moveList.charAt(i))))
                	throw new InvalidFileException("Specified file contains illegal characters");
				moves.add(moveList.charAt(i));
				if (!Character.isLowerCase(moveList.charAt(i)))
					pushesCount++;
				movesCount++;
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
	 * Checks if the player has moved since the last execution of this function.
	 * @return true if the player has moved, false else
	 */
	public boolean hasMoved() {
		if (hasMoved == false)
			return false;
		else 
			hasMoved = false;
		return true;
	}
	
	/**
	 * Retourne une notion de distance tenant compte de la complexite
	 * Concretement, on ne compte pas comme un mouvement un deplacement quand la direction est inchangee par
	 * rapport au mouvement precedent.
	 * @return 
	 */
	public int getDistanceTraveled() {
		if (moves.size() == 0)
			return 0;
		int count = 1;
		char oldDir = Character.toLowerCase(moves.get(0));
		for (int i = 1; i < moves.size(); i++) {
			char newDir = Character.toLowerCase(moves.get(i));
			if (newDir != oldDir)
				count++;
			oldDir = newDir;
		}
		return count;
	}
	
	/**
	 * Resets the MovementTracker.
	 */
	void empty() {
		moves.clear();
		pushesCount = 0;
		movesCount = 0;
	}
}
