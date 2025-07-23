package dLib.util.events.serializableevents;

import java.io.Serializable;
import java.util.function.BiConsumer;

public interface SerializableBiConsumer<Type1, Type2> extends BiConsumer<Type1, Type2>, Serializable {
    long serialVersionUID = 1L;
}
