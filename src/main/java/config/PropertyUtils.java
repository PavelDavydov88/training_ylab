package config;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
//        String s = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
//        try (FileInputStream fis = new FileInputStream("/")) {
        try (FileInputStream fis = new FileInputStream("C:/apache-tomcat-10.1.15/webapps/training_ylab_war/WEB-INF/classes/application.properties")) {
//        try (FileInputStream fis = new FileInputStream("src/main/resources/application.properties")) {
            PROPERTY.load(fis);
            return PROPERTY.getProperty(keyProperty);
        } catch (
                IOException e) {
            throw new IOException("File doesn't exist!");
        }
    }
}
