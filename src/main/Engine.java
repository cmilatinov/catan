package main;

import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_DISABLED;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDrawElements;

import static org.lwjgl.opengl.GL40.*;

import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFWErrorCallback;

import camera.Camera;
import camera.CameraFPS;
import display.DisplayMode;
import display.Window;
import log.Logger;
import objects.Mesh;
import objects.MeshLoader;
import shaders.StaticShader;

public class Engine {

	public static final int ERR_GLFW_INIT = 1;
	public static final int ERR_SHADER_COMPILATION = 2;
	public static final int ERR_SHADER_LINKING = 3;

	public static final Logger LOGGER = new Logger();

	public static void main(String[] args) throws InterruptedException {
		init();
		
		// Window display mode
		DisplayMode mode = new DisplayMode(
				DisplayMode.CENTER,		// Center X
				DisplayMode.CENTER,		// Center Y
				1280,					// Width
				720,					// Height
				GLFW_CURSOR_DISABLED,	// Disable cursor
				true,					// Decorated
				true,					// Use VSYNC
				false,					// Not always on top
				false					// Not fullscreen
			);
		
		// Create window
		Window window = new Window("Hello", mode);
		window.create();
		window.mouse().centerCursorInWindow();
		window.keyboard().registerKeyUp(GLFW_KEY_ESCAPE, (int mods) -> window.close());
		LOGGER.log("Window created");
		
		// Load a cube
		float[] vertices = {
				0, 0, 0,
				0, 0, 1,
				0, 1, 0,
				0, 1, 1,
				1, 0, 0,
				1, 0, 1,
				1, 1, 0,
				1, 1, 1
		};
		int[] indices = {
				0, 6, 4,
				0, 2, 6, 
				
				0, 3, 2,
				0, 1, 3,
				
				2, 7, 6,
				2, 3, 7,
				
				4, 6, 7,
				4, 7, 5,
				
				0, 4, 5,
				0, 5, 1,
				
				1, 5, 7,
				1, 7, 3
		};
		MeshLoader loader = new MeshLoader();
		Mesh cube = loader.loadMesh(indices, vertices);
		
		// OpenGL stuff
		glEnable(GL_DEPTH_TEST);
		
		// Shader
		StaticShader shader = new StaticShader();
		shader.use();
		shader.projectionViewMatrix.set(new Matrix4f());
		shader.modelMatrix.set(new Matrix4f());
		shader.stop();
		
		// Camera
		Camera cam = new CameraFPS(16.0f / 9.0f, 90, window.keyboard(), window.mouse());
		
		long lastTime = System.nanoTime();
		while(!window.shouldClose()) {
			
			long currentTime = System.nanoTime();
			double delta = (currentTime - lastTime) / 10e9d;
			
			glClearColor(0, 0.4f, 0.4f, 1);
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			
			shader.use();
			shader.modelMatrix.set(cam.createProjectionViewMatrix());
			
			cube.getVAO().bind(0);
			glDrawElements(GL_TRIANGLES, cube.getVertexCount(), GL_UNSIGNED_INT, 0);
			cube.getVAO().unbind(0);
			
			shader.stop();
			window.update();
			cam.update(delta);
			
			lastTime = currentTime;
		}
		
		window.destroy();
		LOGGER.log("Window destroyed");
		
		LOGGER.close();
	}

	public static final void init() {

		LOGGER.setLogLevel(Logger.DEBUG);

		// Set glfw error callback.
		GLFWErrorCallback.createPrint(System.err).set();

		// Init GLFW.
		if (!glfwInit()) {
			Engine.LOGGER.log(Logger.ERROR, "Unable to initialize GLFW.");
			Engine.stop(Engine.ERR_GLFW_INIT);
		}

		LOGGER.log("GLFW Init complete");

	}

	public static final void stop(int err) {
		LOGGER.close();
		System.exit(err);
	}

}
