package network.packets;

import network.annotations.SerializableField;

/**
 * This packet is sent from the server to a client to reject an incoming connection.
 */
@SuppressWarnings("unused")
public class PacketRejectConnection extends Packet {

	/**
	 * The reason for the connection rejection.
	 */
	@SerializableField
	private String reason;
	
	/**
	 * Creates a new connection rejecting packet.
	 */
	public PacketRejectConnection() {
		this.reason = "Connection refused by host.";
	}
	
	/**
	 * Creates a new connection rejecting packet with the specified reason.
	 * @param reason The reason given for the connection rejection.
	 */
	public PacketRejectConnection(String reason) {
		this.reason = reason;
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
