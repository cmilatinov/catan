package board;

import board.nodes.Side;
import board.nodes.Vertex;
import entities.EntityToggleable;
import entities.Player;
import objects.TexturedMesh;
import physics.colliders.SphereCollider;
import resources.GameResources;
import resources.Resource;

import java.util.ArrayList;

public abstract class Node extends EntityToggleable implements SphereCollider {

    // List of nodes that are adjacent to the current node
    private final ArrayList<Node> nearbyNodes;

    // Current building
    private Building building;

    // Player owning the node
    private Player owner;

    // Boolean to determine whether or not the node has links to the neighboring nodes
    private boolean linked;

    /**
     * Node constructor.
     * @param model - Textured mesh for the Node.
     */
    public Node(TexturedMesh model) {
        super(model);

        nearbyNodes = new ArrayList<Node>();
    }

    /**
     * Method to create a new node.
     * @param type - the type of the new Node (SIDE or VERTEX).
     * @return new Node of given type.
     */
    public static Node createNode(NodeType type) {
        return switch (type) {
            case SIDE -> new Side(GameResources.get(Resource.MODEL_TILE_BRICK));
            case VERTEX -> new Vertex(GameResources.get(Resource.MODEL_TILE_FOREST));
        };
    }

    public ArrayList<Node> getNearbyNodes() {
        return nearbyNodes;
    }

    public int getLinkSize() {
        return nearbyNodes.size();
    }

    /**
     * Getter that returns whether or not the node is linked to the data structure.
     * @return True if node is linked.
     */
    public boolean isLinked(){ return linked; }

    /**
     * Adds a neighboring node.
     * @param node - Node to be added to the neighboring nodes list.
     */
    public void addNode(Node node) {
        linked = true;
        nearbyNodes.add(node);
    }

    /**
     * Method to check if the node has a building set.
     * @return True if there's no building.
     */
    public boolean isNodeEmpty() {
        return null == building;
    }

    /**
     * Setter for the owner of the node.
     * @param p - new owner of the node;
     */
    public void setOwner(Player p) {
        this.owner = p;
    }

    /**
     * Getter for the owner of the node.
     * @return player object of the owner of the node.
     */
    public Player getOwner() {
        return owner;
    }

    public void setBuilding(Building b) {
        this.building = b;
    }

    public Building getBuilding() {
        return building;
    }

    /**
     * Abstract method to handle the event of clicking on a node.
     * @param owner - owner of the node
     */
    public abstract void settle(Player owner);

    @Override
    public void destroy() {

    }
}
