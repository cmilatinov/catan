package settings;

import log.Logger;
import main.Engine;
import objects.GameScript;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.Pattern;

public class SettingsManager extends GameScript {

    // Path for the settings files.
    private final String PATH;

    // Properties object to load, manage and save data from xml files.
    private final Properties properties = new Properties();

    private final String GAME_PREFIX = "game.";
    private final String WINDOW_PREFIX = "window.";
    private final String AUDIO_PREFIX = "audio.";

    private final SettingsGame gameSettings = new SettingsGame();
    private final SettingsAudio audioSettings = new SettingsAudio();
    private final SettingsWindow windowSettings = new SettingsWindow();

    /**
     * Constructor for Settings object.
     * @param path - the path to the xml file
     */
    public SettingsManager(String path) {
        PATH = path;
    }

    /**
     * Method to load the settings from an xml file.
     */
    public void loadSettings() {
        try {
            properties.loadFromXML(new FileInputStream(PATH));

            properties.forEach((key, value) -> {
                Settings newSettings = null;
                String currPrefix = "";

                if(key.toString().matches(".*" + GAME_PREFIX + ".*")) {
                    currPrefix = GAME_PREFIX;
                    newSettings = gameSettings;
                } else if (key.toString().matches(".*" + WINDOW_PREFIX + ".*")) {
                    currPrefix = WINDOW_PREFIX;
                    newSettings = windowSettings;
                } else if (key.toString().matches(".*" + AUDIO_PREFIX + ".*")) {
                    currPrefix = AUDIO_PREFIX;
                    newSettings = audioSettings;
                }

                try {
                    assert newSettings != null : "Type of settings does not exist.";
                    newSettings.setProperty(key.toString().replace(currPrefix, ""), value.toString());
                } catch (Exception e) {
                    Engine.log(Logger.ERROR,"Failed to create setting: ");
                    Engine.log(Logger.ERROR, e.toString());
                }
            });
        } catch (Exception e) {
            Engine.log(Logger.ERROR,"Failed to load settings: ");
            Engine.log(Logger.ERROR, e.toString());
        }
    }

    public void saveSettings() {
        try {
            properties.storeToXML(new FileOutputStream(PATH), null);
        } catch (IOException e) {
            Engine.log(Logger.ERROR,"Failed to save settings: ");
            Engine.log(Logger.ERROR, e.toString());
        }
    }

    @Override
    public void initialize() {
        loadSettings();
    }

    @Override
    public void destroy() {
        saveSettings();
    }

    public SettingsGame getGameSettings() {
        return gameSettings;
    }

    public SettingsAudio getAudioSettings() {
        return audioSettings;
    }

    public SettingsWindow getWindowSettings() {
        return windowSettings;
    }
}
