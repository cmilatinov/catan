package network.packets;

import network.Packet;

public class PacketAcceptConnection extends Packet {

	private int id;

	/**
	 * Creates a new connection accepting packet.
	 */
	public PacketAcceptConnection() {
		super(PacketType.ACCEPT_CONNECTION);
		this.id = -1;
	}

	/**
	 * Creates a new connection accepting packet.
	 */
	public PacketAcceptConnection(int id) {
		super(PacketType.ACCEPT_CONNECTION);
		this.id = id;
	}

	/**
	 * Parses a connection accepting packet from the given packet data.
	 * @param data The packet data to parse.
	 */
	public PacketAcceptConnection(byte[] data) {
		super(PacketType.ACCEPT_CONNECTION);
	}

	/**
	 * Returns the ID stored in this packet.
	 * @return [<b>int</b>] The ID stored in this packet.
	 */
	public int getID() {
		return id;
	}

	/**
	 * Sets the ID stored in this packet.
	 * @param id The new ID to store.
	 */
	public void setID(int id) {
		this.id = id;
	}

}
