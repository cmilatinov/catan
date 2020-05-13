package resources;

import gameplay.TileTypes;

public enum Resource {
	
	TEXTURE_TILE_BRICK,
	TEXTURE_TILE_DESERT,
	TEXTURE_TILE_FOREST,
	TEXTURE_TILE_SHEEP,
	TEXTURE_TILE_STONE,
	TEXTURE_TILE_WHEAT,
	
	
	TEXTURE_COLOR_BLUE,
	TEXTURE_COLOR_RED,
	TEXTURE_COLOR_YELLOW,
	TEXTURE_COLOR_GREEN,
	TEXTURE_COLOR_PURPLE,
	TEXTURE_COLOR_WHITE,
	
	
	MESH_TILE,
	MESH_ROAD,
	MESH_SETTLEMENT,
	MESH_CITY,
	MESH_ROBBER,
	
	
	MODEL_TILE_BRICK,
	MODEL_TILE_DESERT,
	MODEL_TILE_FOREST,
	MODEL_TILE_SHEEP,
	MODEL_TILE_STONE,
	MODEL_TILE_WHEAT;
	
	public static Resource getTileModel(TileTypes t) {
		Resource r = null;
		
		switch(t) {
		case BRICK:
			r = MODEL_TILE_BRICK;
			break;
		case DESERT:
			r = MODEL_TILE_DESERT;
			break;
		case FOREST:
			r = MODEL_TILE_FOREST;
			break;
		case STONE:
			r = MODEL_TILE_STONE;
			break;
		case WHEAT:
			r = MODEL_TILE_WHEAT;
			break;
		case SHEEP:
			r = MODEL_TILE_SHEEP;
			break;
		}
		
		return r;
	}
}
