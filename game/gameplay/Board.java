package gameplay;

import entities.*;
import gameplay.tiles.Tiles;
import lights.Light;
import objects.GameScript;
import org.joml.Vector3f;
import resources.Resource;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_6;

public class Board extends GameScript {
	private Entity robber;
	private Tiles tiles;
	
	/**
	 * Constructor to create a board.
	 * 
	 * @param boardRadius - Radius of the board.
	 */
	public Board(int boardRadius) {
		// Sets up the amount of tiles per row that we want depending on the board radius.
		tiles = new Tiles(boardRadius);
		tiles.generateMap();
	}

	@Override
	public void stop() {

	}

	@Override
	public void start() {

	}

	@Override
	public void initialize() {
		Entity table = Table.create()
				.scale(10)
				.translate(new Vector3f(0, -0.07f, 0));

		getScene().register(table);

		robber = Robber.create(Resource.TEXTURE_COLOR_BLUE).scale(0.01f);
		getScene().register(robber);
		robber.setPosition(tiles.getDesertTile().getPosition());

		tiles.registerBoard(getScene());

		Entity road = Road
				.create(Resource.TEXTURE_COLOR_BLUE)
				.scale(0.45f)
				.rotate(new Vector3f(0, 90, 0))
				.translate(new Vector3f(0, 0, -3.5f));
		getScene().register(road);

		getScene().getWindow().keyboard().registerKeyUp(GLFW_KEY_6, (int mods) -> {
			tiles.insertBuilding(3, getScene());
		});

		Entity settlement = Settlement
				.create(Resource.TEXTURE_COLOR_BLUE)
				.scale(0.040f)
				.translate(new Vector3f(0, 0, -3));
		getScene().register(settlement);

		Entity city = City
				.create(Resource.TEXTURE_COLOR_BLUE)
				.scale(0.045f)
				.translate(new Vector3f(0, 0, -4));
		getScene().register(city);

		Light light1 = new Light(new Vector3f(0.6f, 0.6f, 0.6f), new Vector3f(500, 1000, 500));
		Light light2 = new Light(new Vector3f(0.6f, 0.6f, 0.6f), new Vector3f(-500, 1000, -500));
		getScene().register(light1);
		getScene().register(light2);

	}

	@Override
	public void update(double delta) {
		robber.rotate((new Vector3f(0, 200 * (float)delta, 0)));
	}

	@Override
	public void destroy() {

	}
}
