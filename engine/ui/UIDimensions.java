package ui;

import org.joml.Matrix2f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class UIDimensions {
	
	public static final int DIMENSIONS = 2;
	
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
	
	private static final float ELEVATION_DISTANCE = 0.01f;
	
	private int width = -1, height = -1;
	private int x = -1, y = -1;
	private int elevation = -1;
	private float rotation;

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

	UIDimensions setRotation(float rotation)
	{
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
	
	public int getElevation() {
		return elevation;
	}

	public float getRotation()
	{
		return this.rotation;
	}

	void reset() {
		this.elevation = this.width = this.height = this.x = this.y = -1;
	}


	public Matrix4f computeModelMatrix(int screenWidth, int screenHeight) {
		Matrix4f result = new Matrix4f();

		result.translate(new Vector3f(2.0f * getCenterX() / screenWidth - 1.0f, 1.0f - 2.0f * getCenterY() / screenHeight, -ELEVATION_DISTANCE * elevation));
		result.scale(new Vector3f((float) width / screenWidth, (float) height / screenHeight, 1));
		result.rotate((float)(Math.toRadians(rotation)), new Vector3f(0, 0, 1));
		return result;
	}
	
	public String toString() {
		return "[" + x + ", " + y + ", " + width + ", " + height + ", " + elevation + "]";
	}
	
}
