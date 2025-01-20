package dLib.util.consumers;

public interface HeptConsumer<T, U, V, W, X, Y, Z> {
    void accept(T t, U u, V v, W w, X x, Y y, Z z);
}
