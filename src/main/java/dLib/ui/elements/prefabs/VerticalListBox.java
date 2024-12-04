package dLib.ui.elements.prefabs;

import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Interactable;
import dLib.ui.themes.UIThemeManager;
import org.lwjgl.input.Mouse;

import java.io.Serializable;
import java.util.ArrayList;

public class VerticalListBox<ItemType> extends VerticalItemBox<ItemType> {
    //region Variables

    //endregion

    //region Constructors

    public VerticalListBox(int xPos, int yPos, int width, int height){
        this(xPos, yPos, width, height, false);
    }

    public VerticalListBox(int xPos, int yPos, int width, int height, boolean noInitScrollbar) {
        super(xPos, yPos, width, height, noInitScrollbar);

        defaultItemHeight = 30;

        reinitializeElements();
    }

    public VerticalListBox(VerticalListBoxData data){
        super(data);

        reinitializeElements();
    }

    //endregion

    //region Methods

    //region Update & Render

    @Override
    public void updateSelf() {
        super.updateSelf();

        int currentYPos = itemBox.getHeight() - itemPadding.y + currentScrollbarOffset;

        for(ItemBoxItem item : originalItems){
            item.renderForItem.hideAndDisable();
        }

        for(ItemBoxItem item : items){
            item.renderForItem.setLocalPosition(itemPadding.x, currentYPos - item.renderForItem.getHeight()); //TODO RF BOUNDING HEIGHT

            if(item.renderForItem.overlapsParent()){
                item.renderForItem.showAndEnable();
            }
            else{
                item.renderForItem.hideAndDisable();
            }

            if(!item.selected){
                item.renderForItem.lightenInstantly();
            }
            else {
                item.renderForItem.darkenInstantly();
            }

            currentYPos -= item.renderForItem.getHeight();
            currentYPos -= itemSpacing;
        }
    }

    public VerticalListBox<ItemType> setDefaultItemHeight(int defaultItemHeight){
        this.defaultItemHeight = defaultItemHeight;
        return this;
    }

    //endregion

    //region Item Management

    @Override
    public UIElement makeUIForItem(ItemType item) {
        TextBox box = (TextBox) super.makeUIForItem(item);
        box.setImage(UIThemeManager.getDefaultTheme().itemBoxVerticalItemBg);
        return box;
    }

    //region Item UI
    public UIElement wrapUIForItem(ItemType item){
        UIElement itemUI = super.wrapUIForItem(item);

        if(canReorder()){
            //Controls
            int elementControlsWidth = (int) (itemUI.getWidthUnscaled() * 0.2f);
            HorizontalBox elementControls = new HorizontalBox(itemUI.getWidthUnscaled() - elementControlsWidth, 0, elementControlsWidth, itemUI.getHeightUnscaled());
            elementControls.disableItemWrapping();

            if(canReorder()){
                //Reorder
                int reorderArrowWidth = (int) (elementControls.getWidthUnscaled() * 0.5f);
                int reorderArrowHeight = (int) (itemUI.getHeightUnscaled() * 0.5f);

                VerticalBox reorderArrows = new VerticalBox(reorderArrowWidth, itemUI.getHeightUnscaled(), reorderArrowWidth, itemUI.getHeightUnscaled());
                reorderArrows.disableItemWrapping();

                Interactable moveUpArrow = new Interactable(UIThemeManager.getDefaultTheme().arrow_up, 0, 0, reorderArrowWidth, reorderArrowHeight){
                    @Override
                    protected void onLeftClick() {
                        super.onLeftClick();
                        moveItemUp(item);
                    }
                };
                Interactable moveDownArrow = new Interactable(UIThemeManager.getDefaultTheme().arrow_down, 0, 0, reorderArrowWidth, reorderArrowHeight){
                    @Override
                    protected void onLeftClick() {
                        super.onLeftClick();
                        moveItemDown(item);
                    }
                };
                reorderArrows.addItem(moveUpArrow);
                reorderArrows.addItem(moveDownArrow);

                elementControls.addItem(reorderArrows);
            }

            itemUI.addChildCS(elementControls);
        }

        return itemUI;
    } //TODO expose

    //endregion

    @Override
    protected int recalculateScrollOffset(float scrollPercentage) {
        return (int) ((getTotalItemHeight() - itemBox.getHeight()) * scrollPercentage);
    }

    @Override
    protected int getTotalItemHeight() {
        int totalHeight = 0;

        for (int i = 0; i < items.size(); i++) {
            ItemBoxItem item = items.get(i);
            totalHeight += item.renderForItem.getHeight();

            if (i != items.size() - 1) {
                totalHeight += itemSpacing;
            }
        }

        return totalHeight;
    }

    //endregion

    public static class VerticalListBoxData extends VerticalItemBoxData implements Serializable {
        private static final long serialVersionUID = 1L;

        @Override
        public UIElement makeUIElement() {
            return new VerticalListBox<>(this);
        }
    }
}
