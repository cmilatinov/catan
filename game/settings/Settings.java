package settings;

public interface Settings {
    void setProperty(String key, String value) throws Exception;
    void update(String key, String value);
}
