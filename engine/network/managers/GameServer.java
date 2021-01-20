package network.managers;

import network.RSA;
import network.UDPReceiver;
import network.UDPSender;
import network.events.EventManager;
import network.events.NetworkEvent;
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

/**
 * Runs a game server handling client connections and network events.
 */
public class GameServer extends Thread {

    /**
     * Represents a remote client.
     */
    private class RemoteClient {

        /**
         * This client's identifier.
         */
        public final int id;

        /**
         * This client's username.
         */
        private final String username;

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
         *
         * @param address The remote IP address and port from which the client is responding.
         * @param key     The client's public RSA key.
         */
        public RemoteClient(int id, String username, InetSocketAddress address, PublicKey key) {
            this.id = id;
            this.username = username;
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
        private final LinkedBlockingDeque<Packet> packets = new LinkedBlockingDeque<>();

        /**
         * The client this handler is serving.
         */
        private final RemoteClient client;

        /**
         * The event manager associated to this client handler.
         */
        private final EventManager eventManager;

        /**
         * The operational state of this handler (running or not).
         */
        private volatile boolean running = false;

        /**
         * Creates a new {@link ClientHandler} serving the specified client.
         *
         * @param client The remote client to serve.
         */
        public ClientHandler(RemoteClient client) {
            this.client = client;
            this.eventManager = new EventManager(sender, this::onTriggerEvent).setRemoteAddress(client.address);
        }

        /**
         * Sends an event to the remote client.
         *
         * @param event The event to send.
         * @return {@link GameClient} This same {@link GameClient} instance to allow for method chaining.
         */
        public ClientHandler sendEvent(NetworkEvent event) {
            eventManager.sendEvent(event);
            return this;
        }

        /**
         * {@inheritDoc}}
         */
        @SuppressWarnings({"DuplicatedCode", "ConstantConditions"})
        public void run() {
            long lastTime = System.currentTimeMillis();
            while (this.running) {

                // Handle timeouts
                long currentTime = System.currentTimeMillis();
                client.timeout -= currentTime - lastTime;
                if (client.timeout < 0)
                    onTimeout(client);
                lastTime = currentTime;

                // Attempt to process pending packets
                try {

                    // Get next packet
                    Packet p = packets.poll(500, TimeUnit.MILLISECONDS);
                    if (p == null) continue;

                    // Reset timeout
                    client.timeout = CLIENT_TIMEOUT;

                    // Handle packet
                    if (PacketPing.class.equals(p.getClass())) {
                        PacketPing pPing = Packets.cast(p);
                        handlePacket(pPing);
                    } else if (PacketEvent.class.equals(p.getClass())) {
                        PacketEvent pEvent = Packets.cast(p);
                        handlePacket(pEvent);
                    } else if (PacketEventConfirmation.class.equals(p.getClass())) {
                        PacketEventConfirmation pEvent = Packets.cast(p);
                        handlePacket(pEvent);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Update event manager
                eventManager.update();

            }
        }

        /**
         * Starts the client handler.
         */
        public void start() {
            if (this.running)
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

        private void handlePacket(PacketEvent packet) {
            eventManager.handlePacket(packet);
        }

        private void handlePacket(PacketEventConfirmation packet) {
            eventManager.handlePacket(packet);
        }

        private void onTriggerEvent(NetworkEvent event) {
            System.out.println("Received event : " + event + " (" + client.address + ")");
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
    private final LinkedBlockingDeque<Pair<InetSocketAddress, Packet>> packetQueue = new LinkedBlockingDeque<>();

    /**
     * The list of currently connected clients.
     */
    private final Map<InetSocketAddress, RemoteClient> clients = new ConcurrentHashMap<>();

    /**
     * Temporarily store RSA keys in a hashmap.
     */
    private final Map<InetSocketAddress, PublicKey> remoteKeys = new HashMap<>();

    /**
     * The next ID to be assigned to a client.
     */
    private int nextID = 0;

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
    @SuppressWarnings("ConstantConditions")
    public GameServer(int port) {
        keys = RSA.generateKeyPair(KEY_SIZE);
        receiver = new UDPReceiver(null, port, keys.getPrivate())
                .withCallback((InetSocketAddress source, Packet packet) -> packetQueue.add(new Pair<>(source, packet)));
        sender = new UDPSender(receiver.getSocket());

        if (receiver.isReady() && sender.isReady())
            ready = true;
    }

    /**
     * Broadcast an event to every client currently connected to the server.
     *
     * @param event The event to send.
     */
    public void broadcastEvent(NetworkEvent event) {
        for (var key : clients.keySet())
            clients.get(key).handler.sendEvent(event);
    }

    /**
     * Returns whether this server is ready to be started or not.
     *
     * @return [<d>boolean</b>] True if this server is ready to operate, false otherwise.
     */
    public boolean isReady() {
        return ready;
    }

    /**
     * Returns whether this server is currently running or not.
     *
     * @return [<d>boolean</b>] True if this server is currently running, false otherwise.
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * {@inheritDoc}
     */
    public void run() {

        receiver.start();
        sender.start();

        while (running) {

            try {

                Pair<InetSocketAddress, Packet> addrPacket = packetQueue.poll(3, TimeUnit.SECONDS);
                if (addrPacket == null)
                    continue;

                InetSocketAddress sourceAddress = addrPacket.first;
                Packet p = addrPacket.second;

                if (PacketKey.class.equals(p.getClass())) {
                    PacketKey pKey = Packets.cast(p);
                    handlePacket(sourceAddress, pKey);
                    continue;
                } else if (PacketConnect.class.equals(p.getClass())) {
                    PacketConnect pConnect = Packets.cast(p);
                    handlePacket(sourceAddress, pConnect);
                    continue;
                } else if (PacketDisconnect.class.equals(p.getClass())) {
                    PacketDisconnect pDisconnect = Packets.cast(p);
                    handlePacket(sourceAddress, pDisconnect);
                    continue;
                }

                RemoteClient client = clients.get(sourceAddress);
                if (client == null)
                    continue;

                client.handler.packets.add(p);

            } catch (Exception ignored) {
            }

        }
    }

    /**
     * Starts the server if it is operational.
     */
    public void start() {
        if (running)
            return;

        if (!ready)
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
        } catch (InterruptedException ignored) {
        }
    }

    /**
     * Handles an invalid packet.
     *
     * @param source The IP address and port the packet was received from.
     * @param packet The packet content.
     */
    private void handlePacket(InetSocketAddress source, PacketKey packet) {

        // Reset client timeout if connected
        RemoteClient client = clients.get(source);
        if (client != null)
            client.timeout = CLIENT_TIMEOUT;

        // Store the remote public key
        if (packet.getKey() != null)
            remoteKeys.put(source, packet.getKey());

        // Send public key back
        if (packet.getRequestRemoteKey()) {
            packet.setRequestRemoteKey(false);
            packet.setKey(keys.getPublic());
            sender.send(packet, source);
        }

    }

    /**
     * Handles a connection packet.
     *
     * @param source The IP address and port the packet was received from.
     * @param packet The packet content.
     */
    private void handlePacket(InetSocketAddress source, PacketConnect packet) {
        RemoteClient client = clients.get(source);
        if (client != null) {
            sender.send(new PacketAcceptConnection(), source, client.key);
            return;
        }

        RemoteClient newClient = new RemoteClient(nextID++, packet.getUsername(), source, remoteKeys.get(source));
        onConnect(newClient);

        sender.send(new PacketAcceptConnection(newClient.id), source, newClient.key);
    }

    /**
     * Handles a disconnect packet.
     *
     * @param source The IP address and port the packet was received from.
     * @param packet The packet content.
     */
    private void handlePacket(InetSocketAddress source, PacketDisconnect packet) {
        RemoteClient client = clients.get(source);
        if (client == null) return;
        onDisconnect(client, packet.getExitCode());
    }

    /**
     * This method fires when a client has disconnected.
     *
     * @param client The client having disconnected.
     */
    private void onConnect(RemoteClient client) {
        System.out.println("User " + client.username + " (" + client.address + ") has connected.");
        clients.put(client.address, client);
        client.handler.start();
    }

    /**
     * This method fires when a client has timed out.
     *
     * @param client The client having timed out.
     */
    private void onTimeout(RemoteClient client) {
        System.out.println("User " + client.username + " (" + client.address + ") has timed out.");
        clients.remove(client.address);
        client.handler.halt();
    }

    /**
     * This method fires when a client has disconnected.
     *
     * @param client   The client having disconnected.
     * @param exitCode A symbolic integer describing the reason for the disconnect.
     */
    private void onDisconnect(RemoteClient client, int exitCode) {
        System.out.println("User " + client.username + " (" + client.address + ") has disconnected. (Exit code = " + exitCode + ")");
        clients.remove(client.address);
        client.handler.halt();
    }

}
