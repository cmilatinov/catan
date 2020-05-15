package scripts;

import entities.*;
import lights.Light;
import objects.GameObject;
import org.joml.Vector3f;
import resources.GameResources;
import resources.Resource;

public class Board extends GameObject {
    Entity robber;

    @Override
    public void stop() {

    }

    @Override
    public void start() {

    }

    @Override
    public void initialize() {
        Entity table = Table.create()
                .scale(10)
                .translate(new Vector3f(0, -0.07f, 0));
        getScene().register(table);

        robber = Robber.create(Resource.TEXTURE_COLOR_BLUE).scale(0.01f);
        getScene().register(robber);

        //<editor-fold defaultstate="collapsed" desc="Model Creations">
        Entity road = Road
                .create(Resource.TEXTURE_COLOR_BLUE)
                .scale(0.45f)
                .rotate(new Vector3f(0, 90, 0))
                .translate(new Vector3f(0, 0, -3.5f));
        getScene().register(road);

        Entity settlement = Settlement
                .create(Resource.TEXTURE_COLOR_BLUE)
                .scale(0.040f)
                .translate(new Vector3f(0, 0, -3));
        getScene().register(settlement);

        Entity city = City
                .create(Resource.TEXTURE_COLOR_BLUE)
                .scale(0.045f)
                .translate(new Vector3f(0, 0, -4));
        getScene().register(city);

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
        //</editor-fold>

    }

    @Override
    public void update(double delta) {
        robber.rotate((new Vector3f(0, 200 * (float)delta, 0)));
    }

    @Override
    public void destroy() {

    }
}
