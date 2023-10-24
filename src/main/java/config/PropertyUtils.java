package config;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
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
        if (PROPERTY.isEmpty()) {
            try (InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties")) {
                PROPERTY.load(input);
            } catch (IOException e) {
                throw new IOException("File doesn't exist!");
            }
        }
        return PROPERTY.getProperty(keyProperty);
    }
}
