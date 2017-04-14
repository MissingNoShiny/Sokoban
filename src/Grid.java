
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

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
	 * The array containing the Crates of the level.
	 */
	private ArrayList <Crate> crates; 
	
	//private ArrayList <Goal> goals; 
	
	//Attention, quand player est private, tout foire
	Player player;
	
	private MovementTracker tracker;
	
	private final static int patternSize = 5;
	
	/**
	 * Creates an object containing an empty matrix of specified width and height.
	 * @param width The width of the matrix
	 * @param height The height of the matrix
	 */
	//Peut etre ajouter instanciation du player dans le constructeur
	public Grid(int width, int height) {
		matrix = new Component[height][width];
		crates = new ArrayList<Crate>(0);
		this.width = width;
		this.height = height;
	}

	public Grid(int width, int height, boolean trackable) {
		this(width, height);
		if (trackable)
			tracker = new MovementTracker(this);
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
	
	public MovementTracker getMovementTracker() {
		return tracker;
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
		for (int i = 0; i < crates.size(); i++) {
			comp = getComponentAt(crates.get(i).getX(), crates.get(i).getY()).getSpriteName();
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
		crates.add(new Crate(x, y, this));
	}
	
	/*
	public void addGoal(int x, int y) {
		goals.add(new Goal());
	}
	*/
	
	public boolean hasCrateAt (int x, int y) {
		String nameComponent = getComponentAt(x, y).getSpriteName();
		return (nameComponent == "Crate" || nameComponent == "CrateOnGoal");
	}
	
	//Il faudrait faire en sorte d'obliger à utiliser hasCrateAt avant ceci
	public Crate getCrateAt(int x, int y) {
		return (Crate) getComponentAt(x, y);
	}
	
	public void fill(Component component) {
		int i, j;
		for (j = 0; j < width; j++) {
			for (i = 0; i < height; i++)
				matrix[i][j] = component;
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
						switch (component.getSpriteName()) {
						case "Goal" :
							buff.write('+');
							break;
						default :
							buff.write('@');
						}
					} else {
						switch (component.getSpriteName()) {
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
			grid = new Grid(width, height, true);
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
		return grid;
	}
	
	private static Component[][] returnPattern(int numberPattern){
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
	
	public static void turnPattern(Component[][] pattern) {
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
					pattern = returnPattern(rand.nextInt(17));
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
	
	private static boolean validateRoom (Grid grid) {
		return true;
	}
	
	public static Grid generateGrid(int width, int height) {
		Grid grid;
		do {
			grid = generateRoom(width, height);
		}while(!validateRoom(grid));
		return grid;
	}
}
