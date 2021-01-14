package network.events;

public abstract class NetworkEvent {

    /*
     * Integer used to indicate the type of event.
     */
    private final int eventID;

    /**
     * Creates a new event of given type.
     */
    public NetworkEvent() {
        this.eventID = NetworkEvents.getEventID(this.getClass());
    }

    /**
     * Returns this event's type as an integer.
     * @return <b>int</b> This event's type integer-encoded value.
     */
    public int getEventID() {
        return eventID;
    }

}