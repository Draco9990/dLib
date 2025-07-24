package dLib.gameplay;

import basemod.BaseMod;
import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import dLib.util.events.localevents.ConsumerEvent;
import dLib.util.events.serializableevents.SerializableBiConsumer;
import dLib.util.helpers.GameplayHelpers;

public class GameplayInformationTracker {
    public static ConsumerEvent<Integer> postInfinityCycleIncreaseGlobalEvent = new ConsumerEvent<>();

    private static int infinityCounter = 0;

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

        GameplayHelpers.postDungeonTransitionGlobalEvent.subscribeManaged((abstractDungeon, abstractDungeon2) -> {
            infinityCounter++;
            postInfinityCycleIncreaseGlobalEvent.invoke(infinityCounter);
        });

        GameplayHelpers.postGameResetGlobalEvent.subscribeManaged(() -> {
            infinityCounter = 0;
        });
    }

    //region Infinity Depth Tracking

    public static int getInfinityCycle() {
        return infinityCounter;
    }

    //endregion Infinity Depth Tracking
}
