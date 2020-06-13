package network;

import network.events.EventTest;
import network.events.NetworkEventType;
import network.events.NetworkEvents;
import network.packets.*;

public class NetworkModule {
	
	/**
	 * Whether or not the module has correctly been initialized and is ready for use.
	 */
	private static boolean READY = false;
	
	/**
	 * Initializes the network module, registering all different packet types.
	 */
	public static void initialize() {
		if(READY)
			return;
		
		Packets.register(PacketType.KEY, PacketKey.class);
		Packets.register(PacketType.CONNECT, PacketConnect.class);
		Packets.register(PacketType.DISCONNECT, PacketDisconnect.class);
		Packets.register(PacketType.ACCEPT_CONNECTION, PacketAcceptConnection.class);
		Packets.register(PacketType.REJECT_CONNECTION, PacketRejectConnection.class);
		Packets.register(PacketType.PING, PacketPing.class);
		Packets.register(PacketType.EVENT, PacketEvent.class);
		Packets.register(PacketType.EVENT_CONFIRMATION, PacketEventConfirmation.class);

		NetworkEvents.register(NetworkEventType.TEST, EventTest.class);
		
		READY = true;
	}
	
	/**
	 * Returns whether or not the module has correctly been initialized and is ready for use.
	 * @return [<b>boolean</b>] Whether or not the module has correctly been initialized and is ready for use.
	 */
	public static boolean isReady() {
		return READY;
	}
	
}
