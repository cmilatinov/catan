package input;

import camera.Camera;
import display.Window;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWCursorPosCallbackI;
import org.lwjgl.glfw.GLFWMouseButtonCallbackI;
import org.lwjgl.glfw.GLFWScrollCallbackI;

import java.nio.DoubleBuffer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.lwjgl.glfw.GLFW.*;

@SuppressWarnings("unused")
public class MouseInput {

    /**
     * The window from which to catch mouse events.
     */
    private final Window window;

    /**
     * An instance to the class handling cursor callbacks.
     */
    private final MouseCursorCallback cursorCallback;

    /**
     * An instance to the class handling mouse button callbacks.
     */
    private final MouseButtonCallback mouseCallback;

    /**
     * An instance to the class handling mouse wheel callbacks.
     */
    private final MouseWheelCallback scrollCallback;

    /**
     * A hash map describing the various registered cursor callbacks.
     */
    private final Map<Integer, MouseMoveCallback> cursorCallbacks = new ConcurrentHashMap<>();

    /**
     * A hash map describing the various registered mouse click callbacks.
     */
    private final Map<Integer, MouseClickCallback> mouseCallbacks = new ConcurrentHashMap<>();

    /**
     * A hash map describing the various registered mouse scroll callbacks.
     */
    private final Map<Integer, MouseScrollCallback> scrollCallbacks = new ConcurrentHashMap<>();

    /**
     * An integer incremented to produce unique handle values for move callbacks.
     */
    private int nextCursorHandle = 1;

    /**
     * An integer incremented to produce unique handle values for click callbacks.
     */
    private int nextMouseHandle = 1;

    /**
     * An integer incremented to produce unique handle values for scroll callbacks.
     */
    private int nextScrollHandle = 1;

    /**
     * A floating-point number describing the sensitivity of mouse movement.
     */
    private float sensitivity = 1.0f;

    /**
     * The last known mouse coordinates.
     */
    private double lastX = 0, lastY = 0;

    /**
     * Indicates whether the next cursor movement callback should be ignored.
     */
    private boolean ignoreNext = false;


    private class MouseCursorCallback implements GLFWCursorPosCallbackI {

        /**
         * {@inheritDoc}
         */
        public void invoke(long hwnd, double x, double y) {
            if (hwnd != window.getHandle())
                return;

            if (ignoreNext) {
                lastX = x;
                lastY = y;
                ignoreNext = false;
                return;
            }

            double dx = sensitivity * (x - lastX);
            double dy = sensitivity * (y - lastY);
            for (int callback : cursorCallbacks.keySet())
                cursorCallbacks.get(callback).invoke(x, y, dx, dy);

            lastX = x;
            lastY = y;
        }

    }

    private class MouseButtonCallback implements GLFWMouseButtonCallbackI {

        /**
         * {@inheritDoc}
         */
        public void invoke(long hwnd, int button, int action, int mods) {
            if (hwnd != window.getHandle())
                return;

            for (int callback : mouseCallbacks.keySet()) {
                MouseClickCallback cb = mouseCallbacks.get(callback);
                if (cb != null)
                    cb.invoke(button, action, mods);
            }
        }

    }

    private class MouseWheelCallback implements GLFWScrollCallbackI {

        /**
         * {@inheritDoc}
         */
        public void invoke(long hwnd, double xoffset, double yoffset) {
            if (hwnd != window.getHandle())
                return;

            for (int callback : scrollCallbacks.keySet())
                scrollCallbacks.get(callback).invoke(xoffset, yoffset);
        }

    }

    /**
     * Creates a new mouse input object using the specified window as its parent.
     *
     * @param window The window from which to catch mouse events.
     */
    public MouseInput(Window window) {
        this.window = window;
        this.cursorCallback = new MouseCursorCallback();
        this.mouseCallback = new MouseButtonCallback();
        this.scrollCallback = new MouseWheelCallback();
    }

    /**
     * Redirects mouse callbacks to the parent window.
     */
    public void redirectWindowCallbacks() {
        glfwSetCursorPosCallback(window.getHandle(), cursorCallback);
        glfwSetMouseButtonCallback(window.getHandle(), mouseCallback);
        glfwSetScrollCallback(window.getHandle(), scrollCallback);
    }

    /**
     * Centers the mouse cursor in its parent window. This method does not fire a
     * mouse move event.
     *
     * @return {@link MouseInput} This same instance of the class.
     */
    public MouseInput centerCursorInWindow() {
        ignoreNext = true;
        Vector2i center = window.getCenter();
        glfwSetCursorPos(window.getHandle(), center.x, center.y);
        return this;
    }

    /**
     * Sets the mouse cursor position with respect to its parent window. This method
     * does not fire a mouse move event.
     *
     * @param x The new x position of the mouse cursor.
     * @param y The new y position of the mouse cursor.
     * @return {@link MouseInput} This same instance of the class.
     */
    public MouseInput setCursorPosition(double x, double y) {
        ignoreNext = true;
        glfwSetCursorPos(window.getHandle(), x, y);
        return this;
    }

    /**
     * Gets the mouse cursor current position with respect to its parent window.
     *
     * @return {@link Vector2i} Current mouse cursor position.
     */
    public Vector2i getCursorPosition() {
        DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer y = BufferUtils.createDoubleBuffer(1);

        glfwGetCursorPos(window.getHandle(), x, y);

        return new Vector2i((int) x.get(0), (int) y.get(0));
    }

    /**
     * Used to specify the mouse sensitivity factor to use for mouse movement
     * calculations.
     *
     * @param sensitivity The new sensitivity value.
     * @return {@link MouseInput} This same instance of the class.
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
     * @return <b>int</b> A handle corresponding to the registered callback.
     */
    public int registerMouseMoveCallback(MouseMoveCallback mouseMove) {
        cursorCallbacks.put(nextCursorHandle, mouseMove);
        return nextCursorHandle++;
    }

    /**
     * Removes the previously added mouse move callback corresponding to the
     * provided handle. This method has no effect when called with a handle that
     * does not exist.
     *
     * @param callback The handle to the callback to be removed.
     */
    public void removeMouseMoveCallback(int callback) {
        cursorCallbacks.remove(callback);
    }

    /**
     * Used to add a callback to fire when the mouse is scrolled. A handle to the callback is returned.
     * The callback may be removed by using {@link #removeMouseScrollCallback} and passing
     * the handle returned from this method.
     *
     * @param mouseScroll The callback to invoke whenever the mouse is scrolled.
     * @return <b>int</b> A handle corresponding to the registered callback.
     */
    public int registerMouseScrollCallback(MouseScrollCallback mouseScroll) {
        scrollCallbacks.put(nextScrollHandle, mouseScroll);
        return nextScrollHandle++;
    }

    /**
     * Removes the previously added mouse scrolled callback corresponding to the
     * provided handle. This method has no effect when called with a handle that
     * does not exist.
     *
     * @param callback The handle to the callback to be removed.
     */
    public void removeMouseScrollCallback(int callback) {
        scrollCallbacks.remove(callback);
    }

    /**
     * Used to add a callback to fire when a mouse button is activated. A handle to
     * the callback is returned. The callback may be removed by using
     * {@link #removeMouseClickCallback} and passing the handle returned from this
     * method.
     *
     * @param mouseClick The callback to invoke whenever a mouse button is activated.
     * @return <b>int</b> A handle corresponding to the registered callback.
     */
    public int registerMouseClickCallback(MouseClickCallback mouseClick) {
        mouseCallbacks.put(nextMouseHandle, mouseClick);
        return nextMouseHandle++;
    }

    /**
     * Removes the previously added mouse click callback corresponding to the
     * provided handle. This method has no effect when called with a handle that
     * does not exist.
     *
     * @param callback The handle to the callback to be removed.
     */
    public void removeMouseClickCallback(int callback) {
        mouseCallbacks.remove(callback);
    }

    /**
     * Returns the current mouse position on the screen.
     *
     * @return {@link Vector2i} An integer vector containing the position of the mouse cursor.
     */
    public Vector2i getMousePosition() {
        DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer y = BufferUtils.createDoubleBuffer(1);
        glfwGetCursorPos(window.getHandle(), x, y);
        return new Vector2i((int)x.get(), (int)y.get());
    }

    /**
     * Returns whether or not a mouse button is currently being pressed.
     *
     * @param button The mouse button to check.
     * @return <b>boolean</b> True if the specified mouse button is being held
     * down, false otherwise.
     */
    public boolean isMouseDown(int button) {
        return glfwGetMouseButton(window.getHandle(), button) == GLFW_PRESS;
    }

    /**
     * Returns whether or not a mouse button has been released.
     *
     * @param button The mouse button to check.
     * @return <b>boolean</b> True if the specified mouse button is being held
     * down, false otherwise.
     */
    public boolean isMouseUp(int button) {
        return glfwGetMouseButton(window.getHandle(), button) == GLFW_RELEASE;
    }

    /**
     * Returns the ray casted by the mouse at its current coordinates.
     *
     * @param camera The camera through which to cast a ray.
     * @return {@link Vector3f} The ray direction in world coordinates.
     */
    public Vector3f getRayAtMouseCoords(Camera camera) {
        Vector4f clipSpace = new Vector4f((float) lastX * 2.0f / window.getWidth() - 1.0f,
                1.0f - (float) lastY * 2.0f / window.getHeight(), -1f, 1f);
        Vector4f eyeSpace = camera.getProjectionMatrix().invert().transform(clipSpace);
        Vector4f worldSpace = camera.getViewMatrix().invert().transform(new Vector4f(eyeSpace.x, eyeSpace.y, -1, 0));
        return new Vector3f(worldSpace.x, worldSpace.y, worldSpace.z).normalize();
    }

}
