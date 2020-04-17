package main;

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
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowAttrib;
import static org.lwjgl.glfw.GLFW.glfwSetWindowMonitor;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import input.KeyboardInput;
import log.Logger;

public class Window {
	
	private static final int NULL = 0; 						
	
	private long window = NULL;
	private String name;
	private DisplayMode mode = DisplayMode.DEFAULT;

	private DisplayModeChangedCallback onDisplayModeChanged = null;
	
	private final KeyboardInput keyboard = new KeyboardInput();
	
	/**
	 * Creates a new window object with the given name.
	 * @param name The name of the window, displayed in the OS.
	 */
	public Window(String name) {
		this.name = name;
	}
	
	/**
	 * Creates a new window object with the given name and display mode.
	 * @param name The name of the window, displayed in the OS.
	 * @param mode The initial display mode of the window, may be changed later.
	 */
	public Window(String name, DisplayMode mode) {
		this.name = name;
		this.mode = mode;
	}
	
	/**
	 * Initializes the window with the given display mode. If no display mode was provided 
	 * when creating the window object, this method will use a default display mode.
	 * If creating the window fails, the engine is stopped and an error is logged.
	 */
	public void create() {
		
		// Init GLFW.
		if(!glfwInit()) {
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
		if(mode.isFullscreen())
			glfwSetWindowMonitor(window, glfwGetPrimaryMonitor(), 0, 0, w, h, GLFW_DONT_CARE);
		else
			glfwSetWindowPos(
					window, 
					mode.getX() == DisplayMode.CENTER ? (vmode.width() / 2) - (w / 2) : mode.getX(), 
					mode.getY() == DisplayMode.CENTER ? (vmode.height() / 2) - (h / 2) : mode.getY());
		
		// Assign key callback
		glfwSetKeyCallback(window, keyboard);
		
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
	 * @param onDisplayModeChanged The new callback to invoke.
	 */
	public void setDisplayModeChangedCallback(DisplayModeChangedCallback onDisplayModeChanged) {
		this.onDisplayModeChanged = onDisplayModeChanged;
	}
	
	/**
	 * Changes this window's display mode given that this window was already created.
	 * @param mode The new display mode to set.
	 */
	public void setDisplayMode(DisplayMode mode) {
		
		if(window == NULL)
			return;
		
		// Set window attributes
		glfwSetWindowAttrib(window, GLFW_DECORATED, mode.isDecorated() ? GLFW_TRUE : GLFW_FALSE);
		glfwSetWindowAttrib(window, GLFW_FLOATING, mode.isAlwaysOnTop() ? GLFW_TRUE : GLFW_FALSE);
		
		// Change window size and undo fullscreen
		GLFWVidMode vmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		int w = mode.getWidth() == DisplayMode.MAX_SIZE ? vmode.width() : mode.getWidth();
		int h = mode.getHeight() == DisplayMode.MAX_SIZE ? vmode.height() : mode.getHeight();
		glfwSetWindowMonitor(window, NULL, 0, 0, w, h, GLFW_DONT_CARE);
		
		// Fullscreen / position
		if(mode.isFullscreen())
			glfwSetWindowMonitor(window, glfwGetPrimaryMonitor(), 0, 0, w, h, GLFW_DONT_CARE);
		else
			glfwSetWindowPos(
					window, 
					mode.getX() == DisplayMode.CENTER ? (vmode.width() / 2) - (w / 2) : mode.getX(), 
					mode.getY() == DisplayMode.CENTER ? (vmode.height() / 2) - (h / 2) : mode.getY());
		
		// VSYNC
		glfwSwapInterval(mode.isVSYNC() ? 1 : 0);
				
		// Show window
		glfwShowWindow(window);
		
		// Mode has changed
		this.mode = mode;
		
		// Run callback
		if(onDisplayModeChanged != null)
			onDisplayModeChanged.invoke(mode);
		
	}
	
	/**
	 * Returns this's window's keyboard input object.
	 * @return [{@link KeyboardInput}] The keyboard input object used to assign key actions.
	 */
	public KeyboardInput getKeyboard() {
		return keyboard;
	}
	
	/**
	 * Indicates whether or not this window should close.
	 * @return [<b>boolean</b>] True if the window has received a closing message, false otherwise.
	 */
	public boolean shouldClose() {
		return glfwWindowShouldClose(window);
	}
	
	/**
	 * Updates the window by swapping its draw buffers and polling any new events received.
	 */
	public void update() {
		glClearColor(1, 1, 1, 1);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glfwSwapBuffers(window);
		glfwPollEvents();
	}
}
