package main;

import display.Window;
import log.Logger;
import objects.FBO;
import objects.GameResourceFactory;
import objects.GameScript;
import render.SceneRenderer;
import resources.GameResourceLoader;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.GL_DEPTH_COMPONENT32;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.opengl.GL30.GL_DEPTH_ATTACHMENT;

public final class Engine {

    public static final int ERR_RUNTIME = 1;
    public static final int ERR_GLFW_INIT = 2;
    public static final int ERR_MONITOR_MODE = 3;
    public static final int ERR_SHADER_COMPILATION = 4;
    public static final int ERR_SHADER_LINKING = 5;

    private static final Logger logger = new Logger();

    private final Window window;

    private GameResourceLoader loader = null;
    private final Deque<Runnable> loadOperations = new ArrayDeque<>();

    private final SceneManager sceneManager;
    private final SceneRenderer sceneRenderer;

    private final SplashScreenScene loadingScene;

    private Scene currentScene;
    private final Class<? extends Scene> initialScene;

    public Engine(Window window, Class<? extends GameResourceLoader> resourceLoader, Class<? extends Scene> initialScene) {
        // Create window if not created yet
        this.window = window;
        if (!window.isCreated())
            window.create()
                    .requestFocus();

        // Save loader instance
        try {
            this.loader = resourceLoader.getConstructor(Engine.class).newInstance(this);
        } catch (Exception e) {
            error(e);
        }

        // Set to empty scene and create scene renderer
        this.sceneManager = new SceneManager(this);
        this.sceneRenderer = new SceneRenderer(window);
        this.loadingScene = new SplashScreenScene(loader.getSplashImage());
        this.sceneManager.registerScene(loadingScene);
        this.initialScene = initialScene;
    }

    private void load() {
        // Depth test, if certain triangles are occluded by others, don't draw them
        glEnable(GL_DEPTH_TEST);

        // Enable back face culling, faces facing away from the camera are not drawn
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);

        // Enable blending to allow for transparency and set the blending function
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        // Enqueue all loading operations
        loader.setOnProgressReceived(progress -> {
            Engine.log(Logger.INFO, "Loading game resources ... " + (progress * 100) + "%");
            loadingScene.setLoadingProgress(progress);
        });
        loader.loadResources();

        // Load scene
        sceneManager.loadScene(SplashScreenScene.class);

        // Render loading scene while loading resources
        long lastTime = System.nanoTime();
        while (!loadOperations.isEmpty()) {

            // Time elapsed since last frame
            long currentTime = System.nanoTime();
            double delta = (currentTime - lastTime) * 1e-9;

            // Update window and scene
            window.update();
            loadingScene.update(delta);

            // Clear screen
            glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            // Render scene
            sceneRenderer.render(loadingScene);

            // Execute one load operation
            loadOperations.pop().run();

            lastTime = currentTime;
        }

    }

    public void run() {

        // Load the needed resources
        load();

        // Load initial scene
        sceneManager.loadScene(initialScene);

        // Multisampled framebuffer
        FBO fbo = FBO.create(window.getWidth(), window.getHeight(), 8)
                .addAttachment(GL_RGB, GL_RGB, GL_UNSIGNED_BYTE, GL_COLOR_ATTACHMENT0, false)
                .addAttachment(GL_DEPTH_COMPONENT32, GL_DEPTH_COMPONENT, GL_FLOAT, GL_DEPTH_ATTACHMENT, false)
                .bindAttachments()
                .unbind();

        long lastTime = System.nanoTime();
        while (!window.shouldClose()) {
            // Time elapsed since last frame
            long currentTime = System.nanoTime();
            double delta = (currentTime - lastTime) * 1e-9;

            // Update
            window.update();
            currentScene.update(delta);

            fbo.bind();

            // Clear screen
            glClearColor(0, 0.4f, 0.4f, 1);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            // Render scene
            sceneRenderer.render(currentScene);

            fbo.unbind();

            // Resolve to display
            fbo.resolve(null, GL_COLOR_ATTACHMENT0, GL_BACK);

            lastTime = currentTime;
        }

        log("Destroying object(s) ...");
        currentScene.destroy();
        window.destroy();
        fbo.destroy();

        log("Unloading all assets ...");
        GameResourceFactory.cleanGameResources();

        logger.close();
    }

    public Window getWindow() {
        return window;
    }

    public SceneManager getSceneManager() {
        return sceneManager;
    }

    public void setCurrentScene(Scene currentScene) {
        this.currentScene = currentScene;
        // Have to set the context of any globals here since
        // this is the only point where the transfer of a new scene happens
        for (GameScript script : Scene.getGlobalScripts()) {
            script.setContext(currentScene);
        }
    }

    public void enqueueLoadOperation(Runnable operation) {
        loadOperations.add(operation);
    }

    /**
     * Stops the game engine with the given exit error code.
     *
     * @param err The error code describing why the engine has stopped. A zero code is taken to mean successful execution.
     *            When calling this method, use the predefined constants in the {@link Engine} class.
     *            (ex: {@link #ERR_GLFW_INIT}, {@link #ERR_MONITOR_MODE}, etc...)
     */
    public static void stop(int err) {
        logger.close();
        System.exit(err);
    }

    /**
     * This method serves as a shortcut to the {@link Logger#setLogLevel} method. The {@link Engine} class has a single
     * static instance of a {@link Logger}.
     *
     * @param logLevel A valid log level describing the nature of subsequent messages. (ex: {@link Logger#INFO},
     *                 {@link Logger#DEBUG}, {@link Logger#WARN}, {@link Logger#ERROR})
     */
    public static void setLogLevel(int logLevel) {
        logger.setLogLevel(logLevel);
    }

    /**
     * This method serves as a shortcut to the {@link Logger#log(String)} method. The {@link Engine} class has a single
     * static instance of a {@link Logger}. This method uses the default log level set through {@link Logger#setLogLevel}
     * and should only be used as a shortcut when writing multiple log statements in one go to avoid log level confusion.
     *
     * @param msg The message to log.
     */
    public static void log(String msg) {
        logger.log(msg);
    }

    /**
     * This method serves as a shortcut to the {@link Logger#log(int, String)} method. The {@link Engine} class has a single
     * static instance of a {@link Logger}.
     *
     * @param logLevel A valid log level describing the nature of the message. (ex: {@link Logger#INFO},
     *                 {@link Logger#DEBUG}, {@link Logger#WARN}, {@link Logger#ERROR})
     * @param msg      The message to log.
     */
    public static void log(int logLevel, String msg) {
        logger.log(logLevel, msg);
    }

    /**
     * Prints the given exception and stops the engine with a runtime error code.
     *
     * @param e The exception whose stack trace to log.
     */
    public static void error(Exception e) {
        log(Logger.ERROR, e.getMessage() + "\n" + Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString).reduce("", (trace, stack) -> stack + trace + "\n"));
        log(Logger.INFO, "Shutting down ...");
        stop(ERR_RUNTIME);
    }

}
