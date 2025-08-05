package dLib.util.events;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import dLib.ui.elements.UIElement;
import dLib.util.Reflection;
import dLib.util.events.serializableevents.SerializableRunnable;
import dLib.util.weak.SerializableWeakHashMap;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;

public class Event<EventType> implements Serializable {
    protected static boolean initialized = false;

    private static AbstractDungeon dungeonContext = null;
    public static <T extends AbstractDungeon> T getDungeonContext() { return (T) dungeonContext; }
    private static AbstractRoom roomContext = null;
    public static <T extends AbstractRoom> T getRoomContext() { return (T) roomContext; }
    private static AbstractEvent eventContext = null;
    public static <T extends AbstractEvent> T getEventContext() { return (T) eventContext; }
    private static AbstractMonster monsterContext = null;
    public static <T extends AbstractMonster> T getMonsterContext() { return (T) monsterContext; }
    private static AbstractRelic relicContext = null;
    public static <T extends AbstractRelic> T getRelicContext() { return (T) relicContext; }
    private static AbstractPower powerContext = null;
    public static <T extends AbstractPower> T getPowerContext() { return (T) powerContext; }

    protected Map<UUID, EventType> eventMap = Collections.synchronizedMap(new LinkedHashMap<>());

    protected SerializableWeakHashMap<Object, ArrayList<UUID>> boundObjects = new SerializableWeakHashMap<>();

    protected SerializableWeakHashMap<UIElement, ArrayList<UUID>> boundUIElements = new SerializableWeakHashMap<>();

    protected HashMap<Class<? extends AbstractDungeon>, HashMap<UUID, EventType>> boundDungeons = new HashMap<>();
    protected HashMap<Class<? extends AbstractRoom>, HashMap<UUID, EventType>> boundRooms = new HashMap<>();
    protected HashMap<Class<? extends AbstractEvent>, HashMap<UUID, EventType>> boundEvents = new HashMap<>();
    protected HashMap<Class<? extends AbstractMonster>, HashMap<UUID, EventType>> boundMonsters = new HashMap<>();
    protected HashMap<Class<? extends AbstractRelic>, HashMap<UUID, EventType>> boundRelics = new HashMap<>();
    protected HashMap<Class<? extends AbstractPower>, HashMap<UUID, EventType>> boundPowers = new HashMap<>();

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

    public UUID subscribeMonster(Class<? extends AbstractMonster> monsterClass, EventType event){
        UUID id = UUID.randomUUID();

        if(!boundMonsters.containsKey(monsterClass)){
            boundMonsters.put(monsterClass, new HashMap<>());
        }
        boundMonsters.get(monsterClass).put(id, event);

        return id;
    }
    public void unsubscribeMonster(Class<? extends AbstractMonster> owner){
        boundMonsters.remove(owner);
    }

    public UUID subscribePower(Class<? extends AbstractPower> powerClass, EventType event){
        UUID id = UUID.randomUUID();

        if(!boundPowers.containsKey(powerClass)){
            boundPowers.put(powerClass, new HashMap<>());
        }
        boundPowers.get(powerClass).put(id, event);

        return id;
    }
    public void unsubscribePower(Class<? extends AbstractPower> owner){
        boundPowers.remove(owner);
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
            for(Class<?> dungeonClass : Reflection.getClassHierarchy(CardCrawlGame.dungeon.getClass())){
                if(boundDungeons.containsKey(dungeonClass)){
                    dungeonContext = CardCrawlGame.dungeon;
                    for(EventType event : boundDungeons.get(dungeonClass).values()){
                        consumer.accept(event);
                    }
                    dungeonContext = null;
                }
            }
        }

        if(AbstractDungeon.getCurrMapNode() != null && AbstractDungeon.getCurrRoom() != null){
            for(Class<?> roomClass : Reflection.getClassHierarchy(AbstractDungeon.getCurrRoom().getClass())){
                if(boundRooms.containsKey(roomClass)){
                    roomContext = AbstractDungeon.getCurrRoom();
                    for(EventType event : boundRooms.get(roomClass).values()){
                        consumer.accept(event);
                    }
                    roomContext = null;
                }
            }

            if(AbstractDungeon.getCurrRoom().event != null){
                for(Class<?> eventClass : Reflection.getClassHierarchy(AbstractDungeon.getCurrRoom().event.getClass())){
                    if(boundEvents.containsKey(eventClass)){
                        eventContext = AbstractDungeon.getCurrRoom().event;
                        for(EventType event : boundEvents.get(eventClass).values()){
                            consumer.accept(event);
                        }
                        eventContext = null;
                    }
                }
            }

            if(AbstractDungeon.getCurrRoom().monsters != null && AbstractDungeon.getCurrRoom().monsters.monsters != null){
                for (AbstractPower p : AbstractDungeon.player.powers){
                    for(Class<?> powerClass : Reflection.getClassHierarchy(p.getClass())){
                        if(boundPowers.containsKey(powerClass)){
                            powerContext = p;
                            for(EventType event : boundPowers.get(powerClass).values()){
                                consumer.accept(event);
                            }
                            powerContext = null;
                        }
                    }
                }

                for(AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters){
                    if(monster.isDeadOrEscaped()){
                        continue;
                    }

                    for(Class<?> monsterClass : Reflection.getClassHierarchy(monster.getClass())){
                        if(boundMonsters.containsKey(monsterClass)){
                            monsterContext = monster;
                            for(EventType event : boundMonsters.get(monsterClass).values()){
                                consumer.accept(event);
                            }
                            monsterContext = null;
                        }
                    }

                    for(AbstractPower p : monster.powers){
                        for(Class<?> powerClass : Reflection.getClassHierarchy(p.getClass())){
                            if(boundPowers.containsKey(powerClass)){
                                powerContext = p;
                                for(EventType event : boundPowers.get(powerClass).values()){
                                    consumer.accept(event);
                                }
                                powerContext = null;
                            }
                        }
                    }
                }
            }
        }

        if(AbstractDungeon.player != null){
            for (AbstractRelic relic : AbstractDungeon.player.relics){
                for(Class<?> relicClass : Reflection.getClassHierarchy(relic.getClass())){
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
            for(Class<?> dungeonClass : Reflection.getClassHierarchy(CardCrawlGame.dungeon.getClass())){
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
        }

        if(AbstractDungeon.getCurrMapNode() != null && AbstractDungeon.getCurrRoom() != null){
            for(Class<?> roomClass : Reflection.getClassHierarchy(AbstractDungeon.getCurrRoom().getClass())){
                if(boundRooms.containsKey(roomClass)){
                    roomContext = AbstractDungeon.getCurrRoom();
                    for(EventType event : boundRooms.get(roomClass).values()){
                        if(!consumer.apply(event)){
                            return; // Stop processing if the condition is not met
                        }
                    }
                    roomContext = null;
                }
            }

            if(AbstractDungeon.getCurrRoom().event != null){
                for(Class<?> eventClass : Reflection.getClassHierarchy(AbstractDungeon.getCurrRoom().event.getClass())){
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

            if(AbstractDungeon.getCurrRoom().monsters != null && AbstractDungeon.getCurrRoom().monsters.monsters != null){
                for (AbstractPower p : AbstractDungeon.player.powers){
                    for(Class<?> powerClass : Reflection.getClassHierarchy(p.getClass())){
                        if(boundPowers.containsKey(powerClass)){
                            powerContext = p;
                            for(EventType event : boundPowers.get(powerClass).values()){
                                if(!consumer.apply(event)){
                                    return; // Stop processing if the condition is not met
                                }
                            }
                            powerContext = null;
                        }
                    }
                }

                for(AbstractMonster monster : AbstractDungeon.getCurrRoom().monsters.monsters){
                    if(monster.isDeadOrEscaped()){
                        continue;
                    }

                    for(Class<?> monsterClass : Reflection.getClassHierarchy(monster.getClass())){
                        if(boundMonsters.containsKey(monsterClass)){
                            monsterContext = monster;
                            for(EventType event : boundMonsters.get(monsterClass).values()){
                                if(!consumer.apply(event)){
                                    return; // Stop processing if the condition is not met
                                }
                            }
                            monsterContext = null;

                            for(AbstractPower p : monster.powers){
                                for(Class<?> powerClass : Reflection.getClassHierarchy(p.getClass())){
                                    if(boundPowers.containsKey(powerClass)){
                                        powerContext = p;
                                        for(EventType event : boundPowers.get(powerClass).values()){
                                            if(!consumer.apply(event)){
                                                return; // Stop processing if the condition is not met
                                            }
                                        }
                                        powerContext = null;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if(AbstractDungeon.player != null){
            for (AbstractRelic relic : AbstractDungeon.player.relics){
                for(Class<?> relicClass : Reflection.getClassHierarchy(relic.getClass())){
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
