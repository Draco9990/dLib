package dLib.util.weak;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

public class WeakSet<T> extends AbstractSet<T> {
    private final Map<T, Boolean> map = new WeakHashMap<T, Boolean>();

    @Override
    public boolean add(T t) {
        return map.put(t, Boolean.TRUE) == null;
    }

    @Override
    public boolean remove(Object o) {
        return map.remove(o) != null;
    }

    @Override
    public boolean contains(Object o) {
        return map.containsKey(o);
    }

    @Override
    public Iterator<T> iterator() {
        return map.keySet().iterator();
    }

    @Override
    public int size() {
        return map.size();
    }
}