package dLib.util.utils;

import basemod.BaseMod;
import basemod.abstracts.CustomSavable;
import basemod.interfaces.PreStartGameSubscriber;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.blights.Shield;
import com.megacrit.cardcrawl.blights.Spear;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.SaveHelper;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.monsters.ending.CorruptHeart;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import com.megacrit.cardcrawl.ui.buttons.ProceedButton;
import com.megacrit.cardcrawl.vfx.combat.BattleStartEffect;
import dLib.gameplay.GameRunData;
import dLib.gameplay.RoomPhaseData;
import dLib.gameplay.SpireLocation;
import dLib.util.HistoryProperty;
import dLib.util.Reflection;
import dLib.util.SerializationHelpers;
import dLib.util.events.localevents.BiConsumerEvent;
import dLib.util.events.localevents.ConsumerEvent;
import dLib.util.events.localevents.RunnableEvent;
import dLib.util.events.localevents.TriConsumerEvent;

public class GameplayUtils {
    //region Variables

    public static RunnableEvent postGameResetGlobalEvent = new RunnableEvent();

    public static BiConsumerEvent<AbstractDungeon, AbstractDungeon> postDungeonChangeGlobalEvent = new BiConsumerEvent<>();
    public static TriConsumerEvent<AbstractDungeon, AbstractDungeon, SaveFile> postDungeonEntryGlobalEvent = new TriConsumerEvent<>();

    public static ConsumerEvent<MapRoomNode> postRoomChangeGlobalEvent = new ConsumerEvent<>();
    public static BiConsumerEvent<MapRoomNode, SaveFile> postRoomInitializeGlobalEvent = new BiConsumerEvent<>();

    public static ConsumerEvent<Integer> postInfinityCycleIncreaseGlobalEvent = new ConsumerEvent<>();
    private static int infinityCounter = 0;

    public static ConsumerEvent<Integer> postRoomPhaseIncreaseGlobalEvent = new ConsumerEvent<>();
    private static int roomPhaseCounter = 0;

    public static RunnableEvent postHeartSlainGlobalEvent = new RunnableEvent();
    private static boolean heartSlain = false;

    public static BiConsumerEvent<SpireLocation, SpireLocation> postPlayerTeleportedGlobalEvent = new BiConsumerEvent<>();

    //endregion Variables

    //region Init

    static void init(){
        //Invoked through reflection to avoid making it available to users

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

        BaseMod.subscribe((PreStartGameSubscriber) () -> Reflection.setFieldValue("current", GameRunData.class, new GameRunData()));

        BaseMod.addSaveField("runData", new CustomSavable<String>() {
            @Override
            public String onSave() {
                return SerializationHelpers.toString(GameRunData.getCurrent());
            }

            @Override
            public void onLoad(String s) {
                Reflection.setFieldValue("current", GameRunData.class, SerializationHelpers.fromString(s));
            }
        });

        postDungeonChangeGlobalEvent.subscribeManaged((abstractDungeon, abstractDungeon2) -> {
            if(abstractDungeon2 instanceof Exordium && !teleporting.get()) {
                infinityCounter++;
                postInfinityCycleIncreaseGlobalEvent.invoke(infinityCounter);
            }
        });
        postGameResetGlobalEvent.subscribeManaged(() -> {
            infinityCounter = 0;
            roomPhaseCounter = 0;
        });

        postRoomChangeGlobalEvent.subscribeManaged(mapRoomNode -> roomPhaseCounter = 0);

        RoomPhaseData.registerStaticEvents();
    }

    //endregion Init

    public static boolean isInARun(){
        return CardCrawlGame.dungeon != null && AbstractDungeon.player != null;
    }

    public static int getInfinityCycle() {
        return infinityCounter;
    }

    public static String getCurrentActName(){
        if (CardCrawlGame.dungeon == null) {
            return null;
        }

        return CardCrawlGame.dungeon.getClass().getSimpleName();
    }

    public static void increaseRoomPhase() {
        roomPhaseCounter++;
        postRoomPhaseIncreaseGlobalEvent.invoke(roomPhaseCounter);
    }
    public static int getRoomPhase() {
        return roomPhaseCounter;
    }

    public static boolean isHeartSlain(){
        return heartSlain;
    }

    private static HistoryProperty<Boolean> teleporting = new HistoryProperty<>(false);
    public static void teleportTo(SpireLocation target){
        if(target.inSameRoomAndPhase()){
            return;
        }

        teleporting.set(true);

        prepareForRoomChange();

        SpireLocation curr = SpireLocation.getCurrent();
        if(target.infinityCounter != getInfinityCycle()){
            //We need to move infinity cycles
            infinityCounter = target.infinityCounter;
            if(AbstractDungeon.player.hasBlight(Shield.ID)) AbstractDungeon.player.getBlight(Shield.ID).increment = target.infinityCounter;
            if(AbstractDungeon.player.hasBlight(Spear.ID)) AbstractDungeon.player.getBlight(Spear.ID).increment = target.infinityCounter;
        }

        if(!target.inSameActAs(curr)){
            //Not in the same act, move acts
            AbstractDungeon d = new CardCrawlGame(CardCrawlGame.preferenceDir).getDungeon(target.act, AbstractDungeon.player);
            AbstractDungeon.firstRoomChosen = true;
        }

        if(target.isBossRoom()){
            teleportToActBossRoom();
        }
        else{
            MapRoomNode n = target.getMapRoomNode();
            if(n != null){
                teleportToRoom(n);
            }
        }

        refreshRoomUI();

        teleporting.set(false);

        postPlayerTeleportedGlobalEvent.invoke(curr, SpireLocation.getCurrent());
    }

    public static void teleportToActBossRoom(){
        SpireLocation curr = SpireLocation.getCurrent();
        boolean logTeleportEvent = false;
        if (!teleporting.get()) {
            prepareForRoomChange();
            logTeleportEvent = true;
        }
        teleporting.set(true);

        MapRoomNode node = new MapRoomNode(-1, 15);
        node.room = new MonsterRoomBoss();
        AbstractDungeon.nextRoom = node;
        if (AbstractDungeon.pathY.size() > 1) {
            AbstractDungeon.pathX.add(AbstractDungeon.pathX.get(AbstractDungeon.pathX.size() - 1));
            AbstractDungeon.pathY.add(AbstractDungeon.pathY.get(AbstractDungeon.pathY.size() - 1) + 1);
        } else {
            AbstractDungeon.pathX.add(1);
            AbstractDungeon.pathY.add(15);
        }

        AbstractDungeon.nextRoomTransitionStart();

        teleporting.revert();

        if(logTeleportEvent){
            postPlayerTeleportedGlobalEvent.invoke(curr, SpireLocation.getCurrent());
        }
    }

    public static void teleportToRoom(MapRoomNode n){
        SpireLocation curr = SpireLocation.getCurrent();
        boolean logTeleportEvent = false;
        if (!teleporting.get()) {
            prepareForRoomChange();
            logTeleportEvent = true;
        }
        teleporting.set(true);

        AbstractDungeon.nextRoom = n;
        AbstractDungeon.overlayMenu.endTurnButton.hide();
        Reflection.invokeMethod("nextRoomTransition", CardCrawlGame.dungeon);
        AbstractDungeon.overlayMenu.proceedButton.hideInstantly();
        SaveHelper.saveIfAppropriate(SaveFile.SaveType.ENTER_ROOM);

        teleporting.revert();
        if(logTeleportEvent){
            postPlayerTeleportedGlobalEvent.invoke(curr, SpireLocation.getCurrent());
        }
    }

    public static void prepareForRoomChange(){
        AbstractDungeon.player.hand.group.clear();
        AbstractDungeon.actionManager.clear();
        AbstractDungeon.effectsQueue.clear();
        AbstractDungeon.effectList.clear();

        for(int i = AbstractDungeon.topLevelEffects.size() - 1; i > 0; --i) {
            if (AbstractDungeon.topLevelEffects.get(i) instanceof BattleStartEffect) {
                AbstractDungeon.topLevelEffects.remove(i);
            }
        }

        AbstractDungeon.combatRewardScreen.clear();
        AbstractDungeon.previousScreen = null;
        AbstractDungeon.closeCurrentScreen();
        CardCrawlGame.music.silenceTempBgmInstantly();
        CardCrawlGame.music.silenceBGMInstantly();
        AbstractDungeon.overlayMenu.endTurnButton.hide();
    }

    public static void refreshRoomUI(){
        AbstractDungeon.overlayMenu.hideBlackScreen();
        AbstractDungeon.settingsScreen.open(false);
        AbstractDungeon.overlayMenu.cancelButton.hide();
        dLib.util.Reflection.invokeMethod("genericScreenOverlayReset", CardCrawlGame.dungeon);
        AbstractDungeon.settingsScreen.abandonPopup.hide();
        AbstractDungeon.settingsScreen.exitPopup.hide();
        AbstractDungeon.screen = AbstractDungeon.CurrentScreen.NONE;
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

    @SpirePatch(clz = CorruptHeart.class, method = "die")
    public static class HeartDeathPatch{
        @SpirePostfixPatch
        public static void Postfix(CorruptHeart __instance){
            if (!AbstractDungeon.getCurrRoom().cannotLose) {
                heartSlain = true;
                postHeartSlainGlobalEvent.invoke();
            }
        }
    }

    //region Reset

    @SpirePatch2(clz = AbstractDungeon.class, method = "reset")
    private static class ResetPatch {
        @SpirePostfixPatch
        public static void Postfix(){
            enteringDoubleBoss = false;
            GameplayUtils.postGameResetGlobalEvent.invoke();
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
                GameplayUtils.postDungeonChangeGlobalEvent.invoke(prevDungeon, CardCrawlGame.dungeon);
            }
            GameplayUtils.postDungeonEntryGlobalEvent.invoke(prevDungeon, CardCrawlGame.dungeon, null);
            GameplayUtils.postRoomChangeGlobalEvent.invoke(AbstractDungeon.currMapNode);
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
            GameplayUtils.postDungeonEntryGlobalEvent.invoke(prevDungeon, CardCrawlGame.dungeon, saveFile);
        }
    }

    //endregion Dungeon Tracking

    //region Room Tracking

    @SpirePatch(clz = AbstractDungeon.class, method = "setCurrMapNode")
    public static class OnRoomEntryPatch{
        public static void Postfix(MapRoomNode currMapNode){
            if(enteringDoubleBoss){
                enteringDoubleBoss = false;
                GameplayUtils.increaseRoomPhase();
            }
            GameplayUtils.postRoomChangeGlobalEvent.invoke(currMapNode);
        }
    }

    @SpirePatch2(clz = AbstractDungeon.class, method = "nextRoomTransition", paramtypez = {com.megacrit.cardcrawl.saveAndContinue.SaveFile.class})
    public static class PostRoomGenerationPatch{
        @SpirePostfixPatch
        public static void Postfix(SaveFile saveFile){
            GameplayUtils.postRoomInitializeGlobalEvent.invoke(AbstractDungeon.currMapNode, saveFile);
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
