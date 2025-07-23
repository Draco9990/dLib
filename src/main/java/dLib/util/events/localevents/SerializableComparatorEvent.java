package dLib.util.events.localevents;

import dLib.util.events.Event;
import dLib.util.events.serializableevents.SerializableComparator;
import dLib.util.events.serializableevents.SerializableConsumer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;

public class SerializableComparatorEvent<Type1> extends Event<SerializableComparator<Type1>> {
    public SerializableComparatorEvent() {
        super();
    }

    public int invoke(Type1 arg1, Type1 arg2){
        final int[] result = {0};

        super.invoke(consumer -> {
            int r = consumer.compare(arg1, arg2);
            if(r > result[0]){
                result[0] = r;
            }
        });

        return result[0];
    }

    public void invoke(ArrayList<Type1> onCollection){
        super.invoke(onCollection::sort);
    }
}
