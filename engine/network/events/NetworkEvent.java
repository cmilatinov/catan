package network.events;

import java.nio.ByteBuffer;

public abstract class NetworkEvent {

    /*
     * Four byte integer used to indicate the type of packet.
     */
    private final int type;

    /**
     * Creates a new event of given type.
     * @param type The event type.
     */
    public NetworkEvent(int type) {
        this.type = type;
    }

    /**
     * Writes the event header in the supplied byte buffer.
     */
    protected void writeHeader(ByteBuffer buffer) {
        buffer.putInt(type);
    }

    /**
     * Encodes the event in a byte array, to be sent over the network.
     */
    public byte[] serialize() {
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.putInt(type);
        return buffer.array();
    }

    /**
     * Returns this event's type as an integer.
     * @return [<b>int</b>] This event's type integer-encoded value.
     */
    public int getType() {
        return type;
    }

}