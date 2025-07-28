package dLib.util.weak;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

public class SerializableWeakHashMap<K, V> implements Serializable {
    private transient Map<SerializableWeakReference<K>, V> map = new HashMap<>();

    public V put(K key, V value) {
        cleanup();
        return map.put(new SerializableWeakReference<>(key), value);
    }

    public V get(K key) {
        cleanup();
        return map.get(new SerializableWeakReference<>(key));
    }

    public boolean containsKey(K key) {
        cleanup();
        return map.containsKey(new SerializableWeakReference<>(key));
    }

    public V remove(K key) {
        cleanup();
        return map.remove(new SerializableWeakReference<>(key));
    }

    public int size() {
        cleanup();
        return (int) map.keySet().stream().filter(ref -> ref.get() != null).count();
    }

    private void cleanup() {
        map.keySet().removeIf(ref -> ref.get() == null);
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        Map<K, V> snapshot = new HashMap<>();
        for (Map.Entry<SerializableWeakReference<K>, V> entry : map.entrySet()) {
            K key = entry.getKey().get();
            if (key != null) {
                snapshot.put(key, entry.getValue());
            }
        }
        out.writeObject(snapshot);
    }

    @SuppressWarnings("unchecked")
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        Map<K, V> snapshot = (Map<K, V>) in.readObject();
        map = new HashMap<>();
        for (Map.Entry<K, V> entry : snapshot.entrySet()) {
            map.put(new SerializableWeakReference<>(entry.getKey()), entry.getValue());
        }
    }

    public Iterator<K> keyIterator() {
        cleanup();
        final Iterator<SerializableWeakReference<K>> it = map.keySet().iterator();
        return new Iterator<K>() {
            private K next = advance();

            private K advance() {
                while (it.hasNext()) {
                    K val = it.next().get();
                    if (val != null) return val;
                    it.remove(); // clean up collected
                }
                return null;
            }

            public boolean hasNext() {
                return next != null;
            }

            public K next() {
                if (next == null) throw new NoSuchElementException();
                K ret = next;
                next = advance();
                return ret;
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}