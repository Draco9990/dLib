package dLib.util.events.serializableevents;

import java.io.Serializable;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface SerializableConsumer<Type1> extends Consumer<Type1>, Serializable {
    long serialVersionUID = 1L;
}
