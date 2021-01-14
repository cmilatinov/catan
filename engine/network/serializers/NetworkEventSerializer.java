package network.serializers;

import network.events.NetworkEvent;
import network.events.NetworkEvents;

import java.nio.ByteBuffer;

public class NetworkEventSerializer extends FieldSerializer<NetworkEvent> {

    /**
     * Serializes a {@link NetworkEvent} instance into a byte array.
     *
     * @param event The event to be serialized.
     * @return <b>byte[]</b> The resulting byte array.
     */
    public byte[] serialize(NetworkEvent event) {
        byte[] eventData = ObjectSerializer.serialize(event);
        return ByteBuffer.allocate(eventData.length + Integer.BYTES)
                .putInt(event.getEventID())
                .put(eventData)
                .array();
    }

    /**
     * Reconstructs a {@link NetworkEvent} instance from a given byte array. This is the reverse operation of {@link #serialize}.
     *
     * @param data The byte data in which the event is serialized.
     * @return {@link NetworkEvent} The resulting event instance.
     */
    public NetworkEvent deserialize(byte[] data) {
        try {
            // Parse event type
            int eventID = -1;
            ByteBuffer buffer = ByteBuffer.wrap(data);
            if (data.length >= Integer.BYTES)
                eventID = buffer.getInt();

            // Get the event class
            Class<? extends NetworkEvent> eventType = NetworkEvents.getEventClass(eventID);
            if (eventType == null)
                return null;

            // Deserialize the event
            byte[] eventData = new byte[buffer.remaining()];
            buffer.get(eventData);
            return ObjectSerializer.deserialize(eventType, eventData);
        } catch (Exception e) {
            // Return a null reference in case of exceptions
            return null;
        }
    }

}
