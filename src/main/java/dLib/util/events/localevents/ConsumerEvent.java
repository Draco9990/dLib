package dLib.util.events.localevents;

import dLib.util.events.Event;
import dLib.util.events.serializableevents.SerializableConsumer;

import java.util.function.Consumer;

public class ConsumerEvent<Type1> extends Event<SerializableConsumer<Type1>> {
    public ConsumerEvent() {
        super();
    }

    public void invoke(Type1 arg1){
        super.invoke(consumer -> consumer.accept(arg1));
    }
}
