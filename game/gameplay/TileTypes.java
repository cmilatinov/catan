package gameplay;

import java.util.Random;

public enum TileTypes {
	SHEEP,
	STONE,
	BRICK,
	WHEAT,
	FOREST,
	DESERT;
	
	public static TileTypes getRandomType() {
        Random random = new Random();
        return values()[random.nextInt(values().length)];
    }
}
