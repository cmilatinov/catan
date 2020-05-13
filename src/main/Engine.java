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
import entities.Entity;
import entities.EntityStatic;
import gameplay.PlayerHand;
import gui.GUI;
import lights.Light;
import log.Logger;
import objects.FBO;
import objects.GameObject;
import objects.TexturedMesh;
import resources.GameResources;
import resources.Resource;
import ui.UIColor;
import ui.UIConstraints;
import ui.UIDimensions;
import ui.UIManager;
import ui.UIQuad;
import ui.constraints.AspectConstraint;
import ui.constraints.CenterConstraint;
import ui.constraints.PixelConstraint;
import ui.constraints.RelativeConstraint;

public class Engine {

	public static final int ERR_GLFW_INIT = 1;
	public static final int ERR_SHADER_COMPILATION = 2;
	public static final int ERR_SHADER_LINKING = 3;

	public static final Logger LOGGER = new Logger();

	private final ArrayList<GameObject> cleanup = new ArrayList<GameObject>();

	private static boolean wireframe = false;
	
	public static void main(String[] args) throws InterruptedException {
		new Engine().run();
	}

	public void run() {
		init();

		// Window display mode
		DisplayMode mode = new DisplayMode(
				DisplayMode.CENTER, 	// Center X
				DisplayMode.CENTER, 	// Center Y
				1280, 					// Width
				720, 					// Height
				GLFW_CURSOR_DISABLED, 	// Disable cursor
				true, 					// Decorated 
				true, 					// Use VSYNC
				false, 					// Not always on top
				false 					// Not fullscreen
		);

		// Create window
		LOGGER.log("Creating window ...");
		Window window = addToCleanup(new Window("Hello", mode).create().requestFocus());
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
		
		// Load resources
		LOGGER.log("Loading assets ...");
		GameResources.loadAll();
		
		// Multisampled framebuffer
		FBO fbo = addToCleanup(FBO.create(window.getWidth(), window.getHeight(), 8)
				.addAttachment(GL_RGB, GL_RGB, GL_UNSIGNED_BYTE, GL_COLOR_ATTACHMENT0, false)
				.addAttachment(GL_DEPTH_COMPONENT32, GL_DEPTH_COMPONENT, GL_FLOAT, GL_DEPTH_ATTACHMENT, false)
				.bindAttachments()
				.unbind());
		
		// Game controller
		GameController controller = new GameController(window);
		
		GUI[] cards = new GUI[10];
		for(int i = 0; i < cards.length; i++) {
			int r = (int) Math.floor(Math.random() * 5);
			switch (r) {
				case 0:
					cards[i] = new GUI(GameResources.get(Resource.TEXTURE_CARD_BRICK));
					break;
				case 1:
					cards[i] = new GUI(GameResources.get(Resource.TEXTURE_CARD_STONE));
					break;
				case 2:
					cards[i] = new GUI(GameResources.get(Resource.TEXTURE_CARD_SHEEP));
					break;
				case 3:
					cards[i] = new GUI(GameResources.get(Resource.TEXTURE_CARD_FOREST));
					break;
				case 4:
					cards[i] = new GUI(GameResources.get(Resource.TEXTURE_CARD_WHEAT));
					break;
			}
			cards[i].setSize(150);
			controller.registerGUI(cards[i]);
		}
		PlayerHand hand = new PlayerHand(window);
		for(GUI card : cards)
			hand.addCard(card);
		hand.update();

		TexturedMesh blueRoad = new TexturedMesh(GameResources.get(Resource.MESH_ROAD), GameResources.get(Resource.TEXTURE_COLOR_BLUE));
		TexturedMesh blueSettlement = new TexturedMesh(GameResources.get(Resource.MESH_SETTLEMENT), GameResources.get(Resource.TEXTURE_COLOR_BLUE));
		TexturedMesh blueCity = new TexturedMesh(GameResources.get(Resource.MESH_CITY), GameResources.get(Resource.TEXTURE_COLOR_BLUE));
		TexturedMesh blueRobber = new TexturedMesh(GameResources.get(Resource.MESH_ROBBER), GameResources.get(Resource.TEXTURE_COLOR_BLUE));
		
		Entity road = new EntityStatic(blueRoad).scale(0.45f).rotate(new Vector3f(0, 90, 0)).translate(new Vector3f(0, 0, -3.5f));
		Entity settlement = new EntityStatic(blueSettlement).scale(0.040f).translate(new Vector3f(0, 0, -3));
		Entity city = new EntityStatic(blueCity).scale(0.045f).translate(new Vector3f(0, 0, -4));
		Entity robber = (new Entity(blueRobber) {
			public void update(double delta) {
				rotate(new Vector3f(0, 200 * (float)delta, 0));
			}
			public boolean shouldUpdate() {
				return true;
			}
			public boolean shouldRender() {
				return true;
			}
		}).scale(0.01f);
		Entity table = new EntityStatic(GameResources.get(Resource.MODEL_TABLE)).scale(10).translate(new Vector3f(0, -0.07f, 0));
		
		controller.registerEntity(road);
		controller.registerEntity(settlement);
		controller.registerEntity(city);
		controller.registerEntity(robber);
		controller.registerEntity(table);
		
		Light sun = new Light(new Vector3f(0.6f, 0.6f, 0.6f), new Vector3f(500, 1000, 500));
		Light sun2 = new Light(new Vector3f(0.6f, 0.6f, 0.6f), new Vector3f(-500, 1000, 500));
		
		controller.registerLight(sun);
		controller.registerLight(sun2);
		
		Entity[] tiles = new Entity[19];
		
		float scale = 0.996f;
		for(int i = 0; i < tiles.length; i++) {
			int r = (int) Math.floor(Math.random() * 5);
			switch(r) {
				case 0:
					tiles[i] = new EntityStatic(GameResources.get(Resource.MODEL_TILE_BRICK)).scale(scale);
					break;
				case 1:
					tiles[i] = new EntityStatic(GameResources.get(Resource.MODEL_TILE_FOREST)).scale(scale);
					break;
				case 2:
					tiles[i] = new EntityStatic(GameResources.get(Resource.MODEL_TILE_SHEEP)).scale(scale);
					break;
				case 3:
					tiles[i] = new EntityStatic(GameResources.get(Resource.MODEL_TILE_STONE)).scale(scale);
					break;
				case 4:
					tiles[i] = new EntityStatic(GameResources.get(Resource.MODEL_TILE_WHEAT)).scale(scale);
					break;
			}
			controller.registerEntity(tiles[i]);
		}
		tiles[0].translate(new Vector3f(-1.732f, 0, 3));
		tiles[1].translate(new Vector3f(0, 0, 3));
		tiles[2].translate(new Vector3f(1.732f, 0, 3));
		
		tiles[3].translate(new Vector3f(-2.598f, 0, 1.5f));
		tiles[4].translate(new Vector3f(-0.866f, 0, 1.5f));
		tiles[5].translate(new Vector3f(0.866f, 0, 1.5f));
		tiles[6].translate(new Vector3f(2.598f, 0, 1.5f));
		
		tiles[7].translate(new Vector3f(-3.464f, 0, 0));
		tiles[8].translate(new Vector3f(-1.732f, 0, 0));
		tiles[9].translate(new Vector3f(0, 0, 0));
		tiles[10].translate(new Vector3f(1.732f, 0, 0));
		tiles[11].translate(new Vector3f(3.464f, 0, 0));
		
		tiles[12].translate(new Vector3f(-2.598f, 0, -1.5f));
		tiles[13].translate(new Vector3f(-0.866f, 0, -1.5f));
		tiles[14].translate(new Vector3f(0.866f, 0, -1.5f));
		tiles[15].translate(new Vector3f(2.598f, 0, -1.5f));
		
		tiles[16].translate(new Vector3f(-1.732f, 0, -3));
		tiles[17].translate(new Vector3f(0, 0, -3));
		tiles[18].translate(new Vector3f(1.732f, 0, -3));
		
		// Skybox
		controller.setSkyboxTexture(GameResources.get(Resource.TEXTURE_SKYBOX));
		
		// UI
		UIManager uiManager = new UIManager(window);
		
		UIQuad box = new UIQuad();
		box.setColor(UIColor.DARK_GRAY);
		box.setBorderRadius(20);
		UIConstraints constraints = new UIConstraints()
				.setX(new PixelConstraint(20, UIDimensions.DIRECTION_LEFT))
				.setY(new PixelConstraint(20, UIDimensions.DIRECTION_TOP))
				.setWidth(new RelativeConstraint(0.15f))
				.setHeight(new AspectConstraint(1));
		uiManager.getContainer().add(box, constraints);
		
		UIQuad box2 = new UIQuad();
		box2.setColor(UIColor.RED);
		UIConstraints constraints2 = new UIConstraints()
				.setX(new CenterConstraint())
				.setY(new CenterConstraint())
				.setWidth(new PixelConstraint(30))
				.setHeight(new PixelConstraint(30));
		box.add(box2, constraints2);
		
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
