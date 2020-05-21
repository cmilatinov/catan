package ui;

public final class UIQuad extends UIComponent {
	
	private UIColor color = UIColor.DARK_GRAY;
	
	private int borderRadius = 0;
	
	public UIQuad setBorderRadius(int borderRadius) {
		this.borderRadius = borderRadius;
		return this;
	}
	
	public UIQuad setColor(UIColor color) {
		this.color = new UIColor(color);
		return this;
	}
	
	public int getBorderRadius() {
		return borderRadius;
	}
	
	public UIColor getColor() {
		return color;
	}
	
}
