package OACRental;

import com.sun.jdi.event.StepEvent;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

public class Setting {
    private Object value;
    private final String name;
    private Object[] choices;
    private DataType type;

    // This little song and dance is just an enum, but one that can be easily converted to
    // a number
    public enum DataType {
        NOT_SET(0),
        BOOLEAN(1),
        STRING(2),
        INTEGER(3);

        private int value;

        private DataType(int value) {
            this.value = value;
        }

        private static HashMap<Integer, DataType> map = new HashMap<>(){{
           put(0, NOT_SET);
           put(1, BOOLEAN);
           put(2, STRING);
           put(3, INTEGER);
        }};

        public static DataType valueOf(int dataType) {
            return (DataType) map.get(dataType);
        }

        public int getValue() {
            return value;
        }
    }

    public Setting(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name may not be empty");
        }

        this.name  = name;
        value = "";
        choices = new Object[0];
        type = DataType.STRING;
    }

    public Setting(String name, Object value) {
        this(name);

        if (value instanceof Boolean) {
            type = DataType.BOOLEAN;
        }
        else if (value instanceof String) {
            type = DataType.STRING;
        }
        else if (value instanceof Integer || value instanceof Long) {
            type = DataType.INTEGER;
        }
        else {
            throw new IllegalArgumentException("Unknown type for value");
        }

        set(value);
    }

    public Setting(String name, Object value, Object[] choices) {
        this(name, value);
        this.choices = choices;
    }

    /**
     * Private constructor, for use by the json serialization interface only
     */
    private Setting(String name, Object value, Object[] choices, DataType type) {
        this.name = name;
        this.value = value;
        this.choices = choices;
        this.type = type;
    }



    public String getName() {
        return name;
    }

    public DataType getType() {
        return type;
    }

    public Object[] getOptions() {
        return choices;
    }

    public Object get() {
        return value;
    }

    public void set(Object value) {
        if (!typeMatches(value)) {
            throw new IllegalArgumentException("Argument is of invalid or unknown type for this setting");
        }
        else if (!allowedValue(value)) {
            throw new IllegalArgumentException("Not an allowed value");
        }

        if (value instanceof Long) {
            this.value = Math.toIntExact((Long)value);
        }
        else {
            this.value = value;
        }
    }

    public boolean matches(Object obj) {
        if (!typeMatches(obj)) {
            return false;
        }

        switch (type) {
            case BOOLEAN -> { return ((Boolean)value) == ((Boolean)obj); }
            case STRING -> { return ((String)value).equals(obj); }
            case INTEGER -> {
                if (obj instanceof Long) {
                    return ((Integer)value) == Math.toIntExact((Long)obj);
                }
                else {
                    return Objects.equals((Integer) value, (Integer) obj);
                }
            }
        }

        throw new IllegalStateException("type is not set, this should not happen");
    }

    public JSONObject toJSONObject() {
        JSONObject obj = new JSONObject();
        obj.put("name", name);
        obj.put("value", value);
        obj.put("type", type.getValue());

        if (choices != null && choices.length > 0) {
            JSONArray arr = new JSONArray();
            arr.addAll(Arrays.asList(choices));

            obj.put("choices", arr);
        }


        return obj;
    }

    public static Setting fromJSONObject(JSONObject obj) {
        String name = null;
        Object value = null;
        DataType type = DataType.NOT_SET;
        Object[] choices = null;

        if (obj.containsKey("name")) {
            name = (String) obj.get("name");
        }
        else {
            throw new IllegalArgumentException("Malformed Setting JSON, missing \"name\" field");
        }

        if (obj.containsKey("value")) {
            value = obj.get("value");
        }
        else {
            throw new IllegalArgumentException("Malformed Setting JSON, missing \"value\" field");
        }

        if (obj.containsKey("type")) {
            Long typeval = (Long) obj.get("type");

            if (typeval <= 0 || typeval >= 4) {
                throw new IllegalArgumentException("Malformed Setting JSON, type field must be 1-3");
            }

            type = DataType.valueOf(Math.toIntExact(typeval));
        }
        else {
            throw new IllegalArgumentException("Malformed setting JSON, missing \"value\" field");
        }

        if (obj.containsKey("choices")) {
            JSONArray arr = (JSONArray) obj.get("choices");
            Object[] objarr = new Object[arr.size()];

            for(int i = 0; i < arr.size(); i++) {
                objarr[i] = arr.get(i);
            }

            choices = objarr;
        }

        if (value instanceof Long) {
            value = Math.toIntExact((Long) value);
        }

        return new Setting(name, value, choices, type);
    }


    private boolean typeMatches(Object value) {
        switch (type) {
            case BOOLEAN -> { return (value instanceof Boolean); }
            case STRING -> { return (value instanceof String); }
            case INTEGER -> { return (value instanceof Integer || value instanceof Long); }
        }

        throw new IllegalStateException("type is not set, this should not happen");
    }

    private boolean allowedValue(Object value) {
        if (choices != null && choices.length > 0) {
            boolean found = false;

            for (var option : choices) {
                if (option.equals(value)) {
                    found = true;
                    break;
                }
            }

            return found;
        }

        return true;
    }
}
