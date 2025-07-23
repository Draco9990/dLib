package dLib.util.events.serializableevents;

import dLib.util.consumers.HexConsumer;
import dLib.util.consumers.OctConsumer;

import java.io.Serializable;

public interface SerializableOctConsumer<T, U, V, W, X, Y, Z, A> extends OctConsumer<T, U, V, W, X, Y, Z, A>, Serializable {
    long serialVersionUID = 1L;
}
