package dLib.util.events.serializableevents;

import dLib.util.consumers.QuadConsumer;
import dLib.util.consumers.QuinConsumer;

import java.io.Serializable;

public interface SerializableQuinConsumer<T, U, V, W, X> extends QuinConsumer<T, U, V, W, X>, Serializable {
    long serialVersionUID = 1L;
}
