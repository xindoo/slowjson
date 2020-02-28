package xyz.xindoo.slowjson;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JSONArray {
    private final List<JSONObject> list;

    public JSONArray() {
        this.list = new ArrayList<>();
    }
    public JSONArray(List<JSONObject> list) {
        this.list = new ArrayList<>(list.size());
        this.list.addAll(list);
    }

    protected JSONArray(JSONParser.ArrayContext arrayCtx) {
        this.list = arrayCtx.value()
                            .stream()
                            .map(valueContext -> new JSONObject(valueContext.obj()))
                            .collect(Collectors.toList());
    }

    public static JSONArray parseArray(String text) {
        JSONLexer lexer = new JSONLexer(CharStreams.fromString(text));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JSONParser parser = new JSONParser(tokens);
        JSONParser.ArrayContext arrayCtx = parser.array();
        return new JSONArray(arrayCtx);
    }

    public JSONObject getJSONObject(int index) {
        return list.get(index);
    }

    public void add(JSONObject jsonObject) {
        list.add(jsonObject);
    }

    @Override
    public String toString() {
        return toJSONString();
    }

    public String toJSONString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        List<String> strList = list.stream().map(JSONObject::toString).collect(Collectors.toList());
        sb.append(String.join(",", strList));
        sb.append("]");
        return sb.toString();
    }
}
