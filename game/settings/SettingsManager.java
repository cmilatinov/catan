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

    private final SettingsGame gameSettings = new SettingsGame(this);
    private final SettingsAudio audioSettings = new SettingsAudio(this);
    private final SettingsWindow windowSettings = new SettingsWindow(this);
    private final SettingsNetwork networkSettings = new SettingsNetwork(this);

    /**
     * Constructor for Settings object.
     * @param path - the path to the xml file
     */
    public SettingsManager(String path) {
        PATH = path;
        loadSettings();
    }

    /**
     * Method to load the settings from an xml file.
     */
    public void loadSettings() {
        try {
            properties.loadFromXML(new FileInputStream(PATH));

            properties.forEach((key, value) -> {
                try {
                    String keyPrefix = key.toString().substring(0, key.toString().indexOf('.'));
                    switch(keyPrefix){
                        case SettingsPrefix.GAME -> gameSettings.setProperty(key.toString().replace(SettingsPrefix.GAME + ".", ""), value.toString());
                        case SettingsPrefix.WINDOW ->  windowSettings.setProperty(key.toString().replace(SettingsPrefix.WINDOW + ".", ""), value.toString());
                        case SettingsPrefix.NETWORK ->  networkSettings.setProperty(key.toString().replace(SettingsPrefix.NETWORK + ".", ""), value.toString());
                        case SettingsPrefix.AUDIO ->  audioSettings.setProperty(key.toString().replace(SettingsPrefix.AUDIO + ".", ""), value.toString());
                        default -> throw new Exception("Settings handler for prefix \"" + keyPrefix + "\" does not exist.");
                    }
                } catch (Exception e) {
                    Engine.log(Logger.ERROR, "Failed to parse settings: ");
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

    public void updateProperty(String key, String value) {
        properties.setProperty(key, value);
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
