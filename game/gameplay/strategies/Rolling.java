package gameplay.strategies;

import entities.Entity;
import entities.Player;
import turnbased.GameStrategy;

public class Rolling implements GameStrategy {
    private boolean turnDone;

    public void resetTurn() {
        turnDone = false;
    }

    @Override
    public boolean isTurnDone() {
        boolean state = turnDone;
        resetTurn();
        return state;
    }

    @Override
    public void onClick(Entity clicked, Player currentPlayer) {

    }
}
