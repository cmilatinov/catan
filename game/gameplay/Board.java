package gameplay;

import entities.*;
import lights.Light;
import objects.GameScript;
import org.joml.Vector3f;
import resources.Resource;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;

public class Board extends GameScript {
	private Entity robber;
	private Tiles tiles;

	private int turn;

	private ArrayList<Vertex> verticesOccupied;
	private ArrayList<Side> sidesOccupied;

	private ArrayList<Player> players = new ArrayList<Player>();
	private int currPlayer = 0;

	EntityToggleable collidingEntity = null;

	/**
	 * Constructor to create a board.
	 * 
	 * @param boardRadius - Radius of the board.
	 */
	public Board(int boardRadius) {
		// Sets up the amount of tiles per row that we want depending on the board radius.
		turn = 0;

		tiles = new Tiles(boardRadius);
		tiles.generateMap();

		verticesOccupied = new ArrayList<Vertex>();
		sidesOccupied = new ArrayList<Side>();

		for(int i = 0; i < 4; i ++)
			players.add(new Player());
		players.get(0).setColor(Resource.TEXTURE_COLOR_BLUE);
		players.get(1).setColor(Resource.TEXTURE_COLOR_GREEN);
		players.get(2).setColor(Resource.TEXTURE_COLOR_PURPLE);
		players.get(3).setColor(Resource.TEXTURE_COLOR_RED);

	}

	public boolean canSettle() {

		return true;
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

		Light sun = new Light(new Vector3f(0.6f, 0.6f, 0.6f), new Vector3f(500, 1000, 500));
		Light sun2 = new Light(new Vector3f(0.6f, 0.6f, 0.6f), new Vector3f(-500, 1000, 500));
		getScene().register(sun);
		getScene().register(sun2);

		getScene().registerKeyUpAction(GLFW_KEY_X, (int mods) -> currPlayer += 1 % 4);
		getScene().registerKeyUpAction(GLFW_KEY_Z, (int mods) -> {if(currPlayer > 0) currPlayer --;});

		// Events
		getScene().registerMouseClickAction((int button, int action, int mods) -> {
			if(button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_RELEASE) {
				Entity clickable = getScene().physics().raycastFromCamera();

				if(clickable instanceof Vertex) {
					Vertex temp = ((Vertex) clickable);
					Vector3f result = new Vector3f();
					if(temp.getBuilding() == null) {
						for(Vertex v : verticesOccupied) {
							v.getPosition().sub(temp.getPosition(), result);
							if(Math.abs(result.length() - 1) < 0.01)
								break;
						}

						if(Math.abs(result.length() - 1) > 0.01) {
							temp.settle(players.get(currPlayer));
							verticesOccupied.add(temp);
						}
					} else {
						getScene().remove(temp.getBuilding());
						temp.upgradeSettlement(players.get(currPlayer));
					}
					if(temp.getBuilding() != null)
						getScene().register(temp.getBuilding());
				} else if (clickable instanceof Side) {
					Side temp = ((Side) clickable);
					if(temp.getRoad() == null) {
						sidesOccupied.add(temp);
						temp.createRoad(players.get(currPlayer));
						getScene().register(temp.getRoad());
					}
				}
			}
		});

	}

	@Override
	public void update(double delta) {
		robber.rotate((new Vector3f(0, 200 * (float)delta, 0)));

		// Hover effect
		Entity currentCollidingEntity = getScene().physics().raycastFromCamera();

		if(collidingEntity != null) {
			collidingEntity.setRender(false);
		}

		if(currentCollidingEntity != null) {
			if(currentCollidingEntity instanceof EntityToggleable) {
				collidingEntity = ((EntityToggleable) currentCollidingEntity);
				collidingEntity.setRender(true);
			}
		}
	}

	@Override
	public void destroy() {

	}
}
