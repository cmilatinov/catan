package network.packets;

import network.annotations.SerializableField;

/**
 * This packet is sent from the client to a server to request a connection.
 */
@SuppressWarnings("unused")
public class PacketConnect extends Packet {

    /**
     * The client's username.
     */
    @SerializableField
    private String username;

    /**
     * Creates a new connection request packet with an empty username.
     */
    public PacketConnect() {
        this.username = "username";
    }

    /**
     * Creates a new connection request packet with the credentials.
     *
     * @param username The username used to authenticate the client.
     */
    public PacketConnect(String username) {
        this.username = username;
    }

    /**
     * Returns the stored username.
     *
     * @return {@link String} The stored username in string format.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets this packet's stored username.
     *
     * @param username The new username this packet is to hold.
     * @return {@link PacketConnect} This same {@link PacketConnect} instance to allow for method chaining.
     */
    public PacketConnect setUsername(String username) {
        this.username = username;
        return this;
    }

}
