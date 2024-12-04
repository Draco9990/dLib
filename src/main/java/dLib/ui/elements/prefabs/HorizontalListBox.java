package dLib.ui.elements.prefabs;

import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Interactable;
import dLib.ui.themes.UIThemeManager;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;
import org.lwjgl.input.Mouse;

import java.io.Serializable;
import java.util.ArrayList;

public class HorizontalListBox<ItemType> extends HorizontalItemBox<ItemType> {
    //region Variables

    //endregion

    //region Constructors

    public HorizontalListBox(AbstractPosition xPos, AbstractPosition yPos){
        this(xPos, yPos, Dim.fill(), Dim.fill());
    }
    public HorizontalListBox(AbstractDimension width, AbstractDimension height){
        this(Pos.perc(0), Pos.perc(0), width, height);
    }
    public HorizontalListBox(AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height){
        this(xPos, yPos, width, height, false);
    }
    public HorizontalListBox(AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height, boolean noInitScrollbar) {
        super(xPos, yPos, width, height, noInitScrollbar);

        reinitializeElements();
    }

    public HorizontalListBox(HorizontalListBoxData data){
        super(data);

        reinitializeElements();
    }

    //endregion

    //region Methods

    //region Update & Render

    @Override
    public void updateSelf() {
        super.updateSelf();

        int currentXPos = itemPadding.x - currentScrollbarOffset;

        for(ItemBoxItem item : originalItems){
            item.renderForItem.hideAndDisable();
        }

        for(ItemBoxItem item : items){
            item.renderForItem.setLocalPosition(currentXPos, -itemPadding.y); //TODO RF BOUNDING HEIGHT

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

            currentXPos += item.renderForItem.getWidth();
            currentXPos += itemSpacing;
        }
    }

    //endregion

    //region Item Management

    //region Item UI

    @Override
    public UIElement makeUIForItem(ItemType item) {
        TextBox box = (TextBox) super.makeUIForItem(item);
        box.setImage(UIThemeManager.getDefaultTheme().itemBoxHorizontalItemBg);
        return box;
    }

    public UIElement wrapUIForItem(ItemType item){
        UIElement itemUI = super.wrapUIForItem(item);

        if(canReorder()){
            //Controls
            /*int elementControlsHeight = (int) (itemUI.getHeightUnscaled() * 0.2f);
            VerticalBox elementControls = new VerticalBox(0, 0, itemUI.getWidthUnscaled(), elementControlsHeight);
            elementControls.disableItemWrapping();

            if(canReorder()){
                //Reorder
                int reorderArrowWidth = (int) (elementControls.getWidthUnscaled() * 0.5f);
                int reorderArrowHeight = (int) (elementControls.getHeightUnscaled() * 0.5f);

                HorizontalBox reorderArrows = new HorizontalBox(0, 0, itemUI.getWidthUnscaled(), reorderArrowHeight);
                reorderArrows.disableItemWrapping();

                Interactable moveUpArrow = new Interactable(UIThemeManager.getDefaultTheme().arrow_left, 0, 0, reorderArrowWidth, reorderArrowHeight){
                    @Override
                    protected void onLeftClick() {
                        super.onLeftClick();
                        moveItemUp(item);
                    }
                };
                Interactable moveDownArrow = new Interactable(UIThemeManager.getDefaultTheme().arrow_right, 0, 0, reorderArrowWidth, reorderArrowHeight){
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

            itemUI.addChildCS(elementControls);*/
        }

        return itemUI;
    } //TODO expose

    //endregion

    @Override
    protected int recalculateScrollOffset(float scrollPercentage) {
        return (int) ((getTotalItemWidth() - itemBox.getWidth()) * scrollPercentage);
    }

    @Override
    protected float recalculateScrollPercentageForItemChange() {
        return currentScrollbarOffset / (float) (getTotalItemWidth() - itemBox.getWidth());
    }

    @Override
    protected int getTotalItemWidth() {
        int totalWidth = 0;

        for (int i = 0; i < items.size(); i++) {
            ItemBoxItem item = items.get(i);
            totalWidth += item.renderForItem.getWidth();

            if (i != items.size() - 1) {
                totalWidth += itemSpacing;
            }
        }

        return totalWidth;
    }

    //endregion

    public static class HorizontalListBoxData extends HorizontalItemBoxData implements Serializable {
        private static final long serialVersionUID = 1L;

        @Override
        public UIElement makeUIElement() {
            return new HorizontalListBox<>(this);
        }
    }
}
