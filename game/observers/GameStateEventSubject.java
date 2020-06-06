package observers;

import scripts.GameManager;

public interface GameStateEventSubject {
    void onGamePhaseEvent(GameManager.GamePhases eventType);
}
