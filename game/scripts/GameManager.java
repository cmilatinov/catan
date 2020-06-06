package scripts;

import entities.*;
import gameplay.strategies.Rolling;
import gameplay.strategies.SettingUp;
import gameplay.strategies.Settling;
import gameplay.strategies.Stealing;
import objects.GameScript;
import objects.InjectableScript;
import observers.GameObserver;
import resources.Resource;
import ui.*;

import java.util.ArrayList;

import static observers.GameObserver.*;
import static org.lwjgl.glfw.GLFW.*;

public class GameManager extends GameScript {
    // Entities registered in the scene that we need access to
    @InjectableScript
    private Tiles tiles;

    public UIQuad box;
    public UIText text;

    private final ArrayList<Player> players = new ArrayList<>();
    private final ArrayList<Vertex> verticesOccupied = new ArrayList<Vertex>();
    private final ArrayList<Side> sidesOccupied = new ArrayList<Side>();

    public final GameObserver gameObserver = new GameObserver();

    // Game strategies
    SettingUp settingUp;
    Settling settling;
    Stealing stealing;
    Rolling rolling;

    private int turn;
    private final int SETUP_TURNS = 2;

    // Game Phases
    public enum GamePhases {
        SETUP,
        ROLLING,
        SETTLING,
        STEALING
    }

    private GamePhases currentGamePhase;

    EntityToggleable collidingEntity = null;

    /**
     * Method to replicate the roll of a die
     * @return - Returns a random number between 1 and 6
     */
    public int roll() {
        int roll = (int)(Math.random() * 6) + 1;
        gameObserver.broadcast(DiceEvents.DICE_ROLLED, roll);
        return roll;
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

        switch(currentGamePhase) {
            case SETUP:
                settingUp.onClick(clicked, getCurrentPlayer());
                if(settingUp.isTurnDone()) {
                    turn ++;
                    if(turn == SETUP_TURNS * players.size())
                        setCurrentGamePhase(GamePhases.ROLLING);

                    gameObserver.broadcast(PlayerEvent.PLAYER_TURN, getCurrentPlayer());
                }
                break;
            case SETTLING:
                settling.onClick(clicked, getCurrentPlayer());
                break;
            case STEALING:
                stealing.onClick(clicked, getCurrentPlayer());
                if(stealing.isTurnDone())
                    setCurrentGamePhase(GamePhases.SETTLING);
                break;
        }
    }

    public void nextTurn(int mods) {
        if(currentGamePhase != GamePhases.SETTLING)
            return;

        setCurrentGamePhase(GamePhases.ROLLING);
        turn ++;
        gameObserver.broadcast(PlayerEvent.PLAYER_TURN, getCurrentPlayer());
    }

    public void onSpaceUp(int mods) {
        if(currentGamePhase != GamePhases.ROLLING)
            return;

        int roll1 = roll();
        int roll2 = roll();

        System.out.print("You rolled: ");
        System.out.println(roll1 + roll2);

        switch(roll1 + roll2) {
            case 7:
                setCurrentGamePhase(GamePhases.STEALING);
                break;
            default:
                for(Tile t : tiles.getTiles(roll1 + roll2))
                    for(Vertex v : t.getOccupiedVertices())
                        if(!t.isEmbargoed())
                            v.getOwner().addResourceCard(t.getType(), v.getBuildingValue());

                setCurrentGamePhase(GamePhases.SETTLING);
                break;
        }
    }

    @Override
    public void initialize() {
        turn = 0;
        setCurrentGamePhase(GamePhases.SETUP);

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

        settingUp = new SettingUp(verticesOccupied, sidesOccupied, getScene());
        settling = new Settling(verticesOccupied, sidesOccupied, getScene());
        rolling = new Rolling();
        stealing = new Stealing(tiles.getRobber());

        getScene().registerMouseClickAction(this::onClick);
        getScene().registerKeyUpAction(GLFW_KEY_SPACE, this::onSpaceUp);
        getScene().registerKeyUpAction(GLFW_KEY_T, this::nextTurn);

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

    public void setCurrentGamePhase(GamePhases currentGamePhase) {
        this.currentGamePhase = currentGamePhase;
        gameObserver.broadcast(currentGamePhase);
    }

    @Override
    public void destroy() {

    }
}
