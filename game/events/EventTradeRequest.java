package events;

import gameplay.TradeContext;
import network.events.NetworkEvent;
import utils.SerializedHashmap;

import java.nio.ByteBuffer;

public class EventTradeRequest extends NetworkEvent {

    private TradeContext tradeContext;

    public EventTradeRequest(TradeContext tradeContext) {
        super(EventType.TRADE_REQUEST);
        this.tradeContext = tradeContext;
    }

    public EventTradeRequest() {
        super(EventType.TRADE_REQUEST);
    }

    public EventTradeRequest(byte[] data) {
        super(EventType.TRADE_REQUEST);
        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
    }

    public byte[] serialize() {
        ByteBuffer data = ByteBuffer.allocate(HEADER_SIZE + ((tradeContext.getOffer().size() * 2) + (tradeContext.getRequest().size() * 2) + 3) * Integer.BYTES);
        writeHeader(data);

        data.putInt(tradeContext.getOffer().size());
        
        SerializedHashmap.serialize(data, tradeContext.getOffer());

        data.putInt(tradeContext.getRequest().size());

        SerializedHashmap.serialize(data, tradeContext.getRequest());

        data.putInt(tradeContext.getTradeOwner().getID());
        return data.array();
    }

    public TradeContext getTradeContext() {
        return tradeContext;
    }

    public void setTradeContext(TradeContext tradeContext) {
        this.tradeContext = tradeContext;
    }
}
