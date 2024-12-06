package dLib.util;

import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Interactable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.Consumer;

public class GlobalEvents {
    /** Variables */
    private static HashMap<Class<?>, ArrayList<Consumer<Object>>> subscriberMap = new HashMap<>();

    /** Methods */
    public static void sendMessage(Object message){
        if(subscriberMap.containsKey(message.getClass())){
            for(Consumer<Object> listener : subscriberMap.get(message.getClass())){
                listener.accept(message);
            }
        }
    }

    public static <T> void subscribe(Class<T> eventToListen, Consumer<T> consumer){
        if(!subscriberMap.containsKey(eventToListen)){
            subscriberMap.put(eventToListen, new ArrayList<>());
        }

        ArrayList<Consumer<Object>> listeners = subscriberMap.get(eventToListen);
        listeners.add((Consumer<Object>) consumer);

        subscriberMap.put(eventToListen, listeners);
    }

    /** Events */
    public static class Events{
        public static class PreLeftClickEvent {
            public UIElement source;

            public PreLeftClickEvent(UIElement source){
                this.source = source;
            }
        }
    }
}
