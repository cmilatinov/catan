package network;

import events.EventType;
import network.events.EventTest;
import network.events.NetworkEvents;

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

		NetworkEvents.register(EventType.TEST, EventTest.class);
		
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
