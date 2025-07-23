package dLib.util.events.serializableevents;

import dLib.util.consumers.OctConsumer;
import dLib.util.consumers.QuadConsumer;

import java.io.Serializable;

public interface SerializableQuadConsumer<Type1, Type2, Type3, Type4> extends QuadConsumer<Type1, Type2, Type3, Type4>, Serializable {
    long serialVersionUID = 1L;
}
