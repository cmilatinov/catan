package input;

import static org.lwjgl.glfw.GLFW.glfwSetCursorPos;

import java.util.HashMap;

import org.joml.Vector2i;
import org.lwjgl.glfw.GLFWCursorPosCallbackI;

import display.Window;

public class MouseInput implements GLFWCursorPosCallbackI {

	private final Window window;

	private int nextHandle = 1;
	private HashMap<Integer, MouseMoveCallback> callbacks = new HashMap<Integer, MouseMoveCallback>();

	private float sensitivity = 1.0f;
	private double lastX = 0, lastY = 0;

	private boolean ignoreNext = false;

	/**
	 * Creates a new mouse input object using the specified window as its parent.
	 * 
	 * @param window The window from which to catch mouse events.
	 */
	public MouseInput(Window window) {
		this.window = window;
	}

	/**
	 * {@inheritDoc}
	 */
	public void invoke(long window, double x, double y) {

		if (window != this.window.getHandle())
			return;

		if (ignoreNext) {
			lastX = x;
			lastY = y;
			ignoreNext = false;
			return;
		}

		double dx = sensitivity * (x - lastX);
		double dy = sensitivity * (y - lastY);
		for(int callback : callbacks.keySet())
			callbacks.get(callback).invoke(x, y, dx, dy);

		lastX = x;
		lastY = y;

	}

	/**
	 * Centers the mouse cursor in its parent window. This method does not fire a
	 * mouse move event.
	 * 
	 * @return [{@link MouseInput}] This same instance of the class.
	 */
	public MouseInput centerCursorInWindow() {
		ignoreNext = true;
		Vector2i center = window.getCenter();
		glfwSetCursorPos(window.getHandle(), (double) center.x / 2, (double) center.y / 2);
		return this;
	}

	/**
	 * Sets the mouse cursor position with respect to its parent window. This method
	 * does not fire a mouse move event.
	 * 
	 * @param x The new x position of the mouse cursor.
	 * @param y The new y position of the mouse cursor.
	 * @return [{@link MouseInput}] This same instance of the class.
	 */
	public MouseInput setCursorPosition(double x, double y) {
		ignoreNext = true;
		glfwSetCursorPos(window.getHandle(), x, y);
		return this;
	}

	/**
	 * Used to specify the mouse sensitivity factor to use for mouse movement
	 * calculations.
	 * 
	 * @param sensitivity The new sensitivity value.
	 * @return [{@link MouseInput}] This same instance of the class.
	 */
	public MouseInput setSensitivity(float sensitivity) {
		this.sensitivity = sensitivity;
		return this;
	}

	/**
	 * Used to add a callback to fire when the mouse is moved. The callback is not
	 * invoked when the mouse cursor is move through {@link #centerCursorInWindow}
	 * or {@link #setCursorPosition}. A handle to the callback is returned. The
	 * callback may be removed by using {@link #removeMouseMoveCallback} and passing
	 * the handle returned from this method.
	 * 
	 * @param mouseMove The callback to invoke whenever the mouse is moved.
	 * @return [{@link MouseInput}] This same instance of the class.
	 */
	public int registerMouseMoveCallback(MouseMoveCallback mouseMove) {
		callbacks.put(nextHandle, mouseMove);
		return nextHandle++;
	}

	/**
	 * Removes the previously added mouse move callback corresponding to the
	 * provided handle. This method has no effect when called with a handle that
	 * does not exist.
	 * 
	 * @param callback The handle to callback to be removed.
	 */
	public void removeMouseMoveCallback(int callback) {
		callbacks.remove(callback);
	}
	
}
