
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Menu extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5237335232850181080L;
	
	/*Je ne sais pas si c'est une bonne solution, je pensais faire un enum pour lister chaque page et
	 *rendre le panel en fonction de la page actuelle.
	 * 
	 * Il me semble que lorsque le menu est affiche, il faudrait que le playing n'existe pas, et inversement,
	 * que lorsqu'on joue, le menu n'existe pas. Donc il n'y aurait pas cette idee que l'un et l'autre coexistent et 
	 * que l'on rende le panel en fonction de ce dont on a besoin. 
	 * 
	 * Il faudrait plus, pour ma part, que lorsqu'un panel en a fini, il se ferme lui-meme et appelle -toujours lui meme
	 * le menu.
	 */

	public Menu(Game game) {
		GridLayout gl = new GridLayout(7,1);
		gl.setVgap(3);
		setLayout(gl);
		
		JLabel title = new JLabel(Game.TITLE, JLabel.CENTER);
		title.setFont(new Font("arial", 0, 100));
		add(title);
		
		Button playButton = new Button("Play");
		playButton.addMouseListener(new ButtonListener(playButton) {
			@Override
			public void mouseReleased(MouseEvent e) {
				try {
					game.loadLevel("../levels/level2.xsb", true);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		Button quitButton = new Button("Quit");
		quitButton.addMouseListener(new ButtonListener(quitButton) {
			@Override
			public void mouseReleased(MouseEvent e) {
				game.stop();
			}
		});
		
		Button generateLevelButton = new Button("Generate level");
		generateLevelButton.addMouseListener(new ButtonListener(generateLevelButton) {
			@Override
			public void mouseReleased(MouseEvent e) {
				game.generateLevel(10, 8, 6, 80);
			}
		});
		
		String[] savesList = getSavesList();
		JComboBox<String> listChoice = new JComboBox<String>(savesList);
		
		Button validateButton = new Button("Valider choix de sauvegarde");
		validateButton.addMouseListener(new ButtonListener(validateButton) {
			@Override
			public void mouseReleased(MouseEvent e) {
				String levelName = (String) listChoice.getSelectedItem();
				try {
					game.loadLevel("../saves/"+levelName, false);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				remove(listChoice);
				remove(validateButton);
			}
		});
		
		Button loadButton = new Button("Load a game");
		loadButton.addMouseListener(new ButtonListener(loadButton) {
			@Override
			public void mouseReleased(MouseEvent e) {
				add(listChoice);
				add(validateButton);
			}
		});
		
		add(playButton);
		
		add(generateLevelButton);
		
		add(loadButton);
		
		add(quitButton);
	}
	
	private static String[] getSavesList() {
		File saveDirectory = new File("../saves/");
		File[] saves = saveDirectory.listFiles();
		String[] tempList = new String[saves.length];
		String name;
		int countFile = 0;
		for (int i = 0; i < saves.length; i++) {
			if (saves[i].isFile()) {
				name = saves[i].getName();
				if (name.endsWith(".xsb")) {
					name = name.substring(0, name.lastIndexOf('.'));
					tempList[countFile] = name;
					countFile++;
				}
			}
		}
		String[] savesList = new String[countFile];
		System.arraycopy(tempList, 0, savesList, 0, countFile);
		return savesList;
	}
	
	public static enum MenuState {
		MAIN;
	}
	
	private MenuState state = MenuState.MAIN;
	
	
	public MenuState getState() {
		return state;
	}
	
	public void paintComponent(Graphics g) {
		
		super.paintComponent(g);
		setBackground(Game.BLEU_CLAIR);
	}
}
