package ui;

import ui.animation.UIAnimation;
import ui.animation.UIAnimator;

import java.util.ArrayList;
import java.util.List;

public class UIComponent {

	final UIDimensions dimensions;
	private UIConstraints constraints = null;
	private final UIAnimator animator = new UIAnimator();
	
	protected List<UIComponent> children = new ArrayList<UIComponent>();
	
	private boolean visible = true;
	
	public UIComponent(UIDimensions dimensions) {
		this.dimensions = dimensions;
	}
	
	public UIComponent() {
		this.dimensions = new UIDimensions();
	}
	
	void setConstraints(UIConstraints constraints) {
		this.constraints = constraints;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
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
	
	public final void computeChildrenDimensions() {
		
		for (int i = 0; i < children.size(); i++) {

			UIComponent child = children.get(i);

			int lastWidth = child.dimensions.getWidth();
			int lastHeight = child.dimensions.getHeight();

			// If constraints exist, compute dimensions using the constraints
			if(child.getConstraints() != null) 
				child.getConstraints().computeDimensions(dimensions, child.dimensions);

			// Otherwise, set the dimensions to be the same as those of the parent component
			else
				child.dimensions.set(dimensions);

			// Set elevation indices
			child.dimensions.setElevation(dimensions.getElevation() + 1);
			child.dimensions.setElevationInParent(i);

			// Text specific update boolean
			if (child instanceof UIText && (lastWidth != child.dimensions.getWidth() || lastHeight != child.dimensions.getHeight()))
				((UIText) child).shouldUpdateImage = true;

			// Compute the children's children's dimensions
			child.computeChildrenDimensions();
			
		}
		
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
		animator.update(delta);
		for (UIComponent child : children)
			child.update(delta);
	}

	public boolean shouldUpdate() {
		return animator.shouldUpdate();
	}
	
}
