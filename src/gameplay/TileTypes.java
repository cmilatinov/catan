package gameplay;

import java.util.Random;

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
