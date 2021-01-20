package resources;

import main.Engine;
import objects.GameResource;

import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_NEAREST;

public class GameResources extends GameResourceLoader {

	public static final String MESH_PATH = "./models/";
	public static final String TEXTURE_PATH = "./textures/";

	/**
	 * Constructs a new {@link GameResourceLoader} for the given {@link Engine} instance.
	 *
	 * @param engine The engine calling this loader.
	 */
	public GameResources(Engine engine) {
		super(engine);
	}

	/**
	 * {@inheritDoc}
	 */
	public void loadResources() {
		// Meshes
		loadMesh(Resource.MESH_CITY, MESH_PATH + "city.obj");
		loadMesh(Resource.MESH_ROAD, MESH_PATH + "road.obj");
		loadMesh(Resource.MESH_ROBBER, MESH_PATH + "robber.obj");
		loadMesh(Resource.MESH_SETTLEMENT, MESH_PATH + "settlement.obj");
		loadMesh(Resource.MESH_TILE, MESH_PATH + "tile.obj");
		loadMesh(Resource.MESH_BOARD, MESH_PATH + "board.obj");
		loadMesh(Resource.MESH_TABLE, MESH_PATH + "table.obj");
		loadMesh(Resource.MESH_TOKEN, MESH_PATH + "token.obj");
		reportProgress(0.1f);

		// Textures
		loadTexture2D(Resource.TEXTURE_MAIN_MENU, TEXTURE_PATH + "fields_blurred.jpg", GL_LINEAR, false);

		loadTexture2D(Resource.TEXTURE_TILE_BRICK, TEXTURE_PATH + "tile_brick.png", GL_LINEAR, true);
		loadTexture2D(Resource.TEXTURE_TILE_DESERT, TEXTURE_PATH + "tile_desert.png", GL_LINEAR, true);
		loadTexture2D(Resource.TEXTURE_TILE_FOREST, TEXTURE_PATH + "tile_forest.png", GL_LINEAR, true);
		loadTexture2D(Resource.TEXTURE_TILE_SHEEP, TEXTURE_PATH + "tile_sheep.png", GL_LINEAR, true);
		reportProgress(0.2f);
		loadTexture2D(Resource.TEXTURE_TILE_STONE, TEXTURE_PATH + "tile_stone.png", GL_LINEAR, true);
		loadTexture2D(Resource.TEXTURE_TILE_WHEAT, TEXTURE_PATH + "tile_wheat.png", GL_LINEAR, true);

		loadTexture2D(Resource.TEXTURE_TOKEN_2, TEXTURE_PATH + "token_02.png", GL_LINEAR, true);
		loadTexture2D(Resource.TEXTURE_TOKEN_3, TEXTURE_PATH + "token_03.png", GL_LINEAR, true);
		loadTexture2D(Resource.TEXTURE_TOKEN_4, TEXTURE_PATH + "token_04.png", GL_LINEAR, true);
		loadTexture2D(Resource.TEXTURE_TOKEN_5, TEXTURE_PATH + "token_05.png", GL_LINEAR, true);
		reportProgress(0.3f);
		loadTexture2D(Resource.TEXTURE_TOKEN_6, TEXTURE_PATH + "token_06.png", GL_LINEAR, true);
		loadTexture2D(Resource.TEXTURE_TOKEN_8, TEXTURE_PATH + "token_08.png", GL_LINEAR, true);
		loadTexture2D(Resource.TEXTURE_TOKEN_9, TEXTURE_PATH + "token_09.png", GL_LINEAR, true);
		loadTexture2D(Resource.TEXTURE_TOKEN_10, TEXTURE_PATH + "token_10.png", GL_LINEAR, true);
		loadTexture2D(Resource.TEXTURE_TOKEN_11, TEXTURE_PATH + "token_11.png", GL_LINEAR, true);
		loadTexture2D(Resource.TEXTURE_TOKEN_12, TEXTURE_PATH + "token_12.png", GL_LINEAR, true);
		reportProgress(0.4f);

		loadTexture2D(Resource.TEXTURE_CARD_BRICK, TEXTURE_PATH + "card_brick.png", GL_LINEAR, true);
		loadTexture2D(Resource.TEXTURE_CARD_FOREST, TEXTURE_PATH + "card_forest.png", GL_LINEAR, true);
		loadTexture2D(Resource.TEXTURE_CARD_SHEEP, TEXTURE_PATH + "card_sheep.png", GL_LINEAR, true);
		loadTexture2D(Resource.TEXTURE_CARD_STONE, TEXTURE_PATH + "card_stone.png", GL_LINEAR, true);
		reportProgress(0.5f);
		loadTexture2D(Resource.TEXTURE_CARD_WHEAT, TEXTURE_PATH + "card_wheat.png", GL_LINEAR, true);

		loadTexture2D(Resource.TEXTURE_BOARD, TEXTURE_PATH + "board.png", GL_LINEAR, true);
		loadTexture2D(Resource.TEXTURE_CARD_KNIGHT, TEXTURE_PATH + "card_knight.png", GL_LINEAR, true);
		loadTexture2D(Resource.TEXTURE_TABLE, TEXTURE_PATH + "wood.jpg", GL_LINEAR, true);
		reportProgress(0.6f);

		// Color textures
		loadTexture2D(Resource.TEXTURE_COLOR_BLUE, TEXTURE_PATH + "color_blue.png", GL_NEAREST, false);
		loadTexture2D(Resource.TEXTURE_COLOR_PURPLE, TEXTURE_PATH + "color_purple.png", GL_NEAREST, false);
		loadTexture2D(Resource.TEXTURE_COLOR_ORANGE, TEXTURE_PATH + "color_orange.png", GL_NEAREST, false);
		loadTexture2D(Resource.TEXTURE_COLOR_RED, TEXTURE_PATH + "color_red.png", GL_NEAREST, false);
		loadTexture2D(Resource.TEXTURE_COLOR_GREEN, TEXTURE_PATH + "color_green.png", GL_NEAREST, false);

		// Skybox
		loadTextureCubeMap(Resource.TEXTURE_SKYBOX, TEXTURE_PATH + "skybox/");

		// Models
		loadModel(Resource.MODEL_TILE_BRICK, Resource.MESH_TILE, Resource.TEXTURE_TILE_BRICK);
		reportProgress(0.7f);
		loadModel(Resource.MODEL_TILE_DESERT, Resource.MESH_TILE, Resource.TEXTURE_TILE_DESERT);
		loadModel(Resource.MODEL_TILE_FOREST, Resource.MESH_TILE, Resource.TEXTURE_TILE_FOREST);
		loadModel(Resource.MODEL_TILE_SHEEP, Resource.MESH_TILE, Resource.TEXTURE_TILE_SHEEP);
		loadModel(Resource.MODEL_TILE_STONE, Resource.MESH_TILE, Resource.TEXTURE_TILE_STONE);
		loadModel(Resource.MODEL_TILE_WHEAT, Resource.MESH_TILE, Resource.TEXTURE_TILE_WHEAT);
		loadModel(Resource.MODEL_BOARD, Resource.MESH_BOARD, Resource.TEXTURE_BOARD);
		loadModel(Resource.MODEL_TABLE, Resource.MESH_TABLE, Resource.TEXTURE_TABLE);
		reportProgress(0.8f);

		loadModel(Resource.MODEL_TOKEN_2, Resource.MESH_TOKEN, Resource.TEXTURE_TOKEN_2);
		loadModel(Resource.MODEL_TOKEN_3, Resource.MESH_TOKEN, Resource.TEXTURE_TOKEN_3);
		loadModel(Resource.MODEL_TOKEN_4, Resource.MESH_TOKEN, Resource.TEXTURE_TOKEN_4);
		loadModel(Resource.MODEL_TOKEN_5, Resource.MESH_TOKEN, Resource.TEXTURE_TOKEN_5);
		loadModel(Resource.MODEL_TOKEN_6, Resource.MESH_TOKEN, Resource.TEXTURE_TOKEN_6);
		reportProgress(0.9f);
		loadModel(Resource.MODEL_TOKEN_8, Resource.MESH_TOKEN, Resource.TEXTURE_TOKEN_8);
		loadModel(Resource.MODEL_TOKEN_9, Resource.MESH_TOKEN, Resource.TEXTURE_TOKEN_9);
		loadModel(Resource.MODEL_TOKEN_10, Resource.MESH_TOKEN, Resource.TEXTURE_TOKEN_10);
		loadModel(Resource.MODEL_TOKEN_11, Resource.MESH_TOKEN, Resource.TEXTURE_TOKEN_11);
		loadModel(Resource.MODEL_TOKEN_12, Resource.MESH_TOKEN, Resource.TEXTURE_TOKEN_12);
		reportProgress(1.0f);
	}

	private void loadMesh(Resource id, String filepath) {
		loadMesh(id.ordinal(), filepath);
	}

	private void loadTexture2D(Resource id, String filepath, int filtering, boolean mipmap) {
		loadTexture2D(id.ordinal(), filepath, filtering, mipmap);
	}

	private void loadTextureCubeMap(Resource id, String filepath) {
		loadTextureCubeMap(id.ordinal(), filepath);
	}

	private void loadModel(Resource id, Resource mesh, Resource texture) {
		loadModel(id.ordinal(), mesh.ordinal(), texture.ordinal());
	}

	public static <T extends GameResource> T get(Resource id) {
		return get(id.ordinal());
	}

}
