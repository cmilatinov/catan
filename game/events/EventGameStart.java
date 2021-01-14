package events;

import entities.Player;
import network.annotations.SerializableField;
import network.events.NetworkEvent;

public class EventGameStart extends NetworkEvent {

    @SerializableField
    private int boardSize;

    @SerializableField
    private long boardSeed;

    @SerializableField
    private Player[] playerList;

    public EventGameStart() {
        boardSize = 3;
        boardSeed = 0;
        playerList = new Player[0];
    }

    public int getBoardSize() {
        return boardSize;
    }

    public long getBoardSeed() {
        return boardSeed;
    }

    public Player[] getPlayerList() {
        return playerList;
    }

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
    }

    public void setBoardSeed(long boardSeed) {
        this.boardSeed = boardSeed;
    }

    public void setPlayerList(Player[] playerList) {
        this.playerList = playerList;
    }

}
