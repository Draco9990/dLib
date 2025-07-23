package dLib.util.events.serializableevents;

import dLib.util.consumers.QuinConsumer;

import java.io.Serializable;

public interface SerializableRunnable extends Runnable, Serializable {
    long serialVersionUID = 1L;
}
