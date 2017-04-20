
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Description
 * @author Vincent Larcin, Joachim Sneessens
 */
public class Grid {
	
	/**
	 * The matrix containing the data of a level.
	 */
	Component[][] matrix;
	
	/**
	 * The height of the matrix.
	 */
	private int height;
	
	/**
	 * The width of the matrix.
	 */
	private int width;
	
	/**
	 * The array containing the list of the crates of the level.
	 */
	private ArrayList <Crate> crateList; 
	
	//private ArrayList <Goal> goals; 
	
	private Player player;
	
	/**
	 * Creates an object containing an empty matrix of specified width and height.
	 * @param width The width of the matrix
	 * @param height The height of the matrix
	 */
	//Peut etre ajouter instanciation du player dans le constructeur
	public Grid(int width, int height) {
		matrix = new Component[height][width];
		crateList = new ArrayList<Crate>(0);
		this.width = width;
		this.height = height;
	}

	/**
	 * Gets the height of the matrix.
	 * @return The height of the matrix
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * Gets the width of the matrix
	 * @return The width of the matrix
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * Gets the content of the matrix at specified position.
	 * @param x The X-coordinate of the cell to get data from
	 * @param y The y-coordinate of the cell to get data from
	 * @return The data contained in specified cell
	 */
	public Component getComponentAt(int x, int y) {
		return matrix[y][x];
	}
	
	/**
	 * Place a new Component at specified position in the matrix
	 * @param x The X-coordinate of the cell to set a new Component
	 * @param y The Y-coordinate of the cell to set a new Component
	 * @param comp  The Component to set at the specified cell
	 */
	public void placeComponentAt(int x, int y, Component comp) {
		matrix[y][x] = comp;
	}
	
	public boolean isWon(){
		boolean test = true;
		String comp;
		for (int i = 0; i < crateList.size(); i++) {
			comp = getComponentAt(crateList.get(i).getX(), crateList.get(i).getY()).getName();
			if  (! comp.equals("CrateOnGoal")) {
				test= false;
			}
		}
		return test;
	}
	
	public void setPlayer (int x, int y) {
		player = new Player (x, y);
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public void addCrate(int x, int y) {
		crateList.add(new Crate(x, y, this));
	}
	

	public boolean hasCrateAt (int x, int y) {
		String nameComponent = getComponentAt(x, y).getName();
		return (nameComponent == "Crate" || nameComponent == "CrateOnGoal");
	}
	
	//Il faudrait faire en sorte d'obliger à utiliser hasCrateAt avant ceci
	public Crate getCrateAt(int x, int y) {
		return (Crate) getComponentAt(x, y);
	}
	
	public ArrayList<Crate> getCrateList() {
		return crateList;
	}
	
	/*
	 * Attention, le fill ne remplit pas tous le tableau avec des instances differentes d'une même classe, mais avec
	 * la même objet a chaque fois. Toutes les cases pointent vers le même objet.
	 */
	public void fill(Component component) {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++)
				matrix[j][i] = component;
		}
	}
	
	public int countAdjacentComponent(String name, int x, int y) {
		int count = 0;
		if (x+1 < width && matrix[x+1][y].getName().equals(name))
			count++;
		if (x-1 >= 0 && matrix[x-1][y].getName().equals(name))
			count++;
		if (y+1 < height && matrix[x][y+1].getName().equals(name))
			count++;
		if (y-1 >= 0 && matrix[x][y-1].getName().equals(name))
			count++;
		return count;
	}

	public void placeBlanks() {
		boolean[][] isFlooded = new boolean[height][width];
		findGrounds(isFlooded, player.getX(), player.getY());
		Component blank = new Blank();
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++)
				if (matrix[j][i].getName().equals("Ground") && !isFlooded[j][i])
					matrix[j][i] = blank;
		}
	}
	
	private void findGrounds(boolean[][] isFlooded, int x, int y) {
		if (isFlooded[y][x] == false && !matrix[y][x].getName().equals("Wall")){
			isFlooded[y][x] = true;
			if (x > 0)
				findGrounds(isFlooded, x-1, y);
			if (x < width-1)
				findGrounds(isFlooded, x+1, y);
			if (y > 0)
				findGrounds(isFlooded, x, y-1);
			if (y < height-1)
				findGrounds(isFlooded, x, y+1);
		}
	}


	public void saveGrid(String path, Game game) {
		File file = new File("..\\levels\\saved\\" + path + ".xsb");
		if (file.exists()) {
			if (!game.canOverrideLevel())
				return;
		}
		int px = this.getPlayer().getX();
		int py = this.getPlayer().getY();
		BufferedWriter buff = null;
		try {
			buff = new BufferedWriter(new FileWriter(file));
			for (int j = 0; j < this.getHeight(); j++) {
				for (int i = 0; i < this.getWidth(); i++) {
					Component component = this.getComponentAt(i, j);
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
	//Je veux dire qu'on sauvegarde une partie en cours. Rien à voir avec l'objet game.
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
		grid.placeBlanks();
		return grid;
	}
}
