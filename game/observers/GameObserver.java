package observers;

import entities.Player;
import scripts.GameManager;

import java.util.ArrayList;

public class GameObserver {

    public enum DiceEvents {
        DICE_ROLLED
    }

    public enum PlayerEvent {
        PLAYER_REMOVED,
        PLAYER_ADDED,
        PLAYER_COLOR_CHANGED, PLAYER_TURN,
    }

    ArrayList<PlayerEventSubject> playerSubscribers = new ArrayList<>();
    ArrayList<DiceEventSubject> diceSubjects = new ArrayList<>();
    ArrayList<GameStateEventSubject> gameStateEventSubjects = new ArrayList<>();

    /**
     * Broadcast a player game event to any subscribers
     * @param event Event Type being broadcasted
     * @param context Data to be passed to subjects
     */
    public void broadcast(PlayerEvent event, Player context)
    {
        for (PlayerEventSubject subject: playerSubscribers) {
            subject.onPlayerEvent(event, context);
        }
    }

    /**
     * Broadcast a dice game event to any subscribers
     * @param event Event Type being broadcasted
     * @param context Data to be passed to subjects
     */
    public void broadcast(DiceEvents event, int context)
    {
        for (DiceEventSubject subject: diceSubjects) {
            subject.onDiceEvent(event, context);
        }
    }

    /**
     * Broadcast a game phase event to any subscribers
     * @param event Event Type being broadcasted
     */
    public void broadcast(GameManager.GamePhases event)
    {
        for (GameStateEventSubject subject: gameStateEventSubjects) {
            subject.onGamePhaseEvent(event);
        }
    }

    /**
     * Register a subject to recieve game events
     * @param subject subject to recieve events
     * @return index of stored subjects
     */
    public int register(PlayerEventSubject subject)
    {
        playerSubscribers.add(subject);
        return playerSubscribers.size() - 1;
    }

    /**
     * Register a subject to recieve game events
     * @param subject subject to recieve events
     * @return index of stored subjects
     */
    public int register(DiceEventSubject subject)
    {
        diceSubjects.add(subject);
        return diceSubjects.size() - 1;
    }

    /**
     * Register a subject to recieve game events
     * @param subject subject to recieve events
     * @return index of stored subjects
     */
    public int register(GameStateEventSubject subject)
    {
        gameStateEventSubjects.add(subject);
        return diceSubjects.size() - 1;
    }

    //TODO: remove subject from events if we really need it
}
