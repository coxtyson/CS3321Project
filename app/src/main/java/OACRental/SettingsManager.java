package OACRental;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;

public class SettingsManager {
    private static final String configFilePath = "config.json";
    private static JSONObject settings;

    private static void applyDefaults() {
        settings = new JSONObject();
        settings.put("database-url", "localhost");
        settings.put("database-port", 3306);
        settings.put("database-username", "");
        settings.put("database-password", "");
        settings.put("database-name", "OAC");
        settings.put("customer-show-email", false);
        settings.put("customer-show-phone", false);
        settings.put("style", "light");
    }


    public static void loadOrCreateSettings() {
        applyDefaults();
        File config = new File(configFilePath);

        if (config.exists()) {
            JSONParser parser = new JSONParser();

            try (var reader = new FileReader(configFilePath)) {
                var newsettings = (JSONObject) parser.parse(reader);
                for (var key : newsettings.keySet()) {
                    settings.put(key, newsettings.get(key));
                }
            }
            catch (Exception ex) {
                System.out.println("Unable to read config, applying default settings");
            }
        }
        else {
            try (var file = new FileWriter(configFilePath)) {
                file.write(settings.toJSONString());
            }
            catch (Exception ex) {
                System.out.println("Unable to write default settings to disk");
            }
        }
    }

    public static boolean settingExists(String name) {
        return settings.containsKey(name);
    }

    public static void updateSetting(String name, Object value) {
        settings.put(name, value);
    }

    public static Object getSetting(String name) {
        if (!settingExists(name)) {
            throw new IllegalArgumentException("Setting " + name + " does not exist");
        }

        Object value = settings.get(name);

        // Extra long precision not needed, and causes type checking issues elsewhere
        if (value instanceof Long) {
            return Math.toIntExact((Long)value);
        }
        else {
            return value;
        }
    }
}
