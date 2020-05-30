package ui;

import org.joml.Vector2f;

public class UIConstraints {

	public static final byte PRIORITY_LAST = 0;
	public static final byte PRIORITY_FIRST = 1;

	private UIConstraint x, y, width, height;
	private float rotation;

	public UIConstraints setX(UIConstraint xConstraint) {
		x = xConstraint;
		return this;
	}

	public UIConstraints setY(UIConstraint yConstraint) {
		y = yConstraint;
		return this;
	}

	public UIConstraints setHeightWidth(UIConstraint scaleConstraint)
	{
		width = scaleConstraint;
		height = scaleConstraint;
		return this;
	}

	public UIConstraints setWidth(UIConstraint widthConstraint) {
		width = widthConstraint;
		return this;
	}

	public UIConstraints setHeight(UIConstraint heightConstraint) {
		height = heightConstraint;
		return this;
	}

	public UIConstraint getX() {
		return x;
	}

	public UIConstraint getY() {
		return y;
	}

	public UIConstraint getWidth() {
		return width;
	}

	public UIConstraint getHeight() {
		return height;
	}

	public float getRotation() {
		return rotation;
	}

	public UIConstraints setRotation(float rotation) {
		this.rotation = rotation;
		return this;
	}

	void computeDimensions(UIDimensions parent, UIDimensions result) {
		
		if (width == null && height == null) {
			result.setWidth(parent.getWidth());
			result.setHeight(parent.getHeight());
		} else if (width == null) {
			result.setWidth(parent.getWidth());
			result.setHeight(height.compute(parent, result, UIDimensions.DIMENSION_HEIGHT));
		} else if (height == null) {
			result.setHeight(parent.getHeight());
			result.setWidth(width.compute(parent, result, UIDimensions.DIMENSION_WIDTH));
		} else if (width.getPriority() >= height.getPriority()) {
			result.setWidth(width.compute(parent, result, UIDimensions.DIMENSION_WIDTH));
			result.setHeight(height.compute(parent, result, UIDimensions.DIMENSION_HEIGHT));
		} else {
			result.setHeight(height.compute(parent, result, UIDimensions.DIMENSION_HEIGHT));
			result.setWidth(width.compute(parent, result, UIDimensions.DIMENSION_WIDTH));
		}
		
		if (x == null && y == null) {
			result.setX(parent.getX());
			result.setY(parent.getY());
		} else if (x == null) {
			result.setX(parent.getX());
			result.setY(y.compute(parent, result, UIDimensions.DIMENSION_Y));
		} else if (y == null) {
			result.setY(parent.getY());
			result.setX(x.compute(parent, result, UIDimensions.DIMENSION_X));
		} else if (x.getPriority() >= y.getPriority()) {
			result.setX(x.compute(parent, result, UIDimensions.DIMENSION_X));
			result.setY(y.compute(parent, result, UIDimensions.DIMENSION_Y));
		} else {
			result.setY(y.compute(parent, result, UIDimensions.DIMENSION_Y));
			result.setX(x.compute(parent, result, UIDimensions.DIMENSION_X));
		}

		result.setRotation(rotation);
	}

}