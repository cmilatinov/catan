package ui;

import entities.Player;
import objects.GameScript;
import objects.InitializeSelfBefore;
import objects.InjectableScript;
import observers.GameObserver;
import observers.GameStateEventSubject;
import observers.PlayerEventSubject;
import observers.PlayerHandEventSubject;
import scripts.GameManager;
import ui.components.GamePhase;
import ui.constraints.PixelConstraint;
import ui.constraints.RelativeConstraint;

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
    UIButton tButton = new UIButton("Start Trade");

    @Override
    public void initialize() {
        gameManager.gameObserver.register((PlayerEventSubject)this);
        gameManager.gameObserver.register((PlayerHandEventSubject)this);
        gameManager.gameObserver.register((GameStateEventSubject)this);

        UIComponent container = getScene().getUiManager().getContainer();

        container.add(gamePhaseUI, gamePhaseUI.getConstraints());

        UIConstraints tButtonConstraints = new UIConstraints()
                .setX(new PixelConstraint(30, UIDimensions.DIRECTION_LEFT))
                .setY(new PixelConstraint(30, UIDimensions.DIRECTION_BOTTOM))
                .setWidth(new RelativeConstraint(0.1f))
                .setHeight(new RelativeConstraint(0.1f));
        container.add(tButton, tButtonConstraints);

        tButton.setOnClick(this::toggleTradeMenu);
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
    public void onPlayerHandEvent(GameObserver.PlayerHandEvent eventType, int type, int count) {
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
