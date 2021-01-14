package entities.board;

import entities.Entity;
import objects.TexturedMesh;
import resources.GameResources;
import resources.Resource;

public class Piece extends Entity {
    // Global piece types
    public static final int ROAD = 0;
    public static final int SETTLEMENT = 1;
    public static final int CITY = 2;

    // Type of piece
    public final int type;

    /**
     * Constructor to create a new piece.
     * @param model - TexturedMesh to be rendered
     * @param type - Type of piece being created
     */
    private Piece(TexturedMesh model, int type) {
        super(model);
        this.type = type;
    }

    /**
     * Method to create a piece given a type and a color.
     * @param type - Type of piece being created
     * @param color - Color for the piece being created
     * @return Object type Piece containing of the type provided and with the given color
     */
    public static Piece create(int type, Resource color) {
        return switch (type) {
            case SETTLEMENT -> new Piece(new TexturedMesh(GameResources.get(Resource.MESH_SETTLEMENT), GameResources.get(color)), SETTLEMENT);
            case CITY -> new Piece(new TexturedMesh(GameResources.get(Resource.MESH_CITY), GameResources.get(color)), CITY);
            case ROAD -> new Piece(new TexturedMesh(GameResources.get(Resource.MESH_ROAD), GameResources.get(color)), ROAD);
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
    }

    /**
     * Method to return the price for a given piece type
     * @param type - Type of piece
     * @return An array containing the cost for each piece
     */
    public static int[] getPrice(int type) {
        return switch (type) {
            case SETTLEMENT -> new int[]{1, 1, 1, 1, 0}; // 1 WOOD, 1 BRICK, 1 SHEEP, 1 WHEAT, 0 ROCK
            case CITY -> new int[]{0, 0, 0, 2, 3};
            case ROAD -> new int[]{1, 1, 0, 0, 0};
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
    }

    /**
     * Returns the value that you get from your piece.
     * @param type - type of piece
     * @return 1 if Settlement otherwise 2
     */
    public static int getPieceValue(int type) {
        return switch(type) {
            case SETTLEMENT -> 1;
            case CITY -> 2;
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
    }

    @Override
    public boolean shouldRender() {
        return true;
    }

    @Override
    public void destroy() {
        this.getModel().destroy();
    }
}
