import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;

public class Game implements Runnable {

	public final String TITLE = "Sokoban";
	private final double TICKS_PER_SECOND = 60.0;
	private final long WAITING_TIME_MS = (long) (10E2 / TICKS_PER_SECOND);
	
	public static final Color BLEU_CLAIR = new Color(135, 206, 250);

	private boolean running = false;
	private Thread thread;
	private Grid grid;
	private Display window;
	private Panel p;
	
	//Plutot que de deplacer player depuis le grid, creer un nouvelle classe (avec un string et un int comme attributs)
	//et faire en sorte que grid retourne les points acquis a ala fin d'un niveau. A chaque nouveau niveau, nouveau grid
	
	public static void main(String[] args) {
		
		Game game = new Game();
		
		/*
		game.grid = new Grid(17, 10);		
		
		game.grid.fill(Component.GROUND);
		
		game.grid.placeComponentAt(6, 5, Component.GOAL);
		game.grid.placeComponentAt(2, 4, Component.GOAL);
		
		game.grid.placeComponentAt(1, 3, Component.WALL);
		game.grid.placeComponentAt(1, 4, Component.WALL);
		game.grid.placeComponentAt(1, 5, Component.WALL);
		game.grid.placeComponentAt(2, 5, Component.WALL);
		game.grid.placeComponentAt(5, 5, Component.WALL);
		
		game.grid.addCrate(5, 8);
		game.grid.addCrate(6, 7);
		
		game.grid.setPlayer(5, 4);
		*/
		
		game.grid = Grid.readGrid("levels\\level1.txt");
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		game.window = new Display(screenSize, game.TITLE);
		game.p = new Panel(game.grid);
		game.window.setContentPane(game.p);
		game.p.setBackground(BLEU_CLAIR);
		game.window.paintPanel(game.p);
		
		game.window.addKeyListener(new Inputs(game));
		
		game.start();
			
	}
	
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
	
	public synchronized void start() {
		if (running)
			return;
		thread = new Thread(this);
		thread.start();
		running = true;
	}
	
	public synchronized void stop() {
		if (!running)
			return;
		try {
			thread.join();
			running = false;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.exit(1);
		
	}
	
	public void tick() {
		//C'est cette m�thode qui va ex�cuter la logique du jeu, TICKS_PER_SECOND fois par seconde
	}
	
	public void keyPressed(KeyEvent e) { 
		int input = e.getKeyCode();
		switch (input){
		case KeyEvent.VK_ENTER:
			System.out.print("Coordonn�es du joueur: ");
			System.out.println(grid.player.getX() + ", " + grid.player.getY());
			System.out.println(grid.player.getDirection());
			break;
		case KeyEvent.VK_UP:
			grid.player.setDirection(Direction.UP);
			grid.player.move(grid);
			break;
		case KeyEvent.VK_DOWN :
			grid.player.setDirection(Direction.DOWN);
			grid.player.move(grid);
			break;
		case KeyEvent.VK_RIGHT:
			grid.player.setDirection(Direction.RIGHT);
			grid.player.move(grid);
			break;
		case KeyEvent.VK_LEFT:
			grid.player.setDirection(Direction.LEFT);
			grid.player.move(grid);
			break;
		case KeyEvent.VK_0:
			grid.placeComponentAt(4,0, Component.GOAL);
		default :
			System.out.println("On a appuy�, \nComposant en (6,5) :" + grid.getComponentAt(6, 5));
		}
		if (grid.isWon())
			System.out.println("Vivent les castors");
		window.paintPanel(p);
	}
	    
	public void keyTyped(KeyEvent e) {
	}

	public void keyReleased(KeyEvent e) {	
	}		
	
}
