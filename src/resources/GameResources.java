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
	
	private static final GameObjectFactory loader = new GameObjectFactory();
	
	public static void loadAll() {
		
		// Meshes
		loadMesh(Resource.MESH_CITY, MESH_PATH + "city.obj");
		loadMesh(Resource.MESH_ROAD, MESH_PATH + "road.obj");
		loadMesh(Resource.MESH_ROBBER, MESH_PATH + "robber.obj");
		loadMesh(Resource.MESH_SETTLEMENT, MESH_PATH + "settlement.obj");
		loadMesh(Resource.MESH_TILE, MESH_PATH + "tile.obj");
		
		// Tile textures
		loadTexture2D(Resource.TEXTURE_TILE_BRICK, TEXTURE_PATH + "tile_brick.png", GL_LINEAR, true);
		loadTexture2D(Resource.TEXTURE_TILE_DESERT, TEXTURE_PATH + "tile_desert.png", GL_LINEAR, true);
		loadTexture2D(Resource.TEXTURE_TILE_FOREST, TEXTURE_PATH + "tile_forest.png", GL_LINEAR, true);
		loadTexture2D(Resource.TEXTURE_TILE_SHEEP, TEXTURE_PATH + "tile_sheep.png", GL_LINEAR, true);
		loadTexture2D(Resource.TEXTURE_TILE_STONE, TEXTURE_PATH + "tile_stone.png", GL_LINEAR, true);
		loadTexture2D(Resource.TEXTURE_TILE_WHEAT, TEXTURE_PATH + "tile_wheat.png", GL_LINEAR, true);
		
		// Color textures
		loadTexture2D(Resource.TEXTURE_COLOR_BLUE, TEXTURE_PATH + "blue.png", GL_NEAREST, false);
		
		// Models
		loadModel(Resource.MODEL_TILE_BRICK, Resource.MESH_TILE, Resource.TEXTURE_TILE_BRICK);
		loadModel(Resource.MODEL_TILE_DESERT, Resource.MESH_TILE, Resource.TEXTURE_TILE_DESERT);
		loadModel(Resource.MODEL_TILE_FOREST, Resource.MESH_TILE, Resource.TEXTURE_TILE_FOREST);
		loadModel(Resource.MODEL_TILE_SHEEP, Resource.MESH_TILE, Resource.TEXTURE_TILE_SHEEP);
		loadModel(Resource.MODEL_TILE_STONE, Resource.MESH_TILE, Resource.TEXTURE_TILE_STONE);
		loadModel(Resource.MODEL_TILE_WHEAT, Resource.MESH_TILE, Resource.TEXTURE_TILE_WHEAT);
		
	}
	
	private static void loadMesh(Resource id, String filepath) {
		resources.put(id, loader.loadOBJ(filepath));
	}
	
	private static void loadTexture2D(Resource id, String filepath, int filtering, boolean mipmap) {
		resources.put(id, loader.loadTexture2D(filepath, filtering, mipmap));
	}
	
	private static void loadModel(Resource id, Resource mesh, Resource texture) {
		resources.put(id, new TexturedMesh(get(mesh), get(texture)));
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends GameObject> T get(Resource id) {
		return (T) resources.get(id);
	}
	
}
