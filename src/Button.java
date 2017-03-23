import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;


public class Button extends JButton{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3219033588828073912L;

	private Color color;
	
	//Le constructeur que j'ai voulu implementer ne marche pas parce que la methode getFontMetrics doit
	//etre appelee sur une instance de Graphics
	/*
	//A constructor which adapt the font to the size
	public Button (String text, int x, int y, int height, int width) {
		this(x, y, text, 100);
		int fontSize = 100;
		int fontHeight = getFontMetrics(new Font("arial", 0, fontSize).getHeight();
		int fontWidth =  getFontMetrics(new Font("arial", 0, fontSize).stringWidth(text);
		while (height < fontHeight || width < fontWidth)
			fontSize -= 4;
		setFont(new Font("arial", 0, fontSize));
	}
	*/
	
	/*
	public Button(int x, int y, String text, int fontSize) {
		super(text);
		setLocation(x, y);
		setFont(new Font("arial", 0, fontSize));
	}
	*/
	
	public Button (String text, Color color, int fontSize) {
		super(text);
		setFocusable(false);
		setFont(new Font("arial", 0, fontSize));
		setSize(200,200);
		setColor(color);
		setBackground(color);
	}
	
	public Button (String text) {
		this(text, Color.orange, 70);
	}
	
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
}
