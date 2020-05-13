package network;

import network.packets.PacketAcceptConnection;
import network.packets.PacketConnect;
import network.packets.PacketKey;
import network.packets.PacketPing;
import network.packets.PacketRejectConnection;
import network.packets.PacketType;

public class NetworkModule {
	
	/**
	 * Whether or not the module has correctly been initialized and is ready for use.
	 */
	private static boolean READY = false;
	
	/**
	 * Initializes the network module, registering all different packet types.
	 */
	public static final void initialize() {
		if(READY)
			return;
		
		Packets.register(PacketType.KEY, PacketKey.class);
		Packets.register(PacketType.CONNECT, PacketConnect.class);
		Packets.register(PacketType.ACCEPT_CONNECTION, PacketAcceptConnection.class);
		Packets.register(PacketType.REJECT_CONNECTION, PacketRejectConnection.class);
		Packets.register(PacketType.PING, PacketPing.class);
		
		READY = true;
	}
	
	/**
	 * Returns whether or not the module has correctly been initialized and is ready for use.
	 * @return [<b>boolean</b>] Whether or not the module has correctly been initialized and is ready for use.
	 */
	public static final boolean isReady() {
		return READY;
	}
	
}
