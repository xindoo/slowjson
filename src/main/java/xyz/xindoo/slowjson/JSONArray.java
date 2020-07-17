package xyz.xindoo.slowjson;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

public class JSONArray implements List<Object> {
    private final List<Object> list;

    public JSONArray() {
        this.list = new ArrayList<>();
    }

    public JSONArray(List<Object> list) {
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
        return (JSONObject) list.get(index);
    }

    @Override
    public String toString() {
        return toJSONString();
    }

    public String toJSONString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        List<String> strList = list.stream().map(Object::toString).collect(Collectors.toList());
        sb.append(String.join(",", strList));
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean add(Object e) {
        return this.list.add(e);
    }

    @Override
    public void add(int index, Object element) {
        this.list.add(index, element);
    }

    @Override
    public Object set(int index, Object element) {
        if (index == -1) {
            this.list.add(element);
            return null;
        } else if (this.list.size() > index) {
            return this.list.set(index, element);
        } else {
            for(int i = this.list.size(); i < index; ++i) {
                this.list.add((Object)null);
            }

            this.list.add(element);
            return null;
        }
    }

    @Override
    public Object get(int index) {
        return this.list.get(index);
    }

    @Override
    public boolean addAll(Collection<? extends Object> c) {
        return this.list.addAll(c);
    }

    @Override
    public ListIterator<Object> listIterator(int index) {
        return this.list.listIterator(index);
    }

    @Override
    public ListIterator<Object> listIterator() {
        return this.list.listIterator();
    }

    @Override
    public int indexOf(Object o) {
        return this.list.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return this.list.lastIndexOf(o);
    }


    @Override
    public List<Object> subList(int fromIndex, int toIndex) {
        return this.list.subList(fromIndex, toIndex);
    }

    @Override
    public void clear() {
        this.list.clear();
    }

    @Override
    public Object remove(int index) {
        return this.list.remove(index);
    }

    @Override
    public int size() {
        return this.list.size();
    }

    @Override
    public boolean isEmpty() {
        return this.list.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return this.list.contains(o);
    }

    @Override
    public Iterator<Object> iterator() {
        return this.list.iterator();
    }

    @Override
    public Object[] toArray() {
        return this.list.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return this.list.toArray(a);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return this.list.containsAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return this.list.retainAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return this.list.removeAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends Object> c) {
        return this.list.addAll(index, c);
    }

    @Override
    public boolean remove(Object o) {
        return this.list.remove(o);
    }

}
