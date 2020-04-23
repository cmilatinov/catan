package main;

import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_DISABLED;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
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
<<<<<<< HEAD
import gameplay.Board;
import gameplay.Tile;
=======
>>>>>>> e5e9742982d4cba7b9424541750580d374924585
import lights.Light;
import log.Logger;
import objects.GameObject;
import objects.TexturedMesh;
import render.EntityRenderer;
import resources.GameResources;
import resources.Resource;

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
		
		// OpenGL stuff
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
		
		// Load resources
		GameResources.loadAll();
		
		TexturedMesh blueRoad = new TexturedMesh(GameResources.get(Resource.MESH_ROAD), GameResources.get(Resource.TEXTURE_COLOR_BLUE));
		TexturedMesh blueSettlement = new TexturedMesh(GameResources.get(Resource.MESH_SETTLEMENT), GameResources.get(Resource.TEXTURE_COLOR_BLUE));
		TexturedMesh blueCity = new TexturedMesh(GameResources.get(Resource.MESH_CITY), GameResources.get(Resource.TEXTURE_COLOR_BLUE));
		TexturedMesh blueRobber = new TexturedMesh(GameResources.get(Resource.MESH_ROBBER), GameResources.get(Resource.TEXTURE_COLOR_BLUE));
		
		Entity road = new Entity(blueRoad).scale(0.45f).rotate(new Vector3f(0, 90, 0)).translate(new Vector3f(0, 0.14f, -3.5f));
		Entity settlement = new Entity(blueSettlement).scale(0.040f).translate(new Vector3f(0, 0.1f, -3));
		Entity city = new Entity(blueCity).scale(0.045f).translate(new Vector3f(0, 0.1f, -4));
		Entity robber = new Entity(blueRobber).scale(0.01f);
		
		Light sun = new Light(new Vector3f(1, 1, 1), new Vector3f(1000, 1000, 0));

		

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
			
			for(int i = 0; i < titty.length; i ++) {
				eRenderer.render(cam, titty[i], sun);
			}
			
			eRenderer.render(cam, road, sun);
			eRenderer.render(cam, settlement, sun);
			eRenderer.render(cam, city, sun);
			
			eRenderer.render(cam, robber, sun);

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
