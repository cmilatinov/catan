package scripts;

import entities.*;
import gameplay.TileTypes;
import lights.Light;
import main.Scene;
import objects.GameScript;
import org.joml.Vector2f;
import org.joml.Vector3f;
import resources.GameResources;
import resources.Resource;

import java.util.*;
import java.util.stream.Collectors;

import static gameplay.TileTypes.*;

public class Tiles extends GameScript{

    private ArrayList<Tile> tiles;
    public ArrayList<Vertex> vertices;
    private ArrayList<Side> sides;

    private Entity robber;

    private int desertIndex = -1;

    private final int BOARD_RADIUS;

    private Map<TileTypes, Integer> tileConfiguration = new HashMap<TileTypes, Integer>();
    private ArrayList<Integer> tokenConfig = new ArrayList<Integer>();

    private int SHEEP_COUNT = 4;
    private int WHEAT_COUNT = 4;
    private int FOREST_COUNT = 4;
    private int STONE_COUNT = 3;
    private int BRICK_COUNT = 3;
    private int DESERT_COUNT = 1;

    private final int SIDES = 6;

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
        BOARD_RADIUS = boardRadius;
    }

    public List<Tile> getTiles(int val) {
        return tiles.stream().filter(t -> t.getValue() == val).collect(Collectors.toList());
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
        sides = new ArrayList<Side>();

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

        generateTiles();
        generateGrid();
    }

    public void generateGrid() {
        for(Tile t : tiles) {
            float vZ, vX, sX, sZ;
            double initialVAngle = Math.PI / 6;
            double initialSAngle = 0;

            Vertex newVertex = null;
            Side newSide = null;

            boolean vExists, sExists;

            for(int i = 0; i < SIDES; i ++) {
                vX = (float) Math.cos(initialVAngle - (i * Math.PI/3)) + t.getPositionX();
                vZ = (float) Math.sin(initialVAngle - (i * Math.PI/3)) + t.getPositionZ();

                sX = (float) (Math.cos(initialSAngle - (i * Math.PI/3)) * 0.866) + t.getPositionX();
                sZ = (float) (Math.sin(initialSAngle - (i * Math.PI/3)) * 0.866) + t.getPositionZ();

                vExists = false;
                sExists = false;

                for(Side s : sides)
                    if(Math.abs(sX - s.getPositionX()) < 0.01 && Math.abs(sZ - s.getPositionZ()) < 0.01 ) {
                        newSide = s;
                        sExists = true;
                    }

                for(Vertex v : vertices)
                    if(Math.abs(vX - v.getPositionX()) < 0.1 && Math.abs(vZ - v.getPositionZ()) < 0.1 ) {
                        newVertex = v;
                        vExists = true;
                    }

                if(!vExists) {
                    newVertex = new Vertex(GameResources.get(Resource.MODEL_TILE_BRICK));
                    newVertex.setPosition(new Vector3f(vX, 0.1f, vZ));
                    vertices.add(newVertex);
                }

                if(!sExists) {
                    newSide = new Side(GameResources.get(Resource.MODEL_TILE_FOREST));
                    newSide.setPosition(new Vector3f(sX, 0.1f, sZ));
                    sides.add(newSide);
                }

                t.addVertex(newVertex);
            }

            Vector3f firstV, secondV, vMatch = new Vector3f();

            for(Side s : sides) {
                firstV = null;
                secondV = null;
                for(Vertex v : vertices) {
                    s.getPosition().sub(v.getPosition(), vMatch);
                    if(vMatch.length() - 0.5 < 0.5) {
                        if(firstV == null){
                            firstV = v.getPosition();
                            s.addVertex(v);
                        }
                        else {
                            secondV = v.getPosition();
                            s.addVertex(v);
                            break;
                        }
                    }
                }
                firstV.sub(secondV);

                s.setRotation(new Vector3f(0, -(float)Math.toDegrees(Math.atan(firstV.z / firstV.x)), 0));
            }
        }
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

    /**
     * Method that returns the tile of type desert
     * @return - Tile of type desert
     */
    public Tile getDesertTile() {
        if(desertIndex == -1)
            return null;
        return tiles.get(desertIndex);
    }

    public Entity getRobber(){
        return robber;
    }


    @Override
    public void initialize() {
        for(Vertex v : vertices)
            getScene().register(v.scale(0.3f));

        for (Tile t : tiles) {
            getScene().register(t);
            if(t.getToken() != null)
                getScene().register(t.getToken());
        }

        for(Side s : sides) {
            getScene().register(s.scale(0.1f));
        }

        robber = Robber.create(Resource.TEXTURE_COLOR_BLUE).scale(0.01f);
        robber.setPosition(getDesertTile().getPosition());
        getDesertTile().setEmbargoed(true);
        getScene().register(robber);
    }
    @Override
    public void destroy() {

    }
}
