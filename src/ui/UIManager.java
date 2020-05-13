package ui;

import display.Window;

public class UIManager {
	
	private final UIComponent root;
	
	private final UIRenderer renderer;
	
	public UIManager(Window window) {
		this.root = new UIComponent(new UIDimensions()
				.setX(0)
				.setY(0)
				.setWidth(window.getWidth())
				.setHeight(window.getHeight())
				.setElevation(0));
		this.renderer = new UIRenderer(window);
	}
	
	public UIComponent getContainer() {
		return root;
	}
	
	public void update(double delta) {
		root.computeChildrenDimensions();
	}
	
	public void render() {
		renderer.render(root);
	}
	
}
