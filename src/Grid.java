
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
	
	public class Wall extends Component {
		
	}
	
	public class Ground extends Component {
		
	}
	
	public class Goal extends Component {
		
	}
	
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
	
	/**
	 * Creates an object containing an empty matrix of specified width and height.
	 * @param width The width of the matrix
	 * @param height The height of the matrix
	 */
	//Peut etre ajouter instanciation du player dans le constructeur
	public Grid(int width, int height) {
		matrix = new Component[height][width];
		crates = new ArrayList<Crate>(0);
		//goals = new ArrayList<Goal>(0);
		this.height = height;
		this.width = width;
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
		for (int i = 0; i < crates.size(); i++) {
			comp = getComponentAt(crates.get(i).getX(), crates.get(i).getY()).getNameSprite();
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
		String nameComponent = getComponentAt(x, y).getNameSprite();
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
	
	
	public static Grid readGrid (String path) {
		Grid grid = null;
		BufferedReader buff = null;
		try {
			buff = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
			String ligne;
			int height = 1, width;
			width = buff.readLine().length();
			while ((ligne = buff.readLine())!=null){
				height++;
			}
			grid = new Grid(width, height);
			buff.close();
			
			buff = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
			Character character;
			System.out.println(width);
			for (int i = 0; i < height; i++){
				ligne = buff.readLine();
				for (int j = 0; j < width; j++) {
					character = ligne.charAt(j);
					switch (character) {
					case ('#') :
						grid.placeComponentAt(j, i, grid.new Wall());
						break;
					case ('$'):
						grid.addCrate(j, i);
						break;
					case(' ') :
						grid.placeComponentAt(j, i, grid.new Ground());
						break;
					case('.') :
						//grid.addGoal(j, i);
						grid.placeComponentAt(j, i, grid.new Goal());
						break;
					case ('@'):
						grid.placeComponentAt(j, i, grid.new Ground());
						grid.setPlayer(j, i);
						break;
					case ('+'):
						grid.placeComponentAt(j, i, grid.new Goal());					
						grid.setPlayer(j, i);
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
		return grid;
	}
	
	public static void saveGrid(Grid grid, String path, Game game) {
		File file = new File("..\\levels\\saved\\" + path + ".xsb");
		if (file.exists()) {
			if (!game.canOverrideLevel())
				return;
		}
		BufferedWriter buff = null;
		try {
			buff = new BufferedWriter(new FileWriter(file));
			for (int j = 0; j < grid.getHeight(); j++) {
				for (int i = 0; i < grid.getWidth(); i++) {
					//Trouver le symbole approprié puis l'écrire dans le fichier
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
}
