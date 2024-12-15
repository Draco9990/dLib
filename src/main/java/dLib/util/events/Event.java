package dLib.util.events;

import com.badlogic.gdx.utils.Disposable;
import dLib.util.events.globalevents.PostDisposeEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class Event<EventType> {
    private ConcurrentHashMap<UUID, EventType> subscribers = new ConcurrentHashMap<>();

    private ConcurrentHashMap<Disposable, ArrayList<UUID>> boundsObjects = new ConcurrentHashMap<>();

    public Event(){
        GlobalEvents.registeredEvents.add(this);
    }

    public UUID subscribeManaged(EventType event){
        UUID id = UUID.randomUUID();
        subscribers.put(id, event);
        return id;
    }

    public void unsubscribeManaged(UUID id){
        subscribers.remove(id);
    }

    public UUID subscribe(Disposable owner, EventType event){
        UUID id = UUID.randomUUID();
        subscribers.put(id, event);

        if(!boundsObjects.containsKey(owner)){
            boundsObjects.put(owner, new ArrayList<>());
        }
        boundsObjects.get(owner).add(id);

        return id;
    }

    public void invoke(Consumer<EventType> consumer){
        for(EventType event : subscribers.values()){
            consumer.accept(event);
        }
    }

    void postObjectDisposed(PostDisposeEvent event){
        for(UUID element : boundsObjects.getOrDefault(event.source, new ArrayList<>())){
            subscribers.remove(element);
        }

        boundsObjects.remove(event.source);
    }
}
