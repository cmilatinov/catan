package gameplay;

import entities.*;
import lights.Light;
import objects.GameScript;
import org.joml.Vector3f;
import resources.Resource;

import java.util.Random;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_6;

public class Board extends GameScript {
	private Entity robber;
	private Tiles tiles;

	public int ind = 0;
	
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

		getScene().getWindow().keyboard().registerKeyUp(GLFW_KEY_6, (int mods) -> {
			tiles.moveSettlement(ind ++);
		});

		Light sun = new Light(new Vector3f(0.6f, 0.6f, 0.6f), new Vector3f(500, 1000, 500));
		Light sun2 = new Light(new Vector3f(0.6f, 0.6f, 0.6f), new Vector3f(-500, 1000, 500));
		getScene().register(sun);
		getScene().register(sun2);

	}

	@Override
	public void update(double delta) {
		robber.rotate((new Vector3f(0, 200 * (float)delta, 0)));
	}

	@Override
	public void destroy() {

	}
}
