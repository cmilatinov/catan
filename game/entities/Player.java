package entities;

import java.util.HashMap;

import gameplay.TileTypes;

public class Player {
	private HashMap<TileTypes, Integer> resourceCards;
	
	public Player() {
		resourceCards = new HashMap<TileTypes, Integer>();
		resourceCards.put(TileTypes.BRICK, 0);
		resourceCards.put(TileTypes.SHEEP, 0);
		resourceCards.put(TileTypes.STONE, 0);
		resourceCards.put(TileTypes.FOREST, 0);
		resourceCards.put(TileTypes.WHEAT, 0);
	}
	
	public void placeCity(int vertex) {
		
	}
	
	public void placeSettlement(int vertex) {
		
	}
	
	public void placeRoad() {
		
	}
	
	public void giveCards(TileTypes type, int value) {
		resourceCards.put(type, resourceCards.get(type) + value);
	}
	
}
