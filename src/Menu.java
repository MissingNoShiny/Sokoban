
import java.awt.CardLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
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
		
		CardLayout cd = new CardLayout();
		setLayout(cd);
		
		JPanel main = new JPanel();
		GridLayout gl = new GridLayout(6, 1, 3, 3);
		gl.setVgap(3);
		main.setLayout(gl);
		main.setBackground(Options.backGroundColor);
		
		JLabel title = new JLabel(Game.TITLE, JLabel.CENTER);
		title.setFont(new Font(Options.fontName, 0, 100));
		main.add(title);
		
		Button campaignButton = new Button("Campain", Options.buttonsColor, Options.menuButtonsFont);
		campaignButton.addMouseListener(new ButtonListener(campaignButton) {
			@Override
			public void mouseReleased(MouseEvent e) {
				cd.next(main.getParent());
			}
		});
		
		Button exitButton = new Button("Exit", Options.buttonsColor, Options.menuButtonsFont);
		exitButton.addMouseListener(new ButtonListener(exitButton) {
			@Override
			public void mouseReleased(MouseEvent e) {
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
		
		DefaultSlider numberCratesSlider = new DefaultSlider(2, (widthLevelSlider.getValue()-2)*(heightLevelSlider.getValue()-2)/5-1, ((widthLevelSlider.getValue()-2)*(heightLevelSlider.getValue()-2)/5-3)/3);
		generatorParameters2.add(numberCratesSlider);
		
		DefaultSlider difficultySlider = new DefaultSlider(0, 20, 5);
		generatorParameters2.add(difficultySlider);
		
		
		widthLevelSlider.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e) {
				numberCratesSlider.setMaximum((widthLevelSlider.getValue()-2)*(heightLevelSlider.getValue()-2)/5-1);
				int sliderSize = numberCratesSlider.getMaximum()-numberCratesSlider.getMinimum();
				numberCratesSlider.setMajorTickSpacing(sliderSize/3);
				numberCratesSlider.setLabelTable(numberCratesSlider.createStandardLabels(sliderSize/3));
		    }    
		});
		
		heightLevelSlider.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e) {
				numberCratesSlider.setMaximum((widthLevelSlider.getValue()-2)*(heightLevelSlider.getValue()-2)/5-1);
				int sliderSize = numberCratesSlider.getMaximum()-numberCratesSlider.getMinimum();
				numberCratesSlider.setMajorTickSpacing(sliderSize/3);
				numberCratesSlider.setLabelTable(numberCratesSlider.createStandardLabels(sliderSize/3));
		    }    
		});
		
		
		Button validateGeneratorButton = new Button("Generate", Options.buttonsColor, Options.defaultFont);
		validateGeneratorButton.addMouseListener(new ButtonListener(validateGeneratorButton) {
			@Override
			public void mouseReleased(MouseEvent e) {
				int difficulty = difficultySlider.getValue();
				int widthLevel = widthLevelSlider.getValue();
				int heightLevel = heightLevelSlider.getValue();
				int numberCrates = numberCratesSlider.getValue();
				game.generateLevel(widthLevel, heightLevel, numberCrates, difficulty);
				generatorFrame.setVisible(false);
			}
		});
		
		Button cancelGeneratorButton = new Button("Cancel", Options.buttonsColor, Options.defaultFont);
		cancelGeneratorButton.addMouseListener(new ButtonListener(cancelGeneratorButton){
			@Override
			public void mouseReleased(MouseEvent e) {
				generatorFrame.setVisible(false);
			}
		});
		
		generatorFrame.add(validateGeneratorButton);
		generatorFrame.add(cancelGeneratorButton);
		
		Button generateLevelButton = new Button("Generate level", Options.buttonsColor, Options.menuButtonsFont);
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
		
		JComboBox<String> listChoice = new JComboBox<String>();
		//listChoice.setAlignmentY(JComboBox.CENTER_ALIGNMENT);
		listChoice.setFont(Options.defaultFont);
		listChoice.setBackground(Options.buttonsColor);
		listChoice.setFocusable(false);
		listChoice.setEditable(false);
		saveChoice.add(listChoice);
		
		JOptionPane saveError = new JOptionPane();
		
		Button validateLoadingButton = new Button("Load", Options.buttonsColor, Options.defaultFont);
		validateLoadingButton.addMouseListener(new ButtonListener(validateLoadingButton) {
			@Override
			public void mouseReleased(MouseEvent e) {
				String levelName = (String) listChoice.getSelectedItem();
				try {
					game.loadLevel("../saves/"+levelName, false);
				} catch (IOException e1) {
					System.out.println("ici");
					JOptionPane.showMessageDialog(saveError, "Loading failed", "Save not found", JOptionPane.ERROR_MESSAGE);
				}
				saveChoice.setVisible(false);
			}
		});
		
		Button cancelLoadingButton = new Button("Cancel", Options.buttonsColor, Options.defaultFont);
		cancelLoadingButton.addMouseListener(new ButtonListener(cancelLoadingButton){
			@Override
			public void mouseReleased(MouseEvent e) {
				saveChoice.setVisible(false);
			}
		});
		
		Button loadButton = new Button("Load a game", Options.buttonsColor, Options.menuButtonsFont);
		loadButton.addMouseListener(new ButtonListener(loadButton) {
			@Override
			public void mouseReleased(MouseEvent e) {
				listChoice.removeAllItems();
				String[] savesList = getSavesList();
				for (int i = 0; i < savesList.length; i++)
					listChoice.addItem(savesList[i]);
				saveChoice.add(validateLoadingButton);
				saveChoice.add(cancelLoadingButton);
				saveChoice.setVisible(true);
			}
		});
		
		
		main.add(campaignButton);
		
		main.add(generateLevelButton);
		
		main.add(loadButton);
		
		main.add(exitButton);
		
		add(main);
		
		JPanel levelListPanel = new JPanel();
		levelListPanel.setOpaque(false);
		int levelIndex = 1;
		File level = new File("../levels/level " + levelIndex + ".xsb");
		while (level.exists()) {
			Button loadLevelButton = new Button("level " + levelIndex, Options.buttonsColor, Options.defaultFont);
			loadLevelButton.addMouseListener(new ButtonListener(loadLevelButton) {
				@Override
				public void mouseReleased(MouseEvent e) {
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
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		setBackground(Options.backGroundColor);
	}
}
