package dLib.util.events.localevents;

import dLib.util.consumers.HexConsumer;
import dLib.util.consumers.QuinConsumer;
import dLib.util.events.Event;

public class HexConsumerEvent<Type1, Type2, Type3, Type4, Type5, Type6> extends Event<HexConsumer<Type1, Type2, Type3, Type4, Type5, Type6>> {
    public HexConsumerEvent() {
        super();
    }

    public void invoke(Type1 arg1, Type2 arg2, Type3 arg3, Type4 arg4, Type5 arg5, Type6 arg6) {
        super.invoke(consumer -> consumer.accept(arg1, arg2, arg3, arg4, arg5, arg6));
    }
}
