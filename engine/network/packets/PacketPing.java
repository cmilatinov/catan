package network.packets;

import network.Packet;

import java.nio.ByteBuffer;

public class PacketPing extends Packet {
	
	/**
	 * This packet's timestamp.
	 */
	private long timestamp;
	
	/**
	 * Indicates whether this ping should be returned to the sender or not.
	 */
	private boolean returnPing;

	/**
	 * Creates a new invalid packet with the current timestamp.
	 */
	public PacketPing() {
		super(PacketType.PING);
		this.timestamp = System.currentTimeMillis();
		this.returnPing = true;
	}
	
	/**
	 * Creates a new invalid packet with the specified timestamp and ping return values.
	 * @param timestamp The timstamp to be store in the packet.
	 * @param returnPing Whether or not this ping should be returned to the sender.
	 */
	public PacketPing(long timestamp, boolean returnPing) {
		super(PacketType.PING);
		this.timestamp = timestamp;
		this.returnPing = returnPing;
	}
	
	/**
	 * Parses a ping packet from the given packet data.
	 * @param data
	 */
	public PacketPing(byte[] data) {
		super(PacketType.PING);
		ByteBuffer buffer = toPayload(data);
		this.timestamp = buffer.getLong();
		this.returnPing = buffer.get() > 0;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public byte[] serialize() {
		ByteBuffer data = ByteBuffer.allocate(HEADER_SIZE + 9);
		serializeHeader(data);
		data.putLong(timestamp)
			.put(returnPing ? (byte)1 : (byte)0);
		return data.array();
	}
	
	/**
	 * Returns whether or not this packet should be returned to the sender.
	 * @return True if this packet should be returned to the sender, false otherwise.
	 */
	public boolean getShouldReturnPing() {
		return returnPing;
	}
	
	/**
	 * Sets whether or not this ping packet should be returned to the sender.
	 * @param returnPing Whether or not to return this ping packet.
	 * @return [{@link PacketPing}] This same {@link PacketPing} instance to allow for method chaining.
	 */
	public PacketPing setShouldReturnPing(boolean returnPing) {
		this.returnPing = returnPing;
		return this;
	}
	
	/**
	 * Returns this packet's stored timestamp.
	 * @return [<b>long</b>] The timestamp given to this packet by its sender. Note that timestamps should only be compared if they originate from the same machine.
	 */
	public long getTimestamp() {
		return timestamp;
	}
	
	/**
	 * Sets this packet's stored timestamp.
	 * @param timestamp The new timestamp this packet is to hold.
	 */
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
}
