package scripts;

import board.Node;
import board.NodeType;
import board.nodes.Side;
import entities.Entity;
import entities.Robber;
import entities.Tile;
import gameplay.ResourceType;
import objects.GameScript;
import org.joml.Vector2f;
import org.joml.Vector3f;
import resources.GameResources;
import resources.Resource;

import java.util.*;
import java.util.stream.Collectors;

import static gameplay.ResourceType.*;

public class Tiles extends GameScript{

    private ArrayList<Tile> tiles;
    private ArrayList<Node> nodes;

    private Entity robber;

    private final int BOARD_RADIUS;

    private Map<ResourceType, Integer> tileConfiguration = new HashMap<ResourceType, Integer>();
    private ArrayList<Integer> tokenConfig = new ArrayList<Integer>();

    private int SHEEP_COUNT = 4;
    private int WHEAT_COUNT = 4;
    private int FOREST_COUNT = 4;
    private int STONE_COUNT = 3;
    private int BRICK_COUNT = 3;
    private int DESERT_COUNT = 1;

    private final int SIDES = 6;

    public Tiles(int boardRadius) {
        BOARD_RADIUS = boardRadius;
    }

    public List<Tile> getTiles(int val) {
        return tiles.stream().filter(t -> t.getValue() == val && !t.isEmbargoed()).collect(Collectors.toList());
    }

    public void resetEmbargoedTile() {
        tiles.forEach(t -> {
            if(t.isEmbargoed())
                t.setEmbargoed(false);
        });
    }

    /**
     * Method generating the tiles and assigning the vertices to each tile
     */
    public void generateMap() {
        robber = Robber.create(Resource.TEXTURE_COLOR_BLUE).scale(0.01f);

        tokenConfig.add(1); //0 -> 2
        tokenConfig.add(2); //1 -> 3
        tokenConfig.add(2); //2 -> 4
        tokenConfig.add(2); //3 -> 5
        tokenConfig.add(2); //4 -> 6
        tokenConfig.add(0); //5 -> 7
        tokenConfig.add(2); //6 -> 8
        tokenConfig.add(2); //7 -> 9
        tokenConfig.add(2); //8 -> 10
        tokenConfig.add(2); //9 -> 11
        tokenConfig.add(1); //10 -> 12

        tileConfiguration.put(SHEEP, SHEEP_COUNT);
        tileConfiguration.put(WHEAT, WHEAT_COUNT);
        tileConfiguration.put(FOREST, FOREST_COUNT);
        tileConfiguration.put(STONE, STONE_COUNT);
        tileConfiguration.put(BRICK, BRICK_COUNT);
        tileConfiguration.put(DESERT, DESERT_COUNT);

        tiles = new ArrayList<Tile>();
        nodes = new ArrayList<Node>();

        // Generates the appropriate amount of types for the tiles
        Iterator tileIterator = tileConfiguration.entrySet().iterator();

        while(tileIterator.hasNext()) {
            Map.Entry mapElement = (Map.Entry)tileIterator.next();
            for(int i = 0; i < (int)mapElement.getValue(); i ++) {
                tiles.add(new Tile
                        (GameResources.get(Resource.getTileModel((ResourceType) mapElement.getKey()))
                                , (ResourceType) mapElement.getKey()));
            }
        }

        Collections.shuffle(tiles);

        int tIndex = 0;

        for(int t = 0; t < tokenConfig.size(); t ++) {
            for(int i = 0; i < tokenConfig.get(t); i ++) {
                while(tiles.get(tIndex).getType() == DESERT)
                    tIndex ++;
                tiles.get(tIndex ++).setValue(t + 2);
            }
        }

        Collections.shuffle(tiles);

        generateTiles();
        generateNodes();

        getDesertTile().setEmbargoed(true);
        robber.setPosition(getDesertTile().getPosition());
    }

    public void generateNodes() {
        float radius;
        double theta;
        boolean isSide;
        Node newNode;

        for(Tile t : tiles) {
            theta = 0;
            isSide = true;

            while(theta != 360) {
                radius = isSide ? 0.866f : 1.0f;

                float nodeX = (float) (Math.cos(Math.toRadians(theta)) * radius) + t.getPositionX();
                float nodeZ = (float) (Math.sin(Math.toRadians(theta)) * radius) + t.getPositionZ();

                newNode = nodes.stream()
                        .filter(node -> Math.abs(nodeX - node.getPositionX()) < 0.01 && Math.abs(nodeZ - node.getPositionZ()) < 0.01)
                        .reduce((o, n) -> n)
                        .orElse(null);

                if (null == newNode) {
                    if(isSide) {
                        newNode = Node.createNode(NodeType.SIDE);
                    } else {
                        newNode = Node.createNode(NodeType.VERTEX);
                        t.addVertex(newNode);
                    }

                    newNode.setPosition(new Vector3f(nodeX, 0.1f, nodeZ));
                    nodes.add(newNode);
                }

                theta += 30;
                isSide = !isSide;
            }
        }

        // TODO: rotate the sides properly
        for(Node currNode :  nodes) {
            for(Node node : nodes) {
                Vector3f result = new Vector3f();
                node.getPosition().sub(currNode.getPosition(), result);
                if(Math.abs(result.length() - 0.5) < 0.1) {
                    currNode.addNode(node);
                }
            }
            if(currNode instanceof Side) {
                Vector3f pos = new Vector3f(currNode.getNearbyNodes().get(0).getPosition());
                pos.sub(currNode.getNearbyNodes().get(1).getPosition());

                currNode.setRotation(new Vector3f(0, -(float)Math.toDegrees(Math.atan(pos.z / pos.x)), 0));
            }
        }
    }

    public List<Tile> getTilesNearVertex(Vector3f position) {
        return tiles.stream().filter(t -> {
            Vector3f result = new Vector3f();
            t.getPosition().sub(position, result);
            return result.length() - 1 < 0.1;
        }).collect(Collectors.toList());
    }

    public void generateTiles() {
        float [] hexCoords = new float [3];
        int zeroIndex = 1;
        int hIndex = 2;
        int unitIncrement = 0;

        int tIndex = 0;

        for(int i = BOARD_RADIUS - 1; i > 0; i --) {
            hexCoords[0] = i;
            hexCoords[1] = 0;
            hexCoords[2] = -i;

            for(int t = 0; t < i * SIDES; t ++) {
                tiles.get(tIndex).setHexCoords(new Vector2f(hexCoords[0], hexCoords[2]));
                tiles.get(tIndex).scale(0.996f);

                //update Hex coordinates and indices
                if(hexCoords[hIndex] == 0) {
                    hIndex = (hIndex + 1) % 3;
                    zeroIndex = (zeroIndex + 1) % 3;
                }

                if(hexCoords[hIndex] < 0)
                    unitIncrement = -1;
                else if(hexCoords[hIndex] > 0)
                    unitIncrement = 1;

                hexCoords[zeroIndex] += unitIncrement;
                hexCoords[hIndex] += (unitIncrement * -1);

                tIndex ++;
            }
        }

        tiles.get(tIndex).setHexCoords(new Vector2f(0, 0));
    }

    /**
     * Method that returns the tile of type desert
     * @return - Tile of type desert
     */
    public Tile getDesertTile() {
        return tiles.stream()
                .filter(t -> t.getType() == DESERT)
                .findFirst()
                .orElse(null);
    }

    public Entity getRobber(){
        return robber;
    }


    @Override
    public void initialize() {
        nodes.forEach(n -> getScene().register(n));

        tiles.stream()
                .peek(t -> getScene().register(t))
                .filter(t -> t.getToken() != null)
                .forEach(t -> getScene().register(t.getToken()));
        getScene().register(robber);
    }
    @Override
    public void destroy() {

    }
}
