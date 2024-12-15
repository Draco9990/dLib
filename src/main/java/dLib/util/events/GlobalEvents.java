package dLib.util.events;

import com.badlogic.gdx.utils.Disposable;
import dLib.ui.elements.UIElement;
import dLib.util.events.globalevents.GlobalEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

public class GlobalEvents {
    /** Variables */
    private static HashMap<Class<? extends GlobalEvent>, ArrayList<Consumer<GlobalEvent>>> subscriberMap = new HashMap<>();

    /** Methods */
    public static void sendMessage(GlobalEvent message){
        if(subscriberMap.containsKey(message.getClass())){
            for(Consumer<GlobalEvent> listener : subscriberMap.get(message.getClass())){
                listener.accept(message);
            }
        }
    }

    public static <T extends GlobalEvent> void subscribe(Class<T> eventToListen, Consumer<T> consumer){
        if(!subscriberMap.containsKey(eventToListen)){
            subscriberMap.put(eventToListen, new ArrayList<>());
        }

        ArrayList<Consumer<GlobalEvent>> listeners = subscriberMap.get(eventToListen);
        listeners.add((Consumer<GlobalEvent>) consumer);

        subscriberMap.put(eventToListen, listeners);
    }

    /** Events */
    public static class Events{
        public static class PreLeftClickEvent extends GlobalEvent{
            public UIElement source;

            public PreLeftClickEvent(UIElement source){
                this.source = source;
            }
        }

        public static class PreHoverEvent extends GlobalEvent{
            public UIElement source;

            public PreHoverEvent(UIElement source){
                this.source = source;
            }
        }

        public static class PreForceFocusChangeEvent extends GlobalEvent{
            public UIElement source;

            public PreForceFocusChangeEvent(UIElement source){
                this.source = source;
            }
        }
    }
}
