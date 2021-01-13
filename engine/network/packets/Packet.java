package network.packets;

import java.nio.ByteBuffer;

/**
 * Represents a packet sent over the network.
 */
public abstract class Packet {
	
	/**
	 * First two leading bytes to indicate the beginning of a new packet.
	 */
	protected static final short PACKET_BEGIN = 0x0420;
	
	/**
	 * The size, in bytes, of a packet header (2 + 4).
	 */
	public static final int HEADER_SIZE = Short.BYTES + Integer.BYTES;
	
	/*
	 * Four byte integer used to indicate the type of packet.
	 */
	private final int type;

	/**
	 * Creates a new packet of given type.
	 * @param type The packet type.
	 */
	protected Packet(int type) {
		this.type = type;
	}
	
	/**
	 * Writes the packet header in the supplied byte buffer.
	 */
	protected void writeHeader(ByteBuffer out) {
		out.putShort(PACKET_BEGIN).putInt(type);
	}
	
	/**
	 * Encodes the packet in a byte array, to be sent over the network.
	 */
	public byte[] serialize() {
		ByteBuffer data = ByteBuffer.allocate(HEADER_SIZE);
		writeHeader(data);
		return data.array();
	}

	/**
	 * Returns this packet's type as an integer.
	 * @return <b>int</b> This packet's type integer-encoded value.
	 */
	public int getType() {
		return type;
	}
	
}
