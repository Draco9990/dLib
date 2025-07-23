package dLib.util.events.localevents;

import dLib.util.events.Event;
import dLib.util.events.serializableevents.SerializableBiConsumer;

import java.util.function.BiConsumer;

public class BiConsumerEvent<Type1, Type2> extends Event<SerializableBiConsumer<Type1, Type2>> {
    public BiConsumerEvent() {
        super();
    }

    public void invoke(Type1 arg1, Type2 arg2){
        super.invoke(consumer -> consumer.accept(arg1, arg2));
    }
}
