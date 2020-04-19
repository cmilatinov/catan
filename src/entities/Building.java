package entities;

import gameplay.Board;

public class Building {

	private enum Type {
		SETTLEMENT,
		CITY
	}
	
	private Type type;
	private Player owner;
	
	public Building(Player owner) {
		type = Type.SETTLEMENT;
		this.owner = owner;
	}
	
	public void upgrade() {
		if(type == Type.CITY) {
			System.out.println("ERROR: Unable to upgrade a city.");
		}
		
		type = Type.CITY;
	}
	
	public int getValue() {
		return type == Type.SETTLEMENT ? 1 : 2;
	}
	
	public Player getOwner() {
		return this.owner;
	}
	
	public void rewardOwner(Board.TileTypes tileType) {
		this.owner.giveCards(tileType, getValue());
	}
	
}
