package network.packets;

import java.nio.ByteBuffer;
import java.security.PublicKey;
import java.util.Arrays;

import network.Packet;
import utils.UserToken;

public class PacketAcceptConnection extends Packet {
	
	/*
	 * A token identifying the connected user, to be sent when performing edits.
	 */
	private UserToken token;
	
	/**
	 * Creates a new connection accepting packet.
	 */
	public PacketAcceptConnection() {
		super(PacketType.ACCEPT_CONNECTION);
	}
	
	/**
	 * Creates a new connection accepting packet with the specified RSA key.
	 * @param key The new RSA public key this packet is to hold.
	 */
	public PacketAcceptConnection(UserToken token) {
		super(PacketType.ACCEPT_CONNECTION);
		this.token = token;
	}
	
	/**
	 * Parses a connection accepting packet from the given packet data.
	 * @param data The packet data to parse.
	 */
	public PacketAcceptConnection(byte[] data) {
		super(PacketType.ACCEPT_CONNECTION);
		byte[] tokenBytes = Arrays.copyOfRange(data, HEADER_SIZE, data.length);
		this.token = tokenBytes.length > 0 ? new UserToken(tokenBytes) : null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public byte[] serialize() {
		if(token != null) {
			byte[] bToken = token.getBytes();
			ByteBuffer data = ByteBuffer.allocate(HEADER_SIZE + bToken.length);
			serializeHeader(data);
			data.put(bToken);
			return data.array();
		} else {
			ByteBuffer data = ByteBuffer.allocate(HEADER_SIZE);
			serializeHeader(data);
			return data.array();
		}
	}
	
	/**
	 * Returns the stored user token. 
	 * @return [{@link PublicKey}] The user token stored in this packet.
	 */
	public UserToken getToken() {
		return token;
	}
	
	/**
	 * Sets this packet's user token.
	 * @param key The new user token this packet is to hold.
	 * @return [{@link PacketConnect}] This same {@link PacketConnect} instance to allow for method chaining.
	 */
	public PacketAcceptConnection setToken(UserToken token) {
		this.token = token;
		return this;
	}
	
}
