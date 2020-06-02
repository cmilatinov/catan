package ui;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import ui.animation.UIAnimationMetrics;
import ui.animation.UIAnimator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;


public class UIComponent {

	private static final float ELEVATION_DISTANCE = 0.01f;
	private static final float ELEVATION_PARENT_DISTANCE = 0.001f;

	final UIDimensions dimensions;
	final UIDimensions lastDimensions;
	boolean sizeChanged = false;

	private UIConstraints constraints = null;
	private final UIAnimator animator = new UIAnimator();
	
	protected List<UIComponent> children = new ArrayList<>();

	private boolean isInteractable = true;
	private boolean visible = true;

	public UIComponent(UIDimensions dimensions) {
		this.dimensions = dimensions;
		this.lastDimensions = new UIDimensions();
	}
	
	public UIComponent() {
		this.dimensions = new UIDimensions();
		this.lastDimensions = new UIDimensions();
	}
	
	void setConstraints(UIConstraints constraints) {
		this.constraints = constraints;
	}
	
	public UIComponent setVisible(boolean visible) {
		this.visible = visible;
		return this;
	}
	
	public UIConstraints getConstraints() {
		return this.constraints;
	}
	
	public UIDimensions getDimensions() {
		return new UIDimensions().set(dimensions);
	}
	
	public boolean isVisible() {
		return visible;
	}

	public boolean isInteractable() {
		return this.isInteractable;
	}

	public void setIsInteractable(boolean isInteractable) {
		this.isInteractable = isInteractable;
	}

	public Stream<UIComponent> flatten()
	{
		return Stream.concat(Stream.of(this), children.stream().flatMap(UIComponent::flatten));
	}

	public void onMouseClick() {

	}

	public void onMouseHover() {

	}

	public void onMouseHoverExit() {

	}
	
	public UIComponent add(UIComponent component, UIConstraints constraints) {
		component.setConstraints(constraints);
		children.add(component);
		return this;
	}

	public UIAnimator animator() {
		return animator;
	}

	public final void update(double delta) {

		// Update the animator if needed
		if (animator.shouldUpdate())
			animator.update(delta);

		// Update this component's bounds if needed
		if (animator.hasAnimation()) {
			UIAnimationMetrics animMetrics = animator.getCurrentAnimationMetrics();
			dimensions
					.setX(dimensions.getX() + (int) animMetrics.x)
					.setY(dimensions.getY() + (int) animMetrics.y)
					.setWidth((int) (dimensions.getWidth() * animMetrics.scale))
					.setHeight((int) (dimensions.getHeight() * animMetrics.scale))
					.setRotation(dimensions.getRotation() + animMetrics.rotation);
		}

		for (int i = 0; i < children.size(); i++) {

			UIComponent child = children.get(i);

			// If constraints exist, compute dimensions using the constraints
			if(child.getConstraints() != null)
				child.getConstraints().computeDimensions(dimensions, child.dimensions);

			// Otherwise, set the dimensions to be the same as those of the parent component
			else
				child.dimensions.set(dimensions);

			// Set elevation indices
			child.dimensions
					.setElevation(dimensions.getElevation() + 1)
					.setElevationInParent(i);

			// Set size changed if width or height is different
			child.sizeChanged = child.dimensions.getWidth() != child.lastDimensions.getWidth()
					|| child.dimensions.getHeight() != child.lastDimensions.getHeight();

			// Update the children
			child.update(delta);

			// Set last dimensions
			child.lastDimensions.set(child.dimensions);

		}

	}

	public Matrix4f computeModelMatrix(int screenWidth, int screenHeight) {
		Matrix4f result = new Matrix4f();

		result.translate(new Vector3f(
				2.0f * dimensions.getCenterX() / screenWidth - 1.0f,
				1.0f - 2.0f * dimensions.getCenterY() / screenHeight,
				-ELEVATION_DISTANCE * dimensions.getElevation() - ELEVATION_PARENT_DISTANCE * dimensions.getElevationInParent()));
		result.scale(new Vector3f((float) dimensions.getWidth() / screenWidth, (float) dimensions.getHeight() / screenHeight, 1));
		result.rotate((float) (Math.toRadians(dimensions.getRotation())), new Vector3f(0, 0, 1));

		return result;
	}

}
