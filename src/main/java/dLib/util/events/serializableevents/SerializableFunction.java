package dLib.util.events.serializableevents;

import java.io.Serializable;
import java.util.function.BiConsumer;
import java.util.function.Function;

public interface SerializableFunction<Type1, Type2> extends Function<Type1, Type2>, Serializable {
    long serialVersionUID = 1L;
}
