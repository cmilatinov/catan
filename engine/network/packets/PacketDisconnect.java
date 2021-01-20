package network.packets;

import network.annotations.SerializableField;

/**
 * This packet is sent from the client to a server to terminate a connection.
 */
@SuppressWarnings("unused")
public class PacketDisconnect extends Packet {

    /**
     * The exit code passed by the client. Leave at zero for expected disconnect.
     */
    @SerializableField
    private int exitCode;

    /**
     * Creates a new disconnection packet.
     */
    public PacketDisconnect() {
        this.exitCode = 0;
    }

    /**
     * Creates a new disconnection packet with the given exit code.
     *
     * @param exitCode The exit code describing the reason for the disconnect.
     */
    public PacketDisconnect(int exitCode) {
        this.exitCode = exitCode;
    }

    /**
     * Returns the exit code store in this packet.
     *
     * @return <b>int</b> The exit code describing the reason for the disconnect.
     */
    public int getExitCode() {
        return exitCode;
    }

    /**
     * Returns the exit code store in this packet.
     *
     * @param exitCode The new exit code to store.
     * @return {@link PacketDisconnect} This same {@link PacketDisconnect} instance to allow for method chaining.
     */
    public PacketDisconnect setReason(int exitCode) {
        this.exitCode = exitCode;
        return this;
    }

}
