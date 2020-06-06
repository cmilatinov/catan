package observers;

public interface DiceEventSubject {
    void onDiceEvent(GameObserver.DiceEvents eventType, int context);
}