package dLib.util.events.serializableevents;

import dLib.util.consumers.QuinConsumer;
import org.apache.logging.log4j.util.TriConsumer;

import java.io.Serializable;

public interface SerializableTriConsumer<T, U, V> extends TriConsumer<T, U, V>, Serializable {
    long serialVersionUID = 1L;
}
