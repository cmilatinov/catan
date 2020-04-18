package display;

import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR;
import static org.lwjgl.glfw.GLFW.GLFW_DECORATED;
import static org.lwjgl.glfw.GLFW.GLFW_DONT_CARE;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_FLOATING;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_COMPAT_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_FORWARD_COMPAT;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowAttrib;
import static org.lwjgl.glfw.GLFW.glfwSetWindowMonitor;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

import static org.lwjgl.glfw.GLFW.*;
import java.nio.IntBuffer;

import org.joml.Vector2f;
import org.joml.Vector2i;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import input.KeyboardInput;
import input.MouseInput;
import log.Logger;
import main.Engine;

public class Window {

	private static final int NULL = 0;

	private long window = NULL;
	private String name;
	private DisplayMode mode = DisplayMode.DEFAULT;

	private DisplayModeChangedCallback onDisplayModeChanged = null;

	private final KeyboardInput keyboard = new KeyboardInput(this);
	private final MouseInput mouse = new MouseInput(this);

	/**
	 * Creates a new window object with the given name.
	 * 
	 * @param name The name of the window, displayed in the OS.
	 */
	public Window(String name) {
		this.name = name;
	}

	/**
	 * Creates a new window object with the given name and display mode.
	 * 
	 * @param name The name of the window, displayed in the OS.
	 * @param mode The initial display mode of the window, may be changed later.
	 */
	public Window(String name, DisplayMode mode) {
		this.name = name;
		this.mode = mode;
	}

	/**
	 * Initializes the window with the given display mode. If no display mode was
	 * provided when creating the window object, this method will use a default
	 * display mode. If creating the window fails, the engine is stopped and an
	 * error is logged.
	 */
	public void create() {

		// Init GLFW.
		if (!glfwInit()) {
			Engine.LOGGER.log(Logger.ERROR, "Unable to initialize GLFW.");
			Engine.stop(Engine.ERR_GLFW_INIT);
		}

		// Window hints
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_FALSE);
		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_COMPAT_PROFILE);
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

		glfwWindowHint(GLFW_DECORATED, mode.isDecorated() ? GLFW_TRUE : GLFW_FALSE);
		glfwWindowHint(GLFW_FLOATING, mode.isAlwaysOnTop() ? GLFW_TRUE : GLFW_FALSE);

		// Create window
		GLFWVidMode vmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		int w = mode.getWidth() == DisplayMode.MAX_SIZE ? vmode.width() : mode.getWidth();
		int h = mode.getHeight() == DisplayMode.MAX_SIZE ? vmode.height() : mode.getHeight();
		window = glfwCreateWindow(
				// Window size
				w, h,
				// Window name
				name,
				// Fullscreen monitor
				NULL,
				// No sharing resources
				NULL);

		// Fullscreen / position
		if (mode.isFullscreen())
			glfwSetWindowMonitor(window, glfwGetPrimaryMonitor(), 0, 0, w, h, GLFW_DONT_CARE);
		else
			glfwSetWindowPos(window, mode.getX() == DisplayMode.CENTER ? (vmode.width() / 2) - (w / 2) : mode.getX(),
					mode.getY() == DisplayMode.CENTER ? (vmode.height() / 2) - (h / 2) : mode.getY());

		// Assign callbacks
		glfwSetKeyCallback(window, keyboard);
		glfwSetCursorPosCallback(window, mouse);

		glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);

		// Set current context
		glfwMakeContextCurrent(window);

		// VSYNC
		glfwSwapInterval(mode.isVSYNC() ? 1 : 0);

		// Show window
		glfwShowWindow(window);

		// Create OpenGL capabilities
		GL.createCapabilities();

	}

	/**
	 * Sets the callback to invoke when the display mode of the window has changed.
	 * 
	 * @param onDisplayModeChanged The new callback to invoke.
	 */
	public Window setDisplayModeChangedCallback(DisplayModeChangedCallback onDisplayModeChanged) {
		this.onDisplayModeChanged = onDisplayModeChanged;
		return this;
	}

	/**
	 * Changes this window's display mode given that this window was already
	 * created.
	 * 
	 * @param mode The new display mode to set.
	 */
	public Window setDisplayMode(DisplayMode mode) {

		if (window == NULL)
			return this;

		// Set window attributes
		glfwSetWindowAttrib(window, GLFW_DECORATED, mode.isDecorated() ? GLFW_TRUE : GLFW_FALSE);
		glfwSetWindowAttrib(window, GLFW_FLOATING, mode.isAlwaysOnTop() ? GLFW_TRUE : GLFW_FALSE);

		// Change window size and undo fullscreen
		GLFWVidMode vmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		int w = mode.getWidth() == DisplayMode.MAX_SIZE ? vmode.width() : mode.getWidth();
		int h = mode.getHeight() == DisplayMode.MAX_SIZE ? vmode.height() : mode.getHeight();
		glfwSetWindowMonitor(window, NULL, 0, 0, w, h, GLFW_DONT_CARE);

		// Fullscreen / position
		if (mode.isFullscreen())
			glfwSetWindowMonitor(window, glfwGetPrimaryMonitor(), 0, 0, w, h, GLFW_DONT_CARE);
		else
			glfwSetWindowPos(window, mode.getX() == DisplayMode.CENTER ? (vmode.width() / 2) - (w / 2) : mode.getX(),
					mode.getY() == DisplayMode.CENTER ? (vmode.height() / 2) - (h / 2) : mode.getY());

		// VSYNC
		glfwSwapInterval(mode.isVSYNC() ? 1 : 0);

		// Show window
		glfwShowWindow(window);

		// Mode has changed
		this.mode = mode;

		// Run callback
		if (onDisplayModeChanged != null)
			onDisplayModeChanged.invoke(mode);

		return this;
	}

	/**
	 * Returns a vector representing the center point of this window in its own
	 * coordinate space.
	 * 
	 * @return [{@link Vector2f}] The center of this window.
	 */
	public Vector2i getCenter() {
		IntBuffer width = BufferUtils.createIntBuffer(1);
		IntBuffer height = BufferUtils.createIntBuffer(1);
		glfwGetWindowSize(window, width, height);
		return new Vector2i(width.get(0), height.get(0));
	}

	/**
	 * Returns this window's GLFW handle.
	 * 
	 * @return [<b>long</b>] The handle representing this window in GLFW.
	 */
	public long getHandle() {
		return window;
	}

	/**
	 * Returns this window's mouse input object. [{@link KeyboardInput}] The mouse
	 * input object used to assign mouse actions.
	 */
	public MouseInput mouse() {
		return mouse;
	}

	/**
	 * Returns this window's keyboard input object. [{@link KeyboardInput}] The
	 * keyboard input object used to assign key actions.
	 */
	public KeyboardInput keyboard() {
		return keyboard;
	}

	/**
	 * Indicates whether or not this window should close.
	 * 
	 * @return [<b>boolean</b>] True if the window has received a closing message,
	 *         false otherwise.
	 */
	public boolean shouldClose() {
		return glfwWindowShouldClose(window);
	}

	/**
	 * Sends a closing message to the underlying window.
	 */
	public void close() {
		glfwSetWindowShouldClose(window, true);
	}

	/**
	 * Destroys the underlying window.
	 */
	public void destroy() {
		glfwDestroyWindow(window);
	}

	/**
	 * Updates the window by swapping its draw buffers and polling any new events
	 * received.
	 */
	public void update() {
		glfwSwapBuffers(window);
		glfwPollEvents();
	}

}
