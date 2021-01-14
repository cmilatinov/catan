package observers;

public interface PlayerHandEventSubject {
    void onPlayerHandEvent(GameObserver.PlayerHandEvent eventType, int type, int count);
}
