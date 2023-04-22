package OACRental;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.*;
import java.util.function.Consumer;

/**
 * @brief a group of settings. Mostly used for nice UI grouping.
 * Implements Iterable, so you can use for-each loops to loop over
 * all the settings in the group
 */
public class SettingGroup implements Iterable<Setting> {
    String name;
    List<Setting> settings;

    public SettingGroup(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name can't be empty");
        }

        this.name = name;
        settings = new ArrayList<>();
    }

    public SettingGroup(String name, Setting[] settings) {
        this(name);
        this.settings.addAll(Arrays.stream(settings).toList());
    }

    /**
     * Private constructor, for use only by the json serialization interface
     */
    private SettingGroup(String name, List<Setting> settings) {
        this.name = name;
        this.settings = settings;
    }

    public String getName() {
        return name;
    }

    public Setting get(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name can't be empty");
        }

        for (var set : settings) {
            if (set.getName() == name) {
                return set;
            }
        }

        throw new IllegalArgumentException("Setting does not exist");
    }

    public Setting getOr(String name, Setting or) {
        try {
            return get(name);
        }
        catch (Exception ex) {
            return or;
        }
    }

    public void add(Setting setting) {
        settings.add(setting);
    }

    public JSONObject toJSONObject() {
        JSONObject obj = new JSONObject();
        JSONArray children = new JSONArray();

        for (var set : settings) {
            children.add(set.toJSONObject());
        }

        obj.put("group", name);
        obj.put("settings", children);

        return obj;
    }

    public static SettingGroup fromJSONObject(JSONObject obj) {
        String name = null;
        List<Setting> sets = new ArrayList<>();

        if (obj.containsKey("group")) {
            name = (String) obj.get("group");
        }
        else {
            throw new IllegalArgumentException("Malformed SettingGroup json, missing field \"group\"");
        }

        if (obj.containsKey("settings")) {
            JSONArray arr = (JSONArray) obj.get("settings");

            for(var setobj : arr) {
                sets.add(Setting.fromJSONObject((JSONObject) setobj));
            }
        }
        else {
            throw new IllegalArgumentException("Malformed SettingGroup json, missing field \"settings\"");
        }

        return new SettingGroup(name, sets);
    }

    @Override
    public Iterator<Setting> iterator() {
        return settings.stream().iterator();
    }

    @Override
    public void forEach(Consumer<? super Setting> action) {
        settings.forEach(action);
    }

    @Override
    public Spliterator<Setting> spliterator() {
        return settings.spliterator();
    }
}
