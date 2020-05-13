package network;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class Packets {
	
	/**
	 * Hashmap containing all packet types possible.
	 */
	private static final Map<Integer, Class<? extends Packet>> packets = new HashMap<Integer, Class<? extends Packet>>();
	
	/**
	 * Creates a new instance of the provided packet type.
	 * @param type The packet type.
	 * @return [{@link Object}] The newly created packet instance.
	 */
	public static final Packet createOfType(int type) {
		
		try {
			
			// Get the packet type
			Class<? extends Packet> packetType = packets.get(type);
			
			// If it does not exist return null reference
			if(packetType == null)
				return null;
			
			// Create a new instance of it
			return packetType.getConstructor().newInstance();
			
		} catch (Exception e) {
			
			// Return a null reference in case of exceptions
			return null;
			
		}
		
	}
	
	/**
	 * Creates a new instance of the provided packet type with the given serialized data.
	 * @param type The packet type.
	 * @return [{@link Object}] The newly created packet instance.
	 */
	public static final Packet createFromData(byte[] data) {
		
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
			
			// Create a new instance of it
			return packetType.getConstructor(byte[].class).newInstance(data);
			
		} catch (Exception e) {
			e.printStackTrace();
			// Return a null reference in case of exceptions
			return null;
			
		}
		
	}
	
	/**
	 * Dynamically maps packet types to packet classes allowing for custom packet implementations.
	 * @param type A unique integer representing the packet.
	 * @param packet The corresponding packet class used to instantiate packets of that type.
	 */
	public static final void register(int type, Class<? extends Packet> packet) {
		packets.put(type, packet);
	}
	
	/**
	 * Casts a packet to its specific subtype.
	 * @param <P> The packet subtype class to cast to.
	 * @param p The packet object to cast.
	 * @return [<b>P</b>] The casted packet object or null if p is not of class P.
	 */
	@SuppressWarnings("unchecked")
	public static final <P extends Packet> P cast(Packet p) {
		try {
			return (P) p;
		} catch (ClassCastException e) {
			return null;
		}
	}
	
}
