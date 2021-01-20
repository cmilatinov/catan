package gameplay.devcard;

public abstract class PlayableCard {
    public final int KNIGHT = 0;
    public final int ROAD_BUILDING = 0;
    public final int YEAR_OF_PLENTY = 0;
    public final int MONOPOLY = 0;
    public final int VICTORY_POINT = 0;

    private int type;

    public PlayableCard(int type) {

    }

    public abstract void activate();

    public int getType(){ return type; }
}
