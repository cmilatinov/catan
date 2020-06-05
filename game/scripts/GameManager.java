package scripts;

import entities.*;
import gameplay.strategies.Rolling;
import gameplay.strategies.SettingUp;
import gameplay.strategies.Settling;
import gameplay.strategies.Stealing;
import objects.GameScript;
import objects.InjectableScript;
import resources.Resource;
import ui.*;
import ui.constraints.AspectConstraint;
import ui.constraints.CenterConstraint;
import ui.constraints.PixelConstraint;
import ui.constraints.RelativeConstraint;

import java.awt.*;
import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;

public class GameManager extends GameScript {
    // Entities registered in the scene that we need access to
    @InjectableScript
    private Tiles tiles;

    public UIQuad box;
    public UIText text;

    private ArrayList<Vertex> verticesOccupied;
    private ArrayList<Side> sidesOccupied;

    private ArrayList<Player> players = new ArrayList<Player>();

    // Game strategies
    SettingUp settingUp;
    Settling settling;
    Stealing stealing;
    Rolling rolling;

    private int turn;
    private final int SETUP_TURNS = 2;

    // Game Phases
    private enum GamePhases {
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
        return (int)(Math.random() * 6) + 1;
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
                        currentGamePhase = GamePhases.ROLLING;
                }
                break;
            case SETTLING:
                settling.onClick(clicked, getCurrentPlayer());
                break;
            case STEALING:
                stealing.onClick(clicked, getCurrentPlayer());
                if(stealing.isTurnDone())
                    currentGamePhase = GamePhases.SETTLING;
                break;
        }
    }

    public void nextTurn(int mods) {
        if(currentGamePhase != GamePhases.SETTLING)
            return;

        currentGamePhase = GamePhases.ROLLING;
        turn ++;
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
                currentGamePhase = GamePhases.STEALING;
                break;
            default:
                for(Tile t : tiles.getTiles(roll1 + roll2))
                    for(Vertex v : t.getOccupiedVertices())
                        if(!t.isEmbargoed())
                            v.getOwner().addResourceCard(t.getType(), v.getBuildingValue());

                currentGamePhase = GamePhases.SETTLING;
                break;
        }
    }

    @Override
    public void initialize() {
        turn = 0;
        currentGamePhase = GamePhases.SETUP;

        for(int i = 0; i < 4; i ++)
            players.add(new Player());
        players.get(0).setColor(Resource.TEXTURE_COLOR_BLUE);
        players.get(1).setColor(Resource.TEXTURE_COLOR_GREEN);
        players.get(2).setColor(Resource.TEXTURE_COLOR_PURPLE);
        players.get(3).setColor(Resource.TEXTURE_COLOR_RED);

        verticesOccupied = new ArrayList<Vertex>();
        sidesOccupied = new ArrayList<Side>();

        settingUp = new SettingUp(verticesOccupied, sidesOccupied, getScene());
        settling = new Settling(verticesOccupied, sidesOccupied, getScene());
        rolling = new Rolling();
        stealing = new Stealing(tiles.getRobber());

        getScene().registerMouseClickAction(this::onClick);
        getScene().registerKeyUpAction(GLFW_KEY_SPACE, this::onSpaceUp);
        getScene().registerKeyUpAction(GLFW_KEY_T, this::nextTurn);

        box = new UIQuad();
        box.setColor(UIColor.RED);
        box.setBorderRadius(20);
        UIConstraints constraints = new UIConstraints()
                .setX(new PixelConstraint(30, UIDimensions.DIRECTION_LEFT))
                .setY(new PixelConstraint(30, UIDimensions.DIRECTION_TOP))
                .setWidth(new RelativeConstraint(0.2f))
                .setHeight(new AspectConstraint(1));
        getScene().getUiManager().getContainer().add(box, constraints);

        text = new UIText(new Font("Comic Sans MS", Font.PLAIN, 18), getCurrentStateName());
        text.setColor(UIColor.BLACK);
        UIConstraints constraints2 = new UIConstraints()
                .setX(new CenterConstraint())
                .setY(new CenterConstraint())
                .setWidth(new RelativeConstraint(0.6f))
                .setHeight(new RelativeConstraint(0.6f));
        box.add(text, constraints2);
    }

    public String getCurrentStateName() {
        return switch(currentGamePhase){
            case SETTLING ->  "Settling";
            case STEALING ->  "Stealing";
            case SETUP -> "Setting up";
            case ROLLING -> "Rolling";
        };
    }

    public Player getCurrentPlayer() {
        return players.get(turn % 4);
    }

    @Override
    public void destroy() {

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

        text.setText(getCurrentStateName());
    }
}
