package dLib.util;

import dLib.ui.elements.UIElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.function.Consumer;

public class UIElementEvent<EventType> {
    private HashMap<UUID, EventType> subscribers = new HashMap<>();

    private HashMap<UIElement, ArrayList<UUID>> boundsObjects = new HashMap<>();

    public UIElementEvent(){
        GlobalEvents.subscribe(GlobalEvents.Events.PostElementDestroyEvent.class, event -> {
            for(UUID element : boundsObjects.getOrDefault(event.source, new ArrayList<>())){
                subscribers.remove(element);
            }

            boundsObjects.remove(event.source);
        });
    }

    public UUID subscribeManaged(EventType event){
        UUID id = UUID.randomUUID();
        subscribers.put(id, event);
        return id;
    }

    public void unsubscribeManaged(UUID id){
        subscribers.remove(id);
    }

    public UUID subscribe(UIElement owner, EventType event){
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
}
