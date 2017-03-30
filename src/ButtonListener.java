import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public abstract class ButtonListener implements MouseListener{
	
	private Button button;
	
	public ButtonListener(Button button) {
		this.button = button;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		button.setBackground(button.getColor().brighter());
	}

	@Override
	public void mouseExited(MouseEvent e) {
		button.setBackground(button.getColor());
	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}
}
