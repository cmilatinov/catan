package network.packets;

import network.RSA;

import java.nio.ByteBuffer;
import java.security.PublicKey;
import java.util.Arrays;

public class PacketKey extends Packet {
	
	/**
	 * Whether or not the receiver should respond with their RSA key.
	 */
	private boolean requestKey;
	
	/**
	 * Optional public RSA key of the server.
	 */
	private PublicKey key;
	
	/**
	 * Creates a new invalid packet with no key and requeting a remote key.
	 */
	public PacketKey() {
		super(PacketType.KEY);
		this.requestKey = true;
		this.key = null;
	}
	
	/**
	 * Creates a new invalid packet carrying the specified key.
	 * @param requestRemoteKey Whether or not the receiver should respond with their own key.
	 * @param key The key to store in this packet.
	 */
	public PacketKey(boolean requestRemoteKey, PublicKey key) {
		super(PacketType.KEY);
		this.requestKey = requestRemoteKey;
		this.key = key;
	}
	
	/**
	 * Parses an invalid packet from the given packet data.
	 * @param data The packet data to parse.
	 */
	public PacketKey(byte[] data) {
		super(PacketType.KEY);
		this.requestKey = data[0] > 0;
		this.key = RSA.toPublicKey(Arrays.copyOfRange(data, 1, data.length));
	}
	
	/**
	 * {@inheritDoc}
	 */
	public byte[] serialize() {
		if(key != null) {
			byte[] bKey = key.getEncoded();
			ByteBuffer data = ByteBuffer.allocate(HEADER_SIZE + 1 + bKey.length);
			writeHeader(data);
			data.put(requestKey ? (byte) 1 : (byte) 0)
				.put(bKey);
			return data.array();
		} else {
			ByteBuffer data = ByteBuffer.allocate(HEADER_SIZE + 1);
			writeHeader(data);
			data.put(requestKey ? (byte) 1 : (byte) 0);
			return data.array();
		}
	}
	
	/**
	 * Returns the stored public RSA key. 
	 * @return [{@link PublicKey}] The public RSA key stored in this packet.
	 */
	public PublicKey getKey() {
		return key;
	}
	
	/**
	 * Sets this packet's public RSA key.
	 * @param key The new RSA public key this packet is to hold.
	 * @return [{@link PacketConnect}] This same {@link PacketConnect} instance to allow for method chaining.
	 */
	public PacketKey setKey(PublicKey key) {
		this.key = key;
		return this;
	}
	
	/**
	 * Returns whether or not the receiver should respond with their own key.
	 * @return [<b>boolean</b>] True if the receiver is to send back their key, false otherwise.
	 */
	public boolean getRequestRemoteKey() {
		return requestKey;
	}
	
	/**
	 * Sets this packet's boolean requesting a remote key.
	 * @param key The new value of the boolean.
	 * @return [{@link PacketConnect}] This same {@link PacketConnect} instance to allow for method chaining.
	 */
	public PacketKey setRequestRemoteKey(boolean requestRemoteKey) {
		this.requestKey = requestRemoteKey;
		return this;
	}
	
}
