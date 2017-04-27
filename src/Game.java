
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;

import javax.swing.JFrame;


public class Game implements Runnable {
	
	public static enum GameState {
		MENU,
		PLAYING;
	}
	
	private GameState state = GameState.MENU;
	
	public static final String TITLE = "Sokoban";
	
	public static final Color BLEU_CLAIR = new Color(135, 206, 250);
	
	public static final int FPS_CAP = 30;
	
	public static final boolean SHOW_PLAYER_ARROWS = false;

	private boolean running = false;
	private Menu menu = new Menu(this);
	private Thread thread;
	private Grid grid;
	private JFrame window;
	private DisplayLevel level;
	private int fpsTemp = 0;
	
	//Plutot que de deplacer player depuis le grid, creer un nouvelle classe (avec un string et un int comme attributs)
	//et faire en sorte que grid retourne les points acquis a a la fin d'un niveau. A chaque nouveau niveau, nouveau grid
	
	public static void main(String[] args) {
		
		Game game = new Game();
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		game.window = new JFrame(Game.TITLE);
		game.window.setSize((int) screenSize.getWidth(), (int) screenSize.getHeight());
		game.window.setLocationRelativeTo(null);
		game.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.window.setVisible(true);
		game.loadMenu();
		
		game.start();
			
	}
	
	public GameState getState() {
		return state;
	}
	
	public int getFps() {
		return fpsTemp;
	}
	
	/**
	 * The game loop.
	 */
	@Override
	public void run() {
		int fps = 0;
		long timer = System.currentTimeMillis();
		while (running) {
			try {
				Thread.sleep(1000/Game.FPS_CAP);
				window.revalidate();
				window.repaint();
				fps++;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (System.currentTimeMillis() - timer >= 1000) {
				fpsTemp = fps;
				fps = 0;
				timer += 1000;
			}
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
			getWindow().dispose();
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
	public void loadLevel(String path, boolean isVirginLevel) throws IOException { //pas trouve d'autre nom pour le booleen :)
		if (isVirginLevel) 
			grid = GridReader.readGrid(path);
		else
			grid = GridReader.loadGame(path);
		level = new DisplayLevel(grid, this);
		window.setContentPane(level);
		level.displayGrid.requestFocusInWindow();
		state = GameState.PLAYING;
	}
	
	public void generateLevel(int width, int height, int numberCrates, int difficulty) {
		grid = GridGenerator.generateGrid(width, height, numberCrates, difficulty);
		level = new DisplayLevel(grid, this);
		window.setContentPane(level);
		level.displayGrid.requestFocusInWindow();
		state = GameState.PLAYING;
	}
	/**
	 * Loads the menu
	 */
	public void loadMenu() {
		window.setContentPane(menu);
		level = null;
		state = GameState.MENU;
	}

	public boolean canOverrideLevel() {
		//methode qui servira a afficher un avertissement si la methode Grid.saveGrid() ecrase un fichier existant
		return true;
	}

	public JFrame getWindow() {
		return window;
	}
}
