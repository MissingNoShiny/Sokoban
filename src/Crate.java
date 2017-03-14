import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Crate extends Position {

	public Crate(int x, int y) {
		super(x, y, new BufferedImage(32, 32, BufferedImage.TYPE_INT_RGB));
		setSprite();
	}
	
	public void setSprite() {
		try {
		    sprite = ImageIO.read(new File("resources/crate.png"));
		} catch (IOException e) {
		}
	}
	
	public void moveUp() {
		y -= 1;
	}
	
	public void moveDown() {
		y += 1;
	}	
	
	public void moveRight() {
		x += 1;
	}
	
	public void moveLeft() {
		x -= 1;
	}
	
}
