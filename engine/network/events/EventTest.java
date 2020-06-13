package network.events;

public class EventTest extends NetworkEvent {

    /**
     * Creates a new test event.
     */
    public EventTest() {
        super(NetworkEventType.TEST);
    }

    /**
     * Deserializes a test event from a byte array.
     * @param data The byte array to use.
     */
    public EventTest(byte[] data) {
        super(NetworkEventType.TEST);

    }

    public String toString() {
        return "tHiS iS a TeSt!";
    }

}
