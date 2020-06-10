package gameplay;

import java.util.Random;

public enum ResourceType {
	SHEEP,
	STONE,
	BRICK,
	WHEAT,
	FOREST,
	DESERT,

	KNIGHT,
	MONOPOLY,
	VICTORY_POINT;
	
	public static ResourceType getRandomType() {
        Random random = new Random();
        return values()[random.nextInt(values().length)];
    }
}
