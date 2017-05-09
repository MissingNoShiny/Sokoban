package be.ac.umons.info.sokoban;

/**
 * A Component used to represent a wall.
 * @author Vincent Larcin, Joachim Sneessens
 */
public class Wall implements Component {

	@Override
	public String getName() {
		return "Wall";
	}

	@Override
	public boolean canBePassedThrough() {
		return false;
	}

}
