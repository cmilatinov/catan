package input;

import display.Window;
import org.lwjgl.glfw.GLFWKeyCallbackI;

import java.util.HashMap;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Manages key input and assigns key callbacks.
 */
public class KeyboardInput implements GLFWKeyCallbackI {

    /**
     * The window from which to catch key events.
     */
    private final Window window;

    /**
     * Hash map containing bindings for key presses.
     */
    private final HashMap<Integer, KeyActionCallback> keyPressed = new HashMap<Integer, KeyActionCallback>();

    /**
     * Hash map containing bindings for key releases.
     */
    private final HashMap<Integer, KeyActionCallback> keyReleased = new HashMap<Integer, KeyActionCallback>();

    /**
     * Creates a new keyboard input object using the specified window as its parent.
     *
     * @param window The window from which to catch key events.
     */
    public KeyboardInput(Window window) {
        this.window = window;
    }

    /**
     * {@inheritDoc}
     */
    public void invoke(long window, int key, int scancode, int action, int mods) {

        if (window != this.window.getHandle())
            return;

        if (keyReleased.get(key) != null && action == GLFW_RELEASE)
            keyReleased.get(key).invoke(mods);

        else if (keyPressed.get(key) != null)
            keyPressed.get(key).invoke(mods);

    }

    /**
     * Returns whether or not a key is currently being pressed.
     *
     * @param key The keycode to check.
     * @return <b>boolean</b> True if the specified key is being held down, false
     * otherwise.
     */
    public boolean isKeyDown(int key) {
        return glfwGetKey(window.getHandle(), key) == GLFW_PRESS;
    }

    /**
     * Registers a callback for a key release.
     *
     * @param key      The keycode to bind the callback to.
     * @param callback The callback to execute when the key is released.
     */
    public void registerKeyUp(int key, KeyActionCallback callback) {
        keyReleased.put(key, callback);
    }

    /**
     * Registers a callback for a key press.
     *
     * @param key      The keycode to bind the callback to.
     * @param callback The callback to execute when the key is pressed down.
     */
    public void registerKeyDown(int key, KeyActionCallback callback) {
        keyPressed.put(key, callback);
    }

}
