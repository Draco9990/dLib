package dLib.util.events.localevents;

import dLib.util.events.Event;
import dLib.util.events.serializableevents.SerializableRunnable;

public class RunnableEvent extends Event<SerializableRunnable> {
    public RunnableEvent() {
        super();
    }

    public void invoke(){
        super.invoke(Runnable::run);
    }
}
