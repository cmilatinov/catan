package scene;

import entities.*;
import scripts.Tiles;
import lights.Light;
import main.Scene;
import org.joml.Vector3f;
import resources.GameResources;
import resources.Resource;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_FILL;

public class Game extends Scene {
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

    @Override
    public void initialize() {
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

        // Skybox
        setSkyboxTexture(GameResources.get(Resource.TEXTURE_SKYBOX));
        // R to Reset Camera
        registerKeyUpAction(GLFW_KEY_R, (int mods) -> getCamera().reset());

        // Q to toggle wireframe
        AtomicBoolean wireframe = new AtomicBoolean(false);
        registerKeyUpAction(GLFW_KEY_ESCAPE, (int mods) -> getWindow().close());
        registerKeyUpAction(GLFW_KEY_Q, (int mods) -> {
            if(!wireframe.get())
                glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
            else
                glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
            wireframe.set(!wireframe.get());
        });

        registerKeyUpAction(GLFW_KEY_1, (int mods) -> sceneManager.loadScene(MainMenu.class));
        registerKeyUpAction(GLFW_KEY_2, (int mods) -> sceneManager.loadScene(Game.class));

        Entity table = Table.create()
                .scale(10)
                .translate(new Vector3f(0, -0.07f, 0));

        register(table);

        Light sun = new Light(new Vector3f(0.6f, 0.6f, 0.6f), new Vector3f(500, 1000, 500));
        Light sun2 = new Light(new Vector3f(0.6f, 0.6f, 0.6f), new Vector3f(-500, 1000, 500));
        register(sun);
        register(sun2);

        register(tiles);

        // Events
        registerKeyUpAction(GLFW_KEY_SPACE, (int mods) -> {
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
        registerKeyUpAction(GLFW_KEY_N, (int mods) -> {
            if(GamePhases.getGamePhase() != GamePhases.TRADING)
                return;
            GamePhases.rolling();
            turn ++;

            System.out.println(players.get(turn % 4).getColor());
        });

        robber = Robber.create(Resource.TEXTURE_COLOR_BLUE).scale(0.01f);
        register(robber);
        robber.setPosition(tiles.getDesertTile().getPosition());

        registerMouseClickAction((int button, int action, int mods) -> {
            if(button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_RELEASE) {
                Entity clickable = physics().raycastFromCamera();

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
                                register(temp.getBuilding());
                            }
                        }
                    } else {
                        if(temp.canUpgrade(players.get(turn % 4))) {
                            remove(temp.getBuilding());
                            temp.upgradeSettlement(players.get(turn % 4));
                            register(temp.getBuilding());
                        }
                    }

                } else if (clickable instanceof Side) {
                    Side temp = ((Side) clickable);
                    if(temp.getRoad() == null) {
                        if(temp.canSetRoad(players.get(turn % 4))) {
                            sidesOccupied.add(temp);
                            temp.createRoad(players.get(turn % 4));
                            register(temp.getRoad());
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
        try {
            super.update(delta);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Hover effect
        Entity currentCollidingEntity = physics().raycastFromCamera();

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
