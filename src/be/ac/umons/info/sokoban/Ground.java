package be.ac.umons.info.sokoban;

/**
 * A Component used to represent ground.
 * @author Vincent Larcin, Joachim Sneessens
 */
public class Ground implements Component{

	@Override
	public boolean canBePassedThrough() {
		return true;
	}
}
