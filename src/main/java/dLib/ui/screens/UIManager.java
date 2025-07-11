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
import dLib.patches.KeyInputEventPatches;
import dLib.properties.objects.Property;
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

    public static void openUIElement(UIElement element){
        if(isOpen(element)){
            return;
        }

        boolean shouldAnimate = element.isVisible();
        if(shouldAnimate){
            element.hideAndDisableInstantly();
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
        pendingClose.remove(element);

        if(shouldAnimate){
            element.showAndEnable();
        }

        UIElement currentlySelectedElement = getCurrentlySelectedElement();
        if(currentlySelectedElement != null && currentlySelectedElement.isControllerSelected()){
            drawControllerFocusCond(element);
        }

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

    public static void drawControllerFocusCond(UIElement target){
        if(!target.isActive()){
            return;
        }

        UIElement selectedElement = getCurrentlySelectedElement();
        boolean controllerFocus;
        if(selectedElement != null){
            controllerFocus = selectedElement.isControllerSelected();
            if(!controllerFocus){
                return;
            }

            selectedElement.deselect();
        }

        selectNextElement(target, new Property<>(null));
    }

    public static void loseFocus(){
        UIElement selectedElement = getCurrentlySelectedElement();
        if(selectedElement != null){
            boolean controllerSelected = selectedElement.isControllerSelected();

            selectedElement.deselect();

            if(controllerSelected){
                selectNextElement(uiElements, new Property<>(null));
            }
        }
    }

    public static void loseFocus(UIElement focused){
        UIElement selectedElement = focused.getSelectedChild();
        if(selectedElement != null){
            boolean controllerSelected = selectedElement.isControllerSelected();

            selectedElement.deselect();

            if(controllerSelected){
                selectNextElement(uiElements, new Property<>(null));
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
        KeyInputEventPatches.linkProxyInput.set(true);

        if(Help.Input.isPressed(CInputActionSet.down, InputActionSet.down)) onDownPressed();
        if(Help.Input.isPressed(CInputActionSet.up, InputActionSet.up)) onUpPressed();
        if(Help.Input.isPressed(CInputActionSet.left, InputActionSet.left)) onLeftPressed();
        if(Help.Input.isPressed(CInputActionSet.right, InputActionSet.right)) onRightPressed();
        if(Help.Input.isPressed(CInputActionSet.proceed, InputActionSet.confirm)) onConfirmPressed();
        if(Help.Input.isPressed(CInputActionSet.cancel, InputActionSet.cancel)) onCancelPressed();

        KeyInputEventPatches.linkProxyInput.revert();
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

    private static boolean selectNextElement(UIElement topParent, Property<UIElement> foundSelectedElement){
        return selectNextElement(new ArrayList<UIElement>(){{ add(topParent); }}, foundSelectedElement);
    }
    private static boolean selectNextElement(ArrayList<UIElement> topParents, Property<UIElement> foundSelectedElement){
        boolean firstPass = true;
        while(true){
            for (UIElement topParent : topParents) {
                if(!topParent.isActive()){
                    continue;
                }

                ArrayList<UIElement> children = topParent.getAllSelectableChildren();

                for (int i = 0; i < children.size(); i++) {
                    UIElement child = children.get(i);

                    if (child.isSelected()) {
                        if(foundSelectedElement.getValue() == child){
                            return false; // We looped back over, current element is the ONLY selectable element
                        }

                        foundSelectedElement.setValue(child);
                    } else if ((!foundSelectedElement.isNull() || !firstPass) && !child.isSelected()) {
                        if(!foundSelectedElement.isNull()) foundSelectedElement.getValue().deselect();
                        child.select(true);
                        return true;
                    }

                    if (!foundSelectedElement.isNull() && child.isControllerModal()) {
                        i--;
                    }
                }
            }

            if(foundSelectedElement.isNull() && !firstPass){
                return false; // No selectable elements found
            }

            firstPass = false;
        }
    }

    private static boolean selectPreviousElement(UIElement topParent, Property<UIElement> foundSelectedElement){
        return selectPreviousElement(new ArrayList<UIElement>(){{ add(topParent); }}, foundSelectedElement);
    }
    private static boolean selectPreviousElement(ArrayList<UIElement> topParents, Property<UIElement> foundSelectedElement){
        boolean firstPass = true;
        while(true){
            for (UIElement topParent : topParents) {
                if(!topParent.isActive()){
                    continue;
                }

                ArrayList<UIElement> children = topParent.getAllSelectableChildren();

                for (int i = children.size() - 1; i >= 0; i--) {
                    UIElement child = children.get(i);

                    if(child.isSelected()){
                        if(foundSelectedElement.getValue() == child){
                            return false; // We looped back over, current element is the ONLY selectable element
                        }

                        foundSelectedElement.setValue(child);
                    }
                    else if((!foundSelectedElement.isNull() || !firstPass) && !child.isSelected()){
                        if(!foundSelectedElement.isNull()) foundSelectedElement.getValue().deselect();
                        child.select(true);
                        return true;
                    }

                    if(!foundSelectedElement.isNull() && child.isControllerModal()){
                        i++;
                    }
                }
            }

            if(foundSelectedElement.isNull() && !firstPass){
                return false; // No selectable elements found
            }

            firstPass = false;
        }
    }

    private static void onDownPressed(){
        Property<UIElement> foundSelectedElement = new Property<>(null);

        UIElement selectedElement = getCurrentlySelectedElement();
        if(selectedElement != null){
            if(selectedElement.onDownInteraction(false)){
                return;
            }

            if(selectedElement.getControllerModalParent() != null){
                selectNextElement(selectedElement.getControllerModalParent(), foundSelectedElement);
                return;
            }
        }

        selectNextElement(uiElements, foundSelectedElement);
    }
    private static void onUpPressed(){
        Property<UIElement> foundSelectedElement = new Property<>(null);

        UIElement selectedElement = getCurrentlySelectedElement();
        if(selectedElement != null){
            if(selectedElement.onUpInteraction(false)){
                return;
            }

            if(selectedElement.getControllerModalParent() != null){
                selectPreviousElement(selectedElement.getControllerModalParent(), foundSelectedElement);
                return;
            }
        }

        selectPreviousElement(uiElements, foundSelectedElement);
    }
    private static void onLeftPressed(){
        UIElement selectedElement = getCurrentlySelectedElement();
        if(selectedElement != null){
            selectedElement.onLeftInteraction(false);
        }
    }
    private static void onRightPressed(){
        UIElement selectedElement = getCurrentlySelectedElement();
        if(selectedElement != null){
            selectedElement.onRightInteraction(false);
        }
    }

    private static void onConfirmPressed(){
        UIElement selectedElement = getCurrentlySelectedElement();
        if(selectedElement != null){
            selectedElement.onConfirmInteraction(false);
        }
    }
    private static void onCancelPressed(){
        UIElement selectedElement = getCurrentlySelectedElement();
        if(selectedElement != null){
            selectedElement.onCancelInteraction(false);
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
                        ElementCalculationManager.calculate(uiElements.get(i));
                        uiElements.get(i).update();
                        ElementCalculationManager.calculate(uiElements.get(i));
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
                        ElementCalculationManager.calculate(uiElements.get(i));
                        uiElements.get(i).update();
                        ElementCalculationManager.calculate(uiElements.get(i));
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
