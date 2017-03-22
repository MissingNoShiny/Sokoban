import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;


//C'est APRES avoir fait ca que j'ai vu qu'il existait deja une classe JButton. Faudra surement refaire.
public class Button {
	
	/**
	 * The marge is the number of pixels that we add to the size of the rect who containt a specified string
	 */
	private final int marge = 10;
	
	private Rectangle rectangle;
	
	private String message;
	
	public Button (int x, int y, int fontSize, String message, Graphics2D g) {
		g.setFont(new Font("arial", 0, 70));
		int height = g.getFontMetrics().getAscent()+marge;    
		int width= g.getFontMetrics().stringWidth(message)+marge; 
		rectangle = new Rectangle(x, y, width, height); 
		this.message = message;
	}
	
	public Button (int x, int y, String message, Graphics2D g) {
		this(x, y, 60, message, g);
	}
	
	public void display(Graphics2D g) {
		g.setColor(Game.ORANGE);
		g.fill(rectangle);
		g.setColor(Game.BLACK);
		g.setFont(new Font("arial", 0, 70));
		g.drawString(message, rectangle.x+marge/2, rectangle.y+rectangle.height-2*marge/3);
	}
	
	public boolean clickedOn (int x, int y) {
		return rectangle.contains(x, y);
	}
}
