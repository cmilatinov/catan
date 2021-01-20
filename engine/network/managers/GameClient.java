package network.managers;

import network.RSA;
import network.UDPReceiver;
import network.UDPSender;
import network.events.EventManager;
import network.events.NetworkEvent;
import network.packets.*;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.security.KeyPair;
import java.security.PublicKey;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * Runs a game client connecting to a remote server and handling network events.
 */
@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class GameClient extends Thread {

    /**
     * The RSA key size in bits.
     */
    private static final int KEY_SIZE = 512;

    /**
     * The time in milliseconds after which an unresponsive client is considered disconnected.
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
     * The event manager used to trigger events sent over the network.
     */
    private final EventManager eventManager;

    /**
     * The server's remote IP address.
     */
    private InetSocketAddress remoteAddress = null;

    /**
     * This client's identifier received from the server it connects to.
     */
    private int clientID = -1;

    /**
     * This client's username credential.
     */
    private String username = null;

    /**
     * The packet queue filled with incoming packets.
     */
    private final LinkedBlockingDeque<Packet> packetQueue = new LinkedBlockingDeque<>();

    /**
     * The callback to execute once a connection is made.
     */
    private Runnable onConnect = null;

    /**
     * The callback to execute once a connection is terminated.
     */
    private Runnable onDisconnect = null;

    /**
     * The callback to execute once a connection has timed out.
     */
    private Runnable onTimeout = null;

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
     * Indicates latency between the client and server in milliseconds.
     */
    private long ping = 0;

    /**
     * Initializes the client to an operational state.
     */
    @SuppressWarnings("ConstantConditions")
    public GameClient() {
        keys = RSA.generateKeyPair(KEY_SIZE);
        receiver = new UDPReceiver(keys.getPrivate()).withCallback((InetSocketAddress source, Packet packet) -> {
            if (remoteAddress == null || remoteAddress.equals(source))
                packetQueue.add(packet);
        });
        sender = new UDPSender(receiver.getSocket());
        eventManager = new EventManager(sender, this::onTriggerEvent);

        if (receiver.isReady() && sender.isReady()) {
            receiver.start();
            sender.start();
            ready = true;
        }
    }

    /**
     * Returns whether this client is ready to be started.
     *
     * @return [<d>boolean</b>] True if this client is ready to operate, false otherwise.
     */
    public boolean isReady() {
        return ready;
    }

    /**
     * Returns whether this client is currently running or not.
     *
     * @return [<d>boolean</b>] True if this client is currently running, false otherwise.
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Runs the client on the current execution thread. This method blocks until the client is stopped.
     */
    @SuppressWarnings({"ConstantConditions", "DuplicatedCode"})
    public void run() {

        long lastPing = System.currentTimeMillis();
        long lastTime = System.currentTimeMillis();
        while (running) {

            // Send ping if connected
            long currentTime = System.currentTimeMillis();
            if (connected && currentTime - lastPing >= 1000) {
                sender.send(new PacketPing(), remoteAddress, remoteKey);
                lastPing = currentTime;
            }

            // Decrease timeout counter
            long delta = currentTime - lastTime;
            if (connected)
                timeout -= delta;

            // Attempt to process pending packets
            out:
            try {
                Packet p = packetQueue.poll(300, TimeUnit.MILLISECONDS);
                if (p == null)
                    break out;

                // Reset timeout counter
                timeout = SERVER_TIMEOUT;

                if (PacketKey.class.equals(p.getClass())) {
                    PacketKey pKey = Packets.cast(p);
                    handlePacket(pKey);
                } else if (PacketAcceptConnection.class.equals(p.getClass())) {
                    PacketAcceptConnection pAcceptConn = Packets.cast(p);
                    handlePacket(pAcceptConn);
                } else if (PacketRejectConnection.class.equals(p.getClass())) {
                    PacketRejectConnection pRejectConn = Packets.cast(p);
                    handlePacket(pRejectConn);
                } else if (PacketPing.class.equals(p.getClass())) {
                    PacketPing pPing = Packets.cast(p);
                    handlePacket(pPing);
                } else if (PacketEvent.class.equals(p.getClass())) {
                    PacketEvent pEvent = Packets.cast(p);
                    handlePacket(pEvent);
                } else if (PacketEventConfirmation.class.equals(p.getClass())) {
                    PacketEventConfirmation pEvent = Packets.cast(p);
                    handlePacket(pEvent);
                }

            } catch (Exception ignored) {
            }

            // Server has timed out
            if (timeout <= 0 && connected)
                timeOut();

            // Update event manager
            eventManager.update();

            // Set last timestamp to current
            lastTime = currentTime;
        }
    }

    /**
     * Starts the client if it is operational.
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
     * Stops the client and kills its thread.
     */
    public void halt() {
        running = false;
        try {
            join();
        } catch (InterruptedException ignored) {
        }
    }

    /**
     * Attempts to connect the client to the remote address and port specified.
     *
     * @param address  The remote IP address in string form (ie. "192.168.0.1") to connect to.
     * @param port     The remote port number to connect to.
     * @param username The username to use.
     * @return {@link GameClient} This same {@link GameClient} instance to allow for method chaining.
     */
    public GameClient connect(String address, int port, String username) {
        if (!ready) return this;
        try {
            remoteAddress = new InetSocketAddress(InetAddress.getByName(address), port);
            eventManager.setRemoteAddress(remoteAddress);
            this.username = username;
            if (remoteKey != null) {
                sender.send(new PacketConnect(username), remoteAddress, remoteKey);
            } else {
                connectOnReceiveKey = true;
                sender.send(new PacketKey(true, keys.getPublic()), remoteAddress);
            }
        } catch (Exception e) {
            remoteAddress = null;
            connected = false;
            e.printStackTrace();
        }
        return this;
    }

    /**
     * Disconnects the client from the remote server it is currently connected to.
     *
     * @return {@link GameClient} This same {@link GameClient} instance to allow for method chaining.
     */
    public GameClient disconnect() {
        if (!ready || !connected) return this;
        sender.send(new PacketDisconnect(), remoteAddress);
        connected = false;
        if (this.onDisconnect != null)
            this.onDisconnect.run();
        return this;
    }

    /**
     * Sets the callback to invoke when a connection to a remote server is made.
     *
     * @param callback The new callback to run once a connection is made.
     * @return {@link GameClient} This same {@link GameClient} instance to allow for method chaining.
     */
    public GameClient onConnect(Runnable callback) {
        this.onConnect = callback;
        return this;
    }

    /**
     * Sets the callback to invoke when a connection to a remote server is terminated.
     *
     * @param callback The new callback to run once a connection is terminated.
     * @return {@link GameClient} This same {@link GameClient} instance to allow for method chaining.
     */
    public GameClient onDisconnect(Runnable callback) {
        this.onDisconnect = callback;
        return this;
    }

    /**
     * Sets the callback to invoke when a connection to a remote server has timed out.
     *
     * @param callback The new callback to run once a connection has timed out.
     * @return {@link GameClient} This same {@link GameClient} instance to allow for method chaining.
     */
    public GameClient onTimeout(Runnable callback) {
        this.onTimeout = callback;
        return this;
    }

    /**
     * Sends an event to the remote peer.
     *
     * @param event The event to send.
     * @return {@link GameClient} This same {@link GameClient} instance to allow for method chaining.
     */
    public GameClient sendEvent(NetworkEvent event) {
        if (!ready && !connected)
            return this;
        eventManager.sendEvent(event);
        return this;
    }

    /**
     * Returns whether or not this client is currently connected to a remote server.
     *
     * @return <b>boolean</b> True if a connection exists between this client and a remote server, false otherwise.
     */
    public boolean isConnected() {
        return connected;
    }

    private void handlePacket(PacketKey packet) {
        if (connectOnReceiveKey && packet.getKey() != null) {
            remoteKey = packet.getKey();
            connectOnReceiveKey = false;
            sender.send(new PacketConnect(username), remoteAddress, remoteKey);
        }
    }

    private void handlePacket(PacketAcceptConnection packet) {
        connected = true;
        clientID = packet.getID();
        if (this.onConnect != null)
            onConnect.run();
        System.out.println("Connected to server.");
    }

    private void handlePacket(PacketRejectConnection packet) {
        System.out.println("Failed to connect to server: " + packet.getReason());
    }

    private void handlePacket(PacketPing packet) {
        ping = (System.currentTimeMillis() - packet.getTimestamp()) / 2;
    }

    private void handlePacket(PacketEvent packet) {
        eventManager.handlePacket(packet);
    }

    private void handlePacket(PacketEventConfirmation packet) {
        eventManager.handlePacket(packet);
    }

    private void timeOut() {
        connected = false;
        remoteAddress = null;
        if (this.onTimeout != null)
            this.onTimeout.run();
        System.out.println("Server timed out.");
    }

    private void onTriggerEvent(NetworkEvent event) {
        System.out.println("Received event: " + event);
    }

    /**
     * Returns the latency between the client and server in milliseconds.
     *
     * @return <b>long</b> The latency between the client and server in milliseconds.
     */
    public long getPing() {
        if (!ready || !connected)
            return 0;
        return ping;
    }

}
