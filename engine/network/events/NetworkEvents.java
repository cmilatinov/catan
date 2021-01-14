package network.events;

import java.util.HashMap;
import java.util.Map;

public class NetworkEvents {

    /**
     * Indicates the number of registered event types.
     */
    private static int numEventTypes = 0;

    /**
     * Hash map translating event IDs to class types.
     */
    private static final Map<Integer, Class<? extends NetworkEvent>> EVENT_CLASS_FROM_ID = new HashMap<>();

    /**
     * Hash map translating event classes to IDs.
     */
    private static final Map<Class<? extends NetworkEvent>, Integer> EVENT_ID_FROM_CLASS = new HashMap<>();

    static {{
        register(EventTest.class);
    }}

    /**
     * Dynamically maps event types to event classes allowing for custom event implementations.
     *
     * @param event The corresponding event class used to instantiate events of that type.
     */
    public static void register(Class<? extends NetworkEvent> event) {
        EVENT_CLASS_FROM_ID.put(numEventTypes, event);
        EVENT_ID_FROM_CLASS.put(event, numEventTypes++);
    }

    /**
     * Returns an integer acting as a unique identifier for a specific {@link NetworkEvent} subclass.
     *
     * @param event The event class for which to retrieve an ID.
     * @return <b>int</b> An integer acting as a unique identifier for a specific {@link NetworkEvent} subclass. A negative integer indicates that the specified event class does not exist or has not been registered.
     */
    public static int getEventID(Class<? extends NetworkEvent> event) {
        Integer eventID = EVENT_ID_FROM_CLASS.get(event);
        return eventID != null ? eventID : -1;
    }

    /**
     * Returns the {@link Class} instance to which an event ID corresponds.
     *
     * @param eventID The event ID for which to retrieve the corresponding class.
     * @return {@link Class} The class to which the event ID corresponds or null if no such event ID exists.
     */
    public static Class<? extends NetworkEvent> getEventClass(int eventID) {
        return EVENT_CLASS_FROM_ID.get(eventID);
    }

    /**
     * Casts an event to its specific subtype.
     *
     * @param <E>   The event subtype class to cast to.
     * @param event The event object to cast.
     * @return <b>E</b> The casted event object or null if event is not of class E.
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
