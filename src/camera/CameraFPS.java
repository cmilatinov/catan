package camera;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_SHIFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;

import org.joml.Vector3f;

import input.KeyboardInput;
import input.MouseInput;

public class CameraFPS extends Camera {
	
	private float speed = 10.0f;
	
	private KeyboardInput keyboard;
	private MouseInput mouse;
	private int mouseCallback;
	
	public CameraFPS(float aspect, float fov, KeyboardInput keyboard, MouseInput mouse) {
		super(aspect, fov);
		this.keyboard = keyboard;
		this.mouse = mouse;
		this.mouseCallback = this.mouse.setSensitivity(0.3f).registerMouseMoveCallback((double x, double y, double dx, double dy) -> 
			rotate(new Vector3f((float)dy, (float)dx, 0)));
	}
	
	public void update(double delta) {
		if (keyboard.isKeyDown(GLFW_KEY_W))
			pos = pos.add(forward().mul((float)delta * speed));

		if (keyboard.isKeyDown(GLFW_KEY_S))
			pos.sub(forward().mul((float)delta * speed));

		if (keyboard.isKeyDown(GLFW_KEY_A))
			pos.sub(right().mul((float)delta * speed));

		if (keyboard.isKeyDown(GLFW_KEY_D))
			pos = pos.add(right().mul((float)delta * speed));

		if (keyboard.isKeyDown(GLFW_KEY_LEFT_SHIFT))
			pos.sub(new Vector3f(0, (float)delta * speed, 0));

		if (keyboard.isKeyDown(GLFW_KEY_SPACE))
			pos = pos.add(new Vector3f(0, (float)delta * speed, 0));
	}
	
	public CameraFPS setSpeed(float speed) {
		this.speed = speed;
		return this;
	}
	
	public void destroy() {
		mouse.removeMouseMoveCallback(mouseCallback);
	}

}
