package dLib.util;

import java.util.HashMap;
import java.util.Map;

public class BiMap<K, V> {
    private final Map<K, V> keyToValueMap = new HashMap<>();
    private final Map<V, K> valueToKeyMap = new HashMap<>();

    public void put(K key, V value) {
        if (keyToValueMap.containsKey(key) || valueToKeyMap.containsKey(value)) {
            throw new IllegalArgumentException("Duplicate key or value");
        }
        keyToValueMap.put(key, value);
        valueToKeyMap.put(value, key);
    }

    public V getByKey(K key) {
        return keyToValueMap.get(key);
    }

    public K getByValue(V value) {
        return valueToKeyMap.get(value);
    }

    public V removeByKey(K key) {
        V value = keyToValueMap.remove(key);
        if (value != null) {
            valueToKeyMap.remove(value);
        }
        return value;
    }

    public K removeByValue(V value) {
        K key = valueToKeyMap.remove(value);
        if (key != null) {
            keyToValueMap.remove(key);
        }
        return key;
    }

    public boolean containsKey(K key) {
        return keyToValueMap.containsKey(key);
    }

    public boolean containsValue(V value) {
        return valueToKeyMap.containsKey(value);
    }

    public void clear() {
        keyToValueMap.clear();
        valueToKeyMap.clear();
    }
}
