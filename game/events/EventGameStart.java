package events;

import entities.Player;
import network.events.NetworkEvent;

import java.nio.ByteBuffer;

public class EventGameStart extends NetworkEvent {

    private int boardSize;
    private long boardSeed;
    private Player[] playerList;

    public EventGameStart() {
        super(EventType.GAME_START);
        boardSize = 3;
        boardSeed = 0;
        playerList = new Player[0];
    }

    public EventGameStart(byte[] data) {
        super(EventType.GAME_START);
        ByteBuffer buffer = ByteBuffer.wrap(data);
        this.boardSize = buffer.getInt();
        this.boardSeed = buffer.getLong();
        this.playerList = new Player[buffer.getInt()];
        for (int i = 0; i < playerList.length; i++)
            playerList[i] = new Player(buffer);
    }

    public byte[] serialize() {
        ByteBuffer data = ByteBuffer.allocate(HEADER_SIZE + 2 * Integer.BYTES + Long.BYTES + playerList.length * Player.BYTES);
        writeHeader(data);
        data.putInt(boardSize)
            .putLong(boardSeed)
            .putInt(playerList.length);
        for (Player player : playerList)
            data.put(player.serialize());
        return data.array();
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
