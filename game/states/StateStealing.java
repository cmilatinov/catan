package states;

import entities.Entity;
import entities.Player;
import entities.board.Tile;
import observers.GameObserver.GameStates;
import scripts.GameManager;

public class StateStealing implements GameState {

    @Override
    public void onClick(GameManager context, Entity clicked, Player player) {
        if(!(clicked instanceof Tile))
            return;

        Tile clickedTile = (Tile) clicked;

        if(!clickedTile.isBlocked()) {
            context.updateRobber(clickedTile.getPosition());
            clickedTile.setIsBlocked(true);
            context.setGameState(new StateSettling());
        }
    }

    @Override
    public void onSpace(GameManager context) {

    }

    @Override
    public GameStates getStateName() {
        return GameStates.STEALING;
    }
}
