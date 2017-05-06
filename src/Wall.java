
/**
 * A Component used to represent a wall.
 * @author Vincent Larcin, Joachim Sneessens
 */
public class Wall extends Component{

	@Override
	public boolean canBePassedThrough() {
		return false;
	}

}
