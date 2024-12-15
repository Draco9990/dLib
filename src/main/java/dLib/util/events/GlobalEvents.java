package dLib.util.events;

import com.badlogic.gdx.utils.Disposable;
import dLib.ui.elements.UIElement;
import dLib.util.events.globalevents.GlobalEvent;
import dLib.util.events.globalevents.PostDisposeEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.function.Consumer;

public class GlobalEvents {
    //region Variables

    static ArrayList<Event<?>> registeredEvents = new ArrayList<>();

    private static HashMap<Class<? extends GlobalEvent>, Event<Consumer<GlobalEvent>>> subscriberMap = new HashMap<>();

    //endregion

    //region Methods

    public static void sendMessage(GlobalEvent message){
        if(subscriberMap.containsKey(message.getClass())){
            subscriberMap.get(message.getClass()).invoke(consumer -> consumer.accept(message));
        }

        if(message instanceof PostDisposeEvent){
            for(Event<?> event : registeredEvents){
                event.postObjectDisposed((PostDisposeEvent) message);
            }
        }
    }

    public static <T extends GlobalEvent> UUID subscribeManaged(Class<T> eventToListen, Consumer<T> consumer){
        if(!subscriberMap.containsKey(eventToListen)){
            subscriberMap.put(eventToListen, new Event<>());
        }

        return subscriberMap.get(eventToListen).subscribeManaged((Consumer<GlobalEvent>) consumer);
    }
    public static void unsubscribeManaged(Class<? extends GlobalEvent> eventToListen, UUID id){
        if(subscriberMap.containsKey(eventToListen)){
            subscriberMap.get(eventToListen).unsubscribeManaged(id);
        }
    }

    public static <T extends GlobalEvent> void subscribe(Disposable owner, Class<T> eventToListen, Consumer<T> consumer){
        if(!subscriberMap.containsKey(eventToListen)){
            subscriberMap.put(eventToListen, new Event<>());
        }

        subscriberMap.get(eventToListen).subscribe(owner, (Consumer<GlobalEvent>) consumer);
    }

    //endregion Methods
}
