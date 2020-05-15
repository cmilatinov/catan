package camera;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_SHIFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

import org.joml.Vector3f;

import display.Window;

public class CameraFPS extends Camera {
	
	private float speed = 10.0f;
	
	private final Window window;
	
	private final int mouseMoveCallback;
	
	public CameraFPS(float fov, Window window) {
		super((float)window.getWidth() / (float)window.getHeight(), fov);
		this.window = window;
		this.mouseMoveCallback = window.mouse()
				.registerMouseMoveCallback((double x, double y, double dx, double dy) -> {
					if(window.mouse().isMouseDown(GLFW_MOUSE_BUTTON_LEFT))
						rotate(new Vector3f((float)dy, (float)dx, 0));
				}
			);
		window.mouse().setSensitivity(0.2f);
	}
	
	public void update(double delta) {
		if (window.keyboard().isKeyDown(GLFW_KEY_W))
			pos.add(forward().mul((float)delta * speed));

		if (window.keyboard().isKeyDown(GLFW_KEY_S))
			pos.sub(forward().mul((float)delta * speed));

		if (window.keyboard().isKeyDown(GLFW_KEY_A))
			pos.sub(right().mul((float)delta * speed));

		if (window.keyboard().isKeyDown(GLFW_KEY_D))
			pos.add(right().mul((float)delta * speed));

		if (window.keyboard().isKeyDown(GLFW_KEY_LEFT_SHIFT))
			pos.sub(new Vector3f(0, (float)delta * speed, 0));

		if (window.keyboard().isKeyDown(GLFW_KEY_SPACE))
			pos.add(new Vector3f(0, (float)delta * speed, 0));
	}
	
	public CameraFPS setSpeed(float speed) {
		this.speed = speed;
		return this;
	}
	
	public void destroy() {
		window.mouse().removeMouseMoveCallback(mouseMoveCallback);
	}

}
