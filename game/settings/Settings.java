package settings;

import log.Logger;
import main.Engine;
import objects.GameScript;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Settings extends GameScript {

    // Path for the settings files.
    private final String PATH;

    // Properties object to load, manage and save data from xml files.
    private final Properties properties = new Properties();

    /**
     * Constructor for Settings object.
     * @param path - the path to the xml file
     */
    public Settings(String path) {
        PATH = path;
    }

    public void loadSettings() {
        try {
            properties.loadFromXML(new FileInputStream(PATH));
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
}
