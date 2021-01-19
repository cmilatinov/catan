package ui;

public class UIDimensions {
	
	public static final int DIMENSION_X = 0;
	public static final int DIMENSION_Y = 1;
	public static final int DIMENSION_WIDTH = 2;
	public static final int DIMENSION_HEIGHT = 3;
	
	public static final int DIRECTION_POSITIVE = 0;
	public static final int DIRECTION_NEGATIVE = 1;
	
	public static final int DIRECTION_TOP = DIRECTION_NEGATIVE;
	public static final int DIRECTION_BOTTOM = DIRECTION_POSITIVE;
	
	public static final int DIRECTION_LEFT = DIRECTION_NEGATIVE;
	public static final int DIRECTION_RIGHT = DIRECTION_POSITIVE;
	
	private int width = -1, height = -1;
	private int x = -1, y = -1;
	private float rotation = 0;
	private int elevation = -1, elevationInParent = -1;

	UIDimensions setWidth(int width) {
		this.width = width;
		return this;
	}
	
	UIDimensions setHeight(int height) {
		this.height = height;
		return this;
	}
	
	UIDimensions setX(int x) {
		this.x = x;
		return this;
	}
	
	UIDimensions setY(int y) {
		this.y = y;
		return this;
	}
	
	UIDimensions setElevation(int elevation) {
		this.elevation = elevation;
		return this;
	}

	UIDimensions setElevationInParent(int elevationInParent) {
		this.elevationInParent = elevationInParent;
		return this;
	}

	UIDimensions setRotation(float rotation) {
		this.rotation = rotation;
		return this;
	}
	
	UIDimensions set(UIDimensions dimensions) {
		this.width = dimensions.width;
		this.height = dimensions.height;
		this.x = dimensions.x;
		this.y = dimensions.y;
		this.elevation = dimensions.elevation;
		this.rotation = dimensions.rotation;
		return this;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getLeftX() {
		return x;
	}
	
	public int getRightX() {
		return x + width;
	}
	
	public int getTopY() {
		return y;
	}
	
	public int getBottomY() {
		return y + height;
	}
	
	public int getCenterX() {
		return x + width / 2;
	}
	
	public int getCenterY() {
		return y + height / 2;
	}

	public float getRotation()
	{
		return this.rotation;
	}

	public int getElevation() {
		return elevation;
	}

	public int getElevationInParent() {
		return elevationInParent;
	}

	public String toString() {
		return "[" + getX() + ", " + getY() + ", " + getWidth() + ", " + getHeight() + "]";
	}

}
