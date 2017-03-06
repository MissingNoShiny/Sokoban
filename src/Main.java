import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Main implements KeyListener {
	public static void main(String[] args) {
		int x;
		//Display fenetre = new Display();
		Player player = new Player();
		do {
			x = keyPressed(e);
			player.move(x);
			if (player.maybeMoved()){
				System.out.println("Le joueur a bougé");
			System.out.println("x =" + player.x + "y =" + player.y + "x = " + x );
			}
		} while (x != 0); 
	}	
    public static int keyPressed(KeyEvent e) { 
    	int input = e.getKeyCode();
    	switch (input){
    	case KeyEvent.VK_ENTER:
        	System.out.println("Vous avez appuyé sur Enter");//Je le laisse, que tu n'aies pas corrigé de fautes d'orthographe pour i-rien
        	return 8545;
    	case KeyEvent.VK_UP:
    		return 1;
    	case KeyEvent.VK_RIGHT :
    		return 2;
    	case KeyEvent.VK_DOWN:
    		return 3;
    	case KeyEvent.VK_LEFT:
    		return 4;
    	case KeyEvent.VK_J: // taper J pour que tout s'arrete
    		return 0;
    	default :
    		return 45;
    	}
    }
}