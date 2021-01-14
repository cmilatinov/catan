package network.events;

import network.annotations.SerializableField;
import network.packets.PacketConnect;

public class EventTest extends NetworkEvent {

    /**
     * The test message.
     */
    @SerializableField
    private String msg;

    /**
     * Creates a new connection request packet with a generic test message.
     */
    public EventTest() {
        this.msg = "This is a test message";
    }

    /**
     * Creates a new test event with the given message.
     * @param msg The message to send to the remote peer.
     */
    public EventTest(String msg) {
        this.msg = msg;
    }

    /**
     * Returns the stored message.
     * @return {@link String} The stored message in string format.
     */
    public String getMessage() {
        return msg;
    }

    /**
     * Sets this packet's stored message.
     * @param msg The new message this packet is to hold.
     * @return {@link PacketConnect} This same {@link PacketConnect} instance to allow for method chaining.
     */
    public EventTest setMessage(String msg) {
        this.msg = msg;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return msg;
    }

}
