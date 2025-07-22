package dLib.util.events;

import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;

public class Event<EventType> {
    public static ArrayList<Event<?>> allRegisteredEvents = new ArrayList<>();

    protected ConcurrentHashMap<UUID, EventType> subscribers = new ConcurrentHashMap<>();

    protected HashMap<Object, ArrayList<UUID>> boundsObjects = new HashMap<>();

    public Event(){
        allRegisteredEvents.add(this);
    }

    public UUID subscribeManaged(EventType event){
        UUID id = UUID.randomUUID();
        subscribers.put(id, event);
        return id;
    }

    public void unsubscribeManaged(UUID id){
        subscribers.remove(id);
    }

    public UUID subscribe(Object owner, EventType event){
        UUID id = UUID.randomUUID();
        subscribers.put(id, event);

        if(!boundsObjects.containsKey(owner)){
            boundsObjects.put(owner, new ArrayList<>());
        }
        boundsObjects.get(owner).add(id);

        return id;
    }

    public void unsubscribe(Object owner){
        if(boundsObjects.containsKey(owner)){
            for(UUID element : boundsObjects.get(owner)){
                subscribers.remove(element);
            }

            boundsObjects.remove(owner);
        }
    }

    public void invoke(Consumer<EventType> consumer){
        //Events can modify subscribers
        for(EventType event : subscribers.values()){
            consumer.accept(event);
        }
    }
    public void invokeWhile(Function<EventType, Boolean> consumer){
        //Events can modify subscribers
        for(EventType event : subscribers.values()){
            if(!consumer.apply(event)){
                return; // Stop processing if the condition is not met
            }
        }
    }

    public void postObjectDisposed(Disposable source){
        if(!boundsObjects.containsKey(source)){
            return;
        }

        for(UUID element : boundsObjects.get(source)){
            subscribers.remove(element);
        }

        boundsObjects.remove(source);
    }

    public int count(){
        return subscribers.size();
    }

    public boolean hasBinding(Object owner) {
        return boundsObjects.containsKey(owner);
    }
}
