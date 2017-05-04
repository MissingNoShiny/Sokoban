
import java.awt.Font;

import javax.swing.JSlider;

public class DefaultSlider extends JSlider{

	private static final long serialVersionUID = -3599885387076371591L;
	
	public DefaultSlider(int min, int max, int majorTickSpacing) {
		super(JSlider.HORIZONTAL, min, max, (min+max)/2);
		setMajorTickSpacing(majorTickSpacing);
		setMinorTickSpacing(1);
		setPaintTicks(true);
		setPaintLabels(true);
		setBackground(Options.buttonsColor);
		setFont(new Font(Options.fontName, 0 , 20));
	}
}
