
import javax.swing.JLabel;

public class defaultLabel extends JLabel {

	private static final long serialVersionUID = -2925126198019609830L;
	
	public defaultLabel(String string) {
		super(string);
		setBackground(Menu.defaultColor);
		setOpaque(true);
		setHorizontalAlignment(JLabel.CENTER);
		setFont(Menu.defaultFont);
	}
}
