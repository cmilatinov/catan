package network;

import network.packets.Packet;

import java.net.InetSocketAddress;

/**
 * Listener for any received packets.
 */
public interface PacketReceivedListener {
	
	/**
	 * Handles a received packet.
	 * @param source The sender's remote IP address.
	 * @param port The sender's remote port number.
	 * @param packet The packet sent.
	 */
	public void invoke(InetSocketAddress source, Packet packet);

}
