package states;

import entities.Entity;

public interface GameState {
    void handle();
    void onClick(Entity clicked);
    void onSpace();
}
