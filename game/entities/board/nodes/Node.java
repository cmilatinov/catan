package entities.board.nodes;

import entities.EntityToggleable;
import entities.Player;
import entities.board.Piece;
import objects.TexturedMesh;
import physics.colliders.SphereCollider;
import resources.GameResources;
import resources.Resource;

import java.util.ArrayList;

public abstract class Node extends EntityToggleable implements SphereCollider {
    // Node Types
    public static final int VERTEX = 0;
    public static final int SIDE = 1;

    // Type of the Node
    private final int type;

    // Player object of the user that owns the Node
    private Player owner = null;

    // Piece placed on the Node
    private Piece piece;

    // List of nearby Nodes.
    private final ArrayList<Node> nearbyNodes = new ArrayList<Node>();

    /**
     * Constructor to create a Node.
     *
     * @param model - TexturedMesh to render
     * @param type - Type of Node being created
     */
    public Node(TexturedMesh model, int type) {
        super(model);
        this.type = type;
    }

    /**
     * Adds a neighboring Node.
     * @param node - Node to be added to the neighboring nodes list.
     */
    public void addNode(Node node) {
        nearbyNodes.add(node);
    }

    /**
     * Method to settle on a Node.
     * @param owner - Player who will own the Node.
     */
    public abstract void settle(Player owner);

    // Setters
    public void setOwner(Player owner) { this.owner = owner; }
    public void setPiece(Piece piece) { this.piece = piece; }

    // Getters
    public ArrayList<Node> getNearbyNodes() { return nearbyNodes; }
    public boolean isEmpty() { return owner == null; }
    public Piece getPiece() { return piece; }
    public int getType(){ return type; }
    public Player getOwner() { return owner; }
}
