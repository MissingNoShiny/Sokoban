
public abstract class Component {

	public String getName () {
		return getClass().getSimpleName();
	}
	
	public boolean canGoTrough () {
		return true;
	};
}
