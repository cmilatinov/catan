package display;

import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_NORMAL;

/**
 * Represents a single display mode applied to a frame. A display mode defines how a frame should be rendered in the context of the operating system.
 */
public class DisplayMode {

    /**
     * Use this constant to represent the center of the screen.
     */
    public static final int CENTER = -1;

    /**
     * Use this constant to represent the maximum allowed size for a specific dimension.
     */
    public static final int MAX_SIZE = -2;

    /**
     * Sample fullscreen display mode. Uses the maximum screen resolution but changes the screen mode. Likely provides a performance boost over borderless-fullscreen but takes much longer to switch to.
     */
    public static final DisplayMode FULLSCREEN = new DisplayMode(
            DisplayMode.CENTER,        // Center X
            DisplayMode.CENTER,        // Center Y
            DisplayMode.MAX_SIZE,    // Max width
            DisplayMode.MAX_SIZE,    // Max height
            GLFW_CURSOR_NORMAL,        // Normal cursor
            false,            // Not decorated
            true,                // Use VSYNC
            false,        // Not always on top
            true            // Fullscreen
    );

    /**
     * Sample borderless-fullscreen display mode. Uses screen resolution, undecorated, and does not change screen mode.
     */
    public static final DisplayMode BORDERLESS_FULLSCREEN = new DisplayMode(
            DisplayMode.CENTER,        // Center X
            DisplayMode.CENTER,        // Center Y
            DisplayMode.MAX_SIZE,    // Max width
            DisplayMode.MAX_SIZE,    // Max height
            GLFW_CURSOR_NORMAL,        // Normal cursor
            false,            // Not decorated
            true,                // Use VSYNC
            false,        // Not always on top
            false            // Not fullscreen
    );

    /**
     * Sample default display mode. Standard HD resolution, windowed, decorated, appears at the center of the screen.
     */
    public static final DisplayMode DEFAULT = new DisplayMode(
            DisplayMode.CENTER,    // Center X
            DisplayMode.CENTER,    // Center Y
            1280,            // Width
            720,                // Height
            GLFW_CURSOR_NORMAL,        // Normal cursor
            true,            // Decorated
            true,            // Use VSYNC
            false,        // Not always on top
            false            // Not fullscreen
    );

    /**
     * The frame's position relative to the screen.
     */
    private final int x, y;

    /**
     * The frame's size in pixels.
     */
    private final int width, height;

    /**
     * Indicates the cursor behavior when the window is in focus.
     */
    private final int cursorMode;

    /**
     * Indicates whether the frame should have a top bar with OS-level window controls.
     */
    private final boolean decorated;

    /**
     * Indicates whether the frame should use vertical sync.
     */
    private final boolean vsync;

    /**
     * Indicates whether the frame should always be displayed on top of other windows.
     */
    private final boolean alwaysOnTop;

    /**
     * Indicates whether the frame should appear in fullscreen mode.
     */
    private final boolean fullscreen;

    /**
     * Creates a copy of an existing {@link DisplayMode}.
     *
     * @param src The source object to copy.
     */
    public DisplayMode(DisplayMode src) {
        this(src.x, src.y, src.width, src.height, src.cursorMode, src.decorated, src.vsync, src.alwaysOnTop, src.fullscreen);
    }

    /**
     * Creates a {@link DisplayMode} instance with the given parameters.
     *
     * @param x           The x-position of the frame with respect to the screen.
     * @param y           The y-position of the frame with respect to the screen.
     * @param width       The width of the frame in pixels.
     * @param height      The height of the frame in pixels.
     * @param cursorMode  The cursor mode to use when the frame is in focus. (ex: {@link org.lwjgl.glfw.GLFW#GLFW_CURSOR_NORMAL} or {@link org.lwjgl.glfw.GLFW#GLFW_CURSOR_HIDDEN})
     * @param decorated   Whether or not the frame should be decorated with OS-specific controls.
     * @param vsync       Whether or not the frame should use VSync when rendering to eliminate screen tearing.
     * @param alwaysOnTop Whether or not the frame should always render on top of other frames.
     * @param fullscreen  Whether or not the frame should displayed in fullscreen mode. If this is true, the monitor's display mode will be set to the frame's resolution.
     */
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

    /**
     * Returns the x-position of the frame with respect to the screen.
     *
     * @return <b>int</b> The x-position of the frame with respect to the screen.
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the y-position of the frame with respect to the screen.
     *
     * @return <b>int</b> The y-position of the frame with respect to the screen.
     */
    public int getY() {
        return y;
    }

    /**
     * Returns the width of the frame in pixels.
     *
     * @return <b>int</b> The width of the frame in pixels.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the height of the frame in pixels.
     *
     * @return <b>int</b> The height of the frame in pixels.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns the cursor mode to use when the frame is in focus.
     *
     * @return <b>int</b> The cursor mode to use when the frame is in focus.
     */
    public int getCursorMode() {
        return cursorMode;
    }

    /**
     * Indicates whether the frame should have a top bar with OS-level window controls.
     *
     * @return <b>boolean</b> True if the display mode requires the frame to be decorated, false otherwise.
     */
    public boolean isDecorated() {
        return decorated;
    }

    /**
     * Indicates whether the frame should use VSync when rendering to eliminate screen tearing.
     *
     * @return <b>boolean</b> True if the display mode requires the frame to use VSync, false otherwise.
     */
    public boolean isVSYNC() {
        return vsync;
    }

    /**
     * Indicates whether the frame should always render on top of other frames.
     *
     * @return <b>boolean</b> True if the display mode requires the frame to always render above other windows, false otherwise.
     */
    public boolean isAlwaysOnTop() {
        return alwaysOnTop;
    }

    /**
     * Indicates whether the frame should displayed in fullscreen mode.
     *
     * @return <b>boolean</b> True if the display mode requires the frame to be displayed in fullscreen mode, false otherwise.
     */
    public boolean isFullscreen() {
        return fullscreen;
    }

}
