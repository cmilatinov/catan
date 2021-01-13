package network.packets;

import utils.StringUtils;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class PacketRejectConnection extends Packet {

	private static final Charset ENCODING = StandardCharsets.UTF_8;

	/**
	 * The reason for the connection rejection.
	 */
	private String reason;
	
	/**
	 * Creates a new connection rejecting packet.
	 */
	public PacketRejectConnection() {
		super(PacketType.REJECT_CONNECTION);
		this.reason = "Connection refused by host.";
	}
	
	/**
	 * Creates a new connection rejecting packet with the specified reason.
	 * @param reason The reason given for the connection rejection.
	 */
	public PacketRejectConnection(String reason) {
		super(PacketType.REJECT_CONNECTION);
		this.reason = reason;
	}
	
	/**
	 * Parses a connection rejecting packet from the given packet data.
	 * @param data The packet data to parse.
	 */
	public PacketRejectConnection(byte[] data) {
		super(PacketType.REJECT_CONNECTION);
		this.reason = StringUtils.createFromBytes(data, ENCODING);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public byte[] serialize() {
		byte[] bReason = StringUtils.getBytes(reason, ENCODING);
		ByteBuffer data = ByteBuffer.allocate(Packet.HEADER_SIZE + bReason.length);
		writeHeader(data);
		data.put(bReason);
		return data.array();
	}
	
	/**
	 * Returns the reason for the connection rejection.
	 * @return {@link String} The reason for the connection rejection.
	 */
	public String getReason() {
		return reason;
	}
	
	/**
	 * Sets the reason for the connection rejection.
	 * @param reason The new reason for rejection.
	 * @return {@link PacketRejectConnection} This same {@link PacketRejectConnection} instance to allow for method chaining.
	 */
	public PacketRejectConnection setReason(String reason) {
		this.reason = reason;
		return this;
	}
	
}
