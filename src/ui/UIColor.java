package ui;

public class UIColor {
	
	public static final UIColor RED = new UIColor(1.0f, 0, 0, 1.0f);
	public static final UIColor GREEN = new UIColor(0, 1.0f, 0, 1.0f);
	public static final UIColor BLUE = new UIColor(0, 0, 1.0f, 1.0f);
	
	public static final UIColor LIGHT_GRAY = new UIColor(0.8f, 0.8f, 0.8f, 1.0f);
	public static final UIColor DARK_GRAY = new UIColor(0.4f, 0.4f, 0.4f, 1.0f);
	
	private final float r, g, b, a;
	
	public UIColor(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
	
	public float getR() {
		return r;
	}
	
	public float getG() {
		return g;
	}
	
	public float getB() {
		return b;
	}
	
	public float getA() {
		return a;
	}
	
}
