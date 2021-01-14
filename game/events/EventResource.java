package events;

import network.annotations.SerializableField;
import network.events.NetworkEvent;

public class EventResource extends NetworkEvent {

    public enum PlayerHandEvent {
        RESOURCES_ADDED,
        RESOURCES_REMOVED
    }

    @SerializableField
    private PlayerHandEvent event;
    private int resource;
    private int count;

    public EventResource() {
        event = PlayerHandEvent.RESOURCES_ADDED;
        resource = Tile.WHEAT;
        count = 0;
    }

    public EventResource(PlayerHandEvent event, ResourceType resource, int count) {
        this.event = event;
        this.resource = resource;
        this.count = count;
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
