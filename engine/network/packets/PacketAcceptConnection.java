package network.packets;

import network.Packet;

import java.nio.ByteBuffer;

public class PacketAcceptConnection extends Packet {
	
	/**
	 * Creates a new connection accepting packet.
	 */
	public PacketAcceptConnection() {
		super(PacketType.ACCEPT_CONNECTION);
	}

	/**
	 * Parses a connection accepting packet from the given packet data.
	 * @param data The packet data to parse.
	 */
	public PacketAcceptConnection(byte[] data) {
		super(PacketType.ACCEPT_CONNECTION);
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
