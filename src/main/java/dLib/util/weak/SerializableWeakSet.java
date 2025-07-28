package dLib.util.weak;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;

public class SerializableWeakSet<T extends Serializable> extends AbstractSet<T> implements Serializable {
    private SerializableWeakHashMap<T, Boolean> map = new SerializableWeakHashMap<>();

    @Override
    public boolean add(T t) {
        return map.put(t, Boolean.TRUE) == null;
    }

    @Override
    public boolean remove(Object o) {
        return map.remove((T) o) != null;
    }

    @Override
    public boolean contains(Object o) {
        return map.containsKey((T) o);
    }

    @Override
    public Iterator<T> iterator() {
        return map.keyIterator();
    }

    @Override
    public int size() {
        return map.size();
    }
}