package resources;

import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_NEAREST;

import java.util.HashMap;

import objects.GameObject;
import objects.GameObjectFactory;
import objects.TexturedMesh;

public class GameResources {
	
	private static final HashMap<Resource, GameObject> resources = new HashMap<Resource, GameObject>();
	
	private static final String MESH_PATH = "./models/";
	private static final String TEXTURE_PATH = "./textures/";
	
	
	private static final int[] GUI_MESH_INDICES = {
		0, 1, 2,
		2, 3, 0
	};
	
	private static final float[] GUI_MESH_VERTICES = {
		-1, -1, 1,
		1, -1, 1,
		1, 1, 1,
		-1, 1, 1
	};
	
	private static final float[] GUI_MESH_UVS = {
		0, 1,
		1, 1,
		1, 0,
		0, 0
	};
	
	private static final float SKYBOX_SIZE = 500;
	private static final int[] SKYBOX_MESH_INDICES = {
		0, 4, 6,
		0, 6, 2, 
		0, 2, 3, 
		0, 3, 1,
		2, 6, 7,
		2, 7, 3,
		4, 7, 6,
		4, 5, 7,
		0, 5, 4,
		0, 1, 5,
		1, 7, 5,
		1, 3, 7,
	};
	
	private static final float[] SKYBOX_MESH_VERTICES = {
		-SKYBOX_SIZE, -SKYBOX_SIZE, -SKYBOX_SIZE,
		-SKYBOX_SIZE, -SKYBOX_SIZE, SKYBOX_SIZE,
		-SKYBOX_SIZE, SKYBOX_SIZE, -SKYBOX_SIZE,
		-SKYBOX_SIZE, SKYBOX_SIZE, SKYBOX_SIZE,
		SKYBOX_SIZE, -SKYBOX_SIZE, -SKYBOX_SIZE,
		SKYBOX_SIZE, -SKYBOX_SIZE, SKYBOX_SIZE,
		SKYBOX_SIZE, SKYBOX_SIZE, -SKYBOX_SIZE,
		SKYBOX_SIZE, SKYBOX_SIZE, SKYBOX_SIZE	
	};
	
	private static final GameObjectFactory loader = new GameObjectFactory();
	
	public static void loadAll() {
		
		// Meshes
		loadMesh(Resource.MESH_CITY, MESH_PATH + "city.obj");
		loadMesh(Resource.MESH_ROAD, MESH_PATH + "road.obj");
		loadMesh(Resource.MESH_ROBBER, MESH_PATH + "robber.obj");
		loadMesh(Resource.MESH_SETTLEMENT, MESH_PATH + "settlement.obj");
		loadMesh(Resource.MESH_TILE, MESH_PATH + "tile.obj");
		loadMesh(Resource.MESH_BOARD, MESH_PATH + "board.obj");
		loadMesh(Resource.MESH_TABLE, MESH_PATH + "table.obj");
		resources.put(Resource.MESH_GUI, loader.loadMesh(GUI_MESH_INDICES, GUI_MESH_VERTICES, GUI_MESH_UVS));
		resources.put(Resource.MESH_SKYBOX, loader.loadMesh(SKYBOX_MESH_INDICES, SKYBOX_MESH_VERTICES));
		
		// Textures
		loadTexture2D(Resource.TEXTURE_TILE_BRICK, TEXTURE_PATH + "tile_brick.png", GL_LINEAR, true);
		loadTexture2D(Resource.TEXTURE_TILE_DESERT, TEXTURE_PATH + "tile_desert.png", GL_LINEAR, true);
		loadTexture2D(Resource.TEXTURE_TILE_FOREST, TEXTURE_PATH + "tile_forest.png", GL_LINEAR, true);
		loadTexture2D(Resource.TEXTURE_TILE_SHEEP, TEXTURE_PATH + "tile_sheep.png", GL_LINEAR, true);
		loadTexture2D(Resource.TEXTURE_TILE_STONE, TEXTURE_PATH + "tile_stone.png", GL_LINEAR, true);
		loadTexture2D(Resource.TEXTURE_TILE_WHEAT, TEXTURE_PATH + "tile_wheat.png", GL_LINEAR, true);
		
		loadTexture2D(Resource.TEXTURE_CARD_BRICK, TEXTURE_PATH + "card_brick.png", GL_LINEAR, true);
		loadTexture2D(Resource.TEXTURE_CARD_FOREST, TEXTURE_PATH + "card_forest.png", GL_LINEAR, true);
		loadTexture2D(Resource.TEXTURE_CARD_SHEEP, TEXTURE_PATH + "card_sheep.png", GL_LINEAR, true);
		loadTexture2D(Resource.TEXTURE_CARD_STONE, TEXTURE_PATH + "card_stone.png", GL_LINEAR, true);
		loadTexture2D(Resource.TEXTURE_CARD_WHEAT, TEXTURE_PATH + "card_wheat.png", GL_LINEAR, true);
		
		loadTexture2D(Resource.TEXTURE_BOARD, TEXTURE_PATH + "board.png", GL_LINEAR, true);
		
		loadTexture2D(Resource.TEXTURE_CARD_KNIGHT, TEXTURE_PATH + "card_knight.png", GL_LINEAR, true);
		
		loadTexture2D(Resource.TEXTURE_TABLE, TEXTURE_PATH + "wood.jpg", GL_LINEAR, true);
		
		// Color textures
		loadTexture2D(Resource.TEXTURE_COLOR_BLUE, TEXTURE_PATH + "blue.png", GL_NEAREST, false);
		
		// Skybox
		loadTextureCubeMap(Resource.TEXTURE_SKYBOX, TEXTURE_PATH + "skybox/");
		
		// Models
		loadModel(Resource.MODEL_TILE_BRICK, Resource.MESH_TILE, Resource.TEXTURE_TILE_BRICK);
		loadModel(Resource.MODEL_TILE_DESERT, Resource.MESH_TILE, Resource.TEXTURE_TILE_DESERT);
		loadModel(Resource.MODEL_TILE_FOREST, Resource.MESH_TILE, Resource.TEXTURE_TILE_FOREST);
		loadModel(Resource.MODEL_TILE_SHEEP, Resource.MESH_TILE, Resource.TEXTURE_TILE_SHEEP);
		loadModel(Resource.MODEL_TILE_STONE, Resource.MESH_TILE, Resource.TEXTURE_TILE_STONE);
		loadModel(Resource.MODEL_TILE_WHEAT, Resource.MESH_TILE, Resource.TEXTURE_TILE_WHEAT);
		loadModel(Resource.MODEL_BOARD, Resource.MESH_BOARD, Resource.TEXTURE_BOARD);
		loadModel(Resource.MODEL_TABLE, Resource.MESH_TABLE, Resource.TEXTURE_TABLE);
		
	}

	public static void cleanAll() {
		loader.destroy();
	}
	
	private static void loadMesh(Resource id, String filepath) {
		resources.put(id, loader.loadOBJ(filepath));
	}
	
	private static void loadTexture2D(Resource id, String filepath, int filtering, boolean mipmap) {
		resources.put(id, loader.loadTexture2D(filepath, filtering, mipmap));
	}
	
	private static void loadTextureCubeMap(Resource id, String filepath) {
		resources.put(id, loader.loadTextureCubeMap(filepath));
	}
	
	private static void loadModel(Resource id, Resource mesh, Resource texture) {
		resources.put(id, new TexturedMesh(get(mesh), get(texture)));
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends GameObject> T get(Resource id) {
		return (T) resources.get(id);
	}
	
}
