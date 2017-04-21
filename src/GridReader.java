import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class GridReader {
	
	private GridReader() {
		
	}
	
	public static void saveGrid(Grid grid, String path, Game game) {
		File file = new File("..\\levels\\saved\\" + path + ".xsb");
		if (file.exists()) {
			if (!game.canOverrideLevel())
				return;
		}
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
	
	/*
	//Je veux dire qu'on sauvegarde une partie en cours. Rien � voir avec l'objet game.
	public static void saveGame(Grid grid, String path, MovementTracker tracker) {
		
	}
	*/
	
	public static Grid readGrid (String path) throws IOException {
		int crateCount = 0, goalCount = 0;
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
						crateCount++;
						break;
					case(' ') :
						grid.placeComponentAt(j, i, new Ground());
						break;
					case('.') :
						grid.placeComponentAt(j, i, new Goal());
						goalCount++;
						break;
					case ('@'):
						grid.placeComponentAt(j, i, new Ground());
						grid.setPlayer(j, i);
						break;
					case ('+'):
						grid.placeComponentAt(j, i, new Goal());
						goalCount++;
						grid.setPlayer(j, i);
						break;
					case ('*'):
						grid.placeComponentAt(j, i, new Goal());
						goalCount++;
						grid.addCrate(j, i);
						crateCount++;
						break;
					}
				}
				while(j < width) {
					grid.placeComponentAt(j, i, new Ground());
					j++;
				}
				if (goalCount != crateCount) 
					throw new IOException();
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
			if (player.canMove(true))
				player.move();
			else 
				throw new IOException("Grid et machin pas valide");
		}
		GridReader.saveGrid(grid, pathOut, game);
	}
	
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
