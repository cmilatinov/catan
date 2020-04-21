package gameplay;

import java.util.*;

import entities.Building;

public class Board {
	private HashMap<TileTypes, Integer> tileQuantities = new HashMap<TileTypes, Integer>();
	private ArrayList<Integer> tileNumbers = new ArrayList<Integer>();
	
	public enum TileTypes {
		SHEEP,
		ROCK,
		BRICK,
		WHEAT,
		WOOD,
		DESERT;
		
		public static TileTypes getRandomType() {
            Random random = new Random();
            return values()[random.nextInt(values().length)];
        }
	}

	private ArrayList<Tile> tiles;
	
	public Board() {
		tileQuantities.put(TileTypes.SHEEP, 4);
		tileQuantities.put(TileTypes.WHEAT, 4);
		tileQuantities.put(TileTypes.WOOD, 4);
		tileQuantities.put(TileTypes.ROCK, 3);
		tileQuantities.put(TileTypes.BRICK, 3);
		tileQuantities.put(TileTypes.DESERT, 1);
		
		tileNumbers.add(1); //0 -> 2
		tileNumbers.add(2); //1 -> 3
		tileNumbers.add(2); //2 -> 4
		tileNumbers.add(2);
		tileNumbers.add(2); //4 -> 6
		tileNumbers.add(0); //5 -> 7
		tileNumbers.add(2);
		tileNumbers.add(2);
		tileNumbers.add(2);
		tileNumbers.add(2);
		tileNumbers.add(1);
		
		createBoard();
	}
	
	private void createBoard() {
		tiles = new ArrayList<Tile>();
		
		Iterator tileIterator = tileQuantities.entrySet().iterator();
		
		while(tileIterator.hasNext()) {
			Map.Entry mapElement = (Map.Entry)tileIterator.next(); 
			for(int i = 0; i < (int)mapElement.getValue(); i ++) {
				tiles.add(new Tile((TileTypes) mapElement.getKey()));
			}
		}
		
		Collections.shuffle(tiles);
		
		int tileNumQuantity = 0;
		int tileNum = 0;
		
		for(int i = 0; i < tiles.size(); i ++) {
			
			while (tileNumQuantity == 0) {
				tileNumQuantity = tileNumbers.get(tileNum); // tileNumbers.get(5) => 0
				tileNum ++;									// tileNum ++ => 6
			}
			
			if(tiles.get(i).getType() != TileTypes.DESERT) {
				tiles.get(i).setValue(tileNum + 1);
				tileNumQuantity --;
			}
		}
		
		Collections.shuffle(tiles);
		
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
		
		public Tile(TileTypes type) {
			this.type = type;
			this.value = (int)(Math.random()*12) + 1;
			vertices = new ArrayList<Vertex>(Arrays.asList(new Vertex(), new Vertex(), new Vertex(), new Vertex(), new Vertex(), new Vertex()));
		}
		
		public Tile(TileTypes type, int value) {
			this.type = type;
			this.value = value;
			vertices = new ArrayList<Vertex>(Arrays.asList(new Vertex(), new Vertex(), new Vertex(), new Vertex(), new Vertex(), new Vertex()));
		}
		
		public int getValue() {
			return value;
		}
		
		public void setValue(int value) {
			this.value = value;
		}
		
		public TileTypes getType() {
			return this.type;
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
		private TileTypes port;
		
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
