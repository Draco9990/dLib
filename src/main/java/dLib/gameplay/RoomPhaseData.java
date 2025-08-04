package dLib.gameplay;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import dLib.gameplay.templates.TRoomPhaseData;
import dLib.util.events.localevents.BiConsumerEvent;
import dLib.util.utils.GameplayUtils;

import java.io.Serializable;

public class RoomPhaseData extends TRoomPhaseData implements Serializable {
    static final long serialVersionUID = 1L;

    //region Variables

    public static BiConsumerEvent<RoomPhaseData, String> postRoomTypeIdentifiedGlobalEvent = new BiConsumerEvent<>();

    public static BiConsumerEvent<RoomPhaseData, RoomOutcome> postOutcomeChangeGlobalEvent = new BiConsumerEvent<>();

    //endregion Variables

    //region Constructors

    public RoomPhaseData(int infinityDepth, String act, int x, int y, int phase) {
        super(infinityDepth, act, x, y, phase);

        roomType.onValueChangedEvent.subscribe(outcome, (_o, outcome) -> postRoomTypeIdentifiedGlobalEvent.invoke(RoomPhaseData.this, outcome));

        outcome.onValueChangedEvent.subscribe(outcome, (_o, outcome) -> postOutcomeChangeGlobalEvent.invoke(RoomPhaseData.this, outcome));
    }

    public static void registerStaticEvents(){
        GameplayUtils.postRoomInitializeGlobalEvent.subscribeManaged((mapRoomNode, saveFile) -> RoomPhaseData.getCurrent().roomType.setValue(mapRoomNode.room.getClass().getSimpleName()));
    }

    //endregion

    //region Methods

    //region Static Getters

    public static RoomPhaseData getCurrent() {
        return getFor(SpireLocation.getCurrent());
    }

    public static RoomPhaseData getFor(SpireLocation location){
        return getFor(location.infinityCounter, location.act, location.x, location.y, location.phase);
    }
    public static RoomPhaseData getFor(int infinityDepth, String actName, int x, int y, int phase){
        return RoomData.getFor(infinityDepth, actName, x, y).getRoomPhase(phase);
    }

    //endregion Static Getters

    //endregion Methods

    //region Patches

    public static class Patches{
        @SpirePatch(clz = AbstractPlayer.class, method = "onVictory")
        public static class OnRoomClearedPatch{
            public static void Postfix(){
                if(AbstractDungeon.getCurrRoom().smoked){
                    RoomPhaseData.getCurrent().outcome.setValue(RoomOutcome.PLAYER_ESCAPED);
                }
                else{
                    RoomPhaseData.getCurrent().outcome.setValue(AbstractDungeon.getMonsters().areMonstersDead() ? RoomOutcome.COMBAT_CLEARED : RoomOutcome.COMBAT_ESCAPED);
                }
            }
        }
    }

    //endregion Patches
}
