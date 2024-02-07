package dLib.ui.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.OverlayMenu;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.mainMenu.MainMenuScreen;
import com.megacrit.cardcrawl.ui.panels.TopPanel;
import dLib.DLib;
import dLib.util.Reflection;
import dLib.util.Help;

public class ScreenManager {
    /** Variables */
    private static AbstractScreen screen;
    private static AbstractScreen pendingScreen;

    private static boolean hidingGameUI = false;
    public static AbstractDungeon.RenderScene cachedRenderScene = null;
    public static AbstractDungeon.CurrentScreen cachedScreen = null;

    /** Screen Open and Close */
    public static void openScreen(AbstractScreen newScreen){
        if(newScreen == null){
            DLib.logError("openScreen called with null newScreen. Stacktrace:");
            Help.Dev.printStacktrace(5);
        }

        if(screen != null){
            screen.onClose();
        }

        pendingScreen = newScreen;
    }
    private static void openPendingScreen(){
        screen = pendingScreen;

        if(CardCrawlGame.isInARun()){
            hideGameUI();
        }
        else{
            CardCrawlGame.mainMenuScreen.screen = ScreenOverridesEnum.CUSTOM_SCREEN;
        }

        screen.onOpen();
        pendingScreen = null;
    }

    public static void closeScreen(){
        if(screen != null){
            AbstractScreen toOpenOnClose = screen.getScreenToOpenOnClose();
            if(toOpenOnClose != null) openScreen(toOpenOnClose);

            screen.onClose();
            screen = null;

            if(CardCrawlGame.isInARun()){
                showGameUI();
            }
            else{
                CardCrawlGame.mainMenuScreen.screen = MainMenuScreen.CurScreen.MAIN_MENU;
            }
        }
    }

    private static void hideGameUI(){
        if(!hidingGameUI){
            cachedRenderScene = AbstractDungeon.rs;
            AbstractDungeon.rs = ScreenOverridesEnum.CUSTOM_SCENE;

            cachedScreen = AbstractDungeon.screen;
            AbstractDungeon.screen = ScreenOverridesEnum.CUSTOM_INGAME_SCREEN;

            hidingGameUI = true;
        }
    }
    private static void showGameUI(){
        if(hidingGameUI){
            if(cachedRenderScene != null) AbstractDungeon.rs = cachedRenderScene;
            if(cachedScreen != null) AbstractDungeon.screen = cachedScreen;
            hidingGameUI = false;

            if(CardCrawlGame.isInARun()){
                AbstractDungeon.overlayMenu.cancelButton.hide();
                Reflection.invokeMethod("genericScreenOverlayReset", AbstractDungeon.class);
            }
        }
    }

    public static AbstractScreen getCurrentScreen(){
        return screen;
    }

    /** Misc methods */
    public static void initializeGlobalStrings(){
        AbstractScreen.globalStrings = CardCrawlGame.languagePack.getUIString(DLib.getModID() + ":AbstractScreen");
    }

    /** Enums */
    public static class ScreenOverridesEnum {
        @SpireEnum
        public static MainMenuScreen.CurScreen CUSTOM_SCREEN;

        @SpireEnum
        public static AbstractDungeon.CurrentScreen CUSTOM_INGAME_SCREEN;

        @SpireEnum
        public static AbstractDungeon.RenderScene CUSTOM_SCENE;
    }

    /** Patches */
    static class ScreenRenderAndUpdatePatches{
        @SpirePatch(clz = MainMenuScreen.class, method = "update")
        public static class CustomScreenUpdatePatch_OutOfGame{
            @SpireInsertPatch(loc=273)
            public static void Insert(){
                if(!CardCrawlGame.isInARun()){
                    if(pendingScreen != null) openPendingScreen();

                    if(screen != null && CardCrawlGame.mainMenuScreen.screen == ScreenOverridesEnum.CUSTOM_SCREEN) screen.update();
                }
            }
        }

        @SpirePatch(clz = MainMenuScreen.class, method = "render")
        public static class CustomScreenRenderPatch_OutOfGame{
            @SpireInsertPatch(loc=616)
            public static void Insert(MainMenuScreen __instance, SpriteBatch sb){
                if(!CardCrawlGame.isInARun()){
                    if(screen != null && CardCrawlGame.mainMenuScreen.screen == ScreenOverridesEnum.CUSTOM_SCREEN) screen.render(sb);
                }
            }
        }

        @SpirePatch(clz = AbstractDungeon.class, method = "update")
        public static class CustomScreenUpdatePatch_InGame{
            public static void Postfix(){
                if(CardCrawlGame.isInARun()){
                    if(pendingScreen != null) openPendingScreen();
                    if(screen != null && CardCrawlGame.mainMenuScreen.screen == ScreenOverridesEnum.CUSTOM_SCREEN) screen.update();
                }
            }
        }

        @SpirePatch(clz = AbstractDungeon.class, method = "render")
        public static class CustomScreenRenderPatch_InGame{
            public static void Postfix(AbstractDungeon __instance, SpriteBatch sb){
                if(CardCrawlGame.isInARun()){
                    if(screen != null && CardCrawlGame.mainMenuScreen.screen == ScreenOverridesEnum.CUSTOM_SCREEN) screen.render(sb);
                }
            }
        }
    }
    static class InGameHideUIPatches{
        @SpirePatch(clz = AbstractPlayer.class, method = "renderRelics")
        public static class NoRelicRenderPatch{
            public static SpireReturn Prefix(){
                if(screen != null && (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.NONE ||  AbstractDungeon.screen == ScreenOverridesEnum.CUSTOM_INGAME_SCREEN)) {
                    return SpireReturn.Return(null);
                }
                return SpireReturn.Continue();
            }
        }

        @SpirePatch(clz = TopPanel.class, method = "update")
        public static class NoTopPanelUpdatePatch{
            public static SpireReturn Prefix(){
                if(screen != null && (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.NONE ||  AbstractDungeon.screen == ScreenOverridesEnum.CUSTOM_INGAME_SCREEN)) {
                    return SpireReturn.Return(null);
                }
                return SpireReturn.Continue();
            }
        }

        @SpirePatch(clz = OverlayMenu.class, method = "update")
        public static class NoOverlayMenuUpdatePatch{
            public static SpireReturn Prefix(OverlayMenu __instance){
                if(screen != null && (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.NONE || AbstractDungeon.screen == ScreenOverridesEnum.CUSTOM_INGAME_SCREEN)) {
                    return SpireReturn.Return(null);
                }
                return SpireReturn.Continue();
            }
        }

        @SpirePatch(clz = OverlayMenu.class, method = "render")
        public static class NoOverlayMenuRenderPatch{
            public static SpireReturn Prefix(OverlayMenu __instance, SpriteBatch sb){
                if(screen != null && (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.NONE ||  AbstractDungeon.screen == ScreenOverridesEnum.CUSTOM_INGAME_SCREEN)) {
                    return SpireReturn.Return(null);
                }
                return SpireReturn.Continue();
            }
        }
    }
}
