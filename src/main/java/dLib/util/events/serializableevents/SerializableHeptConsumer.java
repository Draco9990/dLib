package dLib.util.events.serializableevents;

import dLib.util.consumers.HeptConsumer;

import java.io.Serializable;
import java.util.function.Function;

public interface SerializableHeptConsumer<T, U, V, W, X, Y, Z> extends HeptConsumer<T, U, V, W, X, Y, Z>, Serializable {
    long serialVersionUID = 1L;
}
