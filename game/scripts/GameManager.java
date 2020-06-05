package scripts;

import objects.GameScript;

public class GameManager extends GameScript {
    private enum GamePhases {
        SETUP,
        ROLLING,
        SETTLING,
        STEALING
    }

    private GamePhases currentGamePhase;

    public GameManager() {
        currentGamePhase = GamePhases.SETUP;
    }

    public void onClick(int button, int action, int mods) {

    }

    @Override
    public void initialize() {

    }

    @Override
    public void destroy() {

    }
}
