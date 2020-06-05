package gameplay.strategies;

import entities.Entity;
import entities.Player;
import entities.Tile;
import org.joml.Vector3f;
import turnbased.GameStrategy;

public class Stealing implements GameStrategy {
    Entity robber;
    private boolean turnDone;

    public Stealing(Entity robber) {
        this.robber = robber;
    }

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
        if(!(clicked instanceof Tile))
            return;

        Tile clickedTile = (Tile) clicked;

        if(clickedTile.isEmbargoed())
            return;

        clickedTile.setEmbargoed(true);
        robber.setPosition(clicked.getPosition()).translate(new Vector3f(0, 0.1f, -0.5f));
        turnDone = true;
    }
}
