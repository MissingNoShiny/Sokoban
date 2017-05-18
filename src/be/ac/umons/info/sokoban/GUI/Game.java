package be.ac.umons.info.sokoban.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;

import be.ac.umons.info.sokoban.grid.Grid;
import be.ac.umons.info.sokoban.grid.GridGenerator;
import be.ac.umons.info.sokoban.grid.GridReader;
import be.ac.umons.info.sokoban.grid.InvalidFileException;


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
	 * The panel to display levels on.
	 */
	private LevelDisplay level;
	
	/**
	 * The options of the game.
	 */
	private Options options;
	
	/**
	 * The amount of rendered frame in the last second.
	 */
	private int fpsTemp = 0;
	
	
	public static void main(String[] args) throws IOException {
		
		Locale.setDefault(Locale.ENGLISH);
		
		Game game = new Game();
		game.start();
	}
	
	public Game() {
		options = Options.load();
		menu = new Menu(this);
		window = new JFrame(Game.TITLE);
		window.setExtendedState(JFrame.MAXIMIZED_BOTH);
		window.setMinimumSize(new Dimension(window.getWidth(), window.getHeight()));
		window.setLocationRelativeTo(null);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
		loadMenu();
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
			options.save();
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
		menu.updateCampaignPanel();
		level = null;
	}
	
	/**
	 * Gets the JFrame the game is running in.
	 * @return The JFrame the game is running in
	 */
	public JFrame getWindow() {
		return window;
	}
	
	private static ArrayList<Component> getAllComponents(final Container c) {
	    Component[] comps = c.getComponents();
	    ArrayList<Component> compList = new ArrayList<Component>();
	    for (Component comp : comps) {
	        compList.add(comp);
	        if (comp instanceof Container)
	            compList.addAll(getAllComponents((Container) comp));
	    }
	    return compList;
	}
	
	public void updateOptions(Container frame) {
		ArrayList<Component> componentList = getAllComponents(frame);
		for (Component comp : componentList) {
			if (comp instanceof JButton || comp instanceof JSlider || comp instanceof JToggleButton || comp instanceof JComboBox)
				comp.setBackground(Options.getButtonColor());
			else if (comp instanceof JPanel)
				comp.setBackground(Options.getBackgroundColor());
		}
	}
}
