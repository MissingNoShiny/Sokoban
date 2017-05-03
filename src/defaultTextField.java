
import javax.swing.JTextField;

public class defaultTextField extends JTextField {

	private static final long serialVersionUID = -3137396354945095318L;
	
	public defaultTextField(String string) {
		super(string);
		setBackground(Menu.defaultColor);
		setHorizontalAlignment(JTextField.CENTER);
		setFont(Menu.defaultFont);
	}
	
	public defaultTextField() {
		this("");
	}
}
