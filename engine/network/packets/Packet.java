package network.packets;

/**
 * Represents a packet sent over the network.
 */
public abstract class Packet {

	/*
	 * Integer used to indicate the type of packet.
	 */
	private final int packetID;

	/**
	 * Creates a new event of given type.
	 */
	public Packet() {
		this.packetID = Packets.getPacketID(this.getClass());
	}

	/**
	 * Returns this event's type as an integer.
	 * @return <b>int</b> This event's type integer-encoded value.
	 */
	public int getPacketID() {
		return packetID;
	}
	
}
