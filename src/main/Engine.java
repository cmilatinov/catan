package main;

import static org.lwjgl.glfw.GLFW.glfwInit;

import org.lwjgl.glfw.GLFWErrorCallback;

import log.Logger;

public class Engine {
	
	public static final int ERR_GLFW_INIT = 1;
	public static final int ERR_SHADER_COMPILATION = 2;
	public static final int ERR_SHADER_LINKING = 3;
	
	public static final Logger LOGGER = new Logger();
	
	public static void main(String[] args) throws InterruptedException {
		init();
		
		LOGGER.setLogLevel(Logger.DEBUG);
		
		LOGGER.log("Created window");
		Window window = new Window("Hello", DisplayMode.BORDERLESS_FULLSCREEN);
		window.create();
		
		for(int i = 0; i < 120; i++)
			window.update();
		
		LOGGER.log("Changed display mode");
		window.setDisplayMode(DisplayMode.DEFAULT);
		
		while(!window.shouldClose())
			window.update();
		
		LOGGER.close();
	}
	
	public static final void init() {
		
		// Set glfw error callback.
		GLFWErrorCallback.createPrint(System.err).set();
		
		// Init GLFW.
		if(!glfwInit()) {
			Engine.LOGGER.log(Logger.ERROR, "Unable to initialize GLFW.");
			Engine.stop(Engine.ERR_GLFW_INIT);
		}
		
	}
	
	public static final void stop(int err) {
		LOGGER.close();
		System.exit(err);
	}
	
}
