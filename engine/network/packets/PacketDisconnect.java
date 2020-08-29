package network.packets;

public class PacketDisconnect extends Packet {

	/**
	 * Creates a new connection closing packet.
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

}
