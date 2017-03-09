
public class Game {
	
	Position [][] matrix;
	int height, width;
	
	public Game (int height, int width) {
		matrix = new Position [height][width];
		this.height = height;
		this.width = width;
		
	}
	public int getHeight() {
		return height;
	}
	
	public int getWidth() {
		return width;
	}
	
	/*public boolean isFree(int posX, int posY) {
		if ((posX < height) && (posY < width)) {
			//retourne un rensignement sur ce que contient la case concernee
		}
		else{
			// Erreur de OUF
		}
	}*/
}
