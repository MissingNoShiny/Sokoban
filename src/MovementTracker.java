import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MovementTracker {

	private ArrayList<Character> moves;
	
	public MovementTracker() {
		moves = new ArrayList<Character>();
	}

	public Character getOppositeMove(Character c) {
		Character c2;
		switch (c) {
		case('u'):
			c2 = new Character('d');
			return c2;
		case('d'):
			c2 = new Character('u');
			return c2;
		case('l'):
			c2 = new Character('r');
			return c2;
		case('r'):
			c2 = new Character('l');
			return c2;
		case('U'):
			c2 = new Character('D');
			return c2;
		case('D'):
			c2 = new Character('U');
			return c2;
		case('L'):
			c2 = new Character('R');
			return c2;
		case('R'):
			c2 = new Character('L');
			return c2;
		}
		return null;
	}
	
	public void saveMov(String path) {
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
	
	public void applyToGrid(String pathIn, String pathOut, Game game) throws IOException {
		Grid grid = Grid.readGrid(pathIn);
		//changements à exécuter
		grid.saveGrid(pathOut, game);
	}
}
