package dLib.util.events.localevents;

import dLib.util.consumers.QuinConsumer;
import dLib.util.events.Event;
import dLib.util.events.serializableevents.SerializableQuinConsumer;

public class QuinConsumerEvent<Type1, Type2, Type3, Type4, Type5> extends Event<SerializableQuinConsumer<Type1, Type2, Type3, Type4, Type5>> {
    public QuinConsumerEvent() {
        super();
    }

    public void invoke(Type1 arg1, Type2 arg2, Type3 arg3, Type4 arg4, Type5 arg5) {
        super.invoke(consumer -> consumer.accept(arg1, arg2, arg3, arg4, arg5));
    }
}
