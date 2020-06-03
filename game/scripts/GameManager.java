package scripts;

import entities.Player;
import objects.GameScript;
import resources.Resource;

import java.util.ArrayList;

public class GameManager extends GameScript {
    private int turn;
    private ArrayList<Player> players;
    private int playerCount;

    public GameManager() {
        for(int i = 0; i < 4; i ++)
            players.add(new Player());
        players.get(0).setColor(Resource.TEXTURE_COLOR_BLUE);
        players.get(1).setColor(Resource.TEXTURE_COLOR_GREEN);
        players.get(2).setColor(Resource.TEXTURE_COLOR_PURPLE);
        players.get(3).setColor(Resource.TEXTURE_COLOR_RED);

        turn = 0;
    }

    @Override
    public void stop() {

    }

    @Override
    public void start() {

    }

    @Override
    public void initialize() {

    }

    @Override
    public void update(double delta) {

    }

    @Override
    public void destroy() {

    }
}
