package states;

import entities.Entity;
import entities.Player;
import scripts.GameManager;

public interface GameState {
    void onClick(GameManager context, Entity clicked, Player player);
    void onSpace(GameManager context);
}
