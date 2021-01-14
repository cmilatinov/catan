package network.packets;

import network.annotations.SerializableField;

/**
 * This packet is sent to confirm the receipt of a previously sent {@link network.events.NetworkEvent} carried by a {@link PacketEvent}.
 */
@SuppressWarnings("unused")
public class PacketEventConfirmation extends Packet {

    /**
     * The event's ID.
     */
    @SerializableField
    private long eventID;

    /**
     * Creates a new event confirmation packet with event ID of 0.
     */
    public PacketEventConfirmation() {
        this.eventID = 0;
    }

    /**
     * Creates a new event confirmation packet with the specified event ID.
     */
    public PacketEventConfirmation(long eventID) {
        this.eventID = eventID;
    }

    /**
     * Returns the stored event ID.
     * @return <b>long</b> The event ID stored in this packet.
     */
    public long getEventID() {
        return eventID;
    }

    /**
     * Sets the event ID stored in this packet.
     * @param eventID The new event ID.
     * @return {@link PacketEvent} This same {@link PacketEventConfirmation} instance to allow for method chaining.
     */
    public PacketEventConfirmation setEventID(long eventID) {
        this.eventID = eventID;
        return this;
    }

}
