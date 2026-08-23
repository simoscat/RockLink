package engineering.persistency;

import exception.DAOException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigManager {

    private static Properties properties;

    private ConfigManager() {}

    private static synchronized Properties properties() {
        if (properties == null) {
            Properties prop = new Properties();
            try (InputStream is = new FileInputStream("config.properties")) {
                prop.load(is);
            } catch (IOException e) {
                throw new DAOException("Couldn't read properties file", e);
            }
            properties = prop;
        }
        return properties;
    }

    public static String getProperty(String key) {
        return properties().getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        return properties().getProperty(key, defaultValue);
    }
}
