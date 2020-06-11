package observers;

public interface GameStateEventSubject {
    void onGamePhaseEvent(GameObserver.GameStates eventType);
}
