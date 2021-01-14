package network.packets;

import network.annotations.SerializableField;

/**
 * This packet is sent from the server to a client to accept an incoming connection.
 */
public class PacketAcceptConnection extends Packet {

    /**
     * The client ID to be assigned to the client receiving this packet.
     */
    @SerializableField
    private int id;

    /**
     * Creates a new connection accepting packet.
     */
    public PacketAcceptConnection() {
        this.id = -1;
    }

    /**
     * Creates a new connection accepting packet.
     */
    public PacketAcceptConnection(int id) {
        this.id = id;
    }

    /**
     * Returns the ID stored in this packet.
     *
     * @return <b>int</b> The ID stored in this packet.
     */
    public int getID() {
        return id;
    }

    /**
     * Sets the ID stored in this packet.
     *
     * @param id The new ID to store.
     * @return {@link PacketAcceptConnection} This same {@link PacketAcceptConnection} instance to allow for method chaining.
     */
    public PacketAcceptConnection setID(int id) {
        this.id = id;
        return this;
    }

}
