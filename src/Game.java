
import java.io.IOException;
import java.util.Locale;

import javax.swing.JFrame;


public class Game implements Runnable {
	
	public static final String TITLE = "Sokoban";
	
	public static final int FPS_CAP = 30;

	private boolean running = false;
	private Menu menu = new Menu(this);
	private Thread thread;
	private Grid grid;
	private JFrame window;
	private DisplayLevel level;
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
	 * 
	 * @param width
	 * @param height
	 * @param numberCrates
	 * @param difficulty
	 */
	public void generateLevel(int width, int height, int numberCrates, int difficulty) {
		grid = GridGenerator.generateGrid(width, height, numberCrates, difficulty);
		level = new DisplayLevel(grid, this);
		window.setContentPane(level);
		level.displayGrid.requestFocusInWindow();
	}
	
	/**
	 * Loads the menu
	 */
	public void loadMenu() {
		window.setContentPane(menu);
		level = null;
	}
	
	/**
	 * 
	 * @return
	 */
	public JFrame getWindow() {
		return window;
	}
}
