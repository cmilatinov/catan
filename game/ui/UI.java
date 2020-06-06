package ui;

import entities.Player;
import objects.GameScript;
import objects.InitializeSelfAfter;
import objects.InitializeSelfBefore;
import objects.InjectableScript;
import observers.GameObserver;
import observers.GameStateEventSubject;
import observers.PlayerEventSubject;
import scripts.GameManager;
import ui.components.GamePhase;

@InitializeSelfBefore(clazz = GameManager.class)
public class UI extends GameScript implements PlayerEventSubject, GameStateEventSubject {

    @InjectableScript
    GameManager gameManager;

    @InjectableScript
    PlayerUI playerUI;

    GamePhase gamePhaseUI = new GamePhase();

    @Override
    public void initialize() {
        gameManager.gameObserver.register((PlayerEventSubject)this);
        gameManager.gameObserver.register((GameStateEventSubject)this);

        getScene().getUiManager().getContainer().add(gamePhaseUI, gamePhaseUI.getConstraints());
    }

    @Override
    public void onPlayerEvent(GameObserver.PlayerEvent eventType, Player context) {
        switch (eventType) {
            case PLAYER_TURN -> {
                playerUI.setActivePlayer(context);
            }
            case PLAYER_ADDED -> {
                playerUI.trackPlayer(context);
            }
            case PLAYER_COLOR_CHANGED -> {
                playerUI.updatePlayerColor(context);
            }
            case PLAYER_REMOVED -> {
                playerUI.untrackPlayer(context);
            }
        }
    }


    @Override
    public void onGamePhaseEvent(GameManager.GamePhases eventType) {
        gamePhaseUI.setCurrentStateName(eventType);
    }

    @Override
    public void destroy() {
    }
}
