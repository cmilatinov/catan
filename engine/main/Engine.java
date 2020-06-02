package main;

import display.DisplayMode;
import display.Window;
import log.Logger;
import objects.FBO;
import org.lwjgl.glfw.GLFWErrorCallback;
import resources.GameResources;

import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_NORMAL;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.GL_DEPTH_COMPONENT32;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.opengl.GL30.GL_DEPTH_ATTACHMENT;

public final class Engine {

    public static final int ERR_GLFW_INIT = 1;
    public static final int ERR_MONITOR_MODE = 2;
    public static final int ERR_SHADER_COMPILATION = 3;
    public static final int ERR_SHADER_LINKING = 4;

    public static final Logger LOGGER = new Logger();

    private final Window window;

    private Scene currentScene;

    public Engine() {
        DisplayMode mode = new DisplayMode(
                DisplayMode.CENTER,            // Center X
                DisplayMode.CENTER,            // Center Y
                1920,                    // Width
                1080,                     // Height
                GLFW_CURSOR_NORMAL,            // Normal cursor
                true,                 // Decorated
                true,                    // Use VSYNC
                false,              // Not always on top
                false                 // Not fullscreen
        );
        window = new Window("Hello", mode).create().requestFocus();

        // Load resources
        log("Loading assets ...");
        GameResources.loadAll();

        currentScene = new Scene(window);
    }

    public Scene getCurrentScene() {
        return currentScene;
    }

    public void setCurrentScene(Scene currentScene) {
        this.currentScene = currentScene;
    }

    public Scene newScene() {
        return new Scene(window);
    }

    public void run() {
        init();

        // Create window
        log("Creating window ...");
        window.mouse().centerCursorInWindow();

        // OpenGL stuff
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        // Multisampled framebuffer
        FBO fbo = FBO.create(window.getWidth(), window.getHeight(), 8)
                .addAttachment(GL_RGB, GL_RGB, GL_UNSIGNED_BYTE, GL_COLOR_ATTACHMENT0, false)
                .addAttachment(GL_DEPTH_COMPONENT32, GL_DEPTH_COMPONENT, GL_FLOAT, GL_DEPTH_ATTACHMENT, false)
                .bindAttachments()
                .unbind();

        long lastTime = System.nanoTime();
        long lastSecond = 0;
        int frameCount = 0;
        while (!window.shouldClose()) {
            // Time elapsed since last frame
            long currentTime = System.nanoTime();

            if(((currentTime - lastSecond) / 1e9) >= 1) {
                log(Logger.INFO, "Framerate : " + frameCount);
                frameCount = 0;
                lastSecond = currentTime;
            }

            frameCount += 1;

            double delta = (currentTime - lastTime) * 1e-9;

            // Update
            window.update();
            currentScene.update(delta);
            currentScene.getUiManager().update(delta);

            fbo.bind();

            // Clear screen
            glClearColor(0, 0.4f, 0.4f, 1);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            // Render scene
            currentScene.renderScene();

            // Render UI
            currentScene.getUiManager().render();

            fbo.unbind();

            // Resolve to display
            fbo.resolve(null, GL_COLOR_ATTACHMENT0, GL_BACK);

            lastTime = currentTime;
        }

        log("Destroying object(s) ...");
        currentScene.cleanup();
        window.destroy();
        fbo.destroy();

        log("Unloading all assets ...");
        GameResources.cleanAll();

        LOGGER.close();
    }

    public static void init() {

        log("Initializing GLFW ...");

        // Set glfw error callback.
        GLFWErrorCallback.createPrint(System.err).set();

        // Init GLFW.
        if (!glfwInit()) {
            log(Logger.ERROR, "Unable to initialize GLFW.");
            stop(Engine.ERR_GLFW_INIT);
        }

    }

    public static void stop(int err) {
        LOGGER.close();
        System.exit(err);
    }

    public static void setLogLevel(int logLevel) {
        LOGGER.setLogLevel(logLevel);
    }

    public static void log(String msg) {
        LOGGER.log(msg);
    }

    public static void log(int logLevel, String msg) {
        LOGGER.log(logLevel, msg);
    }

}
