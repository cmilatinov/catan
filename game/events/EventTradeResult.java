package events;

import network.events.NetworkEvent;

import java.nio.ByteBuffer;

public class EventTradeResult extends NetworkEvent {

    private boolean tradeResult;

    public EventTradeResult() {
        super(EventType.TRADE_RESULT);
        tradeResult = false;
    }

    public EventTradeResult(byte[] data) {
        super(EventType.TRADE_RESULT);
        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        tradeResult = byteBuffer.get() > 0;
    }

    public byte[] serialize() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(HEADER_SIZE + Byte.BYTES);
        writeHeader(byteBuffer);
        byteBuffer.put(tradeResult ? (byte)1 : (byte)0);
        return byteBuffer.array();
    }

    public boolean isTradeResult() {
        return tradeResult;
    }

    public void setTradeResult(boolean tradeResult) {
        this.tradeResult = tradeResult;
    }
}
