package dLib.util.weak;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.ref.WeakReference;

public class SerializableWeakReference<T> implements Serializable {
    private transient WeakReference<T> ref;

    public SerializableWeakReference(T referent) {
        this.ref = new WeakReference<T>(referent);
    }

    public T get() {
        return ref.get();
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(ref.get());
    }

    @SuppressWarnings("unchecked")
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        T obj = (T) in.readObject();
        this.ref = new WeakReference<T>(obj);
    }

    @Override
    public int hashCode() {
        T obj = get();
        return (obj != null) ? obj.hashCode() : 0;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof SerializableWeakReference)) return false;
        T a = this.get();
        T b = (T) ((SerializableWeakReference<?>) o).get();
        return a != null && a.equals(b);
    }
}