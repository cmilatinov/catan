package resources;

import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static resources.ProxyGameResource.*;

import java.util.HashMap;

import objects.GameResource;
import objects.GameResourceFactory;
import objects.TexturedMesh;

public class GameResources {
	
	private static final HashMap<Resource, GameResource> resources = new HashMap<>();
	
	private static final String MESH_PATH = "./models/";
	private static final String TEXTURE_PATH = "./textures/";
	
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
	
	private static final GameResourceFactory loader = new GameResourceFactory();
	
	public static void loadAll() {
		
		// Meshes
		proxyMesh(Resource.MESH_CITY, MESH_PATH + "city.obj");
		proxyMesh(Resource.MESH_ROAD, MESH_PATH + "road.obj");
		proxyMesh(Resource.MESH_ROBBER, MESH_PATH + "robber.obj");
		proxyMesh(Resource.MESH_SETTLEMENT, MESH_PATH + "settlement.obj");
		proxyMesh(Resource.MESH_TILE, MESH_PATH + "tile.obj");
		proxyMesh(Resource.MESH_BOARD, MESH_PATH + "board.obj");
		proxyMesh(Resource.MESH_TABLE, MESH_PATH + "table.obj");
		proxyMesh(Resource.MESH_TOKEN, MESH_PATH + "token.obj");
		resources.put(Resource.MESH_SKYBOX, loader.loadMesh(SKYBOX_MESH_INDICES, SKYBOX_MESH_VERTICES));
		
		// Textures
		proxyTexture2D(Resource.TEXTURE_TILE_BRICK, TEXTURE_PATH + "tile_brick.png", GL_LINEAR, true);
		proxyTexture2D(Resource.TEXTURE_TILE_DESERT, TEXTURE_PATH + "tile_desert.png", GL_LINEAR, true);
		proxyTexture2D(Resource.TEXTURE_TILE_FOREST, TEXTURE_PATH + "tile_forest.png", GL_LINEAR, true);
		proxyTexture2D(Resource.TEXTURE_TILE_SHEEP, TEXTURE_PATH + "tile_sheep.png", GL_LINEAR, true);
		proxyTexture2D(Resource.TEXTURE_TILE_STONE, TEXTURE_PATH + "tile_stone.png", GL_LINEAR, true);
		proxyTexture2D(Resource.TEXTURE_TILE_WHEAT, TEXTURE_PATH + "tile_wheat.png", GL_LINEAR, true);

		proxyTexture2D(Resource.TEXTURE_TOKEN_2, TEXTURE_PATH + "token_02.png", GL_LINEAR, true);
		proxyTexture2D(Resource.TEXTURE_TOKEN_3, TEXTURE_PATH + "token_03.png", GL_LINEAR, true);
		proxyTexture2D(Resource.TEXTURE_TOKEN_4, TEXTURE_PATH + "token_04.png", GL_LINEAR, true);
		proxyTexture2D(Resource.TEXTURE_TOKEN_5, TEXTURE_PATH + "token_05.png", GL_LINEAR, true);
		proxyTexture2D(Resource.TEXTURE_TOKEN_6, TEXTURE_PATH + "token_06.png", GL_LINEAR, true);
		proxyTexture2D(Resource.TEXTURE_TOKEN_8, TEXTURE_PATH + "token_08.png", GL_LINEAR, true);
		proxyTexture2D(Resource.TEXTURE_TOKEN_9, TEXTURE_PATH + "token_09.png", GL_LINEAR, true);
		proxyTexture2D(Resource.TEXTURE_TOKEN_10, TEXTURE_PATH + "token_10.png", GL_LINEAR, true);
		proxyTexture2D(Resource.TEXTURE_TOKEN_11, TEXTURE_PATH + "token_11.png", GL_LINEAR, true);
		proxyTexture2D(Resource.TEXTURE_TOKEN_12, TEXTURE_PATH + "token_12.png", GL_LINEAR, true);

		proxyTexture2D(Resource.TEXTURE_CARD_BRICK, TEXTURE_PATH + "card_brick.png", GL_LINEAR, true);
		proxyTexture2D(Resource.TEXTURE_CARD_FOREST, TEXTURE_PATH + "card_forest.png", GL_LINEAR, true);
		proxyTexture2D(Resource.TEXTURE_CARD_SHEEP, TEXTURE_PATH + "card_sheep.png", GL_LINEAR, true);
		proxyTexture2D(Resource.TEXTURE_CARD_STONE, TEXTURE_PATH + "card_stone.png", GL_LINEAR, true);
		proxyTexture2D(Resource.TEXTURE_CARD_WHEAT, TEXTURE_PATH + "card_wheat.png", GL_LINEAR, true);
		
		proxyTexture2D(Resource.TEXTURE_BOARD, TEXTURE_PATH + "board.png", GL_LINEAR, true);
		proxyTexture2D(Resource.TEXTURE_CARD_KNIGHT, TEXTURE_PATH + "card_knight.png", GL_LINEAR, true);
		proxyTexture2D(Resource.TEXTURE_TABLE, TEXTURE_PATH + "wood.jpg", GL_LINEAR, true);
		
		// Color textures
		proxyTexture2D(Resource.TEXTURE_COLOR_BLUE, TEXTURE_PATH + "blue.png", GL_NEAREST, false);
		
		// Skybox
		proxyTextureCubeMap(Resource.TEXTURE_SKYBOX, TEXTURE_PATH + "skybox/");
		
		// Models
		proxyModel(Resource.MODEL_TILE_BRICK, Resource.MESH_TILE, Resource.TEXTURE_TILE_BRICK);
		proxyModel(Resource.MODEL_TILE_DESERT, Resource.MESH_TILE, Resource.TEXTURE_TILE_DESERT);
		proxyModel(Resource.MODEL_TILE_FOREST, Resource.MESH_TILE, Resource.TEXTURE_TILE_FOREST);
		proxyModel(Resource.MODEL_TILE_SHEEP, Resource.MESH_TILE, Resource.TEXTURE_TILE_SHEEP);
		proxyModel(Resource.MODEL_TILE_STONE, Resource.MESH_TILE, Resource.TEXTURE_TILE_STONE);
		proxyModel(Resource.MODEL_TILE_WHEAT, Resource.MESH_TILE, Resource.TEXTURE_TILE_WHEAT);
		proxyModel(Resource.MODEL_BOARD, Resource.MESH_BOARD, Resource.TEXTURE_BOARD);
		proxyModel(Resource.MODEL_TABLE, Resource.MESH_TABLE, Resource.TEXTURE_TABLE);

		proxyModel(Resource.MODEL_TOKEN_2, Resource.MESH_TOKEN, Resource.TEXTURE_TOKEN_2);
		proxyModel(Resource.MODEL_TOKEN_3, Resource.MESH_TOKEN, Resource.TEXTURE_TOKEN_3);
		proxyModel(Resource.MODEL_TOKEN_4, Resource.MESH_TOKEN, Resource.TEXTURE_TOKEN_4);
		proxyModel(Resource.MODEL_TOKEN_5, Resource.MESH_TOKEN, Resource.TEXTURE_TOKEN_5);
		proxyModel(Resource.MODEL_TOKEN_6, Resource.MESH_TOKEN, Resource.TEXTURE_TOKEN_6);
		proxyModel(Resource.MODEL_TOKEN_8, Resource.MESH_TOKEN, Resource.TEXTURE_TOKEN_8);
		proxyModel(Resource.MODEL_TOKEN_9, Resource.MESH_TOKEN, Resource.TEXTURE_TOKEN_9);
		proxyModel(Resource.MODEL_TOKEN_10, Resource.MESH_TOKEN, Resource.TEXTURE_TOKEN_10);
		proxyModel(Resource.MODEL_TOKEN_11, Resource.MESH_TOKEN, Resource.TEXTURE_TOKEN_11);
		proxyModel(Resource.MODEL_TOKEN_12, Resource.MESH_TOKEN, Resource.TEXTURE_TOKEN_12);
		
	}

	public static void cleanAll() {
		loader.destroy();
	}

	private static void loadMesh(Resource id, String filepath) {
		resources.put(id, loader.loadOBJ(filepath));
	}

	private static void proxyMesh(Resource id, String filepath) {
		var proxy = new ProxyGameResource(loader, ResourceType.MESH, filepath);
		resources.put(id, proxy);
	}

	private static void loadTexture2D(Resource id, String filepath, int filtering, boolean mipmap) {
		resources.put(id, loader.loadTexture2D(filepath, filtering, mipmap));
	}


	private static void proxyTexture2D(Resource id, String filepath, int filtering, boolean mipmap) {
		var proxy = new ProxyGameResource(loader, ResourceType.TEXTURE_2D, filepath, filtering, mipmap);
		resources.put(id, proxy);
	}

	private static void loadTextureCubeMap(Resource id, String filepath) {
		resources.put(id, loader.loadTextureCubeMap(filepath));
	}

	private static void proxyTextureCubeMap(Resource id, String filepath) {
		var proxy = new ProxyGameResource(loader, ResourceType.TEXTURE_CUBE_MAP, filepath);
		resources.put(id, proxy);
	}

	private static void loadModel(Resource id, Resource mesh, Resource texture) {
		resources.put(id, new TexturedMesh(get(mesh), get(texture)));
	}

	private static void proxyModel(Resource id, Resource mesh, Resource texture) {
		var proxy = new ProxyGameResource(loader, ResourceType.MODEL, mesh, texture);
		resources.put(id, proxy);
	}

	@SuppressWarnings("unchecked")
	public static <T extends GameResource> T get(Resource id) {
		var resource = resources.get(id);
		if (resource instanceof ProxyGameResource) {
			var resolvedResource = ((ProxyGameResource) resource).resolve();
			resources.put(id, resolvedResource);
			return (T) resolvedResource;
		}
		return (T) resource;
	}
	
}
