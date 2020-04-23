package main;

import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_DISABLED;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
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
import gameplay.Board;
import gameplay.Tile;
import lights.Light;
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
		
		// Meshes
		Mesh meshTile = addObjectToCleanup(loader.loadOBJ("./models/tile.obj"));
		Mesh meshRoad = addObjectToCleanup(loader.loadOBJ("./models/road.obj"));
		Mesh meshSettlement = addObjectToCleanup(loader.loadOBJ("./models/settlement.obj"));
		Mesh meshCity = addObjectToCleanup(loader.loadOBJ("./models/city.obj"));
		Mesh meshRobber = addObjectToCleanup(loader.loadOBJ("./models/robber.obj"));
		
		// Textures
		Texture textureBrick = addObjectToCleanup(loader.loadTexture2D("tile_brick.png", GL_LINEAR, true));
		Texture textureDesert = addObjectToCleanup(loader.loadTexture2D("tile_desert.png", GL_LINEAR, true));
		Texture textureForest = addObjectToCleanup(loader.loadTexture2D("tile_forest.png", GL_LINEAR, true));
		Texture textureSheep = addObjectToCleanup(loader.loadTexture2D("tile_sheep.png", GL_LINEAR, true));
		Texture textureStone = addObjectToCleanup(loader.loadTexture2D("tile_stone.png", GL_LINEAR, true));
		Texture textureWheat = addObjectToCleanup(loader.loadTexture2D("tile_wheat.png", GL_LINEAR, true));
		Texture textureBlue = addObjectToCleanup(loader.loadTexture2D("blue.png", GL_NEAREST, false));
		
		// Textured meshes
		TexturedMesh modelTileBrick = new TexturedMesh(meshTile, textureBrick);
		TexturedMesh modelTileDesert = new TexturedMesh(meshTile, textureDesert);
		TexturedMesh modelTileForest =new TexturedMesh(meshTile, textureForest);
		TexturedMesh modelTileSheep = new TexturedMesh(meshTile, textureSheep);
		TexturedMesh modelTileStone = new TexturedMesh(meshTile, textureStone);
		TexturedMesh modelTileWheat = new TexturedMesh(meshTile, textureWheat);
		TexturedMesh modelRoad = new TexturedMesh(meshRoad, textureBlue);
		TexturedMesh modelSettlement = new TexturedMesh(meshSettlement, textureBlue);
		TexturedMesh modelCity = new TexturedMesh(meshCity, textureBlue);
		TexturedMesh modelRobber = new TexturedMesh(meshRobber, textureBlue);
		
		// Board shit
		Board b = new Board(3);
		Entity[] titty = new Entity[b.tiles.size()];
		TexturedMesh currentMesh;
		float scale = 0.999f;
		
		for(int i = 0; i < b.tiles.size(); i ++) {
			
			switch(b.tiles.get(i).getType()) {
			case SHEEP:
				titty[i] = new Entity(modelTileSheep).scale(scale);
				break;
			case BRICK:
				titty[i] = new Entity(modelTileBrick).scale(scale);
				break;
			case WOOD:
				titty[i] = new Entity(modelTileForest).scale(scale);
				break;
			case WHEAT:
				titty[i] = new Entity(modelTileWheat).scale(scale);
				break;
			case ROCK: 
				titty[i] = new Entity(modelTileStone).scale(scale);
				break;
			case DESERT:
				titty[i] = new Entity(modelTileDesert).scale(scale);
				break;
			}
			
		}
		
		titty[0].translate(new Vector3f(-1.732f, 0, 3));
		titty[1].translate(new Vector3f(0, 0, 3));
		titty[2].translate(new Vector3f(1.732f, 0, 3));
		
		titty[3].translate(new Vector3f(-2.598f, 0, 1.5f));
		titty[4].translate(new Vector3f(-0.866f, 0, 1.5f));
		titty[5].translate(new Vector3f(0.866f, 0, 1.5f));
		titty[6].translate(new Vector3f(2.598f, 0, 1.5f));
		
		titty[7].translate(new Vector3f(-3.464f, 0, 0));
		titty[8].translate(new Vector3f(-1.732f, 0, 0));
		titty[9].translate(new Vector3f(0, 0, 0));
		titty[10].translate(new Vector3f(1.732f, 0, 0));
		titty[11].translate(new Vector3f(3.464f, 0, 0));
		
		titty[12].translate(new Vector3f(-2.598f, 0, -1.5f));
		titty[13].translate(new Vector3f(-0.866f, 0, -1.5f));
		titty[14].translate(new Vector3f(0.866f, 0, -1.5f));
		titty[15].translate(new Vector3f(2.598f, 0, -1.5f));
		
		titty[16].translate(new Vector3f(-1.732f, 0, -3));
		titty[17].translate(new Vector3f(0, 0, -3));
		titty[18].translate(new Vector3f(1.732f, 0, -3));
		
		Entity robber = new Entity(modelRobber).scale(0.01f);
		
		for(int i = 0; i < titty.length; i ++) {
			if(b.tiles.get(i).getType() == Board.TileTypes.DESERT) {
				robber.translate(titty[i].getPosition());
			}
		}
		
		Entity road = new Entity(modelRoad).scale(0.45f).rotate(new Vector3f(0, 90, 0)).translate(new Vector3f(0, 0.14f, -3.5f));
		Entity settlement = new Entity(modelSettlement).scale(0.040f).translate(new Vector3f(0, 0.1f, -3));
		Entity city = new Entity(modelCity).scale(0.045f).translate(new Vector3f(0, 0.1f, -4));
		
		
		
		Light sun = new Light(new Vector3f(1, 1, 1), new Vector3f(300, 1000, 0));

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
