package observers;

import entities.Player;

interface PlayerEventSubject {
    void onPlayerEvent(GameObserver.PlayerEvent eventType, Player context);
}

interface DiceEventSubject {
    void onDiceEvent(GameObserver.DiceEvents eventType, int context);
}
