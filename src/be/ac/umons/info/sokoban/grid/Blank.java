package be.ac.umons.info.sokoban.grid;

/**
 * A placeholder Component to represent empty spaces.
 * @author Vincent Larcin, Joachim Sneessens
 */
class Blank implements Component {

	@Override
	public String getName() {
		return "Blank";
	}

	@Override
	public boolean canBePassedThrough() {
		return false;
	}
}
