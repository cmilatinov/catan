package network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.security.PrivateKey;
import java.util.Arrays;

public class UDPReceiver extends Thread {

	/**
	 * The length of a packet buffer in bytes.
	 */
	private static final int BUFFER_LENGTH = 4096;

	/**
	 * The socket timeout of the receiver, 0 being an infinite timeout.
	 */
	private static final int TIMEOUT = 1000;

	/**
	 * The port to which the receiver is bound.
	 */
	private int port;
	
	/**
	 * The address to which the receiver is bound.
	 */
	private InetAddress address;
	
	/**
	 * The byte buffer used to receive packets.
	 */
	private final byte[] buffer = new byte[BUFFER_LENGTH];

	/**
	 * The private RSA key used for decryption.
	 */
	private final PrivateKey key;

	/**
	 * The datagram socket used to listen for incoming packets.
	 */
	private DatagramSocket socket;

	/**
	 * The callback triggered on receiving a valid packet.
	 */
	private PacketReceivedListener callback = null;
	
	/**
	 * Indicates whether this receiver is ready to be started.
	 */
	private boolean ready = false;
	
	/**
	 * The operational state of this thread (running or not).
	 */
	private volatile boolean running = false;
	
	/**
	 * Initializes a new {@link UDPReceiver} on a random port.
	 * @param key The RSA key to be used for decryption. 
	 */
	public UDPReceiver(PrivateKey key) {
		
		// Store the decryption key
		this.key = key;
		
		try {
			
			// Create the socket and retrieve its port number
			this.socket = new DatagramSocket();
			this.address = this.socket.getLocalAddress();
			this.port = socket.getLocalPort();
			
			// Set the socket timeout
			this.socket.setSoTimeout(TIMEOUT);
			
			// Set ready
			this.ready = true;
			
		} catch (SocketException e) {
			
			// Error while creating socket
			this.socket = null;
			e.printStackTrace();
			
		}
	}

	/**
	 * Initializes a new {@link UDPReceiver} on the specified port.
	 * @param address The IP address to bind the socket to.
	 * @param port The port to listen on.
	 * @param key The RSA key to be used for decryption. 
	 */
	public UDPReceiver(InetAddress address, int port, PrivateKey key) {
		
		// Store the decryption key, address, and port number
		this.address = address;
		this.port = port;
		this.key = key;
		
		try {
			
			// Create the socket on the given address and port
			this.socket = this.address != null ? new DatagramSocket(this.port, this.address) : new DatagramSocket(this.port);
			
			// Set the socket timeout
			this.socket.setSoTimeout(TIMEOUT);
			
			// Set ready
			this.ready = true;
			
		} catch (SocketException e) {
			
			// Error while creating socket
			this.socket = null;
			e.printStackTrace();
			
		}
	}
	
	/**
	 * Initializes a new {@link UDPReceiver} using the specified socket.
	 * @param socket The socket to listen with.
	 * @param key The RSA key to be used for decryption. 
	 */
	public UDPReceiver(DatagramSocket socket, PrivateKey key) {
		
		// Store the socket, decryption key, address, and port number
		this.socket = socket;
		this.address = socket.getLocalAddress();
		this.port = socket.getLocalPort();
		this.key = key;
		
		try {
			
			// Set the socket timeout
			this.socket.setSoTimeout(TIMEOUT);
			
			// Set ready if socket is bound
			if(this.socket.isBound() && !this.socket.isClosed())
				this.ready = true;
			
		} catch (SocketException e) {
			
			// Error while setting socket timeout
			this.socket = null;
			e.printStackTrace();
			
		}
	}
	
	/**
	 * Sets this {@link UDPReceiver}'s callback triggered when a packet is received.
	 * @param callback The callback to use when a packet is received. 
	 * @return [{@link UDPReceiver}] This same {@link UDPReceiver} instance to allow for method chaining.
	 */
	public UDPReceiver withCallback(PacketReceivedListener callback) {
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
				
			} catch (IOException ignored) {}
			
		}

	}

	/**
	 * Starts the receiver if it is operational.
	 */
	public synchronized void start() {
		if(running)
			return;
		
		if(!ready)
			return;
		
		this.running = true;

		super.start();
	}

	/**
	 * Stops the receiver and kills its thread.
	 */
	public synchronized void halt() {
		this.running = false;
	}
	
	/**
	 * Returns whether this receiver is ready to be started.
	 * @return [<d>boolean</b>] True if this receiver is ready to operate, false otherwise.
	 */
	public boolean isReady() {
		return ready;
	}

	/**
	 * Returns the bind port of this receiver.
	 * @return [<b>int</b>] The local port number to which this receiver is bound to.
	 */
	public int getPort() {
		return port;
	}
	
	/**
	 * Returns the bind address of this receiver.
	 * @return [{@link InetAddress}] The local IP address to which this receiver is bound to.
	 */
	public InetAddress getAddress() {
		return address;
	}
	
	/**
	 * Returns the {@link DatagramSocket} backing this receiver.
	 * @return [{@link DatagramSocket}] The {@link DatagramSocket} used to by this receiver receive packets.
	 */
	public DatagramSocket getSocket() {
		return socket;
	}
	
}