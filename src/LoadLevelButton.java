
import java.awt.event.MouseEvent;
import java.io.IOException;

public class LoadLevelButton extends Button {
	
	private static final long serialVersionUID = 1285936146683796485L;

	public LoadLevelButton(int levelIndex, Game game) {
		super("Level " + levelIndex, Menu.defaultColor, Menu.menuButtonsFont);
		addMouseListener(new ButtonListener(this) {
			@Override
			public void mouseReleased(MouseEvent e) {
				try {
					game.loadLevel("../levels/level" + levelIndex + ".xsb", true);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
	}

}
