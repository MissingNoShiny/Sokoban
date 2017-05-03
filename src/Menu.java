
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

	private static final long serialVersionUID = 5237335232850181080L;
	
	public static final String fontName = "arial";
	
	public static final Font defaultFont = new Font(fontName, 0, 40);
	
	public static final Color defaultColor = Color.orange;
	
	private static final Font menuButtonsFont = new Font(fontName, 0, 70);
	
	public Menu(Game game) {
		
		setLayout(new GridLayout(6, 1, 3, 3));
		
		JLabel title = new JLabel(Game.TITLE, JLabel.CENTER);
		title.setFont(new Font(fontName, 0, 100));
		add(title);
		
		Button playButton = new Button("Play", defaultColor, menuButtonsFont);
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
		
		Button quitButton = new Button("Quit", defaultColor, menuButtonsFont);
		quitButton.addMouseListener(new ButtonListener(quitButton) {
			@Override
			public void mouseReleased(MouseEvent e) {
				game.stop();
			}
		});
		
		JFrame generatorFrame = new JFrame();
		generatorFrame.setSize(600, 400);
		generatorFrame.setLocationRelativeTo(null); 
		generatorFrame.setResizable(false);
		generatorFrame.setLayout(new GridLayout(3,1));
		
		JPanel generatorParameters = new JPanel();
		generatorParameters.setLayout(new GridLayout(2, 3));
		generatorFrame.add(generatorParameters);
		
		generatorParameters.add(new defaultLabel("Largeur"));
		generatorParameters.add(new defaultLabel("Hauteur"));
		generatorParameters.add(new defaultLabel("Difficulte"));
		
		defaultTextField widthLevel = new defaultTextField();
		generatorParameters.add(widthLevel);
		
		defaultTextField heightLevel = new defaultTextField();
		generatorParameters.add(heightLevel);
		
		defaultTextField difficulty = new defaultTextField();
		generatorParameters.add(difficulty);
		
		Button validateGeneratorButton = new Button("Valider", defaultColor, defaultFont);
		validateGeneratorButton.addMouseListener(new ButtonListener(validateGeneratorButton) {
			@Override
			public void mouseReleased(MouseEvent e) {
				String str = difficulty.getText();
				int difficulty = Integer.parseInt(str)* 10;
				str = widthLevel.getText();
				int widthLevel = Integer.parseInt(str);
				str = heightLevel.getText();
				int heightLevel = Integer.parseInt(str);
				game.generateLevel(widthLevel, heightLevel, 7, difficulty);
				generatorFrame.setVisible(false);
			}
		});
		
		Button cancelGeneratorButton = new Button("Annuler", defaultColor, defaultFont);
		cancelGeneratorButton.addMouseListener(new ButtonListener(cancelGeneratorButton){
			@Override
			public void mouseReleased(MouseEvent e) {
				generatorFrame.setVisible(false);
			}
		});
		
		generatorFrame.add(validateGeneratorButton);
		generatorFrame.add(cancelGeneratorButton);
		
		Button generateLevelButton = new Button("Generate level", defaultColor, menuButtonsFont);
		generateLevelButton.addMouseListener(new ButtonListener(generateLevelButton) {
			@Override
			public void mouseReleased(MouseEvent e) {
				generatorFrame.setVisible(true);
			}
		});
		
		JFrame saveChoice = new JFrame();
		saveChoice.setSize(500, 300);
		saveChoice.setLocationRelativeTo(null); 
		saveChoice.setResizable(false);
		saveChoice.setLayout(new GridLayout(3,1));
		
		String[] savesList = getSavesList();	
		JComboBox<String> listChoice = new JComboBox<String>(savesList);
		listChoice.setFont(defaultFont);
		listChoice.setBackground(defaultColor);
		listChoice.setFocusable(false);
		listChoice.setEditable(false);
		saveChoice.add(listChoice);
		
		//JLabel errorSave = new JLabel("La sauvegarde a echoue");
		
		Button validateButton = new Button("Valider", defaultColor, defaultFont);
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
		
		Button cancelButton = new Button("Annuler", defaultColor, defaultFont);
		cancelButton.addMouseListener(new ButtonListener(cancelButton){
			@Override
			public void mouseReleased(MouseEvent e) {
				saveChoice.setVisible(false);
			}
		});
		
		Button loadButton = new Button("Load a game", defaultColor, menuButtonsFont);
		loadButton.addMouseListener(new ButtonListener(loadButton) {
			@Override
			public void mouseReleased(MouseEvent e) {
				saveChoice.add(validateButton);
				saveChoice.add(cancelButton);
				saveChoice.setVisible(true);
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
