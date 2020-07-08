package events;

import network.events.NetworkEvent;

import java.nio.ByteBuffer;

public class EventRollResult extends NetworkEvent {
    private int die1, die2;

    public EventRollResult() {
        super(EventType.ROLL_RESULT);
        die1 = 4;
        die2 = 5;
    }

    public EventRollResult(int die1, int die2) {
        super(EventType.ROLL_RESULT);
        this.die1 = die1;
        this.die2 = die2;
    }

    public EventRollResult(byte[] data) {
        super(EventType.ROLL_RESULT);
        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        die1 = byteBuffer.getInt();
        die2 = byteBuffer.getInt();
    }

    public byte[] serialize() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(HEADER_SIZE + 2 * Integer.BYTES);
        writeHeader(byteBuffer);
        byteBuffer.putInt(die1).putInt(die2);
        return byteBuffer.array();
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
