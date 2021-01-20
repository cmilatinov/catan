package events;

import network.annotations.SerializableField;
import network.events.NetworkEvent;

public class EventRollResult extends NetworkEvent {

    @SerializableField
    private int die1;

    @SerializableField
    private int die2;

    public EventRollResult() {
        die1 = 4;
        die2 = 5;
    }

    public EventRollResult(int die1, int die2) {
        this.die1 = die1;
        this.die2 = die2;
    }

    public int getDie1() {
        return die1;
    }

    public int getDie2() {
        return die2;
    }

    public void setDie1(int die1) {
        this.die1 = die1;
    }

    public void setDie2(int die2) {
        this.die2 = die2;
    }
}
