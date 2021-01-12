package scripts;

import board.nodes.Vertex;
import entities.Entity;
import entities.EntityToggleable;
import entities.Player;
import entities.Tile;
import gameplay.ResourceType;
import objects.GameScript;
import objects.InjectableScript;
import observers.GameObserver;
import org.joml.Vector3f;
import resources.Resource;
import states.GameState;
import states.StateSetup;

import java.util.ArrayList;
import java.util.Collections;

import static observers.GameObserver.PlayerEvent;
import static observers.GameObserver.PlayerHandEvent;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

public class GameManager extends GameScript {
    // Entities registered in the scene that we need access to
    @InjectableScript
    private Tiles tiles;

    private final ArrayList<Player> players = new ArrayList<>();
    private boolean playersReversed = false;

    public final GameObserver gameObserver = new GameObserver();

    private int turn;

    private GameState currentState;

    EntityToggleable collidingEntity = null;

    /**
     * Setter for the current game state
     * @param state - State we wish to switch the current game state to.
     */
    public void setGameState(GameState state) {
        this.currentState = state;
        gameObserver.broadcast(currentState.getStateName());
    }

    /**
     * Method to handle the on click event (this will handle clicking on a tile/vertex/side)
     * @param button - Button ID
     * @param action - Action type
     * @param mods
     */
    public void onClick(int button, int action, int mods) {
        if(action == GLFW_PRESS)
            return;

        currentState.onClick(this, getScene().physics().raycastFromCamera(), getCurrentPlayer());
    }

    public void onSpaceReleased(int mods) {
        currentState.onSpace(this);
    }

    public int getPlayerCount() {
        return players.size();
    }

    public void reversePlayers() {
        Collections.reverse(players);
        playersReversed = !playersReversed;
    }

    public void rewardPlayerNearTile(int roll) {
        for(Tile t : tiles.getTiles(roll))
            for(Vertex v : t.getOccupiedVertices()) {
                v.getOwner().addResourceCard(t.getType(), v.getBuildingValue());
                gameObserver.broadcast(PlayerHandEvent.RESOURCES_ADDED, t.getType(), v.getBuildingValue());
            }
    }

    public void rewardPlayerOnNode(Vector3f nodePosition, Player player) {
        for(Tile t : tiles.getTilesNearVertex(nodePosition))
            if(t.getType() != ResourceType.DESERT) {
                player.addResourceCard(t.getType(), 1);
                if(player.getColor() == Resource.TEXTURE_COLOR_BLUE)
                    gameObserver.broadcast(PlayerHandEvent.RESOURCES_ADDED, t.getType(), 1);
            }
    }

    public boolean isPlayersReversed() {
        return playersReversed;
    }

    public void nextTurn() {
        turn ++;
    }

    @Override
    public void initialize() {
        turn = 0;

        Player newPlayer = new Player(0, Resource.TEXTURE_COLOR_BLUE);
        players.add(newPlayer);
        gameObserver.broadcast(PlayerEvent.PLAYER_ADDED, newPlayer);
        gameObserver.broadcast(PlayerEvent.PLAYER_COLOR_CHANGED, players.get(0));

        newPlayer = new Player(1, Resource.TEXTURE_COLOR_ORANGE);
        players.add(newPlayer);
        gameObserver.broadcast(PlayerEvent.PLAYER_ADDED, newPlayer);
        gameObserver.broadcast(PlayerEvent.PLAYER_COLOR_CHANGED, players.get(1));

        newPlayer = new Player(2, Resource.TEXTURE_COLOR_PURPLE);
        players.add(newPlayer);
        gameObserver.broadcast(PlayerEvent.PLAYER_ADDED, newPlayer);
        gameObserver.broadcast(PlayerEvent.PLAYER_COLOR_CHANGED, players.get(2));

        newPlayer = new Player(3, Resource.TEXTURE_COLOR_GREEN);
        players.add(newPlayer);
        gameObserver.broadcast(PlayerEvent.PLAYER_ADDED, newPlayer);
        gameObserver.broadcast(PlayerEvent.PLAYER_COLOR_CHANGED, players.get(3));

        setGameState(new StateSetup(players.size()));

        getScene().setOnSceneClick(this::onClick);
        getScene().registerKeyUpAction(GLFW_KEY_SPACE, this::onSpaceReleased);

        gameObserver.broadcast(PlayerEvent.PLAYER_TURN, getCurrentPlayer());
    }

    public Player getCurrentPlayer() {
        return players.get(turn % 4);
    }

    public void updateRobber(Vector3f pos) {
        tiles.resetEmbargoedTile();
        tiles.getRobber().setPosition(pos).translate(new Vector3f(0, 0, 0.3f));
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

    @Override
    public void destroy() {

    }
}
