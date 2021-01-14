package network.packets;

import network.annotations.SerializableField;

import java.security.PublicKey;

/**
 * This packet is sent to transmit a public RSA key to a remote peer.
 */
@SuppressWarnings("unused")
public class PacketKey extends Packet {

    /**
     * Whether or not the receiver should respond with their RSA key.
     */
    @SerializableField
    private boolean requestKey;

    /**
     * Optional public RSA key of the server.
     */
    @SerializableField
    private PublicKey key;

    /**
     * Creates a new invalid packet with no key and requeting a remote key.
     */
    public PacketKey() {
        this.requestKey = true;
        this.key = null;
    }

    /**
     * Creates a new invalid packet carrying the specified key.
     *
     * @param requestRemoteKey Whether or not the receiver should respond with their own key.
     * @param key              The key to store in this packet.
     */
    public PacketKey(boolean requestRemoteKey, PublicKey key) {
        this.requestKey = requestRemoteKey;
        this.key = key;
    }

    /**
     * Returns the stored public RSA key.
     *
     * @return {@link PublicKey} The public RSA key stored in this packet.
     */
    public PublicKey getKey() {
        return key;
    }

    /**
     * Sets this packet's public RSA key.
     *
     * @param key The new RSA public key this packet is to hold.
     * @return {@link PacketConnect} This same {@link PacketConnect} instance to allow for method chaining.
     */
    public PacketKey setKey(PublicKey key) {
        this.key = key;
        return this;
    }

    /**
     * Returns whether or not the receiver should respond with their own key.
     *
     * @return <b>boolean</b> True if the receiver is to send back their key, false otherwise.
     */
    public boolean getRequestRemoteKey() {
        return requestKey;
    }

    /**
     * Sets this packet's boolean requesting a remote key.
     *
     * @param requestRemoteKey The new value of the boolean.
     * @return {@link PacketConnect} This same {@link PacketConnect} instance to allow for method chaining.
     */
    public PacketKey setRequestRemoteKey(boolean requestRemoteKey) {
        this.requestKey = requestRemoteKey;
        return this;
    }

}
