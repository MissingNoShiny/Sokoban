package be.ac.umons.info.sokoban;

import java.awt.CardLayout;
import java.awt.Color;
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
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Menu extends JPanel {

	private static final long serialVersionUID = 5237335232850181080L;
	
	private Game game;
	
	private JOptionPane IOError = new JOptionPane();
	
	private class LocalSlider extends JSlider{
		private static final long serialVersionUID = -3599885387076371591L;
		
		public LocalSlider(int min, int max, int majorTickSpacing) {
			super(JSlider.HORIZONTAL, min, max, (min+max)/2);
			setMajorTickSpacing(majorTickSpacing);
			setMinorTickSpacing(1);
			setPaintTicks(true);
			setPaintLabels(true);
			setBackground(Options.buttonsColor);
			setFont(new Font(Options.fontName, 0 , 20));
		}

		public void adaptBounds(int value, int value2) {
				
		}
	}

	private class CampaignButton extends Button {
		private static final long serialVersionUID = 1259333880542050320L;
				
		public CampaignButton(String text, Color color, Font font, int levelIndex) {
			super(text, color, font);
			addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						game.loadLevel("levels/" + getText(), true, levelIndex);
					} catch (IOException e1) {
						e1.printStackTrace();
						JOptionPane.showMessageDialog(IOError, "Loading failed", "Error", JOptionPane.ERROR_MESSAGE);
					} catch (InvalidFileException e2) {
						JOptionPane.showMessageDialog(IOError, "Invalid File", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			});
		}
	}
	
	
	private JPanel mainMenuPanel;
	
	public Menu(Game game) {
		
		this.game = game;
		
		setBackground(Options.backGroundColor);
		
		final CardLayout cd = new CardLayout();
		setLayout(cd);
		
		mainMenuPanel = new JPanel();
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
		
		final JFrame generatorFrame = new JFrame();
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
		
		final LocalSlider levelWidthSlider = new LocalSlider(6, 30, 6);
		generatorParameters1.add(levelWidthSlider);
		
		final LocalSlider levelHeightSlider = new LocalSlider(6, 30, 6);
		generatorParameters1.add(levelHeightSlider);
		
		
		JPanel generatorParameters2 = new JPanel();
		generatorParameters2.setLayout(new GridLayout(2, 2));
		generatorFrame.add(generatorParameters2);
		
		generatorParameters2.add(new DefaultLabel("Amount of crates"));
		generatorParameters2.add(new DefaultLabel("Difficulty"));
		
		
		final LocalSlider crateAmountSlider = new LocalSlider(2, 10, 10){
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
		crateAmountSlider.adaptBounds(levelWidthSlider.getValue(), levelHeightSlider.getValue());
		generatorParameters2.add(crateAmountSlider);
		
		final LocalSlider difficultySlider = new LocalSlider(0, 20, 5);
		generatorParameters2.add(difficultySlider);
		
		
		levelWidthSlider.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e) {
				crateAmountSlider.adaptBounds(levelWidthSlider.getValue(), levelHeightSlider.getValue());
		    }    
		});
		
		levelHeightSlider.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e) {
				crateAmountSlider.adaptBounds(levelWidthSlider.getValue(), levelHeightSlider.getValue());
		    }    
		});
		
		
		Button validateGeneratorButton = new Button("Generate", Options.buttonsColor, Options.defaultFont);
		validateGeneratorButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int difficulty = difficultySlider.getValue();
				int widthLevel = levelWidthSlider.getValue();
				int heightLevel = levelHeightSlider.getValue();
				int numberCrates = crateAmountSlider.getValue();
				game.generateLevel(widthLevel, heightLevel, numberCrates, difficulty);
				setEnabledButtons(true);
				generatorFrame.setVisible(false);
			}
		});
		
		Button cancelGeneratorButton = new Button("Cancel", Options.buttonsColor, Options.defaultFont);
		cancelGeneratorButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				setEnabledButtons(true);
				generatorFrame.setVisible(false);
			}
		});
		
		generatorFrame.add(validateGeneratorButton);
		generatorFrame.add(cancelGeneratorButton);
		
		Button generateLevelButton = new Button("Generate level", Options.buttonsColor, Options.menuButtonsFont);
		generateLevelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setEnabledButtons(false);
				generatorFrame.setVisible(true);
			}
		});
		
		final JFrame loadFrame = new JFrame("Load a save");
		loadFrame.setSize(500, 300);
		loadFrame.setLocationRelativeTo(null); 
		loadFrame.setResizable(false);
		loadFrame.setLayout(new GridLayout(3,1));
		
		
		final JComboBox<String> saveChoice = new JComboBox<String>();
		saveChoice.setFont(Options.defaultFont);
		saveChoice.setBackground(Options.buttonsColor);
		saveChoice.setFocusable(false);
		saveChoice.setEditable(false);
		loadFrame.add(saveChoice);
		
		final Button validateLoadingButton = new Button("Load", Options.buttonsColor, Options.defaultFont);
		validateLoadingButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String levelName = (String) saveChoice.getSelectedItem();
				try {
					game.loadLevel("saves/"+levelName, false, -1);
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(IOError, "Loading failed", "Error", JOptionPane.ERROR_MESSAGE);
				} catch (InvalidFileException e2) {
					JOptionPane.showMessageDialog(IOError, "Invalid File", "Error", JOptionPane.ERROR_MESSAGE);
				}
				setEnabledButtons(true);
				loadFrame.setVisible(false);
			}
		});
		
		final Button cancelLoadingButton = new Button("Cancel", Options.buttonsColor, Options.defaultFont);
		cancelLoadingButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				setEnabledButtons(true);
				loadFrame.setVisible(false);
			}
		});
		
		Button loadButton = new Button("Load a game", Options.buttonsColor, Options.menuButtonsFont);
		loadButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveChoice.removeAllItems();
				String[] savesList = getSavesList();
				for (int i = 0; i < savesList.length; i++)
					saveChoice.addItem(savesList[i]);
				loadFrame.add(validateLoadingButton);
				loadFrame.add(cancelLoadingButton);
				setEnabledButtons(false);
				loadFrame.setVisible(true);
			}
		});
		
		
		mainMenuPanel.add(campaignButton);
		
		mainMenuPanel.add(generateLevelButton);
		
		mainMenuPanel.add(loadButton);
		
		mainMenuPanel.add(exitButton);
		
		
		final JPanel levelListPanel = new JPanel();
		levelListPanel.setOpaque(false);
		int levelIndex = 1;
		File level = new File("levels/level " + levelIndex + ".xsb");
		while (level.exists()) {
			levelListPanel.add(new CampaignButton("level " + levelIndex, Options.buttonsColor, Options.defaultFont, levelIndex));
			levelIndex++;
			level = new File("levels/level " + levelIndex + ".xsb");
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
		File saveDirectory = new File("saves/");
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
	
	private void setEnabledButtons(boolean arg) {
		for (int i = 0; i < mainMenuPanel.getComponentCount(); i++) {
			mainMenuPanel.getComponent(i).setEnabled(arg);
		}
	}
}
