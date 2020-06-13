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
	ROAD_BUILDING,
	YEAR_OF_PLENTY,
	VICTORY_POINT;
	
	public static ResourceType getRandomType() {
        Random random = new Random();
        return values()[random.nextInt(values().length)];
    }
}
