package be.ac.umons.info.sokoban;

/**
 * A placeholder Component to represent empty spaces.
 * @author Vincent Larcin, Joachim Sneessens
 */
public class Blank implements Component {

	@Override
	public String getName() {
		return "Blank";
	}

	@Override
	public boolean canBePassedThrough() {
		return false;
	}
}
