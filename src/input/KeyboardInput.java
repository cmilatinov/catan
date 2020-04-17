package input;

import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

import java.util.HashMap;

import org.lwjgl.glfw.GLFWKeyCallback;

/**
 * Manages key input and assigns key callbacks.
 */
public class KeyboardInput extends GLFWKeyCallback {

	// Array holding booleans of each key to keep track of which one is being used
	private static final boolean keys[] = new boolean[Short.MAX_VALUE * 2];

	// Hash maps containing bindings for key presses and releases.
	private final HashMap<Integer, KeyCallback> keyPressed = new HashMap<Integer, KeyCallback>();
	private final HashMap<Integer, KeyCallback> keyReleased = new HashMap<Integer, KeyCallback>();

	/**
	 * {@inheritDoc}
	 */
	public void invoke(long window, int key, int scancode, int action, int mods) {

		keys[key] = action != GLFW_RELEASE;

		if (keyReleased.get(key) != null && action == GLFW_RELEASE)
			keyReleased.get(key).invoke(mods);

		else if (keyPressed.get(key) != null)
			keyPressed.get(key).invoke(mods);

	}

	/**
	 * Returns whether or not a key is currently being pressed.
	 * 
	 * @param key The keycode to check.
	 * @return [<b>boolean</b>] True if the specified key is being held down, false
	 *         otherwise.
	 */
	public static boolean isKeyDown(int key) {
		return keys[key];
	}

	/**
	 * Registers a callback for a key release.
	 * 
	 * @param key      The keycode to bind the callback to.
	 * @param callback The callback to execute when the key is released.
	 */
	public void registerKeyUp(int key, KeyCallback callback) {
		keyReleased.put(key, callback);
	}

	/**
	 * Registers a callback for a key press.
	 * 
	 * @param key      The keycode to bind the callback to.
	 * @param callback The callback to execute when the key is pressed down.
	 */
	public void registerKeyDown(int key, KeyCallback callback) {
		keyPressed.put(key, callback);
	}

}
