package entities;

import gameplay.ResourceType;
import resources.Resource;

import java.nio.ByteBuffer;
import java.util.HashMap;

public class Player {

	public static final int BYTES = 2 * Integer.BYTES;

	private final int id;
	private final Resource color;
	private final HashMap<ResourceType, Integer> resourceCards = new HashMap<ResourceType, Integer>();

	public Player(int id, Resource color) {
		this.id = id;
		this.color = color;
		clearHand();
	}

	public Player(ByteBuffer buffer) {
		this.id = buffer.getInt();
		this.color = Resource.values()[buffer.getInt()];
	}

	public int getID() {
		return id;
	}

	public Resource getColor() {
		return color;
	}

	public int getResourceCards(ResourceType type) {
		return resourceCards.get(type);
	}

	public void removeCards(ResourceType resource, int val) {
		resourceCards.put(resource, resourceCards.get(resource) - val);
	}

	public void addCards(ResourceType resource, int val) {
		resourceCards.put(resource, resourceCards.get(resource) + val);
	}

	public void clearHand() {
		resourceCards.put(ResourceType.BRICK, 0);
		resourceCards.put(ResourceType.SHEEP, 0);
		resourceCards.put(ResourceType.STONE, 0);
		resourceCards.put(ResourceType.FOREST, 0);
		resourceCards.put(ResourceType.WHEAT, 0);
	}

	public byte[] serialize() {
		ByteBuffer data = ByteBuffer.allocate(BYTES);
		data.putInt(id)
			.putInt(color.ordinal());
		return data.array();
	}

}
