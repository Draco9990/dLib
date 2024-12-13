package dLib.ui.elements.prefabs;

import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.ui.themes.UIThemeManager;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.position.AbstractPosition;

import java.io.Serializable;

//Gridboxes do not support elements that change their size after being added. All GridBox items must be identical in dimensions. This is a TODO.
public class GridItemBox<ItemType> extends ItemBox<ItemType>{
    //region Variables

    //endregion

    //region Constructors

    public GridItemBox(AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height) {
        super(xPos, yPos, width, height);

        defaultItemHeight = 75;
        defaultItemWidth = 75;

        itemSpacing = 5;
    }

    public GridItemBox(GridBoxData data){
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
        int currentXPos = 0;

        for(ItemBoxItem item : items){
            if(!item.renderForItem.isActive()){
                continue;
            }

            item.renderForItem.setLocalPosition(currentXPos, currentYPos - item.renderForItem.getHeight());

            currentXPos += item.renderForItem.getWidth() + itemSpacing + item.renderForItem.getPaddingRight();
            if(currentXPos + item.renderForItem.getWidth() + itemSpacing + item.renderForItem.getPaddingRight() > getWidth()){
                currentXPos = 0;

                currentYPos -= item.renderForItem.getHeight();
                currentYPos -= itemSpacing;
                currentYPos -= item.renderForItem.getPaddingBottom();
            }
        }
    }

    //endregion

    //region Item Management

    //region Item UI

    @Override
    public UIElement makeUIForItem(ItemType item) {
        TextBox box = (TextBox) super.makeUIForItem(item);
        box.setImage(UIThemeManager.getDefaultTheme().button_small);
        return box;
    }

    //endregion

    //endregion

    public static class GridBoxData extends ItemBoxData implements Serializable {
        private static final long serialVersionUID = 1L;

        @Override
        public UIElement makeUIElement() {
            return new GridItemBox<>(this);
        }
    }
}
