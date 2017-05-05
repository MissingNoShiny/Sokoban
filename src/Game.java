
import java.io.IOException;
import java.util.Locale;

import javax.swing.JFrame;


public class Game implements Runnable {
	
	/**
	 * The title of the JFrame the game will run in.
	 */
	public static final String TITLE = "Sokoban";
	
	/**
	 * The approximate number of frame the game will render a second.
	 */
	public static final int FPS_CAP = 30;

	/**
	 * Boolean value used to make the game loop run.
	 */
	private boolean running = false;
	
	/**
	 * The JFrame the game will run in.
	 */
	private JFrame window;
	
	/**
	 * The menu of the game.
	 */
	private Menu menu = new Menu(this);
	
	/**
	 * The separate thread the game loop will run in.
	 */
	private Thread thread;
	
	/**
	 * The grid of the current level on screen (when a level is being displayed).
	 */
	private Grid grid;
	
	/**
	 * 
	 */
	private DisplayLevel level;
	
	/**
	 * The amount of rendered frame in the last second.
	 */
	private int fpsTemp = 0;
	
	
	public static void main(String[] args) {
		
		Locale.setDefault(Locale.ENGLISH);
		
		Game game = new Game();
		
		
		game.window = new JFrame(Game.TITLE);
		game.window.setExtendedState(JFrame.MAXIMIZED_BOTH);
		game.window.setResizable(true);
		game.window.setLocationRelativeTo(null);
		game.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.window.setVisible(true);
		game.loadMenu();
		
		game.start();
			
	}
	
	/**
	 * Gets the amount of rendered frames in the last second.
	 * @return The amount of rendered frames in the last second
	 */
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
	public void loadLevel(String path, boolean isClearLevel) throws IOException {
		if (isClearLevel) 
			grid = GridReader.readGrid(path);
		else
			grid = GridReader.loadGame(path);
		level = new DisplayLevel(grid, this);
		window.setContentPane(level);
		level.displayGrid.requestFocusInWindow();
	}
	
	/**
	 * Generates a new level with specified requirements.
	 * @param width The width of the level
	 * @param height The height of the level
	 * @param numberCrates The amount of crates of the level
	 * @param difficulty The difficulty of the level
	 */
	public void generateLevel(int width, int height, int numberCrates, int difficulty) {
		grid = GridGenerator.generateGrid(width, height, numberCrates, difficulty);
		level = new DisplayLevel(grid, this);
		window.setContentPane(level);
		level.displayGrid.requestFocusInWindow();
	}
	
	/**
	 * Loads the menu.
	 */
	public void loadMenu() {
		window.setContentPane(menu);
		level = null;
	}
	
	/**
	 * Gets the JFrame the game is running in.
	 * @return The JFrame the game is running in
	 */
	public JFrame getWindow() {
		return window;
	}
}
