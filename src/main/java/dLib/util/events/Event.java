package dLib.util.events;

import dLib.ui.elements.UIElement;
import dLib.util.events.serializableevents.SerializableRunnable;
import dLib.util.weak.SerializableWeakHashMap;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;

public class Event<EventType> implements Serializable {
    protected Map<UUID, EventType> eventMap = Collections.synchronizedMap(new LinkedHashMap<>());

    protected SerializableWeakHashMap<Object, ArrayList<UUID>> boundObjects = new SerializableWeakHashMap<>();

    protected SerializableWeakHashMap<UIElement, ArrayList<UUID>> boundUIElements = new SerializableWeakHashMap<>();

    public UUID subscribeManaged(EventType event){
        UUID id = UUID.randomUUID();
        eventMap.put(id, event);
        return id;
    }
    public void unsubscribeManaged(UUID id){
        eventMap.remove(id);
    }

    public UUID subscribe(Object owner, EventType event){
        UUID id = UUID.randomUUID();
        eventMap.put(id, event);

        if(!boundObjects.containsKey(owner)){
            boundObjects.put(owner, new ArrayList<>());
        }
        boundObjects.get(owner).add(id);

        return id;
    }
    public void unsubscribe(Object owner){
        if(boundObjects.containsKey(owner)){
            for(UUID element : boundObjects.get(owner)){
                eventMap.remove(element);
            }

            boundObjects.remove(owner);
        }
    }

    public UUID subscribe(UIElement element, EventType event){
        UUID id = UUID.randomUUID();
        eventMap.put(id, event);

        if(!boundUIElements.containsKey(element)){
            boundUIElements.put(element, new ArrayList<>());

            element.postDisposedEvent.subscribe(this, (SerializableRunnable) () -> unsubscribe(element));
        }
        boundUIElements.get(element).add(id);

        return id;
    }
    public void unsubscribe(UIElement owner){
        if(boundUIElements.containsKey(owner)){
            for(UUID element : boundUIElements.get(owner)){
                eventMap.remove(element);
            }

            boundUIElements.remove(owner);

            owner.postDisposedEvent.unsubscribe(this);
        }
    }

    public void invoke(Consumer<EventType> consumer){
        //Events can modify subscribers
        ArrayList<UUID> keySet = new ArrayList<>(eventMap.keySet());
        for(UUID key : keySet){
            if(eventMap.containsKey(key)){
                consumer.accept(eventMap.get(key));
            }
        }
    }
    public void invokeWhile(Function<EventType, Boolean> consumer){
        //Events can modify subscribers
        for(EventType event : eventMap.values()){
            if(!consumer.apply(event)){
                return; // Stop processing if the condition is not met
            }
        }
    }

    public int count(){
        return eventMap.size();
    }

    public boolean hasBinding(Object owner) {
        return boundObjects.containsKey(owner);
    }
}
