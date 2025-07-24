package dLib.gameplay;

import basemod.BaseMod;
import basemod.abstracts.CustomSavable;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.ui.buttons.ProceedButton;
import dLib.util.events.localevents.ConsumerEvent;
import dLib.util.events.serializableevents.SerializableConsumer;
import dLib.util.helpers.GameplayHelpers;

public class GameplayInformationTracker {

    public static ConsumerEvent<Integer> postInfinityCycleIncreaseGlobalEvent = new ConsumerEvent<>();
    private static int infinityCounter = 0;

    public static ConsumerEvent<Integer> postRoomPhaseIncreaseGlobalEvent = new ConsumerEvent<>();
    private static int roomPhaseCounter = 0;

    public static void init(){
        BaseMod.addSaveField("infinityCounter", new CustomSavable<Integer>() {
            @Override
            public Integer onSave() {
                return infinityCounter;
            }

            @Override
            public void onLoad(Integer integer) {
                infinityCounter = integer;
            }
        });

        GameplayHelpers.postDungeonChangeGlobalEvent.subscribeManaged((abstractDungeon, abstractDungeon2) -> {
            infinityCounter++;
            postInfinityCycleIncreaseGlobalEvent.invoke(infinityCounter);
        });
        GameplayHelpers.postGameResetGlobalEvent.subscribeManaged(() -> {
            infinityCounter = 0;
            roomPhaseCounter = 0;
        });

        GameplayHelpers.postRoomChangeGlobalEvent.subscribeManaged(mapRoomNode -> roomPhaseCounter = 0);
    }

    public static int getInfinityCycle() {
        return infinityCounter;
    }

    public static void increaseRoomPhase() {
        roomPhaseCounter++;
        postRoomPhaseIncreaseGlobalEvent.invoke(roomPhaseCounter);
    }
    public static int getRoomPhase() {
        return roomPhaseCounter;
    }

    //region Patches

    @SpirePatch(clz = AbstractImageEvent.class, method = "enterCombatFromImage")
    private static class EventRoomCombatPatch{
        public static void Postfix(){
            increaseRoomPhase();
        }
    }

    @SpirePatch(clz = AbstractEvent.class, method = "enterCombat")
    private static class EventRoomCombatEnterPatch{
        public static void Postfix(){
            increaseRoomPhase();
        }
    }

    @SpirePatch(clz = AbstractImageEvent.class, method = "enterImageFromCombat")
    public static class EventRoomCombatToEventPatch{
        public static void Postfix(){
            increaseRoomPhase();
        }
    }

    //endregion Patches
}
