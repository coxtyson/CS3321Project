package OACRental;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class SettingsManager {
    private static final String configFilePath = "config.json";

    private static List<SettingGroup> settings;

    private static void applyDefaults() {
        settings = new ArrayList<>();

        // Settings are defined here, first in groups, then each groups contains an array of settings
        // It is safe to define new settings. The UI will automatically reflect the new settings or
        // groups, and the old config.json will remain valid.

        // To use settings in places around the application, you can do as simple as
        //      String variable = (String) DataManager.getSettingValue("Some Setting");
        // note that you do have to cast it to the type you want (here, a string)

        // Supported settings types are String, StringEnum, Int, IntEnum, and Bool. See below for some examples

        settings.add(
            new SettingGroup(
                    "Style",
                    new Setting[]{
                        new Setting("Theme", "light", new Object[]{ "light", "dark" })  // String enum setting
                    }
            )
        );

        settings.add(
            new SettingGroup(
                    "Connections",
                    new Setting[]{
                            new Setting("Database URL", "localhost"), // String setting
                            new Setting("Database Port", 3306), // Int setting
                            new Setting("Database Username", ""),
                            new Setting("Database Password", ""),
                            new Setting("Database Name", "OAC")
                    }
            )
        );

        settings.add(
                new SettingGroup(
                        "Data Display",
                        new Setting[]{
                                new Setting("Show Customer Email", false), // Bool setting
                                new Setting("Show Customer Phone", false)
                        }
                )
        );
    }


    public static void loadOrCreateSettings() {
        applyDefaults();

        File config = new File(configFilePath);

        if (!config.exists()) {
            saveSettings();
            return;
        }

        JSONParser parser = new JSONParser();
        List<SettingGroup> foundgroups = new ArrayList<>();

        try (var reader = new FileReader(configFilePath)) {
            JSONObject root = (JSONObject) parser.parse(reader);

            if (root.containsKey("grouplist")) {

                for (var conf : (JSONArray) root.get("grouplist")) {
                    foundgroups.add(SettingGroup.fromJSONObject((JSONObject) conf));
                }

            }
            else {
                throw new Exception("Malformed json, missing key \"grouplist\"");
            }
        }
        catch (Exception ex) {
            System.out.println("Failed to load settings, leaving defaults. Caused by the following error:");
            System.out.println(ex.getMessage());

            foundgroups.clear(); // This ensures nothing else will happen from this point forward
        }

        // One simple solution would be to just do
        //          settings = foundGroups
        // but this would have knock-on consequences, such as down the line if
        // a new setting is added, it would break the current configs, and cause the
        // app to probably crash until the config.json was regenerated. So instead,
        // you want to do this kind of json-diffing procedure, where you just use
        // what you loaded to update the defaults

        for (var grp : foundgroups) { // Loop through all the json groups
            SettingGroup curGroup = null;

            for (var oldgrp : settings) { // Find the same group in the defaults
                if (oldgrp.getName().equals(grp.getName())) {
                    curGroup = oldgrp;
                    break;
                }
            }

            if (curGroup == null) { // If that group doesn't exist, create it
                settings.add(grp);
                continue;
            }

            for (var set : grp) { // Loop through all the settings in the json group
                Setting curSet = null;

                for (var oldset : curGroup) { // Find that setting in the existing group
                    if (oldset.getName().equals(set.getName())) {
                        curSet = oldset;
                    }
                }

                if (curSet == null) { // If it doesn't exist, create it
                    curGroup.add(set);
                    continue;
                }

                if (!set.matches(curSet.get())) { // If it exists, but they have different values, update it
                    curSet.set(set.get());
                }
            }
        }
    }

    public static void saveSettings() {
        JSONObject root = new JSONObject();
        JSONArray groups = new JSONArray();

        for (var grp : settings) {
            groups.add(grp.toJSONObject());
        }

        root.put("grouplist", groups);

        File file = new File(configFilePath);

        if (file.exists()) {
            if (!file.delete()) {
                System.out.println("Failed to delete old config, not saving to avoid corruption");
                return;
            }
        }

        try (var writer = new FileWriter(configFilePath)) {
             writer.write(root.toJSONString());
        }
        catch (Exception ex) {
            System.out.println("Failed to save settings");
        }
    }

    public static List<SettingGroup> getAllSettings() {
        return settings;
    }

    public static boolean settingExists(String name) {
        for (var grp : settings) {
            if (grp.getOr(name, null) != null) {
                return true;
            }
        }

        return false;
    }

    public static void updateSetting(String name, Object value) {
        for (var grp : settings) {
            var set = grp.getOr(name, null);

            if (set != null) {
                set.set(value);
            }
        }
    }

    public static Setting getSetting(String name) {
        if (!settingExists(name)) {
            throw new IllegalArgumentException("Setting " + name + " does not exist");
        }

        for (var grp : settings) {
            var set = grp.getOr(name, null);

            if (set != null) {
                return set;
            }
        }

        throw new IllegalStateException("Should be unreachable, settingExists should catch if the setting is missing");

    }

    public static Object getSettingValue(String name) {
        return getSetting(name).get();
    }
}
