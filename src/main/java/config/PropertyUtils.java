package config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyUtils {
    private static final Properties PROPERTY = new Properties();

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
