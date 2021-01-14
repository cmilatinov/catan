package events;

import gameplay.TradeContext;
import network.annotations.SerializableField;
import network.events.NetworkEvent;

public class EventTradeRequest extends NetworkEvent {

    @SerializableField
    private TradeContext tradeContext;

    public EventTradeRequest(TradeContext tradeContext) {
        this.tradeContext = tradeContext;
    }

    public EventTradeRequest() {}

    public TradeContext getTradeContext() {
        return tradeContext;
    }

    public void setTradeContext(TradeContext tradeContext) {
        this.tradeContext = tradeContext;
    }
}
