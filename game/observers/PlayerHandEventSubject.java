package observers;

import gameplay.ResourceType;

public interface PlayerHandEventSubject {
    void onPlayerHandEvent(GameObserver.PlayerHandEvent eventType, ResourceType type, int count);
}
