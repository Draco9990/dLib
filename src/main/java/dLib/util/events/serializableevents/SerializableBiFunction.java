package dLib.util.events.serializableevents;

import java.io.Serializable;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface SerializableBiFunction<Type1, Type2, Type3> extends BiFunction<Type1, Type2, Type3>, Serializable {
    long serialVersionUID = 1L;
}
