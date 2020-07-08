package events;

import network.events.NetworkEvent;

import java.nio.ByteBuffer;

public class EventTradeResult extends NetworkEvent {
    private boolean tradeResult;

    public EventTradeResult(int type) {
        super(type);
        tradeResult = false;
    }

    public EventTradeResult(byte[] data) {
        super(EventType.ROLL_RESULT);
        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        tradeResult = byteBuffer.getInt() != 0;
    }

    public byte[] serialize() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(Long.BYTES * Integer.BYTES);
        writeHeader(byteBuffer);
        byteBuffer.putInt(tradeResult ? 1 : 0);
        return byteBuffer.array();
    }

    public boolean isTradeResult() {
        return tradeResult;
    }

    public void setTradeResult(boolean tradeResult) {
        this.tradeResult = tradeResult;
    }
}
