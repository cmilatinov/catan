package settings;

import main.Engine;

import java.util.HashMap;

public class SettingsGame implements Settings{
    private int boardRadius;
    private GameModes gameMode;

    private SettingsManager manager;

    private enum GameModes {
        VANILLA
    }

    public SettingsGame(SettingsManager manager) {
        this.manager = manager;
    }

    @Override
    public void setProperty(String key, String value) throws Exception {
        switch(key) {
            case "board_radius" -> setBoardRadius(value);
            case "mode" -> setGameMode(value);
            default -> throw new Exception("Key does not exist in the scope of GameSettings.");
        }
    }

    @Override
    public void update(String key, String value) {
        manager.updateProperty(SettingsPrefix.GAME + "." + key, value);
    }

    private void setBoardRadius(String value) {
        boardRadius = Integer.parseInt(value);
        Engine.log("GAME SETTINGS : Board radius has been set to " + boardRadius);
    }

    public void setBoardRadius(int radius) {
        boardRadius = radius;
        update("board_radius", Integer.toString(radius));
    }

    private void setGameMode(String value) {
        gameMode = GameModes.values()[Integer.parseInt(value)];
        Engine.log("GAME SETTINGS : Game mode has been set to " +
                switch(gameMode) {
                    case VANILLA -> "vanilla";
                } + " mode");
    }

    public int getBoardRadius() {
        return boardRadius;
    }

    public GameModes getGameMode() {
        return gameMode;
    }
}
