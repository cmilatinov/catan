package events;

import gameplay.ResourceType;
import network.events.NetworkEvent;

import java.nio.ByteBuffer;

public class EventResource extends NetworkEvent {

    public enum PlayerHandEvent {
        RESOURCES_ADDED,
        RESOURCES_REMOVED
    }

    private PlayerHandEvent event;
    private ResourceType resourceType;

    private int count;

    public EventResource() {
        super(EventType.RESOURCE);

        event = PlayerHandEvent.RESOURCES_ADDED;
        resourceType = ResourceType.WHEAT;
        count = 0;
    }

    public EventResource(byte[] data) {
        super(EventType.RESOURCE);

        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        event = PlayerHandEvent.values()[byteBuffer.getInt()];
        resourceType = ResourceType.values()[byteBuffer.getInt()];
        count = byteBuffer.getInt();
    }

    public byte[] serialize() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(Long.BYTES + 3 * Integer.BYTES);
        writeHeader(byteBuffer);
        byteBuffer.putInt(event.ordinal()).putInt(resourceType.ordinal()).putInt(count);
        return byteBuffer.array();
    }

    public PlayerHandEvent getEvent() {
        return event;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public int getCount() {
        return count;
    }

    public void setEvent(PlayerHandEvent event) {
        this.event = event;
    }

    public void setResourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    public void setCount(int count) {
        this.count = count;
    }

}
