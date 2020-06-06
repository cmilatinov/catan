package observers;

import entities.Player;

public interface PlayerEventSubject {
    void onPlayerEvent(GameObserver.PlayerEvent eventType, Player context);
}
