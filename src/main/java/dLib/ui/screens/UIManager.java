package dLib.ui.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputActionSet;
import com.megacrit.cardcrawl.screens.mainMenu.MainMenuScreen;
import dLib.patches.InputHelpers;
import dLib.ui.ElementCalculationManager;
import dLib.ui.elements.UIElement;
import dLib.util.Help;
import dLib.util.Reflection;
import dLib.util.helpers.GameplayHelpers;

import java.util.ArrayList;

public class UIManager {

    //region Variables

    private static ArrayList<UIElement> uiElements = new ArrayList<>();
    private static ArrayList<UIElement> pendingClose = new ArrayList<>();

    private static MainMenuScreen.CurScreen cachedScreenMainMenu = null;
    private static AbstractDungeon.CurrentScreen cachedScreenInGame = null;
    private static AbstractDungeon.RenderScene cachedRenderScene = null;

    //endregion

    //region Class Methods

    public static void openUIElement(UIElement element){ //TODO draw 'focus' to the first element
        if(isOpen(element)){
            return;
        }

        if(element.overridesBaseScreen() && !hasBaseScreenOverriders()){
            cachedScreenMainMenu = CardCrawlGame.mainMenuScreen.screen;
            cachedScreenInGame = AbstractDungeon.screen;
            cachedRenderScene = AbstractDungeon.rs;

            CardCrawlGame.mainMenuScreen.screen = ScreenOverridesEnum.CUSTOM_SCREEN;
            AbstractDungeon.screen = ScreenOverridesEnum.CUSTOM_INGAME_SCREEN;
            AbstractDungeon.rs = ScreenOverridesEnum.CUSTOM_SCENE;
        }

        uiElements.add(element);
        if(element.shouldDrawFocusOnOpen()){
            UIElement selectedElement = getCurrentlySelectedElement();
            if(selectedElement != null){
                selectedElement.deselect();
            }

            selectNextElement(element, false);
        }

        pendingClose.remove(element);
    }
    public static void closeUIElement(UIElement element){
        pendingClose.add(element);
    }
    public static void onElementClosed(UIElement element){
        if(element.overridesBaseScreen() && !hasBaseScreenOverriders()){
            CardCrawlGame.mainMenuScreen.screen = cachedScreenMainMenu;
            AbstractDungeon.screen = cachedScreenInGame;
            AbstractDungeon.rs = cachedRenderScene;

            if(GameplayHelpers.isInARun()){
                AbstractDungeon.overlayMenu.cancelButton.hide();
                Reflection.invokeMethod("genericScreenOverlayReset", AbstractDungeon.class);
            }
        }
    }

    private static boolean hasBaseScreenOverriders(){
        for(UIElement uiElement : uiElements){
            if(uiElement.overridesBaseScreen()){
                return true;
            }
        }

        return false;
    }

    public static <UIElementClass> UIElementClass getOpenElementOfType(Class<UIElementClass> screenClass){
        for (UIElement uiElement : uiElements) {
            if(screenClass.isInstance(uiElement)){
                return (UIElementClass) uiElement;
            }
        }

        return null;

    }
    public static ArrayList<UIElement> getOpenElements(){
        return new ArrayList<>(uiElements);
    }

    public static boolean isOpen(UIElement element){
        return uiElements.contains(element);
    }

    //region Input Manager
    private static void updateInput(){
        if(Help.Input.isPressed(CInputActionSet.down, InputActionSet.down)) onDownPressed();
        if(Help.Input.isPressed(CInputActionSet.up, InputActionSet.up)) onUpPressed();
        if(Help.Input.isPressed(CInputActionSet.left, InputActionSet.left)) onLeftPressed();
        if(Help.Input.isPressed(CInputActionSet.right, InputActionSet.right)) onRightPressed();
        if(Help.Input.isPressed(CInputActionSet.proceed, InputActionSet.confirm)) onConfirmPressed();
        if(Help.Input.isPressed(CInputActionSet.cancel, InputActionSet.cancel)) onCancelPressed();
    }

    private static UIElement getCurrentlySelectedElement(){
        for(UIElement uiElement : uiElements){
            if(!uiElement.isActive()){
                continue;
            }

            UIElement selectedElement = uiElement.getSelectedChild();
            if(selectedElement == null){
                continue;
            }

            return selectedElement;
        }

        return null;
    }

    private static boolean selectNextElement(UIElement topParent, Boolean foundSelectedElement){
        ArrayList<UIElement> children = topParent.getAllSelectableChildren();
        for (int i = 0; i < children.size(); i++) {
            UIElement child = children.get(i);

            if (child.isSelected()) {
                Reflection.setFieldValue("value", foundSelectedElement, true);
                child.deselect();
            } else if (foundSelectedElement && !child.isSelected()) {
                child.select();
                return true;
            }

            if (foundSelectedElement && child.isModal()) {
                i--;
            }
        }

        return false;
    }

    private static boolean selectPreviousElement(UIElement topParent, Boolean foundSelectedElement){
        ArrayList<UIElement> children = topParent.getAllSelectableChildren();
        for (int i = children.size() - 1; i >= 0; i--) {
            UIElement child = children.get(i);

            if(child.isSelected()){
                Reflection.setFieldValue("value", foundSelectedElement, true);
                child.deselect();
            }
            else if(foundSelectedElement && !child.isSelected()){
                child.select();
                return true;
            }

            if(foundSelectedElement && child.isModal()){
                i++;
            }
        }

        return false;
    }

    private static void onDownPressed(){
        if(false){ // Interaction context
            UIElement selectedElement = getCurrentlySelectedElement();
            if(selectedElement != null){
                selectedElement.onDownInteraction();
            }
        }
        else{
            Boolean foundSelectedElement = false;

            for(UIElement uiElement : uiElements){
                if(!uiElement.isActive()){
                    continue;
                }

                if(selectNextElement(uiElement, foundSelectedElement)){
                    return;
                }
            }
        }
    }
    private static void onUpPressed(){
        if(false){ // Interaction context
            UIElement selectedElement = getCurrentlySelectedElement();
            if(selectedElement != null){
                selectedElement.onUpInteraction();
            }
        }
        else{
            Boolean foundSelectedElement = false;

            for(UIElement uiElement : uiElements){
                if(!uiElement.isActive()){
                    continue;
                }

                if(selectPreviousElement(uiElement, foundSelectedElement)){
                    return;
                }
            }
        }
    }
    private static void onLeftPressed(){
        UIElement selectedElement = getCurrentlySelectedElement();
        if(selectedElement != null){
            selectedElement.onLeftInteraction();
        }
    }
    private static void onRightPressed(){
        UIElement selectedElement = getCurrentlySelectedElement();
        if(selectedElement != null){
            selectedElement.onRightInteraction();
        }
    }

    private static void onConfirmPressed(){
        UIElement selectedElement = getCurrentlySelectedElement();
        if(selectedElement != null){
            selectedElement.onConfirmInteraction();
        }
    }
    private static void onCancelPressed(){
        UIElement selectedElement = getCurrentlySelectedElement();
        if(selectedElement != null){
            selectedElement.onCancelInteraction();
        }
    }



    //endregion

    //endregion

    //region Patches
    static class ScreenRenderAndUpdatePatches{
        @SpirePatch(clz = MainMenuScreen.class, method = "update")
        public static class CustomScreenUpdatePatch_OutOfGame{
            @SpireInsertPatch(rloc=6)
            public static void Insert(){
                if(!CardCrawlGame.isInARun()){
                    InputHelpers.alreadyHovered = false;

                    for(int i = uiElements.size() - 1; i >= 0; i--){
                        ElementCalculationManager.calculateElementPositionAndDimension(uiElements.get(i));
                        uiElements.get(i).update();
                    }
                    updateInput();

                    for(UIElement pendingCloseElement : pendingClose){
                        uiElements.remove(pendingCloseElement);
                        onElementClosed(pendingCloseElement);
                    }
                    pendingClose.clear();
                }
            }
        }

        @SpirePatch(clz = MainMenuScreen.class, method = "render")
        public static class CustomScreenRenderPatch_OutOfGame{
            @SpirePostfixPatch
            public static void Postfix(MainMenuScreen __instance, SpriteBatch sb){
                if(!CardCrawlGame.isInARun()){
                    for(int i = 0; i < uiElements.size(); i++){
                        uiElements.get(i).render(sb);
                    }
                }
            }
        }

        @SpirePatch(clz = AbstractDungeon.class, method = "update")
        public static class CustomScreenUpdatePatch_InGame{
            public static void Postfix(){
                if(CardCrawlGame.isInARun()){
                    InputHelpers.alreadyHovered = false;

                    for (int i = uiElements.size() - 1; i >= 0; i--) {
                        ElementCalculationManager.calculateElementPositionAndDimension(uiElements.get(i));
                        uiElements.get(i).update();
                    }
                    updateInput();

                    for(UIElement pendingCloseElement : pendingClose){
                        uiElements.remove(pendingCloseElement);
                        onElementClosed(pendingCloseElement);
                    }
                    pendingClose.clear();
                }
            }
        }

        @SpirePatch(clz = AbstractDungeon.class, method = "render")
        public static class CustomScreenRenderPatch_InGame{
            public static void Postfix(AbstractDungeon __instance, SpriteBatch sb){
                if(CardCrawlGame.isInARun()){
                    for (int i = 0; i < uiElements.size(); i++) {
                        uiElements.get(i).render(sb);
                    }
                }
            }
        }
    }
    //endregion

    public static class ScreenOverridesEnum {
        @SpireEnum
        public static MainMenuScreen.CurScreen CUSTOM_SCREEN;

        @SpireEnum
        public static AbstractDungeon.CurrentScreen CUSTOM_INGAME_SCREEN;

        @SpireEnum
        public static AbstractDungeon.RenderScene CUSTOM_SCENE;
    }
}
