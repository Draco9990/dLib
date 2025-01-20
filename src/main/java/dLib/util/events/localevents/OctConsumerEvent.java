package dLib.util.events.localevents;

import dLib.util.consumers.HeptConsumer;
import dLib.util.consumers.OctConsumer;
import dLib.util.events.Event;

public class OctConsumerEvent<Type1, Type2, Type3, Type4, Type5, Type6, Type7, Type8> extends Event<OctConsumer<Type1, Type2, Type3, Type4, Type5, Type6, Type7, Type8>> {
    public OctConsumerEvent() {
        super();
    }

    public void invoke(Type1 arg1, Type2 arg2, Type3 arg3, Type4 arg4, Type5 arg5, Type6 arg6, Type7 arg7, Type8 arg8) {
        super.invoke(consumer -> consumer.accept(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8));
    }
}