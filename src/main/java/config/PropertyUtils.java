package config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * класс предоставляет properties
 */
public class PropertyUtils {
    private static final Properties PROPERTY = new Properties();

    /**
     * метод предоставляет property из configFile
     *
     * @param keyProperty ключ property
     * @return значение property
     * @throws IOException
     */
    public static String getProperty(String keyProperty) throws IOException {
        try (FileInputStream fis = new FileInputStream("src/main/resources/application.properties")) {
            PROPERTY.load(fis);
            return PROPERTY.getProperty(keyProperty);
        } catch (
                IOException e) {
            throw new IOException("File doesn't exist!");
        }
    }
}
