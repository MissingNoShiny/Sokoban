
public class Ground implements Component{

	@Override
	public String getSpriteName() {
		return getClass().getSimpleName();
	}

	@Override
	public boolean canGoTrough() {
		return true;
	}

}
