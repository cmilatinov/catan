package network.packets;

import utils.StringUtils;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class PacketConnect extends Packet {
	
	private static final Charset ENCODING = StandardCharsets.UTF_8;
	
	/**
	 * The client's username.
	 */
	private String username;
	
	/**
	 * Creates a new connection request packet with an empty username.
	 */
	public PacketConnect() {
		super(PacketType.CONNECT);
		this.username = "username";
	}
	
	/**
	 * Creates a new connection request packet with the credentials.
	 * @param username The username used to authenticate the client.
	 */
	public PacketConnect(String username) {
		super(PacketType.CONNECT);
		this.username = username;
	}
	
	/**
	 * Parses a connection request packet from the given packet data.
	 * @param data The packet data to parse.
	 */
	public PacketConnect(byte[] data) {
		super(PacketType.CONNECT);
		username = StringUtils.createFromBytes(data, ENCODING);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public byte[] serialize() {
		byte[] bUsername = StringUtils.getBytes(username, ENCODING);
		ByteBuffer data = ByteBuffer.allocate(HEADER_SIZE + bUsername.length);
		writeHeader(data);
		data.put(bUsername);
		return data.array();
	}
	
	/**
	 * Returns the stored username. 
	 * @return [{@link String}] The stored username in string format.
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * Sets this packet's stored username.
	 * @param username The new username this packet is to hold.
	 * @return [{@link PacketConnect}] This same {@link PacketConnect} instance to allow for method chaining.
	 */
	public PacketConnect setUsername(String username) {
		this.username = username;
		return this;
	}
	
}
