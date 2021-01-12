package network.events;

import events.EventType;
import network.packets.PacketConnect;
import utils.StringUtils;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class EventTest extends NetworkEvent {

    private static final Charset ENCODING = StandardCharsets.UTF_8;

    /**
     * The test message.
     */
    private String msg;

    /**
     * Creates a new connection request packet with a generic test message.
     */
    public EventTest() {
        super(EventType.TEST);
        this.msg = "This is a test message";
    }

    /**
     * Creates a new test event with the given message.
     * @param msg The message to send to the remote peer.
     */
    public EventTest(String msg) {
        super(EventType.TEST);
        this.msg = msg;
    }

    /**
     * Parses a test event packet from the given packet data.
     * @param data The packet data to parse.
     */
    public EventTest(byte[] data) {
        super(EventType.TEST);
        msg = StringUtils.createFromBytes(data, ENCODING);
    }

    /**
     * {@inheritDoc}
     */
    public byte[] serialize() {
        byte[] bMsg = StringUtils.getBytes(msg, ENCODING);
        ByteBuffer data = ByteBuffer.allocate(HEADER_SIZE + 1 + bMsg.length);
        writeHeader(data);
        data.put(bMsg);
        return data.array();
    }

    /**
     * Returns the stored message.
     * @return [{@link String}] The stored message in string format.
     */
    public String getMessage() {
        return msg;
    }

    /**
     * Sets this packet's stored message.
     * @param msg The new message this packet is to hold.
     * @return [{@link PacketConnect}] This same {@link PacketConnect} instance to allow for method chaining.
     */
    public EventTest setMessage(String msg) {
        this.msg = msg;
        return this;
    }

    @Override
    public String toString() {
        return "TEST - " + this.msg;
    }

}
