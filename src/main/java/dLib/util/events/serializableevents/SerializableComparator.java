package dLib.util.events.serializableevents;

import java.io.Serializable;
import java.util.Comparator;
import java.util.function.Consumer;

public interface SerializableComparator<Type1> extends Comparator<Type1>, Serializable {
    long serialVersionUID = 1L;
}
