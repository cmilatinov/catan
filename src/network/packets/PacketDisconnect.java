package network.packets;

import java.nio.ByteBuffer;

import network.Packet;

public class PacketDisconnect extends Packet {
	
	
	/**
	 * Creates a new connection closing packet with a no key.
	 */
	public PacketDisconnect() {
		super(PacketType.DISCONNECT);
	}
	
	/**
	 * Parses a connection closing packet from the given packet data.
	 * @param data The packet data to parse.
	 */
	public PacketDisconnect(byte[] data) {
		super(PacketType.DISCONNECT);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public byte[] serialize() {
		ByteBuffer data = ByteBuffer.allocate(HEADER_SIZE);
		serializeHeader(data);
		return data.array();
	}

}
