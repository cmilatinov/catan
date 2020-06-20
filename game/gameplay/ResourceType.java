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

	public static boolean isDevCard(ResourceType resource) {
		return switch(resource) {
			case SHEEP, STONE, BRICK, WHEAT, FOREST, DESERT -> false;
			case KNIGHT, MONOPOLY, ROAD_BUILDING, YEAR_OF_PLENTY, VICTORY_POINT -> true;
		};
	}

	public static boolean isResource(ResourceType resource) {
		return switch(resource) {
			case SHEEP, STONE, BRICK, WHEAT, FOREST -> true;
			case KNIGHT, MONOPOLY, ROAD_BUILDING, YEAR_OF_PLENTY, VICTORY_POINT, DESERT -> false;
		};
	}
}
