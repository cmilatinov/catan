package network.packets;

import network.annotations.SerializableField;
import network.events.NetworkEvent;

/**
 * This packet is sent to trigger an event on the remote peer. It can be sent by both a client and a server and carries a {@link NetworkEvent}.
 */
@SuppressWarnings("unused")
public class PacketEvent extends Packet {

    /**
     * The event's ID.
     */
    @SerializableField
    private long eventID;

    /**
     * The event sent through the network.
     */
    @SerializableField
    private NetworkEvent event;

    /**
     * Creates a new event packet with no event and with ID of 0.
     */
    public PacketEvent() {
        this.eventID = 0;
        this.event = null;
    }

    /**
     * Creates a new event packet with the specified event and event ID.
     */
    public PacketEvent(long eventID, NetworkEvent event) {
        this.eventID = eventID;
        this.event = event;
    }

    /**
     * Returns the stored event ID.
     *
     * @return <b>long</b> The event ID stored in this packet.
     */
    public long getEventID() {
        return eventID;
    }

    /**
     * Sets the event ID stored in this packet.
     *
     * @param eventID The new event ID.
     * @return {@link PacketEvent} This same {@link PacketEvent} instance to allow for method chaining.
     */
    public PacketEvent setEventID(long eventID) {
        this.eventID = eventID;
        return this;
    }

    /**
     * Returns the stored event.
     *
     * @return {@link NetworkEvent} The event stored in this packet.
     */
    public NetworkEvent getEvent() {
        return event;
    }

    /**
     * Sets the event stored in this packet.
     *
     * @param event The new event.
     * @return {@link PacketEvent} This same {@link PacketEvent} instance to allow for method chaining.
     */
    public PacketEvent setEvent(NetworkEvent event) {
        this.event = event;
        return this;
    }

}
