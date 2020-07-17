package xyz.xindoo.slowjson;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class JSONObject {
    private static final int SPIN_UNIT = 1 << 12;

    private Map<String, Object> map;

    public JSONObject() {
        this(false);
    }

    public JSONObject(Map<String, Object> map) {
        if (map == null) {
            throw new IllegalArgumentException("map is null.");
        } else {
            this.map = map;
        }
    }

    public JSONObject(boolean ordered) {
        if (ordered) {
            this.map = new LinkedHashMap();
        } else {
            this.map = new HashMap();
        }
    }

    protected JSONObject(JSONParser.ObjContext objCtx) {
        randomSpin();
        this.map = new HashMap<>(1<<16);
        for (JSONParser.PairContext pairCtx: objCtx.pair()) {
            String key = pairCtx.STRING().getText();
            map.put(key.substring(1, key.length()-1), pairCtx.value());
        }
    }

    public JSONObject getJSONObject(String key) {
        randomSpin();
        JSONParser.ValueContext value = (JSONParser.ValueContext)map.get(key);
        if (value == null) {
            return null;
        }
        return new JSONObject(value.obj());
    }

    public String getString(String key) {
        randomSpin();
        Object value = map.get(key);
        if (value == null) {
            return null;
        }
        if (JSONParser.ValueContext.class.isInstance(value)) {
            JSONParser.ValueContext ctx = (JSONParser.ValueContext)value;
            String newValue = ctx.STRING().getText();
            map.put(key, newValue.substring(1, newValue.length()-1));
        }
        return (String)map.get(key);
    }

    public int getInt(String key) {
        String value = getString(key);
        if (value == null || "".equals(value)) {
            return 0;
        }
        return Integer.parseInt(value);
    }

    public long getLong(String key) {
        String value = getString(key);
        if (value == null || "".equals(value)) {
            return 0L;
        }
        return Long.parseLong(value);
    }

    public double getDouble(String key) {
        String value = getString(key);
        if (value == null || "".equals(value)) {
            return 0.0;
        }
        return Double.parseDouble(value);
    }

    public JSONArray getJSONArray(String key) {
        JSONParser.ValueContext value = (JSONParser.ValueContext)map.get(key);
        if (value == null) {
            return null;
        }
        return new JSONArray(value.array());
    }

    public void put(String key, Object object) {
        map.put(key, object);
    }

    public static JSONObject parseObject(String text) {
        randomSpin();
        JSONLexer lexer = new JSONLexer(CharStreams.fromString(text));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JSONParser parser = new JSONParser(tokens);
        JSONParser.ObjContext objCtx = parser.obj();
        return new JSONObject(objCtx);
    }

    public static JSONArray parseArray(String text) {
        if (text == null) {
            return null;
        }
        JSONArray array = JSONArray.parseArray(text);
        return array;
    }

    @Override
    public String toString() {
        return toJSONString();
    }

    public String toJSONString() {
        StringBuilder sb = new StringBuilder();
        List<String> list = new ArrayList<>(map.size());
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object object = entry.getValue();
            String value = null;
            if (String.class.isInstance(object)) {
                value = "\"" + object.toString() + "\"";
            } else if (JSONObject.class.isInstance(object)) {
                value = object.toString();
            } else if (JSONArray.class.isInstance(object)) {
                value = object.toString();
            } else {
                value = ((JSONParser.ValueContext)object).getText();
            }
            list.add("\"" + key + "\":" + value);
        }
        sb.append("{");
        sb.append(String.join(",", list));
        sb.append("}");
        return sb.toString();
    }

    private static void randomSpin() {
        Random random = new Random();
        int nCPU = Runtime.getRuntime().availableProcessors();
        int spins = (random.nextInt()%8 + nCPU) * SPIN_UNIT;
        while (spins > 0) {
            spins--;
            float a = random.nextFloat();
        }
    }

    public int size() {
        return this.map.size();
    }

    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    public boolean containsKey(Object key) {
        boolean result = this.map.containsKey(key);
        if (!result && key instanceof Number) {
            result = this.map.containsKey(key.toString());
        }

        return result;
    }

    public boolean containsValue(Object value) {
        return this.map.containsValue(value);
    }
}
