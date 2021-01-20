package network.packets;

import network.annotations.SerializableField;

/**
 * This packet is used to ping the remote peer and calculate latency.
 */
@SuppressWarnings("unused")
public class PacketPing extends Packet {

    /**
     * This packet's timestamp.
     */
    @SerializableField
    private long timestamp;

    /**
     * Indicates whether this ping should be returned to the sender or not.
     */
    @SerializableField
    private boolean returnPing;

    /**
     * Creates a new invalid packet with the current timestamp.
     */
    public PacketPing() {
        this.timestamp = System.currentTimeMillis();
        this.returnPing = true;
    }

    /**
     * Creates a new invalid packet with the specified timestamp and ping return values.
     *
     * @param timestamp  The timestamp to be store in the packet.
     * @param returnPing Whether or not this ping should be returned to the sender.
     */
    public PacketPing(long timestamp, boolean returnPing) {
        this.timestamp = timestamp;
        this.returnPing = returnPing;
    }

    /**
     * Returns whether or not this packet should be returned to the sender.
     *
     * @return True if this packet should be returned to the sender, false otherwise.
     */
    public boolean getShouldReturnPing() {
        return returnPing;
    }

    /**
     * Sets whether or not this ping packet should be returned to the sender.
     *
     * @param returnPing Whether or not to return this ping packet.
     * @return {@link PacketPing} This same {@link PacketPing} instance to allow for method chaining.
     */
    public PacketPing setShouldReturnPing(boolean returnPing) {
        this.returnPing = returnPing;
        return this;
    }

    /**
     * Returns this packet's stored timestamp.
     *
     * @return <b>long</b> The timestamp given to this packet by its sender. Note that timestamps should only be compared if they originate from the same machine.
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Sets this packet's stored timestamp.
     *
     * @param timestamp The new timestamp this packet is to hold.
     */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

}
