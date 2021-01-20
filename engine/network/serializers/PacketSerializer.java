package network.serializers;

import network.packets.Packet;
import network.packets.Packets;

import java.nio.ByteBuffer;

public class PacketSerializer {

    /**
     * Serializes a {@link Packet} instance into a byte array.
     *
     * @param packet The packet to be serialized.
     * @return <b>byte[]</b> The resulting byte array.
     */
    public static byte[] serialize(Packet packet) {
        byte[] packetData = ObjectSerializer.serialize(packet);
        return ByteBuffer.allocate(packetData.length + Integer.BYTES)
                .putInt(packet.getPacketID())
                .put(packetData)
                .array();
    }

    /**
     * Reconstructs a {@link Packet} instance from a given byte array. This is the reverse operation of {@link #serialize}.
     *
     * @param data The byte data in which the packet is serialized.
     * @return {@link Packet} The resulting packet instance.
     */
    public static Packet deserialize(byte[] data) {
        try {
            // Parse packet type
            int packetID = -1;
            ByteBuffer buffer = ByteBuffer.wrap(data);
            if (data.length >= Integer.BYTES)
                packetID = buffer.getInt();

            // Get the event class
            Class<? extends Packet> packetType = Packets.getPacketClass(packetID);
            if (packetType == null)
                return null;

            // Deserialize the event
            byte[] packetData = new byte[buffer.remaining()];
            buffer.get(packetData);
            return ObjectSerializer.deserialize(packetType, packetData);
        } catch (Exception e) {
            // Return a null reference in case of exceptions
            return null;
        }
    }

}
