package network.packets;

import network.Packet;

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

}
