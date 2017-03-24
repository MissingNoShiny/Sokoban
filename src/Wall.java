
public class Wall implements Component{

	@Override
	public String getSpriteName() {
		return getClass().getSimpleName();
	}

	@Override
	public boolean cannotGoTrough() {
		return true;
	}

}
