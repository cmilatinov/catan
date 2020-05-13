package network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.security.PrivateKey;
import java.util.Arrays;

public class NetworkReceiver extends Thread {

	/**
	 * The length of a packet buffer in bytes.
	 */
	private static final int BUFFER_LENGTH = 1024;

	/**
	 * The socket timeout of the receiver, 0 being an infinite timeout.
	 */
	private static final int TIMEOUT = 0;

	/**
	 * The receiver's port.
	 */
	private int port;
	
	/**
	 * The byte buffer used to receive packets.
	 */
	private byte[] buffer = new byte[BUFFER_LENGTH];

	/**
	 * The private RSA key used for decryption.
	 */
	private PrivateKey key;

	/**
	 * The datagram socket used to listen for incoming packets.
	 */
	private DatagramSocket socket;

	/**
	 * The callback triggered on receiving a valid packet.
	 */
	private PacketReceivedListener callback = null;

	/**
	 * The operational state of this thread (running or not).
	 */
	private volatile boolean running = false;
	
	/**
	 * Intializes a new {@link NetworkReceiver} on a random port.
	 * @param key The RSA key to be used for decryption. 
	 */
	public NetworkReceiver(PrivateKey key) {
		
		// Store the decryption key
		this.key = key;
		
		try {
			
			// Create the socket and retrieve its port number
			this.socket = new DatagramSocket();
			this.port = socket.getLocalPort();
			
			// Set the socket timeout
			this.socket.setSoTimeout(TIMEOUT);
			
		} catch (SocketException e) {
			
			// Error while creating socket
			this.socket = null;
			e.printStackTrace();
			
		}
	}
	
	/**
	 * Intializes a new {@link NetworkReceiver} on the specified port.
	 * @param key The RSA key to be used for decryption. 
	 */
	public NetworkReceiver(int port, PrivateKey key) {
		
		// Store the decryption key and port number
		this.port = port;
		this.key = key;
		
		try {
			
			// Create the socket on the given port
			this.socket = new DatagramSocket(this.port);
			
			// Set the socket timeout
			this.socket.setSoTimeout(TIMEOUT);
			
		} catch (SocketException e) {
			
			// Error while creating socket
			this.socket = null;
			e.printStackTrace();
			
		}
	}
	
	/**
	 * Sets this {@link NetworkReceiver}'s callback triggered when a packet is received.
	 * @param callback The callback to use when a packet is received. 
	 * @return [{@link NetworkReceiver}] This same {@link NetworkReceiver} instance to allow for method chaining.
	 */
	public NetworkReceiver withCallback(PacketReceivedListener callback) {
		this.callback = callback;
		return this;
	}

	/**
	 * The entry point of the receiver's thread.
	 */
	public void run() {
		
		// Loop until halted
		while (running) {
			
			try { 
				
				// Receive a new packet
				DatagramPacket dp = new DatagramPacket(buffer, BUFFER_LENGTH);
				socket.receive(dp);
				
				// Copy the packet data
				byte[] data = Arrays.copyOfRange(dp.getData(), dp.getOffset(), dp.getOffset() + dp.getLength());
				
				// Attempt decryption
				if(key != null) {
					byte[] temp = RSA.decrypt(key, data);
					if(temp != null)
						data = temp;
				}
				
				// Attempt to create packet object
				Packet packet = Packets.createFromData(data);
				if(packet == null)
					continue;
				
				// Pass packet to callback
				if(callback != null)
					callback.invoke(new InetSocketAddress(dp.getAddress(), dp.getPort()), packet);
				
			} catch (IOException e) {}
			
		}

	}

	/**
	 * Starts the receiver on the specified port.
	 */
	public synchronized void start() {
		if (running)
			return;
		this.running = true;

		super.start();
	}

	/**
	 * Stops the receiver and kills its thread.
	 */
	public synchronized void halt() {
		this.running = false;
		try {
			this.join();
		} catch (InterruptedException e) {}
	}

}