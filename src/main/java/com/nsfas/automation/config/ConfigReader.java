package com.nsfas.automation.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

/**
 * Loads config.properties once and exposes typed getters.
 * System properties override file values (useful for CI/CD).
 */
public class ConfigReader {

    private static final Logger log = LogManager.getLogger(ConfigReader.class);
    private static final Properties props = new Properties();

    static {
        try (InputStream is = ConfigReader.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (is == null) {
                throw new RuntimeException("config.properties not found on classpath");
            }
            props.load(is);
            log.info("config.properties loaded successfully");
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    private ConfigReader() {}

    /** Returns value from system property first, then config.properties, then defaultValue. */
    public static String get(String key, String defaultValue) {
        return System.getProperty(key, props.getProperty(key, defaultValue));
    }

    public static String get(String key) {
        String value = get(key, null);
        if (value == null) throw new RuntimeException("Missing config key: " + key);
        return value;
    }

    public static int getInt(String key, int defaultValue) {
        return Integer.parseInt(get(key, String.valueOf(defaultValue)));
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return Boolean.parseBoolean(get(key, String.valueOf(defaultValue)));
    }

    /**
     * Updates a key in memory, as a system property (visible immediately in this JVM),
     * and persisted to the config.properties file on disk so standalone re-runs see it.
     * Writes to both target/test-classes/ and src/test/resources/ (survives mvn clean).
     */
    public static synchronized void set(String key, String value) {
        props.setProperty(key, value);
        System.setProperty(key, value);
        try {
            URL url = ConfigReader.class.getClassLoader().getResource("config.properties");
            if (url == null) {
                log.warn("Cannot persist {}: config.properties not on classpath", key);
                return;
            }
            Path targetPath = Paths.get(url.toURI());
            updateFileProperty(targetPath, key, value);

            // Also update src/test/resources so the change survives mvn clean
            // targetPath: project/target/test-classes/config.properties → 3 parents up = project root
            Path srcPath = targetPath.getParent().getParent().getParent()
                    .resolve("src/test/resources/config.properties");
            if (Files.exists(srcPath)) {
                updateFileProperty(srcPath, key, value);
            }
        } catch (Exception e) {
            log.warn("Could not persist {} to config.properties on disk: {}", key, e.getMessage());
        }
    }

    private static void updateFileProperty(Path filePath, String key, String value) throws IOException {
        List<String> lines = Files.readAllLines(filePath);
        boolean found = false;
        for (int i = 0; i < lines.size(); i++) {
            String trimmed = lines.get(i).trim();
            if (trimmed.startsWith(key + "=") || trimmed.startsWith(key + " =")) {
                lines.set(i, key + "=" + value);
                found = true;
                break;
            }
        }
        if (!found) {
            lines.add(key + "=" + value);
        }
        Files.write(filePath, lines);
        log.info("config.properties updated on disk [{}]: {}={}", filePath.getFileName(), key, value);
    }
}
