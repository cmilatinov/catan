package turnbased;


import entities.Entity;
import entities.Player;

public interface GameStrategy {
    public boolean isTurnDone();
    public void onClick(Entity clicked, Player currentPlayer);
}
