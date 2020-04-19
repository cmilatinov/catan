package entities;

import java.util.HashMap;

import gameplay.Board;

public class Player {
	private HashMap<Board.TileTypes, Integer> resourceCards;
	
	public Player() {
		resourceCards = new HashMap<Board.TileTypes, Integer>();
		resourceCards.put(Board.TileTypes.BRICK, 0);
		resourceCards.put(Board.TileTypes.SHEEP, 0);
		resourceCards.put(Board.TileTypes.ROCK, 0);
		resourceCards.put(Board.TileTypes.WOOD, 0);
		resourceCards.put(Board.TileTypes.WHEAT, 0);
	}
	
	public void placeCity(int vertex) {
		
	}
	
	public void placeSettlement(int vertex) {
		
	}
	
	public void placeRoad() {
		
	}
	
	public void giveCards(Board.TileTypes type, int value) {
		resourceCards.put(type, resourceCards.get(type) + value);
	}
	
}
