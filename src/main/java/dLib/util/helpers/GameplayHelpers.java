package dLib.util.helpers;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.neow.NeowRoom;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import com.megacrit.cardcrawl.ui.buttons.ProceedButton;
import dLib.gameplay.GameplayInformationTracker;
import dLib.util.events.Event;
import dLib.util.events.localevents.BiConsumerEvent;
import dLib.util.events.localevents.ConsumerEvent;
import dLib.util.events.localevents.RunnableEvent;
import dLib.util.events.localevents.TriConsumerEvent;

import java.util.Objects;

public class GameplayHelpers {
    public static RunnableEvent postGameResetGlobalEvent = new RunnableEvent();

    public static BiConsumerEvent<AbstractDungeon, AbstractDungeon> postDungeonChangeGlobalEvent = new BiConsumerEvent<>();
    public static TriConsumerEvent<AbstractDungeon, AbstractDungeon, SaveFile> postDungeonEntryGlobalEvent = new TriConsumerEvent<>();

    public static ConsumerEvent<MapRoomNode> postRoomChangeGlobalEvent = new ConsumerEvent<>();
    public static BiConsumerEvent<MapRoomNode, SaveFile> postRoomInitializeGlobalEvent = new BiConsumerEvent<>();

    public static boolean isInARun(){
        return CardCrawlGame.dungeon != null && AbstractDungeon.player != null;
    }

    public static String getCurrentActName(){
        if (CardCrawlGame.dungeon == null) {
            return null;
        }

        return CardCrawlGame.dungeon.getClass().getSimpleName();
    }

    //region Patches

    //region Reset

    @SpirePatch2(clz = AbstractDungeon.class, method = "reset")
    private static class ResetPatch {
        @SpirePostfixPatch
        public static void Postfix(){
            enteringDoubleBoss = false;
            postGameResetGlobalEvent.invoke();
        }
    }

    //endregion Reset

    //region Dungeon Tracking

    @SpirePatch2(clz = CardCrawlGame.class, method = "getDungeon", paramtypez = { String.class, AbstractPlayer.class })
    private static class NewDungeonEntry1Patch {
        private static AbstractDungeon prevDungeon;

        @SpirePrefixPatch
        public static void Prefix(String key, AbstractPlayer p){
            prevDungeon = CardCrawlGame.dungeon;
        }

        @SpirePostfixPatch
        public static void Postfix(String key, AbstractPlayer p) {
            if(prevDungeon != null){
                postDungeonChangeGlobalEvent.invoke(prevDungeon, CardCrawlGame.dungeon);
            }
            postDungeonEntryGlobalEvent.invoke(prevDungeon, CardCrawlGame.dungeon, null);
            postRoomChangeGlobalEvent.invoke(AbstractDungeon.currMapNode);
        }
    }

    @SpirePatch2(clz = CardCrawlGame.class, method = "getDungeon", paramtypez = { String.class, AbstractPlayer.class, SaveFile.class })
    private static class NewDungeonEntry2Patch {
        private static AbstractDungeon prevDungeon;

        @SpirePrefixPatch
        public static void Prefix(String key, AbstractPlayer p, SaveFile saveFile){
            prevDungeon = CardCrawlGame.dungeon;
        }

        @SpirePostfixPatch
        public static void Postfix(String key, AbstractPlayer p, SaveFile saveFile) {
            postDungeonEntryGlobalEvent.invoke(prevDungeon, CardCrawlGame.dungeon, saveFile);
        }
    }

    //endregion Dungeon Tracking

    //region Room Tracking

    @SpirePatch(clz = AbstractDungeon.class, method = "setCurrMapNode")
    public static class OnRoomEntryPatch{
        public static void Postfix(MapRoomNode currMapNode){
            if(enteringDoubleBoss){
                enteringDoubleBoss = false;
                GameplayInformationTracker.increaseRoomPhase();
            }
            postRoomChangeGlobalEvent.invoke(currMapNode);
        }
    }

    @SpirePatch2(clz = AbstractDungeon.class, method = "nextRoomTransition", paramtypez = {com.megacrit.cardcrawl.saveAndContinue.SaveFile.class})
    public static class PostRoomGenerationPatch{
        @SpirePostfixPatch
        public static void Postfix(SaveFile saveFile){
            postRoomInitializeGlobalEvent.invoke(AbstractDungeon.currMapNode, saveFile);
        }
    }

    private static boolean enteringDoubleBoss = false;
    @SpirePatch(clz = ProceedButton.class, method = "goToDoubleBoss")
    public static class DoubleBossPatch{
        public static void Prefix(){
            enteringDoubleBoss = true;
        }
    }

    //endregion Room Tracking

    //endregion Patches
}
