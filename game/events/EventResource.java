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
    private ResourceType resource;
    private int count;

    public EventResource() {
        super(EventType.RESOURCE);
        event = PlayerHandEvent.RESOURCES_ADDED;
        resource = ResourceType.WHEAT;
        count = 0;
    }

    public EventResource(PlayerHandEvent event, ResourceType resource, int count) {
        super(EventType.RESOURCE);
        this.event = event;
        this.resource = resource;
        this.count = count;
    }

    public EventResource(byte[] data) {
        super(EventType.RESOURCE);
        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        event = PlayerHandEvent.values()[byteBuffer.getInt()];
        resource = ResourceType.values()[byteBuffer.getInt()];
        count = byteBuffer.getInt();
    }

    public byte[] serialize() {
        ByteBuffer data = ByteBuffer.allocate(HEADER_SIZE + 3 * Integer.BYTES);
        writeHeader(data);
        data.putInt(event.ordinal())
            .putInt(resource.ordinal())
            .putInt(count);
        return data.array();
    }

    public PlayerHandEvent getEvent() {
        return event;
    }

    public ResourceType getResource() {
        return resource;
    }

    public int getCount() {
        return count;
    }

    public void setEvent(PlayerHandEvent event) {
        this.event = event;
    }

    public void setResource(ResourceType resource) {
        this.resource = resource;
    }

    public void setCount(int count) {
        this.count = count;
    }

}
