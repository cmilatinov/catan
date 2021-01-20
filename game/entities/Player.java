package entities;

import entities.board.Tile;
import objects.TexturedMesh;
import resources.Resource;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Player {

	public static final int BYTES = 2 * Integer.BYTES;

	private final int id;
	private final Resource color;
	private final ArrayList<Integer> resourceCards = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0));

//	public DevelopmentCards(TexturedMesh model) {
//		this(model, 14, 2, 2, 2, 5);
//	}
//
//	@Override
//	public boolean shouldRender() {
//		return true;
//	}
//
//	public DevelopmentCards(TexturedMesh model, int knights, int roadBuilding, int yearOfPlenty, int monopoly, int victoryPoint) {
//		super(model);
//
//		addCards(knights, ResourceType.KNIGHT);
//		addCards(roadBuilding, ResourceType.ROAD_BUILDING);
//		addCards(yearOfPlenty, ResourceType.YEAR_OF_PLENTY);
//		addCards(monopoly, ResourceType.MONOPOLY);
//		addCards(victoryPoint, ResourceType.VICTORY_POINT);
//	}

	public Player(int id, Resource color) {
		this.id = id;
		this.color = color;
		clearHand();
	}

	public Player(ByteBuffer buffer) {
		this.id = buffer.getInt();
		this.color = Resource.values()[buffer.getInt()];
	}

	public void removeResourceCards(int resource, int count) { resourceCards.set(resource, resourceCards.get(resource) - count); }

	public void addResourceCard(int resource, int count) { resourceCards.set(resource, resourceCards.get(resource) + count); }

	public int getID() {
		return id;
	}

	public Resource getColor() {
		return color;
	}

	public int getResourceCards(int type) {
		return resourceCards.get(type);
	}

	public boolean canAfford(int[] price) {
		for(int i =0; i < price.length; i ++)
			if(resourceCards.get(i) < price[i])
				return false;

		return true;
	}

	public void purchasePiece(int[] price) {
		for(int i = 0; i < price.length; i ++)
			removeResourceCards(i, price[i]);
	}

	public void clearHand() {
		resourceCards.set(Tile.WOOD, 0);
		resourceCards.set(Tile.BRICK, 0);
		resourceCards.set(Tile.SHEEP, 0);
		resourceCards.set(Tile.WHEAT, 0);
		resourceCards.set(Tile.STONE, 0);
	}

	public byte[] serialize() {
		ByteBuffer data = ByteBuffer.allocate(BYTES);
		data.putInt(id)
			.putInt(color.ordinal());
		return data.array();
	}

}
