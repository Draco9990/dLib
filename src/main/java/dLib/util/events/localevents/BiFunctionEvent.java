package dLib.util.events.localevents;

import dLib.util.events.Event;
import dLib.util.events.serializableevents.SerializableBiFunction;
import dLib.util.events.serializableevents.SerializableFunction;

import java.util.ArrayList;

public class BiFunctionEvent<Type1, Type2, Type3> extends Event<SerializableBiFunction<Type1, Type2, Type3>> {
    public BiFunctionEvent() {
        super();
    }

    public ArrayList<Type3> invoke(Type1 arg1, Type2 arg2){
        ArrayList<Type3> results = new ArrayList<>();

        super.invoke(consumer -> {
            results.add(consumer.apply(arg1, arg2));
        });

        return results;
    }

    public ArrayList<Type3> invokeWhile(Type1 arg1, Type2 arg2, SerializableFunction<Type3, Boolean> condition){
        ArrayList<Type3> results = new ArrayList<>();

        super.invokeWhile(consumer -> {
            Type3 result = consumer.apply(arg1, arg2);
            results.add(result);

            return condition.apply(result);
        });

        return results;
    }
}
