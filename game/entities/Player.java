package entities;

import java.util.HashMap;

import gameplay.ResourceType;
import resources.Resource;

public class Player {
	private final HashMap<ResourceType, Integer> resourceCards = new HashMap<ResourceType, Integer>();
	private Resource color;

	public final int SETTLEMENTS = 5, ROADS = 15, CITIES = 4;
	private int freeSettlements = 0, freeRoads = 0;

	public Player(Resource color) {
		clearHand();
		this.color = color;
	}

	public Player() {
		clearHand();
		color = Resource.TEXTURE_COLOR_BLUE;
	}

	public void addResourceCard(ResourceType resource, int val) {
		resourceCards.put(resource, resourceCards.get(resource) + val);
	}

	public int getResourceCards(ResourceType type) {
		return resourceCards.get(type);
	}


	public void clearHand() {
		resourceCards.put(ResourceType.BRICK, 0);
		resourceCards.put(ResourceType.SHEEP, 0);
		resourceCards.put(ResourceType.STONE, 0);
		resourceCards.put(ResourceType.FOREST, 0);
		resourceCards.put(ResourceType.WHEAT, 0);
	}

	public void reduceFreeSettlements() { freeSettlements --;}

	public void reduceFreeRoads () { freeRoads --; }

	public void addFreeSettlement() {
		freeSettlements ++;
	}

	public void addFreeRoad() {
		freeRoads ++;
	}

	public int getFreeRoads() {
		return freeRoads;
	}

	public int getFreeSettlements() {
		return freeSettlements;
	}

	public Resource getColor() {
		return color;
	}

	public void setColor(Resource color) {
		this.color = color;
	}
	
	public void giveCards(ResourceType type, int value) {
		resourceCards.put(type, resourceCards.get(type) + value);
	}
	
}
