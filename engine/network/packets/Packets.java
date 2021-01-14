package network.packets;

import java.util.HashMap;
import java.util.Map;

public class Packets {

	/**
	 * Indicates the number of registered packet types.
	 */
	private static int numPacketTypes = 0;

	/**
	 * Hash map translating packet IDs to class types.
	 */
	private static final Map<Integer, Class<? extends Packet>> PACKET_CLASS_FROM_ID = new HashMap<>();

	/**
	 * Hash map translating packet classes to IDs.
	 */
	private static final Map<Class<? extends Packet>, Integer> PACKET_ID_FROM_CLASS = new HashMap<>();

	static {{
		register(PacketKey.class);
		register(PacketConnect.class);
		register(PacketDisconnect.class);
		register(PacketAcceptConnection.class);
		register(PacketRejectConnection.class);
		register(PacketPing.class);
		register(PacketEvent.class);
		register(PacketEventConfirmation.class);
	}}

	/**
	 * Dynamically maps packet types to packet classes allowing for custom packet implementations.
	 *
	 * @param packet The corresponding packet class used to instantiate packets of that type.
	 */
	public static void register(Class<? extends Packet> packet) {
		PACKET_CLASS_FROM_ID.put(numPacketTypes, packet);
		PACKET_ID_FROM_CLASS.put(packet, numPacketTypes++);
	}

	/**
	 * Returns an integer acting as a unique identifier for a specific {@link Packet} subclass.
	 *
	 * @param packet The packet class for which to retrieve an ID.
	 * @return <b>int</b> An integer acting as a unique identifier for a specific {@link Packet} subclass. A negative integer indicates that the specified packet class does not exist or has not been registered.
	 */
	public static int getPacketID(Class<? extends Packet> packet) {
		Integer packetID = PACKET_ID_FROM_CLASS.get(packet);
		return packetID != null ? packetID : -1;
	}

	/**
	 * Returns the {@link Class} instance to which an packet ID corresponds.
	 *
	 * @param packetID The packet ID for which to retrieve the corresponding class.
	 * @return {@link Class} The class to which the packet ID corresponds or null if no such packet ID exists.
	 */
	public static Class<? extends Packet> getPacketClass(int packetID) {
		return PACKET_CLASS_FROM_ID.get(packetID);
	}

	/**
	 * Casts an packet to its specific subtype.
	 *
	 * @param <P>   The packet subtype class to cast to.
	 * @param packet The packet object to cast.
	 * @return <b>P</b> The casted packet object or null if packet is not of class P.
	 */
	@SuppressWarnings("unchecked")
	public static <P extends Packet> P cast(Packet packet) {
		try {
			return (P) packet;
		} catch (ClassCastException e) {
			return null;
		}
	}
	
}
