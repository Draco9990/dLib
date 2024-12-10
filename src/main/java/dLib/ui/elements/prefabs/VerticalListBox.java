package dLib.ui.elements.prefabs;

import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.ui.themes.UIThemeManager;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;

import java.io.Serializable;

public class VerticalListBox<ItemType> extends ItemBox<ItemType> {
    //region Variables

    //endregion

    //region Constructors

    public VerticalListBox(AbstractPosition xPos, AbstractPosition yPos){
        this(xPos, yPos, Dim.fill(), Dim.fill());
    }
    public VerticalListBox(AbstractDimension width, AbstractDimension height){
        this(Pos.px(0), Pos.px(0), width, height);
    }
    public VerticalListBox(AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height) {
        super(xPos, yPos, width, height);

        setVerticalContentAlignment(Alignment.VerticalAlignment.TOP);

        defaultItemHeight = 30;
    }

    public VerticalListBox(VerticalListBoxData data){
        super(data);
    }

    //endregion

    //region Methods

    //region Update & Render

    @Override
    public void updateSelf() {
        super.updateSelf();

        if(getVerticalContentAlignment() == Alignment.VerticalAlignment.BOTTOM) updateListBottomTop();
        else if(getVerticalContentAlignment() == Alignment.VerticalAlignment.CENTER) updateListCentered();
        else if(getVerticalContentAlignment() == Alignment.VerticalAlignment.TOP) updateListTopBottom();
    }

    private void updateListBottomTop(){
    }

    private void updateListCentered(){

    }

    private void updateListTopBottom(){
        int currentYPos = getHeight();

        for(ItemBoxItem item : items){
            if(!item.renderForItem.isActiveNoOverlapCheck()){
                continue;
            }

            currentYPos -= item.renderForItem.getPaddingTop();

            item.renderForItem.setLocalPositionY(currentYPos - item.renderForItem.getHeight());

            currentYPos -= item.renderForItem.getHeight();
            currentYPos -= itemSpacing;
            currentYPos -= item.renderForItem.getPaddingBottom();
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
            /*int elementControlsWidth = (int) (itemUI.getWidthUnscaled() * 0.2f);
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

            itemUI.addChildCS(elementControls);*/
        }

        return itemUI;
    } //TODO expose

    //endregion

    //endregion

    public static class VerticalListBoxData extends ItemBoxData implements Serializable {
        private static final long serialVersionUID = 1L;

        @Override
        public UIElement makeUIElement() {
            return new VerticalListBox<>(this);
        }
    }
}
