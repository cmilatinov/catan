package scripts;

import entities.*;
import gameplay.Tiles;
import objects.GameScript;
import org.joml.Vector3f;
import resources.Resource;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class GameManager extends GameScript {
    EntityToggleable collidingEntity = null;

    private Tiles tiles;
    private Entity robber;

    private ArrayList<Vertex> verticesOccupied;
    private ArrayList<Side> sidesOccupied;

    private ArrayList<Player> players = new ArrayList<Player>();

    enum GamePhases {
        ROLLING,
        TRADING,
        STEALING;

        public static GamePhases currentPhase = ROLLING;

        public static GamePhases getGamePhase(){
            return currentPhase;
        }

        public static void rolling() {
            currentPhase = ROLLING;
        }

        public static void trading() {
            currentPhase = TRADING;
        }

        public static void stealing() {
            currentPhase = STEALING;
        }
    }

    private int turn;

    public int roll() {
        return (int)(Math.random() * 6) + 1;
    }

    public GameManager() {
        // initializing variables
        tiles = new Tiles(3);
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

    @Override
    public void destroy() {

    }

    @Override
    public void initialize() {
        tiles.register(getScene());

        // Events
        getScene().registerKeyUpAction(GLFW_KEY_SPACE, (int mods) -> {
            if(GamePhases.getGamePhase() != GamePhases.ROLLING)
                return;

            int roll1 = roll();
            int roll2 = roll();

            System.out.print("You rolled: ");
            System.out.println(roll1 + roll2);

            if(roll1 + roll2 == 7) {
                // Setting phase to stealing
                // phaseIndex = 2;
                System.out.println("Robber has been rolled");
                GamePhases.stealing();
            } else {
                for(Tile t : tiles.getTiles(roll1 + roll2)) {
                    for(Vertex v : t.getOccupiedVertices()) {
                        v.getOwner().addResourceCard(t.getType(), v.getBuildingValue());

                        System.out.println(v.getOwner() + " will be getting " + v.getBuildingValue() + " cards of type " + t.getType());
                    }
                }
                GamePhases.trading();
            }
        });

        // this is for going next turn
        getScene().registerKeyUpAction(GLFW_KEY_N, (int mods) -> {
            if(GamePhases.getGamePhase() != GamePhases.TRADING)
                return;
            GamePhases.rolling();
            turn ++;

            System.out.println(players.get(turn % 4).getColor());
        });

        robber = Robber.create(Resource.TEXTURE_COLOR_BLUE).scale(0.01f);
        getScene().register(robber);
        robber.setPosition(tiles.getDesertTile().getPosition());

        getScene().registerMouseClickAction((int button, int action, int mods) -> {
            if(button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_RELEASE) {
                Entity clickable = getScene().physics().raycastFromCamera();

                if(clickable instanceof Vertex) {
                    Vertex temp = ((Vertex) clickable);
                    Vector3f result = new Vector3f();
                    if(temp.getBuilding() == null) {
                        // Checks if the vertex is next to an occupied vertex
                        for(Vertex v : verticesOccupied) {
                            v.getPosition().sub(temp.getPosition(), result);
                            if(Math.abs(result.length() - 1) < 0.01)
                                break;
                        }

                        if(Math.abs(result.length() - 1) > 0.01) {
                            if(temp.canSettle(players.get(turn % 4))) {
                                temp.settle(players.get(turn % 4));
                                verticesOccupied.add(temp);
                                getScene().register(temp.getBuilding());
                            }
                        }
                    } else {
                        if(temp.canUpgrade(players.get(turn % 4))) {
                            getScene().remove(temp.getBuilding());
                            temp.upgradeSettlement(players.get(turn % 4));
                            getScene().register(temp.getBuilding());
                        }
                    }

                } else if (clickable instanceof Side) {
                    Side temp = ((Side) clickable);
                    if(temp.getRoad() == null) {
                        if(temp.canSetRoad(players.get(turn % 4))) {
                            sidesOccupied.add(temp);
                            temp.createRoad(players.get(turn % 4));
                            getScene().register(temp.getRoad());
                        }
                    }
                } else  if (clickable instanceof Tile) {
                    if(GamePhases.getGamePhase() == GamePhases.STEALING) {
                        robber.setPosition(clickable.getPosition()).translate(new Vector3f(0, 0.1f, -0.5f));
                        GamePhases.trading();
                    }

                }
            }
        });
    }

    @Override
    public void update(double delta) {
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
}
