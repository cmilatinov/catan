package network;

import network.packets.*;
import utils.Pair;

import java.net.InetSocketAddress;
import java.security.KeyPair;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

public class GameServer extends Thread {
	
	/**
	 * Represents a remote client.
	 */
	private class RemoteClient {
		
		/**
		 * The remote IP address and port from which the client is responding.
		 */
		public final InetSocketAddress address;
		
		/**
		 * The handler thread for this client instance.
		 */
		public final ClientHandler handler;
		
		/**
		 * The public RSA key associated to this client used for encryption purposes.
		 */
		public PublicKey key;
		
		/**
		 * The amount of time after which this client will be considered disconnected.
		 */
		public volatile long timeout = CLIENT_TIMEOUT;
		
		/**
		 * Creates a new client instance with the specified IP address and port number.
		 * @param address The remote IP address and port from which the client is responding.
		 * @param key The client's public RSA key.
		 */
		public RemoteClient(InetSocketAddress address, PublicKey key) {
			this.address = address;
			this.key = key;
			this.handler = new ClientHandler(this);
		}
		
	}
	
	/**
	 * Serves a single client.
	 */
	private class ClientHandler extends Thread {
		
		/**
		 * The packet queue to serve for this client.
		 */
		private final LinkedBlockingDeque<Packet> packets = new LinkedBlockingDeque<Packet>();
		
		/**
		 * The client this handler is serving.
		 */
		private final RemoteClient client;
		
		/**
		 * The operational state of this handler (running or not).
		 */
		private volatile boolean running = false;
		
		/**
		 * Creates a new {@link ClientHandler} serving the specified client.
		 * @param client The remote client to serve.
		 */
		public ClientHandler(RemoteClient client) {
			this.client = client;
		}
		
		/**
		 * {@inheritDoc}}
		 */
		public void run() {
			long lastTime = System.currentTimeMillis();
			while(this.running) {
				
				// Handle timeouts
				long currentTime = System.currentTimeMillis();
				client.timeout -= currentTime - lastTime;
				if(client.timeout < 0)
					onTimeout(client);
				lastTime = currentTime;
				
				// Attempt to process pending packets
				try {
					
					// Get next packet
					Packet p = packets.poll(1, TimeUnit.SECONDS);
					if(p == null) continue;
					
					// Reset timeout
					client.timeout = CLIENT_TIMEOUT;
					
					// Handle packet
					switch(p.getType()) {
							
						case PacketType.PING:
							PacketPing pPing = Packets.cast(p);
							handlePacket(pPing);
							break;
					
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}
		
		/**
		 * Starts the client handler.
		 */
		public void start() {
			if(this.running)
				return;
			
			this.running = true;
			
			super.start();
		}
		
		/**
		 * Stops the client handler.
		 */
		public void halt() {
			this.running = false;
		}
		
		private void handlePacket(PacketPing packet) {
			if (packet.getShouldReturnPing()) {
				packet.setShouldReturnPing(false);
				sender.send(packet, client.address, client.key);
			}
		}
		
	}
	
	/**
	 * The RSA key size in bits.
	 */
	private static final int KEY_SIZE = 512;
	
	/**
	 * The amount of time, in milliseconds, after which a client not sending a response is considered disconnected.
	 */
	private static final int CLIENT_TIMEOUT = 10000;
	
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
	 * The packet queue filled with incoming packets.
	 */
	private final LinkedBlockingDeque<Pair<InetSocketAddress, Packet>> packetQueue = new LinkedBlockingDeque<Pair<InetSocketAddress, Packet>>();
	
	/**
	 * The list of currently connected clients.
	 */
	private final Map<InetSocketAddress, RemoteClient> clients = new ConcurrentHashMap<InetSocketAddress, RemoteClient>();
	
	/**
	 * Temporarily store RSA keys in a hashmap.
	 */
	private final Map<InetSocketAddress, PublicKey> remoteKeys = new HashMap<InetSocketAddress, PublicKey>();
	
	/**
	 * Indicates whether the server is ready to be started.
	 */
	private boolean ready = false;
	
	/**
	 * Operational state of the server (running or not).
	 */
	private volatile boolean running = false;
	
	/**
	 * Initializes the server to an operational state.
	 */
	public GameServer(int port) {
		keys = RSA.generateKeyPair(KEY_SIZE);
		receiver = new UDPReceiver(null, port, keys.getPrivate()).withCallback((InetSocketAddress source, Packet packet) -> {
			packetQueue.add(new Pair<InetSocketAddress, Packet>(source, packet));
		});
		sender = new UDPSender(receiver.getSocket());
		
		if(receiver.isReady() && sender.isReady())
			ready = true;
	}
	
	/**
	 * Returns whether this server is ready to be started.
	 * @return [<d>boolean</b>] True if this server is ready to operate, false otherwise.
	 */
	public boolean isReady() {
		return ready;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void run() {
		
		receiver.start();
		sender.start();
		
		while(running) {
			
			try {
				
				Pair<InetSocketAddress, Packet> addrPacket = packetQueue.poll(1, TimeUnit.SECONDS);
				if(addrPacket == null) 
					continue;
				
				InetSocketAddress sourceAddress = addrPacket.first;
				Packet p = addrPacket.second;
				
				switch(p.getType()) {
				
					case PacketType.KEY:
						PacketKey pKey = Packets.cast(p);
						handlePacket(sourceAddress, pKey);
						continue;
					
					case PacketType.CONNECT:
						PacketConnect pConnect = Packets.cast(p);
						handlePacket(sourceAddress, pConnect);
						continue;
						
					case PacketType.DISCONNECT:
						PacketDisconnect pDisconnect = Packets.cast(p);
						handlePacket(sourceAddress, pDisconnect);
						continue;
						
				}
					
				RemoteClient client = clients.get(sourceAddress);
				if(client == null)
					continue;
				
				client.handler.packets.add(p);
				
			} catch (Exception ignored) {}
			
		}
	}
	
	/**
	 * Starts the server if it is operational.
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
	 * Stops the server and kills its thread.
	 */
	public void halt() {
		running = false;
		try {
			join();
		} catch (InterruptedException e) {}
	}
	
	/**
	 * Handles an invalid packet.
	 * @param source The IP address and port the packet was received from.
	 * @param packet The packet content.
	 */
	private void handlePacket(InetSocketAddress source, PacketKey packet) {
		
		// Reset client timeout if connected
		RemoteClient client = clients.get(source);
		if(client != null)
			client.timeout = CLIENT_TIMEOUT;
		
		// Store the remote public key
		if(packet.getKey() != null)
			remoteKeys.put(source, packet.getKey());
		
		// Send public key back
		if(packet.getRequestRemoteKey()) {
			packet.setRequestRemoteKey(false);
			packet.setKey(keys.getPublic());
			sender.send(packet, source);
		}
		
	}
	
	/**
	 * Handles a connection packet.
	 * @param source The IP address and port the packet was received from.
	 * @param packet The packet content.
	 */
	private void handlePacket(InetSocketAddress source, PacketConnect packet) {
		RemoteClient client = clients.get(source);
		if(client != null) {
			sender.send(new PacketAcceptConnection(), source, client.key);
			return;
		}
		
		RemoteClient newClient = new RemoteClient(source, remoteKeys.get(source));
		onConnect(newClient);
		
		sender.send(new PacketAcceptConnection(), source, newClient.key);
	}
	
	/**
	 * Handles a disconnect packet.
	 * @param source The IP address and port the packet was received from.
	 * @param packet The packet content.
	 */
	private void handlePacket(InetSocketAddress source, PacketDisconnect packet) {
		RemoteClient client = clients.get(source);
		if(client == null) return;
		onDisconnect(client);
	}
	
	/**
	 * This method fires when a client has disconnected.
	 * @param client The client having disconnected.
	 */
	private void onConnect(RemoteClient client) {
		System.out.println("Client " + client.address + " has connected.");
		clients.put(client.address, client);
		client.handler.start();
	}
	
	/**
	 * This method fires when a client has timed out.
	 * @param client The client having timed out.
	 */
	private void onTimeout(RemoteClient client) {
		System.out.println("Client " + client.address + " has timed out.");
		clients.remove(client.address);
		client.handler.halt();
	}
	
	/**
	 * This method fires when a client has disconnected.
	 * @param client The client having disconnected.
	 */
	private void onDisconnect(RemoteClient client) {
		System.out.println("Client " + client.address + " has disconnected.");
		clients.remove(client.address);
		client.handler.halt();
	}
	
}
