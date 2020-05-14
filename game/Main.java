import display.Window;
import entities.Entity;
import entities.EntityStatic;
import gameplay.PlayerHand;
import gui.GUI;
import lights.Light;
import main.Engine;
import main.GameController;
import objects.TexturedMesh;
import org.joml.Vector3f;
import resources.GameResources;
import resources.Resource;
import ui.*;
import ui.constraints.AspectConstraint;
import ui.constraints.CenterConstraint;
import ui.constraints.PixelConstraint;
import ui.constraints.RelativeConstraint;

public class Main {
    public static void main(String[] args) {
        var engine = new Engine();
        UIManager uiManager = engine.getUiManager();
        GameController controller = engine.getController();
        Window window = engine.getWindow();

        test(controller, uiManager, window);

        engine.run();
    }

    // Move This out later
    protected static void test(GameController controller, UIManager uiManager, Window window) {
        // Skybox
        controller.setSkyboxTexture(GameResources.get(Resource.TEXTURE_SKYBOX));

        // Cards
        GUI[] cards = new GUI[10];
        for(int i = 0; i < cards.length; i++) {
            int r = (int) Math.floor(Math.random() * 5);
            switch (r) {
                case 0 -> cards[i] = new GUI(GameResources.get(Resource.TEXTURE_CARD_BRICK));
                case 1 -> cards[i] = new GUI(GameResources.get(Resource.TEXTURE_CARD_STONE));
                case 2 -> cards[i] = new GUI(GameResources.get(Resource.TEXTURE_CARD_SHEEP));
                case 3 -> cards[i] = new GUI(GameResources.get(Resource.TEXTURE_CARD_FOREST));
                case 4 -> cards[i] = new GUI(GameResources.get(Resource.TEXTURE_CARD_WHEAT));
            }
            cards[i].setSize(150);
            controller.registerGUI(cards[i]);
        }
        PlayerHand hand = new PlayerHand(window);
        for(GUI card : cards)
            hand.addCard(card);
        hand.update();


        // UI
        UIQuad box = new UIQuad();
        box.setColor(UIColor.DARK_GRAY);
        box.setBorderRadius(20);
        UIConstraints constraints = new UIConstraints()
                .setX(new PixelConstraint(20, UIDimensions.DIRECTION_LEFT))
                .setY(new PixelConstraint(20, UIDimensions.DIRECTION_TOP))
                .setWidth(new RelativeConstraint(0.15f))
                .setHeight(new AspectConstraint(1));
        uiManager.getContainer().add(box, constraints);

        UIQuad box2 = new UIQuad();
        box2.setColor(UIColor.RED);
        UIConstraints constraints2 = new UIConstraints()
                .setX(new CenterConstraint())
                .setY(new CenterConstraint())
                .setWidth(new PixelConstraint(30))
                .setHeight(new PixelConstraint(30));
        box.add(box2, constraints2);

        // Objects
        TexturedMesh blueRoad = new TexturedMesh(GameResources.get(Resource.MESH_ROAD), GameResources.get(Resource.TEXTURE_COLOR_BLUE));
        TexturedMesh blueSettlement = new TexturedMesh(GameResources.get(Resource.MESH_SETTLEMENT), GameResources.get(Resource.TEXTURE_COLOR_BLUE));
        TexturedMesh blueCity = new TexturedMesh(GameResources.get(Resource.MESH_CITY), GameResources.get(Resource.TEXTURE_COLOR_BLUE));
        TexturedMesh blueRobber = new TexturedMesh(GameResources.get(Resource.MESH_ROBBER), GameResources.get(Resource.TEXTURE_COLOR_BLUE));

        Entity road = new EntityStatic(blueRoad).scale(0.45f).rotate(new Vector3f(0, 90, 0)).translate(new Vector3f(0, 0, -3.5f));
        Entity settlement = new EntityStatic(blueSettlement).scale(0.040f).translate(new Vector3f(0, 0, -3));
        Entity city = new EntityStatic(blueCity).scale(0.045f).translate(new Vector3f(0, 0, -4));
        Entity robber = (new Entity(blueRobber) {
            public void update(double delta) {
                rotate(new Vector3f(0, 200 * (float)delta, 0));
            }
            public boolean shouldUpdate() {
                return true;
            }
            public boolean shouldRender() {
                return true;
            }
        }).scale(0.01f);
        Entity table = new EntityStatic(GameResources.get(Resource.MODEL_TABLE)).scale(10).translate(new Vector3f(0, -0.07f, 0));

        controller.registerEntity(road);
        controller.registerEntity(settlement);
        controller.registerEntity(city);
        controller.registerEntity(robber);
        controller.registerEntity(table);

        Light sun = new Light(new Vector3f(0.6f, 0.6f, 0.6f), new Vector3f(500, 1000, 500));
        Light sun2 = new Light(new Vector3f(0.6f, 0.6f, 0.6f), new Vector3f(-500, 1000, 500));

        controller.registerLight(sun);
        controller.registerLight(sun2);

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
            controller.registerEntity(tiles[i]);
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
}
