package main;

import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_DISABLED;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glEnable;

import java.util.ArrayList;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWErrorCallback;

import camera.Camera;
import camera.CameraFPS;
import display.DisplayMode;
import display.Window;
import entities.Entity;
import log.Logger;
import objects.GameObject;
import objects.GameObjectFactory;
import objects.Mesh;
import objects.Texture;
import objects.TexturedMesh;
import render.EntityRenderer;

public class Engine {

	public static final int ERR_GLFW_INIT = 1;
	public static final int ERR_SHADER_COMPILATION = 2;
	public static final int ERR_SHADER_LINKING = 3;

	public static final Logger LOGGER = new Logger();

	private final ArrayList<GameObject> cleanup = new ArrayList<GameObject>();

	public static void main(String[] args) throws InterruptedException {
		new Engine().run();
	}

	public void run() {
		init();

		// Window display mode
		DisplayMode mode = new DisplayMode(DisplayMode.CENTER, // Center X
				DisplayMode.CENTER, // Center Y
				1280, // Width
				720, // Height
				GLFW_CURSOR_DISABLED, // Disable cursor
				true, // Decorated
				true, // Use VSYNC
				false, // Not always on top
				false // Not fullscreen
		);

		// Create window
		LOGGER.log("Creating window ...");
		Window window = addObjectToCleanup(new Window("Hello", mode));
		window.create();

		// Escape to close the game
		window.keyboard().registerKeyUp(GLFW_KEY_ESCAPE, (int mods) -> window.close());
		window.mouse().centerCursorInWindow();

		// Load a cube
		GameObjectFactory loader = new GameObjectFactory();
		Mesh mesh = loader.loadOBJ("./models/cube.obj");
		Texture texture = loader.loadTexture2D("diamond_ore.png", GL_NEAREST, true);
		TexturedMesh model = addObjectToCleanup(new TexturedMesh(mesh, texture));
		Entity cube = new Entity(model).scale(0.5f).rotate(new Vector3f(180, 0, 0)).translate(new Vector3f(0, 0, -5));

		// OpenGL stuff
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);

		// Shader
		EntityRenderer eRenderer = addObjectToCleanup(new EntityRenderer());

		// Camera
		Camera cam = addObjectToCleanup(
				new CameraFPS(16.0f / 9.0f, 70, window.keyboard(), window.mouse()).translate(new Vector3f(0, 0, 1)));

		long lastTime = System.nanoTime();
		while (!window.shouldClose()) {

			// Time elapsed since last frame
			long currentTime = System.nanoTime();
			double delta = (currentTime - lastTime) * 1e-9;

			// Clear
			glClearColor(0, 0.4f, 0.4f, 1);
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

			// Render
			eRenderer.render(cam, cube);

			// Update
			window.update();
			cam.update(delta);

			lastTime = currentTime;
		}

		LOGGER.log("Destroying " + cleanup.size() + " object(s) ...");
		for(GameObject o : cleanup)
			o.destroy();

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
	private <T extends GameObject> T addObjectToCleanup(GameObject object) {
		cleanup.add(object);
		return (T) object;
	}

}
