package ui;

import entities.Player;
import gameplay.ResourceType;
import objects.GameScript;
import objects.InitializeSelfBefore;
import objects.InjectableScript;
import observers.GameObserver;
import observers.GameStateEventSubject;
import observers.PlayerEventSubject;
import observers.PlayerHandEventSubject;
import scripts.GameManager;
import ui.components.GamePhase;
import ui.components.TradeButton;

@InitializeSelfBefore(clazz = GameManager.class)
public class UI extends GameScript implements PlayerEventSubject, PlayerHandEventSubject, GameStateEventSubject {

    @InjectableScript
    GameManager gameManager;

    @InjectableScript
    PlayerUI playerUI;

    @InjectableScript
    PlayerHandUI playerHandUI;

    @InjectableScript
    TradeMenuUI tradeMenuUI;

    GamePhase gamePhaseUI = new GamePhase();
    TradeButton tButton = new TradeButton();

    @Override
    public void initialize() {
        gameManager.gameObserver.register((PlayerEventSubject)this);
        gameManager.gameObserver.register((PlayerHandEventSubject)this);
        gameManager.gameObserver.register((GameStateEventSubject)this);

        getScene().getUiManager().getContainer().add(gamePhaseUI, gamePhaseUI.getConstraints());
        getScene().getUiManager().getContainer().add(tButton, tButton.getConstraints());

        tButton.setOnMouseClickEvent(this::toggleTradeMenu);
    }

    public void toggleTradeMenu() {
        tradeMenuUI.toggleTradingMenu();
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

    @Override
    public void onGamePhaseEvent(GameObserver.GameStates eventType) {
        gamePhaseUI.setCurrentStateName(eventType);
    }
}
