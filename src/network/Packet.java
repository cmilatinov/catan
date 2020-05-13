package network;

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
	 * The size, in bytes, of a packet header (2 + 4 + 8).
	 */
	public static final int HEADER_SIZE = 6;
	
	/*
	 * Four byte integer used to indicate the type of packet.
	 */
	protected final int type;
	
	/**
	 * Encodes the packet header in a byte array.
	 */
	protected void serializeHeader(ByteBuffer out) {
		out.putShort(PACKET_BEGIN).putInt(type);
	}
	
	/**
	 * Creates a {@link ByteBuffer} and advances its pointer to the start of the payload (ie. after the header).
	 * @param data The data from which to construct the buffer.
	 * @return [{@link ByteBuffer}] A buffer starting at the packets actual contents (ie. not containing the header).
	 */
	protected ByteBuffer toPayload(byte[] data) {
		return ByteBuffer.wrap(data, HEADER_SIZE, data.length - HEADER_SIZE);
	}
	
	/**
	 * Encodes the packet in a byte array, to be sent over the network.
	 */
	abstract public byte[] serialize();
	
	/**
	 * Creates a new Packet of given type, and with given payload.
	 * @param type The packet type.
	 * @param payload The packet payload.
	 */
	protected Packet(int type) {
		this.type = type;
	}
	
	/**
	 * Returns this packet's type as an integer.
	 * @return [<b>int</b>] This packet's type integer encoded value.
	 */
	public int getType() {
		return type;
	}
	
}
