package scripts;

import entities.*;
import states.GameState;
import states.StateSettling;
import objects.GameScript;
import objects.InjectableScript;
import observers.GameObserver;
import resources.Resource;

import java.util.ArrayList;
import java.util.Collections;

import static observers.GameObserver.*;
import static org.lwjgl.glfw.GLFW.*;

public class GameManager extends GameScript {
    // Entities registered in the scene that we need access to
    @InjectableScript
    private Tiles tiles;

    private final ArrayList<Player> players = new ArrayList<>();
    private boolean playersReversed = false;

    public final GameObserver gameObserver = new GameObserver();

    private int turn;
    private final int SETUP_TURNS = 2;

    private GameState currentState;

    EntityToggleable collidingEntity = null;

    /**
     * Setter for the current game state
     * @param state - State we wish to switch the current game state to.
     */
    public void setGameState(GameState state) {
        this.currentState = state;
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

        Entity clicked = getScene().physics().raycastFromCamera();

        currentState.onClick(clicked);

//        Entity clicked = getScene().physics().raycastFromCamera();
//
//        switch(currentGamePhase) {
//            case SETUP:
//                settingUp.onClick(clicked, getCurrentPlayer());
//                if(settingUp.isTurnDone()) {
//                    turn ++;
//                    if(turn == SETUP_TURNS * players.size())
//                        setCurrentGamePhase(GamePhases.ROLLING);
//
//                    gameObserver.broadcast(PlayerEvent.PLAYER_TURN, getCurrentPlayer());
//                }
//                break;
//            case SETTLING:
//                settling.onClick(clicked, getCurrentPlayer());
//                break;
//            case STEALING:
//                stealing.onClick(clicked, getCurrentPlayer());
//                if(stealing.isTurnDone())
//                    setCurrentGamePhase(GamePhases.SETTLING);
//                break;
//        }
    }

    public void onSpaceReleased(int mods) {
        currentState.onSpace();
    }

    public int getPlayerCount() {
        return players.size();
    }

    public void reversePlayers() {
        Collections.reverse(players);
        playersReversed = !playersReversed;
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
        currentState = new StateSettling();

        for(int i = 0; i < 4; i ++) {
            Player newPlayer = new Player();
            players.add(newPlayer);
            gameObserver.broadcast(PlayerEvent.PLAYER_ADDED, newPlayer);
        }
        players.get(0).setColor(Resource.TEXTURE_COLOR_BLUE);
        gameObserver.broadcast(PlayerEvent.PLAYER_COLOR_CHANGED, players.get(0));
        players.get(1).setColor(Resource.TEXTURE_COLOR_GREEN);
        gameObserver.broadcast(PlayerEvent.PLAYER_COLOR_CHANGED, players.get(1));
        players.get(2).setColor(Resource.TEXTURE_COLOR_PURPLE);
        gameObserver.broadcast(PlayerEvent.PLAYER_COLOR_CHANGED, players.get(2));
        players.get(3).setColor(Resource.TEXTURE_COLOR_RED);
        gameObserver.broadcast(PlayerEvent.PLAYER_COLOR_CHANGED, players.get(3));

        getScene().registerMouseClickAction(this::onClick);
        getScene().registerKeyUpAction(GLFW_KEY_SPACE, this::onSpaceReleased);

        gameObserver.broadcast(PlayerEvent.PLAYER_TURN, getCurrentPlayer());
    }

    public Player getCurrentPlayer() {
        return players.get(turn % 4);
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
