package ui;

import entities.Player;
import gameplay.ResourceType;
import objects.GameScript;
import objects.InitializeSelfBefore;
import objects.InjectableScript;
import observers.GameObserver;
import observers.PlayerEventSubject;
import observers.PlayerHandEventSubject;
import scripts.GameManager;
import ui.components.GamePhase;

@InitializeSelfBefore(clazz = GameManager.class)
public class UI extends GameScript implements PlayerEventSubject, PlayerHandEventSubject {

    @InjectableScript
    GameManager gameManager;

    @InjectableScript
    PlayerUI playerUI;

    @InjectableScript
    PlayerHandUI playerHandUI;

    GamePhase gamePhaseUI = new GamePhase();

    @Override
    public void initialize() {
        gameManager.gameObserver.register((PlayerEventSubject)this);
        gameManager.gameObserver.register((PlayerHandEventSubject)this);

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
    public void destroy() {
    }

    @Override
    public void onPlayerHandEvent(GameObserver.PlayerHandEvent eventType, ResourceType type, int count) {
        switch(eventType) {
            case RESOURCES_ADDED -> {
                for(int i = 0; i < count; i ++)
                    playerHandUI.addCard(type);
            }
            case RESOURCES_REMOVED -> {

            }
        }
    }
}
