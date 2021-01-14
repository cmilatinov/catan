package events;

import gameplay.ResourceType;
import network.annotations.SerializableField;
import network.events.NetworkEvent;

public class EventResource extends NetworkEvent {

    public enum PlayerHandEvent {
        RESOURCES_ADDED,
        RESOURCES_REMOVED
    }

    @SerializableField
    private PlayerHandEvent event;

    @SerializableField
    private ResourceType resource;

    @SerializableField
    private int count;

    public EventResource() {
        event = PlayerHandEvent.RESOURCES_ADDED;
        resource = ResourceType.WHEAT;
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
