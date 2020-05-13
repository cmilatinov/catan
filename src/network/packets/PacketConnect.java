package network.packets;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import network.Packet;
import utils.StringUtils;

public class PacketConnect extends Packet {
	
	private static final Charset ENCODING = StandardCharsets.UTF_8;
	
	/**
	 * The client's Cloud-Code username.
	 */
	private String username;
	
	/**
	 * The client's Cloud-Code password.
	 */
	private String password;
	
	/**
	 * Creates a new connection request packet with an empty username and password set.
	 */
	public PacketConnect() {
		super(PacketType.CONNECT);
		this.username = "";
		this.password = "";
	}
	
	/**
	 * Creates a new connection request packet with the credentials.
	 * @param username The username used to authenticate the client.
	 * @param password The password used to authenticate the client.
	 */
	public PacketConnect(String username, String password) {
		super(PacketType.CONNECT);
		this.username = username;
		this.password = password;
	}
	
	/**
	 * Parses a connection request packet from the given packet data.
	 * @param data The packet data to parse.
	 */
	public PacketConnect(byte[] data) {
		super(PacketType.CONNECT);
		ByteBuffer buffer = toPayload(data);
		
		byte usernameSize = buffer.get();
		byte[] usernameBytes = new byte[usernameSize];
		buffer.get(usernameBytes);
		
		byte passwordSize = buffer.get();
		byte[] passwordBytes = new byte[passwordSize];
		buffer.get(passwordBytes);
		
		username = StringUtils.createFromBytes(usernameBytes, ENCODING);
		password = StringUtils.createFromBytes(passwordBytes, ENCODING);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public byte[] serialize() {
		byte[] bUsername = StringUtils.getBytes(username, ENCODING);
		byte[] bPassword = StringUtils.getBytes(password, ENCODING);
		ByteBuffer data = ByteBuffer.allocate(HEADER_SIZE + 2 + bUsername.length + bPassword.length);
		serializeHeader(data);
		data.put((byte) bUsername.length)
			.put(bUsername)
			.put((byte) bPassword.length)
			.put(bPassword);
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
	
	/**
	 * Returns the stored password. 
	 * @return [{@link String}] The stored password in string format.
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * Sets this packet's stored password.
	 * @param password The new password this packet is to hold.
	 * @return [{@link PacketConnect}] This same {@link PacketConnect} instance to allow for method chaining.
	 */
	public PacketConnect setPassword(String password) {
		this.password = password;
		return this;
	}
	
}
