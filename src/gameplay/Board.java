package gameplay;

import java.util.*;

import entities.Building;

public class Board {
	private HashMap<TileTypes, Integer> tileQuantities = new HashMap<TileTypes, Integer>();
	private ArrayList<Integer> tileNumbers = new ArrayList<Integer>();
	
	private final int BOARD_VERTICES = 54;
	private final int[] BOARD_TILES_PER_ROW = {3, 4, 5, 4, 3};
	
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
		ArrayList<Vertex> boardVertices = new ArrayList<Vertex>();
		
		for(int i = 0; i < BOARD_VERTICES; i ++) {
			boardVertices.add(new Vertex());
		}
		
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
		
		int tileIndex = 0;
		int vertexIndex = 0;
		
		int vertexRowConst = 6;
		int vertexIndexConst = 3;
		// Binding vertices
		for(int row = 0; row < BOARD_TILES_PER_ROW.length; row ++ ) {
			
			switch(BOARD_TILES_PER_ROW[row]) {
			case 3:
				vertexRowConst = 6;
				break;
			case 4:
				vertexRowConst = 8;
				break;
			case 5:
				vertexRowConst = 9;
				break;
			}
			
			for(int t = 0; t < BOARD_TILES_PER_ROW[row]; t ++ ) {
				tiles.get(tileIndex).addVertex(boardVertices.get(vertexIndex));
				tiles.get(tileIndex).addVertex(boardVertices.get(vertexIndex + 1));
				tiles.get(tileIndex).addVertex(boardVertices.get(vertexIndex + 2));
				tiles.get(tileIndex).addVertex(boardVertices.get(vertexIndex + vertexRowConst + 2));
				tiles.get(tileIndex).addVertex(boardVertices.get(vertexIndex + vertexRowConst + 3));
				tiles.get(tileIndex).addVertex(boardVertices.get(vertexIndex + vertexRowConst + 4));
				
				vertexIndex += 2;
			}
			
			if(row > 0 && BOARD_TILES_PER_ROW[row - 1] > BOARD_TILES_PER_ROW[row]) {
				vertexIndexConst ++;
			}
			
			vertexIndex += vertexIndexConst;
		}
		
		
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
	
}
