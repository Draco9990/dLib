package dLib.ui.elements.items.itembox;

import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.text.ImageTextBox;
import dLib.ui.resources.UICommonResources;
import dLib.util.bindings.texture.Tex;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;

import java.io.Serializable;

public class HorizontalListBox<ItemType> extends ItemBox<ItemType> {
    //region Variables

    //endregion

    //region Constructors

    public HorizontalListBox(AbstractPosition xPos, AbstractPosition yPos){
        this(xPos, yPos, Dim.fill(), Dim.fill());
    }
    public HorizontalListBox(AbstractDimension width, AbstractDimension height){
        this(Pos.px(0), Pos.px(0), width, height);
    }
    public HorizontalListBox(AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height) {
        super(xPos, yPos, width, height);
    }

    public HorizontalListBox(HorizontalListBoxData data){
        super(data);
    }

    //endregion

    //region Methods

    //region Update & Render

    @Override
    public void updateSelf() {
        super.updateSelf();

        if(getHorizontalContentAlignment() == Alignment.HorizontalAlignment.LEFT) updateItemsLeftRight();
        else if(getHorizontalContentAlignment() == Alignment.HorizontalAlignment.CENTER) updateItemsCentered();
        else if(getHorizontalContentAlignment() == Alignment.HorizontalAlignment.RIGHT) updateItemsRightLeft();
    }

    private void updateItemsLeftRight(){
        int currentXPos = 0;

        for(ItemBoxItem item : items){
            if(!item.renderForItem.isActive()){
                continue;
            }

            currentXPos += item.renderForItem.getPaddingLeft();
            item.renderForItem.setLocalPositionX(currentXPos);

            currentXPos += item.renderForItem.getWidth();
            currentXPos += itemSpacing;
            currentXPos += item.renderForItem.getPaddingRight();
        }
    }
    private void updateItemsCentered(){

    }
    private void updateItemsRightLeft(){

    }

    //endregion

    //region Item Management

    //region Item UI

    @Override
    public UIElement makeUIForItem(ItemType item) {
        ImageTextBox box = (ImageTextBox) super.makeUIForItem(item);
        box.setImage(Tex.stat(UICommonResources.itembox_itembg_vertical));
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

    //endregion

    public static class HorizontalListBoxData extends ItemBoxData implements Serializable {
        private static final long serialVersionUID = 1L;

        @Override
        public UIElement makeUIElement() {
            return new HorizontalListBox<>(this);
        }
    }
}
