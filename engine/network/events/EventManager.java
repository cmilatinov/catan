package network.events;

import network.UDPSender;
import network.packets.PacketEvent;
import network.packets.PacketEventConfirmation;

import java.net.InetSocketAddress;
import java.util.ArrayList;

public class EventManager {

    /**
     * Amount of time in milliseconds after which to resend an event if no confirmation was received.
     */
    private static final int EVENT_CONFIRMATION_TIMEOUT = 500;

    /**
     * Represents an event that has already been sent by this peer.
     */
    private static class EventSent {
        public final long eventID;
        public final NetworkEvent event;
        public long timestamp;

        private EventSent(long eventID, NetworkEvent event) {
            this.eventID = eventID;
            this.event = event;
            this.timestamp = System.currentTimeMillis();
        }

        public boolean equals(Object obj) {
            if (obj == null)
                return false;

            if (!(obj instanceof EventSent))
                return false;

            return ((EventSent)obj).eventID == eventID;
        }

        private void resetTimestamp() {
            this.timestamp = System.currentTimeMillis();
        }
    }

    private long nextSendingEventID = 0;
    private final ArrayList<EventSent> eventsSent = new ArrayList<>(10);

    private long nextReceivingEventID = 0;
    private final ArrayList<Long> unreceivedEvents = new ArrayList<>(10);

    private final UDPSender sender;
    private InetSocketAddress remote = null;
    private final NetworkEventListener eventTrigger;

    /**
     * Creates an event manager that processes incoming event packets triggers a specified callback ONCE PER UNIQUE EVENT.
     * @param sender The sender to use to send confirmation packets.
     * @param eventTrigger The callback to invoke ONCE PER UNIQUE EVENT.
     */
    public EventManager(UDPSender sender, NetworkEventListener eventTrigger) {
        this.sender = sender;
        this.eventTrigger = eventTrigger;
    }

    /**
     * Sets this manager's remote peer address.
     * @param remote The remote peer's IP address and port.
     * @return [{@link EventManager}] This same instance of the class.
     */
    public EventManager setRemoteAddress(InetSocketAddress remote) {
        this.remote = remote;
        return this;
    }

    /**
     * Processes an event packet received from the remote peer.
     * @param pEvent The packet received from the remote peer.
     */
    public void handlePacket(PacketEvent pEvent) {

        // Event and event ID
        long eventID = pEvent.getEventID();
        NetworkEvent event = pEvent.getEvent();

        // Difference between next expected event ID and received event ID
        long diff = eventID - nextReceivingEventID;

        // Send confirmation packet to remote
        sender.send(new PacketEventConfirmation(pEvent.getEventID()), remote);

        // Event ID matches or is ahead of next ID
        if (diff >= 0) {

            // Trigger event callback
            eventTrigger.invoke(event);

            // Add any missed event IDs to unreceived list
            for (long i = eventID - diff; i < eventID; i++)
                unreceivedEvents.add(i);

        // Event ID is behind next ID
        } else {

            // Find event ID in unreceived list
            int index = unreceivedEvents.indexOf(eventID);

            // If we do find it, trigger event callback and remove ID from the list
            if (index >= 0) {
                eventTrigger.invoke(event);
                unreceivedEvents.remove(index);
            }

        }

        // Increment the next ID by diff + 1 if diff + 1 is positive
        nextReceivingEventID += Math.max(diff + 1, 0);

    }

    /**
     * Processes an event confirmation packet received from the remote peer.
     * @param pEventConfirm The packet received from the remote peer.
     */
    public void handlePacket(PacketEventConfirmation pEventConfirm) {

        // Remove the confirmed event from events sent if it exists
        eventsSent.stream()
                .filter(s -> s.eventID == pEventConfirm.getEventID())
                .findFirst()
                .ifPresent(eventsSent::remove);

    }

    /**
     * Sends an event to the remote peer.
     * @param event The event to send.
     */
    public void sendEvent(NetworkEvent event) {

        // Get next event ID
        long eventID = nextSendingEventID++;

        // Send event to peer
        sender.send(new PacketEvent(eventID, event), remote);

        // Save sent event
        eventsSent.add(new EventSent(eventID, event));

    }

    /**
     * Updates the event manager, resending event packets if necessary.
     */
    public void update() {

        // Resend any event that has not been confirmed within the defined time duration
        eventsSent.forEach(es -> {
            if (System.currentTimeMillis() - es.timestamp > EVENT_CONFIRMATION_TIMEOUT) {
                sender.send(new PacketEvent(es.eventID, es.event), remote);
                es.resetTimestamp();
            }
        });

    }

}
