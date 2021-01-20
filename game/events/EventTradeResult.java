package events;

import network.annotations.SerializableField;
import network.events.NetworkEvent;

public class EventTradeResult extends NetworkEvent {

    @SerializableField
    private boolean tradeResult;

    public EventTradeResult() {
        tradeResult = false;
    }

    public boolean isTradeResult() {
        return tradeResult;
    }

    public void setTradeResult(boolean tradeResult) {
        this.tradeResult = tradeResult;
    }
}
