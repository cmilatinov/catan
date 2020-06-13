package network.packets;

import network.Packet;

import java.nio.ByteBuffer;

public class PacketEventConfirmation extends Packet {

    /**
     * The event's ID.
     */
    private long eventID;

    /**
     * Creates a new event confirmation packet with event ID of 0.
     */
    public PacketEventConfirmation() {
        super(PacketType.EVENT_CONFIRMATION);
        this.eventID = 0;
    }

    /**
     * Creates a new event confirmation packet with the specified event ID.
     */
    public PacketEventConfirmation(long eventID) {
        super(PacketType.EVENT_CONFIRMATION);
        this.eventID = eventID;
    }

    /**
     * Deserializes an event confirmation packet from a byte array.
     */
    public PacketEventConfirmation(byte[] data) {
        super(PacketType.EVENT_CONFIRMATION);
        ByteBuffer buffer = ByteBuffer.wrap(data);
        this.eventID = buffer.getLong();
    }

    /**
     * {@inheritDoc}
     */
    public byte[] serialize() {
        ByteBuffer buffer = ByteBuffer.allocate(HEADER_SIZE + Long.BYTES);
        writeHeader(buffer);
        buffer.putLong(eventID);
        return buffer.array();
    }

    /**
     * Returns the stored event ID.
     * @return [<b>long</b>] The event ID stored in this packet.
     */
    public long getEventID() {
        return eventID;
    }

    /**
     * Sets the event ID stored in this packet.
     * @param eventID The new event ID.
     * @return [{@link PacketEvent}] This same {@link PacketEventConfirmation} instance to allow for method chaining.
     */
    public PacketEventConfirmation setEventID(long eventID) {
        this.eventID = eventID;
        return this;
    }

}
