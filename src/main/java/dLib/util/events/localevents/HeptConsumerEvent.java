package dLib.util.events.localevents;

import dLib.util.consumers.HeptConsumer;
import dLib.util.consumers.HexConsumer;
import dLib.util.events.Event;

public class HeptConsumerEvent<Type1, Type2, Type3, Type4, Type5, Type6, Type7> extends Event<HeptConsumer<Type1, Type2, Type3, Type4, Type5, Type6, Type7>> {
    public HeptConsumerEvent() {
        super();
    }

    public void invoke(Type1 arg1, Type2 arg2, Type3 arg3, Type4 arg4, Type5 arg5, Type6 arg6, Type7 arg7) {
        super.invoke(consumer -> consumer.accept(arg1, arg2, arg3, arg4, arg5, arg6, arg7));
    }
}
