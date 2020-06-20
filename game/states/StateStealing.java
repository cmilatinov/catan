package states;

import entities.Entity;
import entities.Player;
import entities.Tile;
import observers.GameObserver.GameStates;
import scripts.GameManager;

public class StateStealing implements GameState {

    @Override
    public void onClick(GameManager context, Entity clicked, Player player) {
        if(!(clicked instanceof Tile))
            return;

        Tile clickedTile = (Tile) clicked;

        if(!clickedTile.isEmbargoed()) {
            context.updateRobber(clickedTile.getPosition());
            clickedTile.setEmbargoed(true);
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
