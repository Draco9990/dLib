package dLib.util.events.localevents;

import dLib.util.events.Event;

import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class FunctionEvent<Type1, Type2> extends Event<Function<Type1, Type2>> {
    public FunctionEvent() {
        super();
    }

    public ArrayList<Type2> invoke(Type1 arg1){
        ArrayList<Type2> results = new ArrayList<>();

        super.invoke(consumer -> {
            results.add(consumer.apply(arg1));
        });

        return results;
    }
}
