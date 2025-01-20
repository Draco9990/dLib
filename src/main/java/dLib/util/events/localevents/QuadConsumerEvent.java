package dLib.util.events.localevents;

import dLib.util.consumers.QuadConsumer;
import dLib.util.events.Event;
import org.apache.logging.log4j.util.TriConsumer;

public class QuadConsumerEvent<Type1, Type2, Type3, Type4> extends Event<QuadConsumer<Type1, Type2, Type3, Type4>> {
    public QuadConsumerEvent() {
        super();
    }

    public void invoke(Type1 arg1, Type2 arg2, Type3 arg3, Type4 arg4){
        super.invoke(consumer -> consumer.accept(arg1, arg2, arg3, arg4));
    }
}
