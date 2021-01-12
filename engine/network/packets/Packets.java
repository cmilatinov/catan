package network.packets;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Packets {
	
	/**
	 * Hashmap containing all possible packet types.
	 */
	private static final Map<Integer, Class<? extends Packet>> packets = new HashMap<>() {{
		put(PacketType.KEY, PacketKey.class);
		put(PacketType.CONNECT, PacketConnect.class);
		put(PacketType.DISCONNECT, PacketDisconnect.class);
		put(PacketType.ACCEPT_CONNECTION, PacketAcceptConnection.class);
		put(PacketType.REJECT_CONNECTION, PacketRejectConnection.class);
		put(PacketType.PING, PacketPing.class);
		put(PacketType.EVENT, PacketEvent.class);
		put(PacketType.EVENT_CONFIRMATION, PacketEventConfirmation.class);
	}};
	
	/**
	 * Creates a new packet instance with the given serialized data.
	 * @param data The serialized packet data.
	 * @return [{@link Packet}] The newly created packet instance.
	 */
	public static Packet createFromData(byte[] data) {
		
		try {
			
			// Parse packet header
			int type = -1;
			ByteBuffer bb = ByteBuffer.wrap(data);
			if(bb.getShort(0) == Packet.PACKET_BEGIN && data.length >= Packet.HEADER_SIZE)
				type = bb.getInt(2);
			
			// Get the packet type
			Class<? extends Packet> packetType = packets.get(type);
			
			// If it does not exist return null reference
			if(packetType == null)
				return null;
			
			// Create a new instance of it with the rest of the data
			return packetType.getConstructor(byte[].class)
					.newInstance(Arrays.copyOfRange(data, Packet.HEADER_SIZE, data.length));
			
		} catch (Exception e) {

			// Return a null reference in case of exceptions
			return null;
			
		}
		
	}
	
	/**
	 * Dynamically maps packet types to packet classes allowing for custom packet implementations.
	 * @param type A unique integer representing the packet.
	 * @param packet The corresponding packet class used to instantiate packets of that type.
	 */
	public static void register(int type, Class<? extends Packet> packet) {
		packets.put(type, packet);
	}
	
	/**
	 * Casts a packet to its specific subtype.
	 * @param <P> The packet subtype class to cast to.
	 * @param p The packet object to cast.
	 * @return [<b>P</b>] The casted packet object or null if p is not of class P.
	 */
	@SuppressWarnings("unchecked")
	public static <P extends Packet> P cast(Packet p) {
		try {
			return (P) p;
		} catch (ClassCastException e) {
			return null;
		}
	}
	
}
