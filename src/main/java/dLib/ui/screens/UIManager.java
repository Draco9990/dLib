package dLib.ui.screens;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputActionSet;
import com.megacrit.cardcrawl.screens.mainMenu.MainMenuScreen;
import dLib.ui.elements.UIElement;
import dLib.util.Help;

import java.util.ArrayList;
import java.util.List;

public class UIManager {

    //region Variables

    private static ArrayList<UIElement> uiElements = new ArrayList<>();

    //endregion

    //region Class Methods

    public static void openUIElement(UIElement element){ //TODO draw 'focus' to the first element
        uiElements.add(element);
    }
    public static void reopenPreviousUIElement(){
        if(uiElements.isEmpty()){
            return;
        }

        uiElements.get(uiElements.size() - 1).showAndEnable();
    }
    public static void closeUIElement(UIElement element){
        for (int i = uiElements.size() - 1; i >= 0; i--) {
            if(uiElements.get(i) == element){
                uiElements.remove(i);
                return;
            }
        }
    }
    public static void hideAllUIElements(){
        for (UIElement uiElement : uiElements) {
            if(uiElement.isVisible()){
                uiElement.hideAndDisable();
            }
        }
    }

    public static <UIElementClass> UIElementClass getOpenScreenOfType(Class<UIElementClass> screenClass){
        for (UIElement uiElement : uiElements) {
            if(screenClass.isInstance(uiElement)){
                return (UIElementClass) uiElement;
            }
        }

        return null;

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
        ArrayList<UIElement.UIElementChild> children = topParent.getAllChildrenRaw();
        for (UIElement.UIElementChild child : children) {
            if(child.element.isSelected()){
                foundSelectedElement = true;
                child.element.deselect();
            }
            else if(foundSelectedElement && !child.element.isSelected() && child.isControllerSelectable){
                child.element.select();
                return true;
            }
        }

        return false;
    }

    private static boolean selectPreviousElement(UIElement topParent, Boolean foundSelectedElement){
        ArrayList<UIElement.UIElementChild> children = topParent.getAllChildrenRaw();
        for (int i = children.size() - 1; i >= 0; i--) {
            UIElement.UIElementChild child = children.get(i);
            if(child.element.isSelected()){
                foundSelectedElement = true;
                child.element.deselect();
            }
            else if(foundSelectedElement && !child.element.isSelected() && child.isControllerSelectable){
                child.element.select();
                return true;
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
                    for(int i = 0; i < uiElements.size(); i++){
                        uiElements.get(i).update();
                    }
                    updateInput();
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
                    for (int i = 0; i < uiElements.size(); i++) {
                        uiElements.get(i).update();
                    }
                    updateInput();
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
}
