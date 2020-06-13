package network.packets;

import network.Packet;
import network.events.NetworkEvent;
import network.events.NetworkEvents;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class PacketEvent extends Packet {

    /**
     * The event's ID.
     */
    private long eventID;

    /**
     * The event sent through the network.
     */
    private NetworkEvent event;

    /**
     * Creates a new event packet with no event and with ID of 0.
     */
    public PacketEvent() {
        super(PacketType.EVENT);
        this.eventID = 0;
        this.event = null;
    }

    /**
     * Creates a new event packet with the specified event and event ID.
     */
    public PacketEvent(long eventID, NetworkEvent event) {
        super(PacketType.EVENT);
        this.eventID = eventID;
        this.event = event;
    }

    /**
     * Deserializes an event packet from a byte array.
     * @param data The byte array to deserialize.
     */
    public PacketEvent(byte[] data) {
        super(PacketType.EVENT);
        ByteBuffer buffer = ByteBuffer.wrap(data);
        this.eventID = buffer.getLong();
        this.event = NetworkEvents.createFromData(Arrays.copyOfRange(data, Long.BYTES, data.length));
    }

    /**
     * {@inheritDoc}
     */
    public byte[] serialize() {
        if (event == null) {
            ByteBuffer buffer = ByteBuffer.allocate(HEADER_SIZE + Long.BYTES);
            writeHeader(buffer);
            buffer.putLong(eventID);
            return buffer.array();
        } else {
            byte[] serializedEvent = event.serialize();
            ByteBuffer buffer = ByteBuffer.allocate(HEADER_SIZE + Long.BYTES + serializedEvent.length);
            writeHeader(buffer);
            buffer.putLong(eventID)
                    .put(serializedEvent);
            return buffer.array();
        }
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
     * @return [{@link PacketEvent}] This same {@link PacketEvent} instance to allow for method chaining.
     */
    public PacketEvent setEventID(long eventID) {
        this.eventID = eventID;
        return this;
    }

    /**
     * Returns the stored event.
     * @return [{@link NetworkEvent}] The event stored in this packet.
     */
    public NetworkEvent getEvent() {
        return event;
    }

    /**
     * Sets the event stored in this packet.
     * @param event The new event.
     * @return [{@link PacketEvent}] This same {@link PacketEvent} instance to allow for method chaining.
     */
    public PacketEvent setEvent(NetworkEvent event) {
        this.event = event;
        return this;
    }

}
