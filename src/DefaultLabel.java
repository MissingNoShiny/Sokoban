
import javax.swing.JLabel;

public class DefaultLabel extends JLabel {

	private static final long serialVersionUID = -2925126198019609830L;
	
	public DefaultLabel(String string) {
		super(string);
		setBackground(Menu.defaultColor);
		setOpaque(true);
		setHorizontalAlignment(JLabel.CENTER);
		setFont(Menu.defaultFont);
	}
}
