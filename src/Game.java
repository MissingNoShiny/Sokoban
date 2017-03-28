
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;


public class Game implements Runnable {
	
	public static enum GameState {
		MENU,
		PLAYING;
	}
	
	private GameState state = GameState.MENU;
	
	public static final String TITLE = "Sokoban";
	
	public static final Color BLEU_CLAIR = new Color(135, 206, 250);
	public static final Color ORANGE = new Color(255, 165, 0);
	public static final Color BLACK = new Color(0, 0, 0);

	private boolean running = false;
	private Menu menu = new Menu(this);
	private Thread thread;
	private Grid grid;
	private Display window;
	private DisplayLevel level;
	
	//Plutot que de deplacer player depuis le grid, creer un nouvelle classe (avec un string et un int comme attributs)
	//et faire en sorte que grid retourne les points acquis a a la fin d'un niveau. A chaque nouveau niveau, nouveau grid
	
	public static void main(String[] args) {
		
		Game game = new Game();
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		game.window = new Display(screenSize, Game.TITLE);
		game.loadMenu();
		
		game.start();
			
	}
	
	public GameState getState() {
		return state;
	}
	
	/**
	 * The game loop.
	 */
	@Override
	public void run() {
		while (running) {
			window.refresh();
		}
	}
	
	/**
	 * Starts the game loop.
	 */
	public synchronized void start() {
		if (running)
			return;
		thread = new Thread(this);
		thread.start();
		running = true;
	}
	
	/**
	 * Stops the game loop and closes the game.
	 */
	public synchronized void stop() {
		if (!running)
			return;
		try {
			window.dispose();
			running = false;
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 
		System.exit(1);
	}
	
	/**
	 * Loads the level of specified path.
	 * @param path The path of the level to load (must end with ".xsb")
	 * @throws IOException If the path is incorrect or doesn't exist
	 */
	public void loadLevel(String path) throws IOException {
		grid = Grid.readGrid(path);
		level = new DisplayLevel(grid, this);
		window.setPanel(level);
		level.requestFocusInWindow();
		state = GameState.PLAYING;
	}
	
	/**
	 * Loads the menu
	 */
	public void loadMenu() {
		window.setPanel(menu);
		state = GameState.MENU;
	}

	public boolean canOverrideLevel() {
		//methode qui servira a afficher un avertissement si la methode Grid.saveGrid() ecrase un fichier existant
		return true;
	}
}
