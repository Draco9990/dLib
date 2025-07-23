package dLib.util.events.serializableevents;

import dLib.util.consumers.HeptConsumer;
import dLib.util.consumers.HexConsumer;

import java.io.Serializable;

public interface SerializableHexConsumer<T, U, V, W, X, Y> extends HexConsumer<T, U, V, W, X, Y>, Serializable {
    long serialVersionUID = 1L;
}
