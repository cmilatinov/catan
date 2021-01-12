package network.events;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class NetworkEvents {

    /**
     * Hashmap containing all possible event types.
     */
    private static final Map<Integer, Class<? extends NetworkEvent>> events = new HashMap<Integer, Class<? extends NetworkEvent>>();

    /**
     * Creates a new event instance with the given serialized data.
     * @param data The serialized event data.
     * @return [{@link NetworkEvent}] The newly created event instance.
     */
    public static NetworkEvent createFromData(byte[] data) {

        try {

            // Parse event type
            int type = -1;
            ByteBuffer bb = ByteBuffer.wrap(data);
            if(data.length >= Integer.BYTES)
                type = bb.getInt(0);

            // Get the event type
            Class<? extends NetworkEvent> eventType = events.get(type);

            // If it does not exist return null reference
            if(eventType == null)
                return null;

            // Create a new instance of the event with the rest of the data
            return eventType.getConstructor(byte[].class)
                    .newInstance((Object)Arrays.copyOfRange(data, Integer.BYTES, data.length));

        } catch (Exception e) {

            // Return a null reference in case of exceptions
            return null;

        }

    }

    /**
     * Dynamically maps event types to event classes allowing for custom event implementations.
     * @param type A unique integer representing the event.
     * @param event The corresponding event class used to instantiate events of that type.
     */
    public static void register(int type, Class<? extends NetworkEvent> event) {
        events.put(type, event);
    }

    /**
     * Casts an event to its specific subtype.
     * @param <E> The event subtype class to cast to.
     * @param event The event object to cast.
     * @return [<b>P</b>] The casted event object or null if event is not of class E.
     */
    @SuppressWarnings("unchecked")
    public static <E extends NetworkEvent> E cast(NetworkEvent event) {
        try {
            return (E) event;
        } catch (ClassCastException e) {
            return null;
        }
    }

}
