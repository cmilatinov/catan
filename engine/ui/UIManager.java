package ui;

import display.Window;
import ui.constraints.PixelConstraint;

import java.awt.*;

public class UIManager {

	private final UIText framerate;

	private final UIComponent root;
	
	private final UIRenderer renderer;

	private float time = 0;
	private int fps = 0;
	
	public UIManager(Window window) {
		root = new UIComponent(new UIDimensions()
				.setX(0)
				.setY(0)
				.setWidth(window.getWidth())
				.setHeight(window.getHeight())
				.setElevation(0))
				.setVisible(false);
		renderer = new UIRenderer(window);

		// FPS
		framerate = new UIText(new Font("Arial", Font.BOLD, 30), "FPS");
		framerate.setColor(UIColor.GREEN);
		UIConstraints fpsConstraints = new UIConstraints()
				.setX(new PixelConstraint(4, UIDimensions.DIRECTION_LEFT))
				.setY(new PixelConstraint(4, UIDimensions.DIRECTION_BOTTOM))
				.setWidth(new PixelConstraint(100))
				.setHeight(new PixelConstraint(30));
		root.add(framerate, fpsConstraints);
	}
	
	public UIComponent getContainer() {
		return root;
	}
	
	public void update(double delta) {
		time += delta;
		fps++;
		if (time > 1.0f) {
			framerate.setText(Integer.toString(fps));
			time = fps = 0;
		}
		root.update(delta);
	}
	
	public void render() {
		renderer.render(root);
	}
	
}
