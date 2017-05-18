package be.ac.umons.info.sokoban.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicSliderUI;

import be.ac.umons.info.sokoban.grid.GridReader;
import be.ac.umons.info.sokoban.grid.InvalidFileException;

public class Menu extends JPanel {

	private static final long serialVersionUID = 5237335232850181080L;
	
	private static final int campaignLevelsAmount = 25;
	
	private Game game;
	
	private JOptionPane IOError = new JOptionPane();
	
	private JPanel mainMenuPanel;
	
	private CardLayout cd = new CardLayout();
	
	private CampaignPanel campaignPanel = new CampaignPanel();
	
	private class LocalSlider extends JSlider{
		private static final long serialVersionUID = -3599885387076371591L;
		
		public LocalSlider(int min, int max, int majorTickSpacing) {
			super(JSlider.HORIZONTAL, min, max, (min+max)/2);
			setMajorTickSpacing(majorTickSpacing);
			setMinorTickSpacing(1);
			setPaintTicks(true);
			setPaintLabels(true);
			setBackground(Options.getButtonColor());
			setFont(new Font(Options.fontName, 0 , 20));
		}

		public void adaptBounds(int value, int value2) {
				
		}
	}
	
	private class CampaignPanel extends JPanel {
		
		private static final long serialVersionUID = 1523939439467197461L;
		
		private CampaignButton[] campaignButtonsList = new CampaignButton[campaignLevelsAmount];
		
		public CampaignPanel() {
			GridBagLayout campaignLayout = new GridBagLayout();
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.fill = GridBagConstraints.BOTH;
			setLayout(campaignLayout);
			setOpaque(false);
			int levelIndex = 1;
			File level = new File("levels/level " + levelIndex + ".xsb");
			while (level.exists()) {
				gbc.gridx = (levelIndex-1)%5;
				gbc.gridy = (levelIndex-1)/5;
				campaignButtonsList[levelIndex-1] = new CampaignButton(levelIndex);
				add(campaignButtonsList[levelIndex-1], gbc);
				levelIndex++;
				level = new File("levels/level " + levelIndex + ".xsb");
			}
			
			Button returnButton = new Button("Return", Options.getButtonColor(), Options.smallFont);
			returnButton.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent e) {
					cd.show(getParent(), "mainMenuPanel");
				}
			});
			
			gbc.gridx = 4;
			gbc.gridy = 7;
			add(returnButton, gbc);
		}
		
		public void update() {
			int max = GridReader.getMaxIndexLevel()+1;
			for (int i = 0; i < max; i++) 
				campaignButtonsList[i].setEnabled(true);
		}
	}
	
	private class CampaignButton extends Button {
		private static final long serialVersionUID = 1259333880542050320L;
				
		public CampaignButton(final int levelIndex) {
			super("level " + levelIndex, Options.getButtonColor(), Options.smallFont);
			addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						game.loadLevel("levels/" + getText(), levelIndex, "level "+levelIndex);
					} catch (IOException e1) {
						e1.printStackTrace();
						JOptionPane.showMessageDialog(IOError, "Loading failed", "Error", JOptionPane.ERROR_MESSAGE);
					} catch (InvalidFileException e2) {
						JOptionPane.showMessageDialog(IOError, "Invalid File", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			});
			setEnabled(false);
		}
	}
	
	/**
	 * A class used to change the knob color of JSliders.
	 * https://coderanch.com/t/338457/java/JSlider-knob-color
	 * @author Gregg Bolinger
	 */
	private class ColoredThumbSliderUI extends BasicSliderUI {
	 
	    Color thumbColor;
	    ColoredThumbSliderUI(JSlider s, Color tColor) {
	        super(s);
	        thumbColor=tColor;
	    }
	 
	    public void paint( Graphics g, JComponent c ) {
	        recalculateIfInsetsChanged();
	        recalculateIfOrientationChanged();
	        Rectangle clip = g.getClipBounds();
	 
	        if ( slider.getPaintTrack() && clip.intersects( trackRect ) ) {
	            paintTrack( g );
	        }
	        if ( slider.getPaintTicks() && clip.intersects( tickRect ) ) {
	            paintTicks( g );
	        }
	        if ( slider.getPaintLabels() && clip.intersects( labelRect ) ) {
	            paintLabels( g );
	        }
	        if ( slider.hasFocus() && clip.intersects( focusRect ) ) {
	            paintFocus( g );      
	        }
	        if ( clip.intersects( thumbRect ) ) {
	            Color savedColor = slider.getBackground();
	            slider.setBackground(thumbColor);
	            paintThumb( g );
	            slider.setBackground(savedColor);
	        }
	    }
	}
	
	public Menu(final Game gameInput) {
		
		this.game = gameInput;
		
		setBackground(Options.getBackgroundColor());
		
		setLayout(cd);
		
		mainMenuPanel = new JPanel();
		mainMenuPanel.setLayout(new GridLayout(6, 1, 3, 3));
		mainMenuPanel.setOpaque(false);
		add(mainMenuPanel, "mainMenuPanel");
		
		JPanel optionsPanel = new JPanel();
		add(optionsPanel, "optionsPanel");
		Button optionsButton = new Button("Options", Options.getButtonColor(), Options.bigFont);
		optionsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cd.show(mainMenuPanel.getParent(), "optionsPanel");
			}
		});
		
		JLabel title = new JLabel(Game.TITLE, JLabel.CENTER);
		title.setFont(Options.titleFont);
		mainMenuPanel.add(title);
		
		add(campaignPanel, "campaignPanel");
		Button campaignButton = new Button("Campaign", Options.getButtonColor(), Options.bigFont);
		campaignButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				campaignPanel.update();
				cd.show(mainMenuPanel.getParent(), "campaignPanel");
			}
		});
		
		Button exitButton = new Button("Exit", Options.getButtonColor(), Options.bigFont);
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				game.stop();
			}
		});
		
		final DefaultFrame generatorFrame = new DefaultFrame("Generator options", 800, 400);
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
		
		final LocalSlider crateAmountSlider = new LocalSlider(2, 10, 10) {
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
		
		
		Button validateGeneratorButton = new Button("Generate", Options.getButtonColor(), Options.smallFont);
		validateGeneratorButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				game.generateLevel(levelWidthSlider.getValue(), levelHeightSlider.getValue(), crateAmountSlider.getValue(), difficultySlider.getValue());
				setEnabledButtons(true);
				generatorFrame.setVisible(false);
			}
		});
		
		Button cancelGeneratorButton = new Button("Cancel", Options.getButtonColor(), Options.smallFont);
		cancelGeneratorButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				setEnabledButtons(true);
				generatorFrame.setVisible(false);
			}
		});
		
		generatorFrame.add(validateGeneratorButton);
		generatorFrame.add(cancelGeneratorButton);
		
		Button generateLevelButton = new Button("Generate level", Options.getButtonColor(), Options.bigFont);
		generateLevelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setEnabledButtons(false);
				generatorFrame.setVisible(true);
			}
		});
		
		final DefaultFrame loadFrame = new DefaultFrame("Load a save", 500, 300);
		loadFrame.setLayout(new GridLayout(3,1));
		
		
		final JComboBox<String> saveChoice = new JComboBox<String>();
		saveChoice.setFont(Options.smallFont);
		saveChoice.setBackground(Options.getButtonColor());
		saveChoice.setFocusable(false);
		saveChoice.setEditable(false);
		loadFrame.add(saveChoice);
		
		final Button validateLoadingButton = new Button("Load", Options.getButtonColor(), Options.smallFont);
		validateLoadingButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String levelName = (String) saveChoice.getSelectedItem();
				try {
					game.loadLevel("saves/"+levelName, -1, levelName);
					setEnabledButtons(true);
					loadFrame.setVisible(false);
				} catch (IOException | InvalidFileException e1) {
					JOptionPane.showMessageDialog(IOError, e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		final Button cancelLoadingButton = new Button("Cancel", Options.getButtonColor(), Options.smallFont);
		cancelLoadingButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				setEnabledButtons(true);
				loadFrame.setVisible(false);
			}
		});
		
		Button loadButton = new Button("Load a game", Options.getButtonColor(), Options.bigFont);
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
		mainMenuPanel.add(optionsButton);
		mainMenuPanel.add(exitButton);
		
		optionsPanel.setBackground(Options.getBackgroundColor());
		optionsPanel.setLayout(new GridLayout(7, 1, 3, 3));
		
		JLabel optionsPanelTitle = new JLabel("Options", SwingConstants.CENTER);
		optionsPanelTitle.setFont(Options.titleFont);
		
		JPanel optionsButtonColorPanel = new JPanel();
		optionsButtonColorPanel.setLayout(new GridLayout(1, 3, 3, 3));
		optionsButtonColorPanel.setOpaque(false);
		
		final JLabel optionsButtonColorLabel = new JLabel("Button Color:", SwingConstants.CENTER);
		optionsButtonColorLabel.setFont(Options.bigFont);
		optionsButtonColorLabel.setOpaque(true);
		optionsButtonColorLabel.setBackground(Options.getButtonColor());
		
		JPanel optionsButtonColorSliders = new JPanel();
		optionsButtonColorSliders.setLayout(new GridLayout(3, 1, 0, 0));
		
		final JSlider optionsButtonColorSlider1 = new JSlider(0, 255, 16);
		optionsButtonColorSlider1.setUI(new ColoredThumbSliderUI(optionsButtonColorSlider1, Color.red));
		optionsButtonColorSlider1.setBackground(Options.getButtonColor());
		optionsButtonColorSlider1.setValue(optionsButtonColorLabel.getBackground().getRed());
		optionsButtonColorSlider1.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				optionsButtonColorLabel.setBackground(new Color(
						optionsButtonColorSlider1.getValue(), 
						optionsButtonColorLabel.getBackground().getGreen(), 
						optionsButtonColorLabel.getBackground().getBlue()
						));
			}
		});
		
		final JSlider optionsButtonColorSlider2 = new JSlider(0, 255, 16);
		optionsButtonColorSlider2.setUI(new ColoredThumbSliderUI(optionsButtonColorSlider2, Color.green));
		optionsButtonColorSlider2.setBackground(Options.getButtonColor());
		optionsButtonColorSlider2.setValue(optionsButtonColorLabel.getBackground().getGreen());
		optionsButtonColorSlider2.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				optionsButtonColorLabel.setBackground(new Color(
						optionsButtonColorLabel.getBackground().getRed(), 
						optionsButtonColorSlider2.getValue(), 
						optionsButtonColorLabel.getBackground().getBlue()
						));
			}
		});
		
		final JSlider optionsButtonColorSlider3 = new JSlider(0, 255, 16);
		optionsButtonColorSlider3.setUI(new ColoredThumbSliderUI(optionsButtonColorSlider3, Color.blue));
		optionsButtonColorSlider3.setBackground(Options.getButtonColor());
		optionsButtonColorSlider3.setValue(optionsButtonColorLabel.getBackground().getBlue());
		optionsButtonColorSlider3.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				optionsButtonColorLabel.setBackground(new Color(
						optionsButtonColorLabel.getBackground().getRed(), 
						optionsButtonColorLabel.getBackground().getGreen(),
						optionsButtonColorSlider3.getValue() 
						));
			}
		});
		
		optionsButtonColorSliders.add(optionsButtonColorSlider1);
		optionsButtonColorSliders.add(optionsButtonColorSlider2);
		optionsButtonColorSliders.add(optionsButtonColorSlider3);
		
		Button optionsButtonColorReset = new Button("Reset", Options.getButtonColor(), Options.bigFont);
		optionsButtonColorReset.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				optionsButtonColorSlider1.setValue(Options.DEFAULT_BUTTON_COLOR.getRed());
				optionsButtonColorSlider2.setValue(Options.DEFAULT_BUTTON_COLOR.getGreen());
				optionsButtonColorSlider3.setValue(Options.DEFAULT_BUTTON_COLOR.getBlue());
			}
			
		});
		
		optionsButtonColorPanel.add(optionsButtonColorLabel);
		optionsButtonColorPanel.add(optionsButtonColorSliders);
		optionsButtonColorPanel.add(optionsButtonColorReset);
		
		
		JPanel optionsBackgroundColorPanel = new JPanel();
		optionsBackgroundColorPanel.setLayout(new GridLayout(1, 3, 3, 3));
		optionsBackgroundColorPanel.setOpaque(false);
		
		final JPanel optionsBackgroundColorLabelPanel = new JPanel();
		optionsBackgroundColorLabelPanel.setLayout(new BorderLayout());
		optionsBackgroundColorLabelPanel.setBackground(Options.getBackgroundColor());
		
		final JLabel optionsBackgroundColorLabel = new JLabel("Background Color:", SwingConstants.CENTER);
		optionsBackgroundColorLabel.setFont(Options.bigFont);
		
		optionsBackgroundColorLabelPanel.add(optionsBackgroundColorLabel, BorderLayout.CENTER);
		
		JPanel optionsBackgroundColorSliders = new JPanel();
		optionsBackgroundColorSliders.setLayout(new GridLayout(3, 1, 0, 0));
		
		final JSlider optionsBackgroundColorSlider1 = new JSlider(0, 255, 16);
		optionsBackgroundColorSlider1.setUI(new ColoredThumbSliderUI(optionsBackgroundColorSlider1, Color.red));
		optionsBackgroundColorSlider1.setBackground(Options.getButtonColor());
		optionsBackgroundColorSlider1.setValue(optionsBackgroundColorLabelPanel.getBackground().getRed());
		optionsBackgroundColorSlider1.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				optionsBackgroundColorLabelPanel.setBackground(new Color(
						optionsBackgroundColorSlider1.getValue(), 
						optionsBackgroundColorLabelPanel.getBackground().getGreen(), 
						optionsBackgroundColorLabelPanel.getBackground().getBlue()
						));
			}
		});
		
		final JSlider optionsBackgroundColorSlider2 = new JSlider(0, 255, 16);
		optionsBackgroundColorSlider2.setUI(new ColoredThumbSliderUI(optionsBackgroundColorSlider2, Color.green));
		optionsBackgroundColorSlider2.setBackground(Options.getButtonColor());
		optionsBackgroundColorSlider2.setValue(optionsBackgroundColorLabelPanel.getBackground().getGreen());
		optionsBackgroundColorSlider2.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				optionsBackgroundColorLabelPanel.setBackground(new Color(
						optionsBackgroundColorLabelPanel.getBackground().getRed(), 
						optionsBackgroundColorSlider2.getValue(), 
						optionsBackgroundColorLabelPanel.getBackground().getBlue()
						));
			}
		});
		
		final JSlider optionsBackgroundColorSlider3 = new JSlider(0, 255, 16);
		optionsBackgroundColorSlider3.setUI(new ColoredThumbSliderUI(optionsBackgroundColorSlider3, Color.blue));
		optionsBackgroundColorSlider3.setBackground(Options.getButtonColor());
		optionsBackgroundColorSlider3.setValue(optionsBackgroundColorLabelPanel.getBackground().getBlue());
		optionsBackgroundColorSlider3.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				optionsBackgroundColorLabelPanel.setBackground(new Color(
						optionsBackgroundColorLabelPanel.getBackground().getRed(), 
						optionsBackgroundColorLabelPanel.getBackground().getGreen(),
						optionsBackgroundColorSlider3.getValue() 
						));
			}
		});
		
		optionsBackgroundColorSliders.add(optionsBackgroundColorSlider1);
		optionsBackgroundColorSliders.add(optionsBackgroundColorSlider2);
		optionsBackgroundColorSliders.add(optionsBackgroundColorSlider3);
		
		Button optionsBackgroundColorReset = new Button("Reset", Options.getButtonColor(), Options.bigFont);
		optionsBackgroundColorReset.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				optionsBackgroundColorSlider1.setValue(Options.DEFAULT_BACKGROUND_COLOR.getRed());
				optionsBackgroundColorSlider2.setValue(Options.DEFAULT_BACKGROUND_COLOR.getGreen());
				optionsBackgroundColorSlider3.setValue(Options.DEFAULT_BACKGROUND_COLOR.getBlue());
			}
			
		});
		
		optionsBackgroundColorPanel.add(optionsBackgroundColorLabelPanel);
		optionsBackgroundColorPanel.add(optionsBackgroundColorSliders);
		optionsBackgroundColorPanel.add(optionsBackgroundColorReset);
		
		JPanel optionsTextureDirPanel = new JPanel();
		optionsTextureDirPanel.setLayout(new GridLayout(1, 2, 0, 0));
		optionsTextureDirPanel.setOpaque(false);
		
		JLabel optionsTextureDirLabel = new JLabel("Texture Pack: ", SwingConstants.CENTER);
		optionsTextureDirLabel.setFont(Options.bigFont);
		optionsTextureDirLabel.setBackground(Options.getButtonColor());
		optionsTextureDirLabel.setOpaque(true);
		
		final JComboBox<String> optionsTextureDirList = new JComboBox<String>();
		optionsTextureDirList.setFont(Options.bigFont);
		optionsTextureDirList.setBackground(Options.getButtonColor());
		optionsTextureDirList.setFocusable(false);
		optionsTextureDirList.setEditable(false);
		String[] texturePackList = getTexturePackList();
		for (int i = 0; i < texturePackList.length; i++)
			optionsTextureDirList.addItem(texturePackList[i]);
		
		optionsTextureDirPanel.add(optionsTextureDirLabel);
		optionsTextureDirPanel.add(optionsTextureDirList);	
		
		final JToggleButton optionsPlayerArrowsButton = new JToggleButton();
		optionsPlayerArrowsButton.setBackground(Options.getButtonColor());
		if (Options.arePlayerArrowsShown()) {
			optionsPlayerArrowsButton.setText("Player Arrows: Shown");
			optionsPlayerArrowsButton.setSelected(true);
		}
		else
			optionsPlayerArrowsButton.setText("Player Arrows: Hidden");
		optionsPlayerArrowsButton.setFont(Options.bigFont);
		optionsPlayerArrowsButton.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				if (optionsPlayerArrowsButton.isSelected())
					optionsPlayerArrowsButton.setText("Player Arrows: Shown");
				else
					optionsPlayerArrowsButton.setText("Player Arrows: Hidden");
			}
		});
		
		JPanel optionsButtonsPanel = new JPanel();
		optionsButtonsPanel.setOpaque(false);
		optionsButtonsPanel.setLayout(new GridLayout(1, 2, 3, 3));
		
		Button optionsSaveButton = new Button("Save", Options.getButtonColor(), Options.bigFont);
		optionsSaveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Options.setButtonColor(new Color(optionsButtonColorSlider1.getValue(), optionsButtonColorSlider2.getValue(), optionsButtonColorSlider3.getValue()));
				Options.setBackgroundColor(new Color(optionsBackgroundColorSlider1.getValue(), optionsBackgroundColorSlider2.getValue(), optionsBackgroundColorSlider3.getValue()));
				Options.setTextureDir((String) optionsTextureDirList.getSelectedItem());
				Options.setPlayerArrowsShown(optionsPlayerArrowsButton.isSelected());
				gameInput.updateOptions(getParent());
				gameInput.updateOptions(generatorFrame);
				gameInput.updateOptions(loadFrame);
				cd.show(mainMenuPanel.getParent(), "mainMenuPanel");
			}
		});
		
		Button optionsCancelButton = new Button("Cancel", Options.getButtonColor(), Options.bigFont);
		optionsCancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				optionsButtonColorSlider1.setValue(Options.getButtonColor().getRed());
				optionsButtonColorSlider2.setValue(Options.getButtonColor().getGreen());
				optionsButtonColorSlider3.setValue(Options.getButtonColor().getBlue());
				optionsBackgroundColorSlider1.setValue(Options.getBackgroundColor().getRed());
				optionsBackgroundColorSlider2.setValue(Options.getBackgroundColor().getGreen());
				optionsBackgroundColorSlider3.setValue(Options.getBackgroundColor().getBlue());
				optionsTextureDirList.setSelectedItem(Options.getTextureDir());
				optionsPlayerArrowsButton.setSelected(Options.arePlayerArrowsShown());
				cd.show(mainMenuPanel.getParent(), "mainMenuPanel");
			}
		});
		
		optionsButtonsPanel.add(optionsSaveButton);
		optionsButtonsPanel.add(optionsCancelButton);
		
		optionsPanel.add(optionsPanelTitle);
		optionsPanel.add(optionsButtonColorPanel);
		optionsPanel.add(optionsBackgroundColorPanel);
		optionsPanel.add(optionsTextureDirPanel);
		optionsPanel.add(optionsPlayerArrowsButton);
		optionsPanel.add(new JLabel());
		optionsPanel.add(optionsButtonsPanel);
	}
	
	public void updateCampaignPanel() {
		campaignPanel.update();
	}
	
	/**
	 * 
	 * @return
	 */
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
	
	private String[] getTexturePackList() {
		File folder = new File("resources");
		File[] texturePackFileList = folder.listFiles();
		String[] texturePackList = new String[texturePackFileList.length];
		for (int i = 0; i < texturePackFileList.length; i++)
			texturePackList[i] = texturePackFileList[i].getPath().split("\\\\")[1];
		return texturePackList;
	}
}
