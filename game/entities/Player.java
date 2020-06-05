package entities;

import java.util.HashMap;

import gameplay.TileTypes;
import resources.Resource;

public class Player {
	private final HashMap<TileTypes, Integer> resourceCards = new HashMap<TileTypes, Integer>();
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

	public void addResourceCard(TileTypes resource, int val) {
		resourceCards.put(resource, resourceCards.get(resource) + val);
	}

	public int getResourceCards(TileTypes type) {
		return resourceCards.get(type);
	}


	public void clearHand() {
		resourceCards.put(TileTypes.BRICK, 0);
		resourceCards.put(TileTypes.SHEEP, 0);
		resourceCards.put(TileTypes.STONE, 0);
		resourceCards.put(TileTypes.FOREST, 0);
		resourceCards.put(TileTypes.WHEAT, 0);
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
	
	public void giveCards(TileTypes type, int value) {
		resourceCards.put(type, resourceCards.get(type) + value);
	}
	
}
