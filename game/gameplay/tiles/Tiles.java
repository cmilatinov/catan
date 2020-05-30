package gameplay.tiles;

import entities.Building;
import entities.Entity;
import entities.EntityStatic;
import main.Scene;
import org.joml.Vector2f;
import org.joml.Vector3f;
import resources.GameResources;
import resources.Resource;

import java.util.*;

import static gameplay.tiles.TileTypes.*;

public class Tiles {

    private ArrayList<Tile> tiles;
    private ArrayList<Vertex> vertices;

    private int desertIndex = -1;

    private int boardRadius;

    private Map<TileTypes, Integer> tileConfiguration = new HashMap<TileTypes, Integer>();
    private ArrayList<Integer> tokenConfig = new ArrayList<Integer>();

    private int SHEEP_COUNT = 4;
    private int WHEAT_COUNT = 4;
    private int FOREST_COUNT = 4;
    private int STONE_COUNT = 3;
    private int BRICK_COUNT = 3;
    private int DESERT_COUNT = 1;

    private final int HEX_TILE = 6;

    public Tiles(int boardRadius, int sheepCount, int wheatCount, int forestCount, int stoneCount, int brickCount, int desertCount) {
        this(boardRadius);

        SHEEP_COUNT = sheepCount;
        WHEAT_COUNT = wheatCount;
        FOREST_COUNT = forestCount;
        STONE_COUNT = stoneCount;
        BRICK_COUNT = brickCount;
        DESERT_COUNT = desertCount;
    }

    public Tiles(int boardRadius) {
        this.boardRadius = boardRadius;
    }

    /**
     * Method generating the tiles and assigning the vertices to each tile
     */
    public void generateMap() {
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
        vertices = new ArrayList<Vertex>();

        // Generates the appropriate amount of types for the tiles
        Iterator tileIterator = tileConfiguration.entrySet().iterator();

        while(tileIterator.hasNext()) {
            Map.Entry mapElement = (Map.Entry)tileIterator.next();
            for(int i = 0; i < (int)mapElement.getValue(); i ++) {
                tiles.add(new Tile
                        (GameResources.get(Resource.getTileModel((TileTypes) mapElement.getKey()))
                                , (TileTypes) mapElement.getKey()));
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

        positionTiles();
        positionVertices();
    }

    public void positionVertices() {
        for(Tile t : tiles) {
            float z, x, newX, newZ;
            double initialAngle = Math.PI / 6;
            Vertex newVertex = null;
            boolean exists;

            for(int i = 0; i < HEX_TILE; i ++) {
                x = (float) Math.cos(initialAngle - (i * Math.PI/3));
                z = (float) Math.sin(initialAngle - (i * Math.PI/3));

                newX = x + t.getPositionX();
                newZ = z + t.getPositionZ();
                exists = false;

                for(Vertex v : vertices)
                    if(Math.abs(newX - v.getPositionX()) < 0.1 && Math.abs(newZ - v.getPositionZ()) < 0.1 ) {
                        newVertex = v;
                        exists = true;
                    }

                if(!exists) {
                    newVertex = new Vertex(GameResources.get(Resource.MODEL_TILE_BRICK));
                    newVertex.setPosition(new Vector3f(newX, 0.1f, newZ));
                    vertices.add(newVertex);
                }

                t.addVertex(newVertex);
            }
        }
    }

    public void positionTiles() {
        float [] hexCoords = new float [3];
        int zeroIndex = 1;
        int hIndex = 2;
        int unitIncrement = 0;

        int tIndex = 0;

        for(int i = boardRadius - 1; i > 0; i --) {
            hexCoords[0] = i;
            hexCoords[1] = 0;
            hexCoords[2] = -i;

            for(int t = 0; t < i * HEX_TILE; t ++) {
                tiles.get(tIndex).setHexCoords(new Vector2f(hexCoords[0], hexCoords[2]));
                tiles.get(tIndex).scale(0.996f);

                if(tiles.get(tIndex).getType() == DESERT)
                    desertIndex = tIndex;

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

    public Tile getDesertTile() {
        if(desertIndex == -1)
            return null;
        return tiles.get(desertIndex);
    }

    /**
     * Method to register the tile entities into a given scene.
     * @param scene - The scene you want to register the tiles into
     */
    public void registerTiles(Scene scene) {
        for (Tile t : tiles) {
            scene.register(t);
        }
    }

    public void registerBoard(Scene scene) {
        for(Vertex v : vertices)
            scene.register(v.scale(0.3f));

        for (Tile t : tiles) {
            scene.register(t);
            if(t.getToken() != null)
                scene.register(t.getToken());
        }

    }

    public void insertBuilding(int index, Scene scene) {
        vertices.get(index).setBuilding(new Building(scene));
    }

    /**
     * This method checks if a given angle is one of the possible angles in a hex, meaning Pi*2/6
     *
     * @param theta The angle to check
     * @return True if it's one of the 6 angles, otherwise, False
     */
    private boolean isAngleInHex(float theta) {
        float alpha = 0;
        float alphaIncrement = (float) 360 / (HEX_TILE);
        for(int i = 0; i <=HEX_TILE; i ++) {
            if(alpha == theta)
                return true;
            alpha += alphaIncrement;
        }
        return false;
    }

}
