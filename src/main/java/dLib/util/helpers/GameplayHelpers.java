package dLib.util.helpers;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import dLib.util.events.localevents.BiConsumerEvent;
import dLib.util.events.localevents.RunnableEvent;
import dLib.util.events.localevents.TriConsumerEvent;

public class GameplayHelpers {
    public static RunnableEvent postGameResetGlobalEvent = new RunnableEvent();

    public static BiConsumerEvent<AbstractDungeon, AbstractDungeon> postDungeonTransitionGlobalEvent = new BiConsumerEvent<>();
    public static TriConsumerEvent<AbstractDungeon, AbstractDungeon, SaveFile> postDungeonEntryGlobalEvent = new TriConsumerEvent<>();

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

    @SpirePatch2(clz = CardCrawlGame.class, method = "reset")
    private static class ResetPatch {
        @SpirePostfixPatch
        public static void Postfix(){
            postGameResetGlobalEvent.invoke();
        }
    }

    @SpirePatch2(clz = AbstractDungeon.class, method = "getDungeon", paramtypez = { String.class, AbstractPlayer.class })
    private static class NewDungeonEntry1Patch {
        private static AbstractDungeon prevDungeon;

        @SpirePrefixPatch
        public static void Prefix(String key, AbstractPlayer p){
            prevDungeon = CardCrawlGame.dungeon;
        }

        @SpirePostfixPatch
        public static void Postfix(String key, AbstractPlayer p) {
            postDungeonTransitionGlobalEvent.invoke(prevDungeon, CardCrawlGame.dungeon);
            postDungeonEntryGlobalEvent.invoke(prevDungeon, CardCrawlGame.dungeon, null);
        }
    }

    @SpirePatch2(clz = AbstractDungeon.class, method = "getDungeon", paramtypez = { String.class, AbstractPlayer.class, SaveFile.class })
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

    //endregion Patches
}
