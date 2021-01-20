package entities.board;

import entities.Entity;
import entities.EntityStatic;
import entities.board.nodes.Node;
import entities.board.nodes.Vertex;
import objects.TexturedMesh;
import org.joml.Vector2f;
import org.joml.Vector3f;
import physics.colliders.SphereCollider;
import resources.GameResources;
import resources.Resource;

import java.util.ArrayList;

public class Tile extends Entity implements SphereCollider {
    // Tile types
    public static final int WOOD = 0;
    public static final int BRICK = 1;
    public static final int SHEEP = 2;
    public static final int WHEAT = 3;
    public static final int STONE = 4;

    public static final int DESERT = 5;

    // Type of tile
    private final int type;

    // Number that must be rolled for the given tile
    private int value;

    // Whether the robber is no the tile or not
    private boolean isBlocked;

    // Coordinates of the tile in hex coordinates
    private Vector2f hexCoords;

    // Entity to display the tile value
    private EntityStatic token;

    // List of nodes that are affected by this tile
    private ArrayList<Vertex> nodes = new ArrayList<Vertex>();

    /**
     * Constructor to create a tile
     * @param model - TexturedMesh to be rendered
     * @param type - Type of tile
     */
    public Tile(TexturedMesh model, int type) { this(model, type, -1); }

    /**
     * Constructor to create a tile
     * @param model - TexturedMesh that must be rendered
     * @param type - Type of tile
     * @param value - Number that must be rolled to get the tile resources
     */
    public Tile(TexturedMesh model, int type, int value) {
        this(model, type, value, false);
    }

    /**
     * Constructor to create a tile
     * @param model - TexturedMesh that must be rendered
     * @param type - Type of tile
     * @param value - Number that must be rolled to get the tile resources
     * @param isBlocked - Boolean to tell whether or not you can get the resources
     */
    public Tile(TexturedMesh model, int type, int value, boolean isBlocked) {
        super(model);

        this.type = type;
        this.value = value;
        this.isBlocked = isBlocked;
    }

    @Override
    public boolean shouldRender() {
        return true;
    }

    @Override
    public void destroy() {
        this.getModel().destroy();
    }

    @Override
    public float getRadius() {
        return 0.5f;
    }

    // Setters
    public void setIsBlocked(boolean isBlocked) {
        this.isBlocked = isBlocked;
    }
    public void setHexCoords(Vector2f vec) {
        this.hexCoords = vec;
        calculatePixelCoords();
    }
    public void setValue(int value) {
        this.value = value;
        token = new EntityStatic(GameResources.get(Resource.getToken(value)));
    }
    public void addNode(Vertex node){ nodes.add(node); }

    private void calculatePixelCoords() {
        float z = (3 * hexCoords.y) / 2;
        float x = (float) (Math.sqrt(3) * (hexCoords.y/2 + hexCoords.x));

        setPosition(new Vector3f(x, 0, z));

        if(type != DESERT) {
            token.setPosition(getPosition());
            token.translate(new Vector3f(0, 0.1f, 0));
            token.scale(0.2f);
        }
    }

    // Getters
    public int getValue() {
        return value;
    }
    public int getType() {
        return type;
    }
    public Entity getToken() {
        return token;
    }
    public boolean isBlocked() {
        return isBlocked;
    }
    public Vector2f getHexCoords() { return hexCoords; }
    public ArrayList<Vertex> getNodes() { return nodes; }
}
