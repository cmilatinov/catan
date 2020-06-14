package events;

import entities.Player;
import network.events.NetworkEvent;

import java.util.ArrayList;
import java.util.List;

public class EventGameStart extends NetworkEvent {

    private int boardSize;
    private long boardSeed;
    private Player[] playerList;

    public EventGameStart() {
        super(EventType.GAME_START);
        boardSize = 3;
        boardSeed = 0;

    }

    public EventGameStart(byte[] data) {
        super(EventType.GAME_START);
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
