package be.ac.umons.info.sokoban;

import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
	private Menu menu;
	
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
	private LevelDisplay level;
	
	private Options options;
	
	/**
	 * The amount of rendered frame in the last second.
	 */
	private int fpsTemp = 0;
	
	
	public static void main(String[] args) {
		
		Locale.setDefault(Locale.ENGLISH);
		
		Game game = new Game();
		
		game.loadOptions();
		
		game.menu = new Menu(game);
		
		game.window = new JFrame(Game.TITLE);
		game.window.setExtendedState(JFrame.MAXIMIZED_BOTH);
		game.window.setMinimumSize(new Dimension(game.window.getWidth(), game.window.getHeight()));
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
		saveOptions();
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
	 * @throws InvalidFileException 
	 */
	public void loadLevel(String path, int levelIndex, String levelName) throws IOException, InvalidFileException {
		if (levelIndex < 0)
			grid = GridReader.loadGame(path, false);
		else
			grid = GridReader.loadGame(path, true);
		level = new LevelDisplay(grid, this, levelIndex, levelName);
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
		level = new LevelDisplay(grid, this, -2, "NOTHING");
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
	
	private void loadOptions() {
		if (new File("options.ser").exists()) {
			try {
				FileInputStream fileIn = new FileInputStream("options.ser");
				ObjectInputStream in = new ObjectInputStream(fileIn);
				options = (Options) in.readObject();
				in.close();
				fileIn.close();
				options.load();
				System.out.println("Options loaded");
			} catch (IOException i) {
				i.printStackTrace();
			} catch (ClassNotFoundException c) {
				System.out.println("Options class not found");
				c.printStackTrace();
			}
		} else 
			options = new Options();
	}
	
	private void saveOptions() {
		try {
			FileOutputStream fileOut = new FileOutputStream("options.ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			options.save();
			out.writeObject(options);
			out.close();
			fileOut.close();
			System.out.println("Options saved");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the JFrame the game is running in.
	 * @return The JFrame the game is running in
	 */
	public JFrame getWindow() {
		return window;
	}
}
