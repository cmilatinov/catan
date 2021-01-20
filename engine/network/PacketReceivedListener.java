package network;

import network.packets.Packet;

import java.net.InetSocketAddress;

/**
 * Listener for any received packets.
 */
public interface PacketReceivedListener {
	
	/**
	 * Handles a received packet.
	 * @param source The sender's remote IP address and port.
	 * @param packet The packet sent.
	 */
	void invoke(InetSocketAddress source, Packet packet);

}
