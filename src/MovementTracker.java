import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MovementTracker {

	/**
	 * The ArrayList which stores the moves made on a grid.
	 */
	private ArrayList<Character> moves;
	
	/**
	 * The grid to track the moves of.
	 */
	private Grid grid;
	
	/**
	 * The number of moves contained in the ArrayList moves.
	 */
	private int movesCount;
	
	/**
	 * The number of pushed contained in the ArrayList moves.
	 */
	private int pushesCount;
	
	/**
	 * 
	 * @param grid The grid to track the moves of.
	 */
	public MovementTracker(Grid grid) {
		moves = new ArrayList<Character>();
		this.grid = grid;
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
	public void addMove() {
		Direction d = grid.getPlayer().getDirection();
		switch(d) {
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
		movesCount += 1;
	}
	
	/**
	 * Saves the last push made by the player, based on his direction after said move.
	 */
	public void addPush() {
		Direction d = grid.getPlayer().getDirection();
		switch(d) {
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
		movesCount += 1;
		pushesCount += 1;
	}
	
	/**
	 * Undoes the last move/push of the grid.
	 */
	public void undo() {
		Player player = grid.getPlayer();
		if (moves.isEmpty())
			return;
		Character c = moves.get(moves.size()-1);
		moves.remove(moves.size()-1);
		movesCount--;
		switch(c){
		
		case('u'):
			player.moveBackDown(grid);
			break;
		case('d'):
			player.moveBackUp(grid);
			break;
		case('r'):
			player.moveBackLeft(grid);
			break;
		case('l'):
			player.moveBackRight(grid);
			break;
		case('U'):
			player.pullCrateDown(grid);
			pushesCount--;
			break;	
		case('D'):
			player.pullCrateUp(grid);
			pushesCount--;
			break;	
		case('R'):
			player.pullCrateLeft(grid);
			pushesCount--;
			break;	
		case('L'):
			player.pullCrateRight(grid);
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
	}
	
	public void reset() {
		while (!moves.isEmpty())
			undo();
	}
	
	/**
	 * Saves the current moves ArrayList to a .mov file at specified path.
	 * @param path The path to save to (must end with ".mov")
	 * @throws IOException
	 */
	public void saveMov(String path) throws IOException {
		if (!path.endsWith(".mov"))
			throw new IOException("path must end with \".mov\"");
		if (moves.isEmpty())
			throw new IOException();
		File file = new File(path);
		BufferedWriter buff = null;
		try {
			buff = new BufferedWriter(new FileWriter(file));
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
	
	public void readMov(String path) throws IOException {
		if (!path.endsWith(".mov"))
			throw new IOException();
		BufferedReader buff = null;
		try {
			buff = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
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
	 * Retourne une notion de distance tenant compte de la complexite
	 * Concretement, on ne compte pas comme un mouvement un deplacement quand la direction est inchangee par
	 * rapport au mouvement precedent.
	 * @return 
	 */
	public int getDistanceTraveled() {
		if (moves.size() > 0)
			return 0;
		int distance = 0;
		for (int i = 0; i < moves.size(); i++) {

		}
		return distance;
	}
	
	
	public void applyToGrid(String pathIn, String pathOut, Game game) throws IOException {
		Grid grid = Grid.readGrid(pathIn);
		Player player = grid.getPlayer();
		for (int i = 0; i < moves.size(); i++) {
			switch(moves.get(i)){
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
			if (player.canMove(grid, true))
				player.move(grid);
			else 
				throw new IOException("Grid et machin pas valide");
		}
		grid.saveGrid(pathOut, game);
	}
	
	
	public String toString() {
		return moves.toString();
	}
}
