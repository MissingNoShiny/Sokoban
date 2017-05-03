
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Menu extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5237335232850181080L;

	public Menu(Game game) {
		
		CardLayout cd = new CardLayout();
		setLayout(cd);
		
		JPanel main = new JPanel();
		GridLayout gl = new GridLayout(7,1);
		gl.setVgap(3);
		main.setLayout(gl);
		main.setBackground(Game.BLEU_CLAIR);
		
		JLabel title = new JLabel(Game.TITLE, JLabel.CENTER);
		title.setFont(new Font("arial", 0, 100));
		main.add(title);
		
		Button playButton = new Button("Play");
		playButton.addMouseListener(new ButtonListener(playButton) {
			@Override
			public void mouseReleased(MouseEvent e) {
				cd.next(main.getParent());
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
				game.generateLevel(10, 8, 6, 100);
			}
		});
		
		JFrame saveChoice = new JFrame();
		saveChoice.setSize(300, 300);
		saveChoice.setLayout(new GridLayout(3,1));
		
		String[] savesList = getSavesList();	
		JComboBox<String> listChoice = new JComboBox<String>(savesList);
		listChoice.setFont(new Font("arial", 0, 40));
		listChoice.setBackground(Color.orange);
		listChoice.setFocusable(false);
		listChoice.setEditable(false);
		
		Button validateButton = new Button("Valider", Color.orange, 40);
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
				saveChoice.setVisible(false);
			}
		});
		
		Button cancelButton = new Button("Annuler", Color.orange, 40);
		cancelButton.addMouseListener(new ButtonListener(cancelButton){
			@Override
			public void mouseReleased(MouseEvent e) {
				saveChoice.setVisible(false);
			}
		});
		
		Button loadButton = new Button("Load a game");
		loadButton.addMouseListener(new ButtonListener(loadButton) {
			@Override
			public void mouseReleased(MouseEvent e) {
				saveChoice.add(validateButton);
				saveChoice.add(cancelButton);
				saveChoice.setVisible(true);
			}
		});
		
		saveChoice.add(listChoice);
		
		
		main.add(playButton);
		
		main.add(generateLevelButton);
		
		main.add(loadButton);
		
		main.add(quitButton);
		
		add(main);
		
		JPanel levelListPanel = new JPanel();
		levelListPanel.setBackground(Game.BLEU_CLAIR);
		int levelIndex = 1;
		File level = new File("../levels/level" + levelIndex + ".xsb");
		while (level.exists()) {
			levelListPanel.add(new LoadLevelButton(levelIndex, game));
			levelIndex++;
			level = new File("../levels/level" + levelIndex + ".xsb");
		}
		
		Button returnButton = new Button("Return");
		returnButton.addMouseListener(new ButtonListener(returnButton){
			@Override
			public void mouseReleased(MouseEvent e) {
				cd.previous(levelListPanel.getParent());
			}
		});
		
		levelListPanel.add(returnButton);
		
		add(levelListPanel);
		
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
