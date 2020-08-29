package settings;

public class SettingsWindow implements Settings {

    SettingsManager manager;

    public SettingsWindow(SettingsManager manager) {
        this.manager = manager;
    }

    @Override
    public void setProperty(String key, String value) throws Exception {

    }

    @Override
    public void update(String key, String value) {
        manager.updateProperty(SettingsPrefix.GAME + "." + key, value);
    }
}
