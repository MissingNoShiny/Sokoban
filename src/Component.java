
public abstract class Component {

	public String getSpriteName () {
		return getClass().getSimpleName();
	}
	
	public boolean canGoTrough () {
		return true;
	};
}
