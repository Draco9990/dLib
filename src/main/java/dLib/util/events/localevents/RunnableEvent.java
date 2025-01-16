package dLib.util.events.localevents;

import dLib.util.events.Event;

public class RunnableEvent extends Event<Runnable> {
    public RunnableEvent() {
        super();
    }

    public void invoke(){
        super.invoke(Runnable::run);
    }
}
