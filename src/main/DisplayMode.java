package main;

public class DisplayMode {
	
	public static final int CENTER = -1;
	public static final int MAX_SIZE = -2;
	
	public static final DisplayMode FULLSCREEN = new DisplayMode(
			DisplayMode.CENTER,		// Center X
			DisplayMode.CENTER,		// Center Y
			DisplayMode.MAX_SIZE,	// Max width
			DisplayMode.MAX_SIZE,	// Max height
			false,					// Not decorated
			true,					// Use VSYNC
			false,					// Not always on top
			true					// Fullscreen
		);
	
	public static final DisplayMode BORDERLESS_FULLSCREEN = new DisplayMode(
			DisplayMode.CENTER,		// Center X
			DisplayMode.CENTER,		// Center Y
			DisplayMode.MAX_SIZE,	// Max width
			DisplayMode.MAX_SIZE,	// Max height
			false,					// Not decorated
			true,					// Use VSYNC
			false,					// Not always on top
			false					// Not fullscreen
		);
	
	public static final DisplayMode DEFAULT = new DisplayMode(
			DisplayMode.CENTER, 	// Center X
			DisplayMode.CENTER, 	// Center Y
			1280, 					// Width
			720, 					// Height
			true, 					// Decorated
			true, 					// Use VSYNC
			false,					// Not always on top
			false					// Not fullscreen
		);
	
	private int x, y;
	private int w, h;
	
	private boolean decorated;
	private boolean vsync;
	private boolean alwaysOnTop;
	private boolean fullscreen;
	
	public DisplayMode(int x, int y, int width, int height, boolean decorated, boolean vsync, boolean alwaysOnTop, boolean fullscreen) {
		this.x = x;
		this.y = y;
		this.w = width;
		this.h = height;
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
		return w;
	}
	
	public int getHeight() {
		return h;
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
	
}
