package dLib.util.events.globalevents;

import dLib.util.events.localevents.ConsumerEvent;

public interface Constructable {
    ConsumerEvent<Constructable> postConstructGlobalEvent = new ConsumerEvent<>();

    default void postConstruct(){

    }
}
