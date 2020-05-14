package main;

import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_DISABLED;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Q;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_R;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_COMPONENT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_LINE;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_RGB;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL14.GL_DEPTH_COMPONENT32;
import static org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0;
import static org.lwjgl.opengl.GL30.GL_DEPTH_ATTACHMENT;

import java.util.ArrayList;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWErrorCallback;

import camera.Camera;
import camera.CameraFPS;
import display.DisplayMode;
import display.Window;
import log.Logger;
import objects.FBO;
import objects.GameObject;
import resources.GameResources;
import ui.UIManager;

public class Engine {

	public static final int ERR_GLFW_INIT = 1;
	public static final int ERR_SHADER_COMPILATION = 2;
	public static final int ERR_SHADER_LINKING = 3;

	public static final Logger LOGGER = new Logger();

	private final ArrayList<GameObject> cleanup = new ArrayList<GameObject>();

	private static boolean wireframe = false;

	/**
	 * "God" Objects
	 */
	private final Window window;
	private final GameController controller;
	private final UIManager uiManager;

	public Engine() {
		DisplayMode mode = new DisplayMode(
				DisplayMode.CENTER,            // Center X
				DisplayMode.CENTER,            // Center Y
				1280,                    // Width
				720,                        // Height
				GLFW_CURSOR_DISABLED,            // Disable cursor
				true,                    // Decorated
				true,                    // Use VSYNC
				false,                    // Not always on top
				false                    // Not fullscreen
		);
		window = addToCleanup(new Window("Hello", mode).create().requestFocus());
		// Load resources
		LOGGER.log("Loading assets ...");
		GameResources.loadAll();

		controller = new GameController(window);
		uiManager = new UIManager(window);
	}

	public Window getWindow() {
		return window;
	}

	public GameController getController() {
		return controller;
	}

	public UIManager getUiManager () {
		return uiManager;
	}

	public void run() {
		init();

		// Create window
		LOGGER.log("Creating window ...");
		window.mouse().centerCursorInWindow();

		// Camera
		Camera cam = addToCleanup(new CameraFPS(70, window).translate(new Vector3f(0, 0, 1)));
		
		// Q to toggle wireframe
		window.keyboard().registerKeyUp(GLFW_KEY_ESCAPE, (int mods) -> window.close());
		window.keyboard().registerKeyUp(GLFW_KEY_Q, (int mods) -> {
			if(!wireframe)
				glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
			else 
				glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
			wireframe = !wireframe;
		});
		window.keyboard().registerKeyUp(GLFW_KEY_R, (int mods) -> cam.reset());
		
		// OpenGL stuff
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

		// Multisampled framebuffer
		FBO fbo = addToCleanup(FBO.create(window.getWidth(), window.getHeight(), 8)
				.addAttachment(GL_RGB, GL_RGB, GL_UNSIGNED_BYTE, GL_COLOR_ATTACHMENT0, false)
				.addAttachment(GL_DEPTH_COMPONENT32, GL_DEPTH_COMPONENT, GL_FLOAT, GL_DEPTH_ATTACHMENT, false)
				.bindAttachments()
				.unbind());

		long lastTime = System.nanoTime();
		while (!window.shouldClose()) {

			// Time elapsed since last frame
			long currentTime = System.nanoTime();
			double delta = (currentTime - lastTime) * 1e-9;
			
			// Update
			window.update();
			cam.update(delta);
			controller.update(delta);
			uiManager.update(delta);
			
			fbo.bind();
			
			// Clear screen
			glClearColor(0, 0.4f, 0.4f, 1);
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			
			// Render scene
			controller.renderScene(cam);
			
			// Render UI
			uiManager.render();
			
			fbo.unbind();
			
			// Resolve to display
			fbo.resolve(null, GL_COLOR_ATTACHMENT0, GL_BACK);
			
			
			lastTime = currentTime;
		}

		LOGGER.log("Destroying " + cleanup.size() + " object(s) ...");
		for(GameObject o : cleanup)
			o.destroy();

		LOGGER.log("Unloading all assets ...");
		GameResources.cleanAll();
		
		LOGGER.close();
	}

	public static final void init() {

		LOGGER.setLogLevel(Logger.DEBUG);
		LOGGER.log("Initializing GLFW ...");

		// Set glfw error callback.
		GLFWErrorCallback.createPrint(System.err).set();

		// Init GLFW.
		if (!glfwInit()) {
			Engine.LOGGER.log(Logger.ERROR, "Unable to initialize GLFW.");
			Engine.stop(Engine.ERR_GLFW_INIT);
		}

	}

	public static final void stop(int err) {
		LOGGER.close();
		System.exit(err);
	}

	@SuppressWarnings({ "unchecked" })
	private <T extends GameObject> T addToCleanup(GameObject object) {
		cleanup.add(object);
		return (T) object;
	}

}
