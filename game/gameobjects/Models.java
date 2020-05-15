package gameobjects;

import entities.DynamicEntity;
import entities.Entity;
import entities.EntityStatic;
import lights.Light;
import objects.GameObject;
import objects.TexturedMesh;
import org.joml.Vector3f;
import resources.GameResources;
import resources.Resource;

public class Models extends GameObject {
    @Override
    public void stop() {

    }

    @Override
    public void start() {

    }

    @Override
    public void initialize() {
        TexturedMesh blueRoad = new TexturedMesh(GameResources.get(Resource.MESH_ROAD), GameResources.get(Resource.TEXTURE_COLOR_BLUE));
        TexturedMesh blueSettlement = new TexturedMesh(GameResources.get(Resource.MESH_SETTLEMENT), GameResources.get(Resource.TEXTURE_COLOR_BLUE));
        TexturedMesh blueCity = new TexturedMesh(GameResources.get(Resource.MESH_CITY), GameResources.get(Resource.TEXTURE_COLOR_BLUE));
        TexturedMesh blueRobber = new TexturedMesh(GameResources.get(Resource.MESH_ROBBER), GameResources.get(Resource.TEXTURE_COLOR_BLUE));

        Entity road = new EntityStatic(blueRoad).scale(0.45f).rotate(new Vector3f(0, 90, 0)).translate(new Vector3f(0, 0, -3.5f));
        Entity settlement = new EntityStatic(blueSettlement).scale(0.040f).translate(new Vector3f(0, 0, -3));
        Entity city = new EntityStatic(blueCity).scale(0.045f).translate(new Vector3f(0, 0, -4));
        Entity robber = (new DynamicEntity(blueRobber) {
            public void update(double delta) {
                rotate(new Vector3f(0, 200 * (float)delta, 0));
            }
        }).scale(0.01f);

        Entity table = new EntityStatic(GameResources.get(Resource.MODEL_TABLE)).scale(10).translate(new Vector3f(0, -0.07f, 0));

        getScene().register(road);
        getScene().register(settlement);
        getScene().register(city);
        getScene().register(robber);
        getScene().register(table);

        Light sun = new Light(new Vector3f(0.6f, 0.6f, 0.6f), new Vector3f(500, 1000, 500));
        Light sun2 = new Light(new Vector3f(0.6f, 0.6f, 0.6f), new Vector3f(-500, 1000, 500));

        getScene().register(sun);
        getScene().register(sun2);

        // Tiles
        Entity[] tiles = new Entity[19];
        float scale = 0.996f;
        for(int i = 0; i < tiles.length; i++) {
            int r = (int) Math.floor(Math.random() * 5);
            switch (r) {
                case 0 -> tiles[i] = new EntityStatic(GameResources.get(Resource.MODEL_TILE_BRICK)).scale(scale);
                case 1 -> tiles[i] = new EntityStatic(GameResources.get(Resource.MODEL_TILE_FOREST)).scale(scale);
                case 2 -> tiles[i] = new EntityStatic(GameResources.get(Resource.MODEL_TILE_SHEEP)).scale(scale);
                case 3 -> tiles[i] = new EntityStatic(GameResources.get(Resource.MODEL_TILE_STONE)).scale(scale);
                case 4 -> tiles[i] = new EntityStatic(GameResources.get(Resource.MODEL_TILE_WHEAT)).scale(scale);
            }
            getScene().register(tiles[i]);
        }
        tiles[0].translate(new Vector3f(-1.732f, 0, 3));
        tiles[1].translate(new Vector3f(0, 0, 3));
        tiles[2].translate(new Vector3f(1.732f, 0, 3));

        tiles[3].translate(new Vector3f(-2.598f, 0, 1.5f));
        tiles[4].translate(new Vector3f(-0.866f, 0, 1.5f));
        tiles[5].translate(new Vector3f(0.866f, 0, 1.5f));
        tiles[6].translate(new Vector3f(2.598f, 0, 1.5f));

        tiles[7].translate(new Vector3f(-3.464f, 0, 0));
        tiles[8].translate(new Vector3f(-1.732f, 0, 0));
        tiles[9].translate(new Vector3f(0, 0, 0));
        tiles[10].translate(new Vector3f(1.732f, 0, 0));
        tiles[11].translate(new Vector3f(3.464f, 0, 0));

        tiles[12].translate(new Vector3f(-2.598f, 0, -1.5f));
        tiles[13].translate(new Vector3f(-0.866f, 0, -1.5f));
        tiles[14].translate(new Vector3f(0.866f, 0, -1.5f));
        tiles[15].translate(new Vector3f(2.598f, 0, -1.5f));

        tiles[16].translate(new Vector3f(-1.732f, 0, -3));
        tiles[17].translate(new Vector3f(0, 0, -3));
        tiles[18].translate(new Vector3f(1.732f, 0, -3));
    }

    @Override
    public void update() {

    }

    @Override
    public void destroy() {

    }
}
