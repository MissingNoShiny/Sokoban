public abstract class Component {
	
	public Component () {
		
	}
	
	public String getNameSprite () {
		return getClass().getSimpleName();
	}	
}
