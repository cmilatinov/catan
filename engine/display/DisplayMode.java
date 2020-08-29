package display;

import static org.lwjgl.glfw.GLFW.*;

public class DisplayMode {
	
	public static final int CENTER = -1;
	public static final int MAX_SIZE = -2;
	
	public static final DisplayMode FULLSCREEN = new DisplayMode(
			DisplayMode.CENTER,		// Center X
			DisplayMode.CENTER,		// Center Y
			DisplayMode.MAX_SIZE,	// Max width
			DisplayMode.MAX_SIZE,	// Max height
			GLFW_CURSOR_NORMAL,		// Normal cursor
			false,			// Not decorated
			true,				// Use VSYNC
			false,		// Not always on top
			true			// Fullscreen
		);
	
	public static final DisplayMode BORDERLESS_FULLSCREEN = new DisplayMode(
			DisplayMode.CENTER,		// Center X
			DisplayMode.CENTER,		// Center Y
			DisplayMode.MAX_SIZE,	// Max width
			DisplayMode.MAX_SIZE,	// Max height
			GLFW_CURSOR_NORMAL,		// Normal cursor
			false,			// Not decorated
			true,				// Use VSYNC
			false,		// Not always on top
			false			// Not fullscreen
		);
	
	public static final DisplayMode DEFAULT = new DisplayMode(
			DisplayMode.CENTER, 	// Center X
			DisplayMode.CENTER, 	// Center Y
			1280, 			// Width
			720, 				// Height
			GLFW_CURSOR_NORMAL,		// Normal cursor
			true, 			// Decorated
			true, 			// Use VSYNC
			false,		// Not always on top
			false			// Not fullscreen
		);
	
	private final int x, y;
	private final int width, height;
	
	private final int cursorMode;
	
	private final boolean decorated;
	private final boolean vsync;
	private final boolean alwaysOnTop;
	private final boolean fullscreen;
	
	public DisplayMode(int x, int y, int width, int height, int cursorMode, boolean decorated, boolean vsync, boolean alwaysOnTop, boolean fullscreen) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.cursorMode = cursorMode;
		this.decorated = decorated;
		this.vsync = vsync;
		this.alwaysOnTop = alwaysOnTop;
		this.fullscreen = fullscreen;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getCursorMode() {
		return cursorMode;
	}
	
	public boolean isDecorated() {
		return decorated;
	}
	
	public boolean isVSYNC() {
		return vsync;
	}
	
	public boolean isAlwaysOnTop() {
		return alwaysOnTop;
	}
	
	public boolean isFullscreen() {
		return fullscreen;
	}

	public DisplayMode copy() {
		return new DisplayMode(x, y, width, height, cursorMode, decorated, vsync, alwaysOnTop, fullscreen);
	}
	
}
