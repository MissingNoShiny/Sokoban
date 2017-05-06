package be.ac.umons.info.sokoban;

/**
 * A Component used to represent a goal.
 * @author Vincent Larcin, Joachim Sneessens
 */
public class Goal implements Component{

	@Override
	public boolean canBePassedThrough() {
		return true;
	}

}
