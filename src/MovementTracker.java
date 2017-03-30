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
	 * 
	 * @param grid The grid to track the moves of.
	 */
	public MovementTracker(Grid grid) {
		moves = new ArrayList<Character>();
		this.grid = grid;
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
	}
	
	/**
	 * Undoes the last move/push of the grid.
	 */
	public void undo() {
		Player player = grid.getPlayer();
		if (moves.isEmpty())
			return;
		Crate crate;
		Character c = moves.get(moves.size()-1);
		moves.remove(moves.size()-1);
		switch(c){
		case('u'):
			player.setY(player.getY()+1);
			break;	
		case('d'):
			player.setY(player.getY()-1);
			break;	
		case('r'):
			player.setX(player.getX()-1);
			break;	
		case('l'):
			player.setX(player.getX()+1);
			break;	
		case('U'):
			crate = grid.getCrateAt(player.getX(), player.getY()-1);
			player.setY(player.getY()+1);
			crate.move(Direction.DOWN);
			break;	
		case('D'):
			crate = grid.getCrateAt(player.getX(), player.getY()+1);
			player.setY(player.getY()-1);
			crate.move(Direction.UP);
			break;	
		case('R'):
			crate = grid.getCrateAt(player.getX()+1, player.getY());
			player.setX(player.getX()-1);
			crate.move(Direction.LEFT);
			break;	
		case('L'):
			crate = grid.getCrateAt(player.getX()-1, player.getY());
			player.setX(player.getX()+1);
			crate.move(Direction.RIGHT);
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
	
	public static void applyToGrid(String pathIn, String pathOut, Game game) throws IOException {
		Grid grid = Grid.readGrid(pathIn);
		//changements à exécuter
		grid.saveGrid(pathOut, game);
	}
	
	public String toString() {
		return moves.toString();
	}
}
