package dLib.util.events.localevents;

import dLib.util.events.Event;
import dLib.util.events.serializableevents.SerializableTriConsumer;
import org.apache.logging.log4j.util.TriConsumer;

public class TriConsumerEvent<Type1, Type2, Type3> extends Event<SerializableTriConsumer<Type1, Type2, Type3>> {
    public TriConsumerEvent() {
        super();
    }

    public void invoke(Type1 arg1, Type2 arg2, Type3 arg3){
        super.invoke(consumer -> consumer.accept(arg1, arg2, arg3));
    }
}
