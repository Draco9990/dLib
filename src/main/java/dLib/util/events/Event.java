package dLib.util.events;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import dLib.ui.elements.UIElement;
import dLib.util.events.serializableevents.SerializableRunnable;
import dLib.util.weak.SerializableWeakHashMap;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;

public class Event<EventType> implements Serializable {
    protected static boolean initialized = false;

    public static AbstractDungeon dungeonContext = null;
    public static AbstractRoom roomContext = null;
    public static AbstractEvent eventContext = null;
    public static AbstractRelic relicContext = null;

    protected Map<UUID, EventType> eventMap = Collections.synchronizedMap(new LinkedHashMap<>());

    protected SerializableWeakHashMap<Object, ArrayList<UUID>> boundObjects = new SerializableWeakHashMap<>();

    protected SerializableWeakHashMap<UIElement, ArrayList<UUID>> boundUIElements = new SerializableWeakHashMap<>();

    protected HashMap<Class<? extends AbstractDungeon>, HashMap<UUID, EventType>> boundDungeons = new HashMap<>();
    protected HashMap<Class<? extends AbstractRoom>, HashMap<UUID, EventType>> boundRooms = new HashMap<>();
    protected HashMap<Class<? extends AbstractEvent>, HashMap<UUID, EventType>> boundEvents = new HashMap<>();
    protected HashMap<Class<? extends AbstractRelic>, HashMap<UUID, EventType>> boundRelics = new HashMap<>();

    public UUID subscribeManaged(EventType event){
        UUID id = UUID.randomUUID();
        eventMap.put(id, event);
        return id;
    }
    public void unsubscribeManaged(UUID id){
        eventMap.remove(id);

        boundRelics.forEach((relicClass, eventMap) -> {
            eventMap.remove(id);
        });
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

    public UUID subscribeRelic(Class<? extends AbstractRelic> relic, EventType event){
        UUID id = UUID.randomUUID();

        if(!boundRelics.containsKey(relic)){
            boundRelics.put(relic, new HashMap<>());
        }
        boundRelics.get(relic).put(id, event);

        return id;
    }
    public void unsubscribeRelic(Class<? extends AbstractRelic> owner){
        boundRelics.remove(owner);
    }

    public UUID subscribeEvent(Class<? extends AbstractEvent> eventClass, EventType event){
        UUID id = UUID.randomUUID();

        if(!boundEvents.containsKey(eventClass)){
            boundEvents.put(eventClass, new HashMap<>());
        }
        boundEvents.get(eventClass).put(id, event);

        return id;
    }
    public void unsubscribeEvent(Class<? extends AbstractEvent> owner){
        boundEvents.remove(owner);
    }

    public UUID subscribeDungeon(Class<? extends AbstractDungeon> dungeonClass, EventType event){
        UUID id = UUID.randomUUID();

        if(!boundDungeons.containsKey(dungeonClass)){
            boundDungeons.put(dungeonClass, new HashMap<>());
        }
        boundDungeons.get(dungeonClass).put(id, event);

        return id;
    }
    public void unsubscribeDungeon(Class<? extends AbstractDungeon> owner){
        boundDungeons.remove(owner);
    }

    public UUID subscribeRoom(Class<? extends AbstractRoom> roomClass, EventType event){
        UUID id = UUID.randomUUID();

        if(!boundRooms.containsKey(roomClass)){
            boundRooms.put(roomClass, new HashMap<>());
        }
        boundRooms.get(roomClass).put(id, event);

        return id;
    }
    public void unsubscribeRoom(Class<? extends AbstractRoom> owner){
        boundRooms.remove(owner);
    }

    public void invoke(Consumer<EventType> consumer){
        if(!initialized){
            return;
        }

        //Events can modify subscribers
        ArrayList<UUID> keySet = new ArrayList<>(eventMap.keySet());
        for(UUID key : keySet){
            if(eventMap.containsKey(key)){
                consumer.accept(eventMap.get(key));
            }
        }

        if(CardCrawlGame.dungeon != null){
            Class<? extends AbstractDungeon> dungeonClass = CardCrawlGame.dungeon.getClass();
            if(boundDungeons.containsKey(dungeonClass)){
                dungeonContext = CardCrawlGame.dungeon;
                for(EventType event : boundDungeons.get(dungeonClass).values()){
                    consumer.accept(event);
                }
                dungeonContext = null;
            }
        }

        if(AbstractDungeon.getCurrMapNode() != null && AbstractDungeon.getCurrRoom() != null){
            Class<? extends AbstractRoom> roomClass = AbstractDungeon.getCurrRoom().getClass();
            if(boundRooms.containsKey(roomClass)){
                roomContext = AbstractDungeon.getCurrRoom();
                for(EventType event : boundRooms.get(roomClass).values()){
                    consumer.accept(event);
                }
                roomContext = null;
            }

            if(AbstractDungeon.getCurrRoom().event != null){
                Class<? extends AbstractEvent> eventClass = AbstractDungeon.getCurrRoom().event.getClass();
                if(boundEvents.containsKey(eventClass)){
                    eventContext = AbstractDungeon.getCurrRoom().event;
                    for(EventType event : boundEvents.get(eventClass).values()){
                        consumer.accept(event);
                    }
                    eventContext = null;
                }
            }
        }

        if(AbstractDungeon.player != null){
            for (AbstractRelic relic : AbstractDungeon.player.relics){
                Class<? extends AbstractRelic> relicClass = relic.getClass();
                if(boundRelics.containsKey(relicClass)){
                    relicContext = relic;
                    for(EventType event : boundRelics.get(relicClass).values()){
                        consumer.accept(event);
                    }
                    relicContext = null;
                }
            }
        }
    }
    public void invokeWhile(Function<EventType, Boolean> consumer){
        if(!initialized){
            return;
        }

        //Events can modify subscribers
        for(EventType event : eventMap.values()){
            if(!consumer.apply(event)){
                return; // Stop processing if the condition is not met
            }
        }

        if(CardCrawlGame.dungeon != null){
            Class<? extends AbstractDungeon> dungeonClass = CardCrawlGame.dungeon.getClass();
            if(boundDungeons.containsKey(dungeonClass)){
                dungeonContext = CardCrawlGame.dungeon;
                for(EventType event : boundDungeons.get(dungeonClass).values()){
                    if(!consumer.apply(event)){
                        return; // Stop processing if the condition is not met
                    }
                }
                dungeonContext = null;
            }
        }

        if(AbstractDungeon.getCurrMapNode() != null && AbstractDungeon.getCurrRoom() != null){
            Class<? extends AbstractRoom> roomClass = AbstractDungeon.getCurrRoom().getClass();
            if(boundRooms.containsKey(roomClass)){
                roomContext = AbstractDungeon.getCurrRoom();
                for(EventType event : boundRooms.get(roomClass).values()){
                    if(!consumer.apply(event)){
                        return; // Stop processing if the condition is not met
                    }
                }
                roomContext = null;
            }

            if(AbstractDungeon.getCurrRoom().event != null){
                Class<? extends AbstractEvent> eventClass = AbstractDungeon.getCurrRoom().event.getClass();
                if(boundEvents.containsKey(eventClass)){
                    eventContext = AbstractDungeon.getCurrRoom().event;
                    for(EventType event : boundEvents.get(eventClass).values()){
                        if(!consumer.apply(event)){
                            return; // Stop processing if the condition is not met
                        }
                    }
                    eventContext = null;
                }
            }
        }

        if(AbstractDungeon.player != null){
            for (AbstractRelic relic : AbstractDungeon.player.relics){
                Class<? extends AbstractRelic> relicClass = relic.getClass();
                if(boundRelics.containsKey(relicClass)){
                    relicContext = relic;
                    for(EventType event : boundRelics.get(relicClass).values()){
                        if(!consumer.apply(event)){
                            return; // Stop processing if the condition is not met
                        }
                    }
                    relicContext = null;
                }
            }
        }
    }

    public int count(){
        return eventMap.size();
    }

    public boolean hasBinding(Object owner) {
        return boundObjects.containsKey(owner);
    }

    @SpirePatch2(clz = CardCrawlGame.class, method = "update")
    public static class InitializePatch {
        @SpirePrefixPatch
        public static void Prefix() {
            initialized = true;
        }
    }
}
