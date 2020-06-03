package network;

import network.packets.*;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.security.KeyPair;
import java.security.PublicKey;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

public class GameClient extends Thread {
	
	/**
	 * The RSA key size in bits.
	 */
	private static final int KEY_SIZE = 512;
	
	/**
	 * The RSA key size in bits.
	 */
	private static final int SERVER_TIMEOUT = 10000;
	
	/**
	 * The RSA public key used to encrypt messages sent to the remote server.
	 */
	private PublicKey remoteKey = null;
	
	/**
	 * The RSA keypair used for encryption purposes.
	 */
	private final KeyPair keys;
	
	/**
	 * The receiver handling packet reception and decryption.
	 */
	private final UDPReceiver receiver;
	
	/**
	 * The sender handling packet dispatch and encryption.
	 */
	private final UDPSender sender;
	
	/**
	 * The server's remote IP address.
	 */
	private InetSocketAddress remoteAddress = null;
	
	/**
	 * This client's username credential.
	 */
	private String username = null;
	
	/**
	 * This client's password credential.
	 */
	private String password = null;
	
	/**
	 * The packet queue filled with incoming packets.
	 */
	private volatile LinkedBlockingDeque<Packet> packetQueue = new LinkedBlockingDeque<Packet>();
	
	/**
	 * The callback to execute once a connection is made.
	 */
	private Runnable onConnect = null;
	
	/**
	 * A countdown timer managing server timeout.
	 */
	private int timeout = SERVER_TIMEOUT;
	
	/**
	 * Indicates whether the client is ready to be started.
	 */
	private boolean ready = false;
	
	/**
	 * Operational state of the client (running or not).
	 */
	private volatile boolean running = false;
	
	/**
	 * Whether or not this client should attempt to connect when the remote RSA public key is received.
	 */
	private boolean connectOnReceiveKey = false;
	
	/**
	 * Indicates whether the client is currently connected to a remote server.
	 */
	private boolean connected = false;
	
	/**
	 * Initializes the client to an operational state.
	 */
	public GameClient() {
		keys = RSA.generateKeyPair(KEY_SIZE);
		receiver = new UDPReceiver(keys.getPrivate()).withCallback((InetSocketAddress source, Packet packet) -> {
			if(remoteAddress == null || remoteAddress.equals(source))
				packetQueue.add(packet);
		});
		sender = new UDPSender(receiver.getSocket());
		
		if(keys != null && receiver.isReady() && sender.isReady()) {
			receiver.start();
			sender.start();
			ready = true;
		}
	}
	
	/**
	 * Runs the client on the current execution thread. This method blocks until the client is stopped.
	 */
	public void run() {
		
		long lastPing = System.currentTimeMillis();
		long lastTime = System.currentTimeMillis();
		while(running) {
			
			// Send ping if connected
			long currentTime = System.currentTimeMillis();
			if(connected && currentTime - lastPing >= 1000) {
				sender.send(new PacketPing(), remoteAddress, remoteKey);
				lastPing = currentTime;
			}
			
			// Decrease timeout counter
			long delta = currentTime - lastTime;
			if(connected)
				timeout -= delta;
			
			// Attempt to process pending packets
			out:
			try {
				Packet p = packetQueue.poll(1, TimeUnit.SECONDS);
				if(p == null)
					break out;
				
				// Reset timeout counter
				timeout = SERVER_TIMEOUT;

				switch (p.getType()) {
					case PacketType.KEY -> {
						PacketKey pKey = Packets.cast(p);
						handlePacket(pKey);
					}
					case PacketType.ACCEPT_CONNECTION -> {
						PacketAcceptConnection pAcceptConn = Packets.cast(p);
						handlePacket(pAcceptConn);
					}
					case PacketType.REJECT_CONNECTION -> {
						PacketRejectConnection pRejectConn = Packets.cast(p);
						handlePacket(pRejectConn);
					}
					case PacketType.PING -> {
						PacketPing pPing = Packets.cast(p);
						handlePacket(pPing);
					}
				}
			} catch(Exception ignored) {}
			
			// Server has timed out
			if(timeout <= 0 && connected)
				timeOut();
			
			// Set last timestamp to current
			lastTime = currentTime;
		}
	}
	
	/**
	 * Starts the client if it is operational.
	 */
	public void start() {
		if(running)
			return;
		
		if(!ready)
			return;
		
		running = true;

		super.start();
	}

	/**
	 * Stops the client and kills its thread.
	 */
	public void halt() {
		running = false;
		try {
			join();
		} catch (InterruptedException e) {}
	}
	
	/**
	 * Attempts to connect the client to the remote address and port specified.
	 *
	 * @param address The remote IP address in string form (ie. "192.168.0.1") to connect to.
	 * @param port The remote port number to connect to.
	 * @param username The username to use.
	 * @return [{@link GameClient}] This same {@link GameClient} instance to allow for method chaining.
	 */
	public GameClient connect(String address, int port, String username) {
		if(!ready) return this;
		try {
			remoteAddress = new InetSocketAddress(InetAddress.getByName(address), port);
			this.username = username;
			this.password = password;
			if(remoteKey != null) {
				sender.send(new PacketConnect(username), remoteAddress, remoteKey);
			} else {
				connectOnReceiveKey = true;
				sender.send(new PacketKey(true, keys.getPublic()), remoteAddress);
			}
		} catch(Exception e) {
			remoteAddress = null;
			connected = false;
			e.printStackTrace();
		}
		return this;
	}
	
	/**
	 * Sets the callback to invoke when a connection to a remote server is made.
	 * @param callback The new callback to run once a connection is made.
	 * @return [{@link GameClient}] This same {@link GameClient} instance to allow for method chaining.
	 */
	public GameClient onConnect(Runnable callback) {
		this.onConnect = callback;
		return this;
	}
	
	/**
	 * Returns whether or not this client is currently connected to a remote server.
	 * @return [<b>boolean</b>] True if a connection exists between this client and a remote server, false otherwise.
	 */
	public boolean isConnected() {
		return connected;
	}
	
	/**
	 * Returns whether this client is ready to be started.
	 * @return [<d>boolean</b>] True if this client is ready to operate, false otherwise.
	 */
	public boolean isReady() {
		return ready;
	}
	
	private void handlePacket(PacketKey packet) {
		if(connectOnReceiveKey && packet.getKey() != null) {
			remoteKey = packet.getKey();
			connectOnReceiveKey = false;
			sender.send(new PacketConnect(username), remoteAddress, remoteKey);
		}
	}
	
	private void handlePacket(PacketAcceptConnection packet) {
		connected = true;
		if(this.onConnect != null)
			onConnect.run();
		System.out.println("Connected to server.");
	}
	
	private void handlePacket(PacketRejectConnection packet) {
		System.out.println("Failed to connect to server: " + packet.getReason());
	}
	
	private void handlePacket(PacketPing packet) {
		System.out.println("Ping: " + (System.currentTimeMillis() - packet.getTimestamp()));
	}
	
	private void timeOut() {
		connected = false;
		remoteAddress = null;
		System.out.println("Server timed out.");
	}
}
