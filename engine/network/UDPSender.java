package network;

import network.packets.Packet;
import network.serializers.PacketSerializer;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.security.PublicKey;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Used to send UDP packets over the network. This class implements its own thread.
 */
@SuppressWarnings("unused")
public class UDPSender extends Thread {

    /**
     * The port to which the sender is bound.
     */
    private int port;

    /**
     * The datagram socket used to listen for incoming packets.
     */
    private DatagramSocket socket;

    /**
     * A queue of packets to be sent.
     */
    private final LinkedBlockingDeque<DatagramPacket> packets = new LinkedBlockingDeque<>();

    /**
     * Indicates whether this sender is ready to be started.
     */
    private boolean ready = false;

    /**
     * The operational state of this thread (running or not).
     */
    private volatile boolean running = false;

    /**
     * Initializes a new {@link UDPSender} on a random port.
     */
    public UDPSender() {
        try {
            // Create the socket and retrieve its port number
            this.socket = new DatagramSocket();
            this.port = socket.getLocalPort();

            // Set ready
            this.ready = true;

        } catch (SocketException e) {

            // Error while creating socket
            this.socket = null;
            e.printStackTrace();

        }
    }

    /**
     * Initializes a new {@link UDPSender} on a given port.
     *
     * @param port The port on which to bind the sender's socket.
     */
    public UDPSender(int port) {
        this.port = port;

        try {
            // Create the socket on the specified port
            this.socket = new DatagramSocket(port);

            // Set ready
            this.ready = true;

        } catch (SocketException e) {

            // Error while creating socket
            this.socket = null;
            e.printStackTrace();

        }
    }

    /**
     * Initializes a new {@link UDPSender} on a random port.
     *
     * @param socket The socket to listen with.
     */
    public UDPSender(DatagramSocket socket) {
        this.socket = socket;
        this.port = socket.getLocalPort();

        // Set ready if socket is bound
        if (this.socket.isBound() && !this.socket.isClosed())
            this.ready = true;
    }

    /**
     * The entry point of the receiver's thread.
     */
    public void run() {
        // Loop until halted
        while (running) {
            try {

                // Remove a packet from the queue
                DatagramPacket packet = packets.take();

                // Send it over UDP
                socket.send(packet);

            } catch (Exception e) {
                // TODO Log failed to send packet
                e.printStackTrace();

            }
        }
    }

    /**
     * Starts the sender if it is operational.
     */
    public synchronized void start() {
        if (running)
            return;

        if (!ready)
            return;

        this.running = true;

        super.start();
    }

    /**
     * Stops the sender and kills its thread.
     */
    public synchronized void halt() {
        this.running = false;
        try {
            this.join();
        } catch (InterruptedException ignored) {
        }
    }

    /**
     * Returns whether this sender is ready to be started.
     *
     * @return [<d>boolean</b>] True if this receiver is ready to operate, false otherwise.
     */
    public boolean isReady() {
        return ready;
    }

    /**
     * Returns the bind port of this sender.
     *
     * @return <b>int</b> The local port number to which this sender is bound to.
     */
    public int getPort() {
        return port;
    }

    /**
     * Enqueues a packet to be sent to the specified IP and port.
     *
     * @param packet The packet to send.
     * @param dest   The destination IP address and port.
     */
    public void send(Packet packet, InetSocketAddress dest) {
        byte[] data = PacketSerializer.serialize(packet);
        packets.add(new DatagramPacket(data, data.length, dest));
    }

    /**
     * Enqueues an encrypted packet to be sent to the specified IP and port.
     *
     * @param packet The packet to send.
     * @param dest   The destination IP address and port number.
     * @param key    The public RSA key to encrypt the packet with.
     */
    public void send(Packet packet, InetSocketAddress dest, PublicKey key) {
        if (key == null) {
            send(packet, dest);
            return;
        }

        byte[] data = PacketSerializer.serialize(packet);
        byte[] encrypted = RSA.encrypt(key, data);

        if (encrypted != null)
            packets.add(new DatagramPacket(encrypted, encrypted.length, dest));
    }

}