import java.awt.event.MouseEvent;
import java.io.IOException;

public class LoadLevelButton extends Button {
	
	public LoadLevelButton(int levelIndex, Game game) {
		super("Level " + levelIndex);
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
