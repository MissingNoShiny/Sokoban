
import java.awt.CardLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Menu extends JPanel {

	private static final long serialVersionUID = 5237335232850181080L;
	
	public Menu(Game game) {
		
		setBackground(Options.backGroundColor);
		
		CardLayout cd = new CardLayout();
		setLayout(cd);
		
		JPanel mainMenuPanel = new JPanel();
		mainMenuPanel.setLayout(new GridLayout(6, 1, 3, 3));
		mainMenuPanel.setOpaque(false);
		add(mainMenuPanel);
		
		JLabel title = new JLabel(Game.TITLE, JLabel.CENTER);
		title.setFont(new Font(Options.fontName, 0, 100));
		mainMenuPanel.add(title);
		
		Button campaignButton = new Button("Campain", Options.buttonsColor, Options.menuButtonsFont);
		campaignButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cd.next(mainMenuPanel.getParent());
			}
		});
		
		Button exitButton = new Button("Exit", Options.buttonsColor, Options.menuButtonsFont);
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				game.stop();
			}
		});
		
		JFrame generatorFrame = new JFrame();
		generatorFrame.setTitle("Generator options");
		generatorFrame.setSize(800, 400);
		generatorFrame.setLocationRelativeTo(null); 
		generatorFrame.setResizable(false);
		generatorFrame.setLayout(new GridLayout(4,1));
		
		JPanel generatorParameters1 = new JPanel();
		generatorParameters1.setLayout(new GridLayout(2, 2));
		generatorFrame.add(generatorParameters1);
		
		generatorParameters1.add(new DefaultLabel("Width"));
		generatorParameters1.add(new DefaultLabel("Height"));
		
		DefaultSlider widthLevelSlider = new DefaultSlider(6, 30, 6);
		generatorParameters1.add(widthLevelSlider);
		
		DefaultSlider heightLevelSlider = new DefaultSlider(6, 30, 6);
		generatorParameters1.add(heightLevelSlider);
		
		
		JPanel generatorParameters2 = new JPanel();
		generatorParameters2.setLayout(new GridLayout(2, 2));
		generatorFrame.add(generatorParameters2);
		
		generatorParameters2.add(new DefaultLabel("Amount of crates"));
		generatorParameters2.add(new DefaultLabel("Difficulty"));
		
		
		DefaultSlider numberCratesSlider = new DefaultSlider(2, 10, 10){
			private static final long serialVersionUID = 1L;
			@Override
			public void adaptBounds(int widthLevel, int heightLevel) {
				setMaximum((widthLevel-2)*(heightLevel-2)/5);
				int sliderSize = getMaximum()-getMinimum();
				if (sliderSize/3 > 0) {
					setMajorTickSpacing(sliderSize/3);
					setLabelTable(createStandardLabels(sliderSize/3));
				}
				else {
					setMajorTickSpacing(1);
					setLabelTable(createStandardLabels(1));
				}
			}
		};
		numberCratesSlider.adaptBounds(widthLevelSlider.getValue(), heightLevelSlider.getValue());
		generatorParameters2.add(numberCratesSlider);
		
		DefaultSlider difficultySlider = new DefaultSlider(0, 20, 5);
		generatorParameters2.add(difficultySlider);
		
		
		widthLevelSlider.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e) {
				numberCratesSlider.adaptBounds(widthLevelSlider.getValue(), heightLevelSlider.getValue());
		    }    
		});
		
		heightLevelSlider.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e) {
				numberCratesSlider.adaptBounds(widthLevelSlider.getValue(), heightLevelSlider.getValue());
		    }    
		});
		
		
		Button validateGeneratorButton = new Button("Generate", Options.buttonsColor, Options.defaultFont);
		validateGeneratorButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int difficulty = difficultySlider.getValue();
				int widthLevel = widthLevelSlider.getValue();
				int heightLevel = heightLevelSlider.getValue();
				int numberCrates = numberCratesSlider.getValue();
				game.generateLevel(widthLevel, heightLevel, numberCrates, difficulty);
				generatorFrame.setVisible(false);
			}
		});
		
		Button cancelGeneratorButton = new Button("Cancel", Options.buttonsColor, Options.defaultFont);
		cancelGeneratorButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				generatorFrame.setVisible(false);
			}
		});
		
		generatorFrame.add(validateGeneratorButton);
		generatorFrame.add(cancelGeneratorButton);
		
		Button generateLevelButton = new Button("Generate level", Options.buttonsColor, Options.menuButtonsFont);
		generateLevelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				generatorFrame.setVisible(true);
			}
		});
		
		JFrame loadFrame = new JFrame("Load a save");
		loadFrame.setSize(500, 300);
		loadFrame.setLocationRelativeTo(null); 
		loadFrame.setResizable(false);
		loadFrame.setLayout(new GridLayout(3,1));
		
		JComboBox<String> listChoice = new JComboBox<String>();
		listChoice.setFont(Options.defaultFont);
		listChoice.setBackground(Options.buttonsColor);
		listChoice.setFocusable(false);
		listChoice.setEditable(false);
		loadFrame.add(listChoice);
		
		JOptionPane saveError = new JOptionPane();
		
		Button validateLoadingButton = new Button("Load", Options.buttonsColor, Options.defaultFont);
		validateLoadingButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String levelName = (String) listChoice.getSelectedItem();
				try {
					game.loadLevel("../saves/"+levelName, false);
				} catch (IOException e1) {
					System.out.println("ici");
					JOptionPane.showMessageDialog(saveError, "Loading failed.\nSave not found.", "Error", JOptionPane.ERROR_MESSAGE);
				}
				loadFrame.setVisible(false);
			}
		});
		
		Button cancelLoadingButton = new Button("Cancel", Options.buttonsColor, Options.defaultFont);
		cancelLoadingButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				loadFrame.setVisible(false);
			}
		});
		
		Button loadButton = new Button("Load a game", Options.buttonsColor, Options.menuButtonsFont);
		loadButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				listChoice.removeAllItems();
				String[] savesList = getSavesList();
				for (int i = 0; i < savesList.length; i++)
					listChoice.addItem(savesList[i]);
				loadFrame.add(validateLoadingButton);
				loadFrame.add(cancelLoadingButton);
				loadFrame.setVisible(true);
			}
		});
		
		
		mainMenuPanel.add(campaignButton);
		
		mainMenuPanel.add(generateLevelButton);
		
		mainMenuPanel.add(loadButton);
		
		mainMenuPanel.add(exitButton);

		
		JPanel levelListPanel = new JPanel();
		levelListPanel.setOpaque(false);
		int levelIndex = 1;
		File level = new File("../levels/level " + levelIndex + ".xsb");
		while (level.exists()) {
			Button loadLevelButton = new Button("level " + levelIndex, Options.buttonsColor, Options.defaultFont);
			loadLevelButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						game.loadLevel("../levels/" + loadLevelButton.getText() + ".xsb", true);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			});
			levelListPanel.add(loadLevelButton);
			levelIndex++;
			level = new File("../levels/level " + levelIndex + ".xsb");
		}
		
		Button returnButton = new Button("Return", Options.buttonsColor, Options.defaultFont);
		returnButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
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
}
