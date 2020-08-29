package events;

import network.events.NetworkEvent;

public class EventRollRequest extends NetworkEvent {

    public EventRollRequest() {
        super(EventType.ROLL_REQUEST);
    }

    public EventRollRequest(byte[] data) {
        super(EventType.ROLL_REQUEST);
    }

}
