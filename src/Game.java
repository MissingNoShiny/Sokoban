
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class Game implements Runnable {
	
	public static enum GameState {
		MENU,
		PLAYING;
	}
	
	private GameState state = GameState.MENU;
	
	public static final String TITLE = "Sokoban";
	private final double TICKS_PER_SECOND = 60.0;
	private final long WAITING_TIME_MS = (long) (10E2 / TICKS_PER_SECOND);
	
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
	
	/**
	 * The game loop.
	 */
	@Override
	public void run() {
		long timer = System.currentTimeMillis();
		int ticks = 0;
		while (running) {
			try {
				Thread.sleep(WAITING_TIME_MS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			ticks ++;
			if (System.currentTimeMillis() - timer > 1000) {
				tick();
				timer += 1000;
				System.out.println("Tps: " + ticks);
				ticks = 0;
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
	
	public void tick() {
		//C'est cette méthode qui va exécuter la logique du jeu, TICKS_PER_SECOND fois par seconde
	}
	
	//Inputs
	//Keyboard
	
	public void keyPressed(KeyEvent e) { 
		int input = e.getKeyCode();
		switch (input){
		case KeyEvent.VK_ENTER:
			System.out.print("Coordonnées du joueur: ");
			System.out.println(grid.player.getX() + ", " + grid.player.getY());
			System.out.println(grid.player.getDirection());
			break;
		case KeyEvent.VK_UP:
			System.out.println("up");
			grid.getPlayer().setDirection(Direction.UP);
			grid.getPlayer().move(grid);
			break;
		case KeyEvent.VK_DOWN :
			grid.getPlayer().setDirection(Direction.DOWN);
			grid.getPlayer().move(grid);
			break;
		case KeyEvent.VK_RIGHT:
			grid.getPlayer().setDirection(Direction.RIGHT);
			grid.getPlayer().move(grid);
			break;
		case KeyEvent.VK_LEFT:
			grid.getPlayer().setDirection(Direction.LEFT);
			grid.getPlayer().move(grid);
			break;

		default :
			System.out.println("On a appuyé, \nComposant en (6,5) :" + grid.getComponentAt(6, 5));
		}
		if (grid.isWon())
			System.out.println("Vivent les castors");
		window.refresh();
	}
	    
	//Mouse
	
	
	public void loadLevel(String path) throws IOException {
			grid = Grid.readGrid(path);
		level = new DisplayLevel(grid);
		level.addKeyListener(new Inputs(this));
		window.setPanel(level);
		level.requestFocusInWindow();
		state = GameState.PLAYING;
	}
	
	public void loadMenu() {
		window.setPanel(menu);
		state = GameState.MENU;
	}

	public boolean canOverrideLevel() {
		//methode qui servira a afficher un avertissement si la methode Grid.saveGrid() ecrase un fichier existant
		return false;
	}
}
