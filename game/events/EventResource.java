package events;

import entities.board.Tile;
import network.events.NetworkEvent;

import java.nio.ByteBuffer;

public class EventResource extends NetworkEvent {

    public enum PlayerHandEvent {
        RESOURCES_ADDED,
        RESOURCES_REMOVED
    }

    private PlayerHandEvent event;
    private int resource;
    private int count;

    public EventResource() {
        super(EventType.RESOURCE);
        event = PlayerHandEvent.RESOURCES_ADDED;
        resource = Tile.WHEAT;
        count = 0;
    }

    public EventResource(PlayerHandEvent event, int resource, int count) {
        super(EventType.RESOURCE);
        this.event = event;
        this.resource = resource;
        this.count = count;
    }

    public EventResource(byte[] data) {
        super(EventType.RESOURCE);
        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        event = PlayerHandEvent.values()[byteBuffer.getInt()];
        count = byteBuffer.getInt();
    }

    public byte[] serialize() {
        ByteBuffer data = ByteBuffer.allocate(HEADER_SIZE + 3 * Integer.BYTES);
        writeHeader(data);
        data.putInt(event.ordinal())
            .putInt(count);
        return data.array();
    }

    public PlayerHandEvent getEvent() {
        return event;
    }

    public int getCount() {
        return count;
    }

    public void setEvent(PlayerHandEvent event) {
        this.event = event;
    }

    public void setCount(int count) {
        this.count = count;
    }

}
