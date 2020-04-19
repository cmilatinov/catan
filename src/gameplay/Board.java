package gameplay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import entities.Building;

public class Board {
	public enum TileTypes {
		SHEEP,
		ROCK,
		BRICK,
		WHEAT,
		WOOD,
		DESSERT;
		
		public static TileTypes getRandomType() {
            Random random = new Random();
            return values()[random.nextInt(values().length)];
        }
	}

	private ArrayList<Tile> tiles;
	
	public Board() {
		tiles = new ArrayList<Tile>(
				Arrays.asList(new Tile(), new Tile(), new Tile()
				, new Tile(), new Tile(), new Tile(), new Tile()
				, new Tile(), new Tile(), new Tile(), new Tile()
				, new Tile(), new Tile(), new Tile(), new Tile()));
	}
	
	public int roll() {
		int die1 = (int)(Math.random()*6) + 1;
		int die2 = (int)(Math.random()*6) + 1;
		
		return die1 + die2;
	}
	
	public void playTurn() {
		int diceValue = roll();
		Tile[] tilesRolled = tiles.stream().filter(t -> t.getValue() == diceValue).toArray(Tile[]::new);
		
		for(Tile t : tilesRolled) {
			t.rewardSettlers();
		}
	}
	
	public static class Tile {
		
		private TileTypes type;
		private ArrayList<Vertex> vertices;
		private int value;
		
		public Tile() {
			type = TileTypes.getRandomType();
			value = (int)(Math.random()*12) + 1; 
			vertices = new ArrayList<Vertex>(Arrays.asList(new Vertex(), new Vertex(), new Vertex(), new Vertex(), new Vertex(), new Vertex()));
		}
		
		public int getValue() {
			return value;
		}
		
		public void rewardSettlers() {
			vertices.forEach(v -> {
				if(v.getBuilding() != null) {
					v.getBuilding().rewardOwner(type);
				}
			});
		}
		
	}
	
	public static class Vertex {
		private Building building;
		
		public Vertex() {
			
		}
		
		public Building getBuilding() {
			return building;
		}
		
		public void setBuilding(Building building) {
			this.building = building;
		}
		
		public void upgradeBuilding() {
			building.upgrade();
		}
		
		public int getBuildingValue() {
			return building.getValue();
		}
	}
	
}
