package gameplay;

import java.util.*;
import resources.GameResources;
import resources.Resource;

public class Board {
	private Map<TileTypes, Integer> tileQuantities = new HashMap<TileTypes, Integer>();
	private ArrayList<Integer> tileNumbers = new ArrayList<Integer>();
	private ArrayList<Tile> tiles;
	
	private int boardVertices = 0;
	private int boardRadius;
	private final int[] TILES_PER_ROW;
	
	/**
	 * Constructor to create a board.
	 * 
	 * @param mapRadius - Radius of the board.
	 */
	public Board(int boardRadius) {
		// Sets up the amount of tiles per row that we want depending on the board radius.
		this.boardRadius = boardRadius;
		
		TILES_PER_ROW = new int[boardRadius * 2 - 1];
		boardVertices = 0;
		for(int i = 0; i <= (int)TILES_PER_ROW.length/2; i ++) {
			boardVertices += (((boardRadius + i) * 4) + 2);
			TILES_PER_ROW[i] = boardRadius + i;
			TILES_PER_ROW[TILES_PER_ROW.length - i - 1] = boardRadius + i;
		}
		
		tileQuantities.put(TileTypes.SHEEP, 4);
		tileQuantities.put(TileTypes.WHEAT, 4);
		tileQuantities.put(TileTypes.FOREST, 4);
		tileQuantities.put(TileTypes.STONE, 3);
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
		ArrayList<Vertex> verticesList = new ArrayList<Vertex>();
		
		for(int i = 0; i < this.boardVertices; i ++) {
			verticesList.add(new Vertex());
		}
		
		Iterator tileIterator = tileQuantities.entrySet().iterator();
		
		while(tileIterator.hasNext()) {
			Map.Entry mapElement = (Map.Entry)tileIterator.next(); 
			for(int i = 0; i < (int)mapElement.getValue(); i ++) {
				tiles.add(new Tile
						(GameResources.get(Resource.getTileModel((TileTypes) mapElement.getKey()))
						, (TileTypes) mapElement.getKey()));
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
		
		// Assigning all the vertices to the tiles
		// t for tile and v for vertex
		int vIndex = 0;
		int tIndex = 0;
		
		double startHexX = 2;
		double startHexY = 0;
		double hexZ = 2;
		
		double currHexX;
		double currHexY;
		
		for(int row = 0; row < TILES_PER_ROW.length; row ++) {
			currHexX = startHexX * -1;
			currHexY = startHexY;
			
			for(int t = 0; t < TILES_PER_ROW[row]; t ++) {
				
				hexZ = (row - 2) * -1;
				
				tiles.get(tIndex).addVertex(verticesList.get(vIndex ++));
				tiles.get(tIndex).addVertex(verticesList.get(vIndex ++));
				tiles.get(tIndex).addVertex(verticesList.get(vIndex));
				
				tiles.get(tIndex).addVertex(verticesList.get(vIndex + (TILES_PER_ROW[row] * 2)));
				tiles.get(tIndex).addVertex(verticesList.get(vIndex + (TILES_PER_ROW[row] * 2) + 1));
				tiles.get(tIndex).addVertex(verticesList.get(vIndex + (TILES_PER_ROW[row] * 2) + 2));
				
				tiles.get(tIndex).setHexagonalCoords(currHexX++, currHexY--, hexZ);
				
				tiles.get(tIndex).getHexagonalCoords();
			}
			
			if(row + 1 != TILES_PER_ROW.length) {
				if(TILES_PER_ROW[row] > TILES_PER_ROW[row+1]) {
					startHexX --;
				} else {
					startHexY ++;
				}
			} else {
				startHexX --;
			}
			
			vIndex ++;
		}
		
		
		
		// Positioning all the tiles
		// First storing all the tiles in a clock type of order
		int[] outerclock = new int[3];
		for(int l = 0; l < outerclock.length; l ++) {
			for(int r = 0; r < TILES_PER_ROW.length -1; r ++) {
				int bRad = boardRadius + 1;
				for(int t = 0; t < bRad; t ++) {
					
				}
			}
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
	
	public ArrayList<Tile> getTiles() {
		return tiles;
	}
	
}
