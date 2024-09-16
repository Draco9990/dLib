package dLib.ui.elements.prefabs;

import dLib.ui.elements.UIElement;
import dLib.ui.themes.UIThemeManager;

import java.io.Serializable;

//Gridboxes do not support elements that change their size after being added. All GridBox items must be identical in dimensions. This is a TODO.
public class VerticalGridBox<ItemType> extends VerticalItemBox<ItemType>{
    //region Variables

    //endregion

    //region Constructors

    public VerticalGridBox(int xPos, int yPos, int width, int height){
        this(xPos, yPos, width, height, false);
    }

    public VerticalGridBox(int xPos, int yPos, int width, int height, boolean noInitScrollbar) {
        super(xPos, yPos, width, height, noInitScrollbar);

        defaultItemHeight = 75;
        defaultItemWidth = 75;

        itemSpacing = 5;

        reinitializeElements();
    }

    public VerticalGridBox(VerticalGridBoxData data){
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
        int currentXPos = itemPadding.x;

        for(ItemBoxItem item : originalItems){
            item.renderForItem.hideAndDisable();
        }

        for(ItemBoxItem item : items){
            item.renderForItem.setLocalPosition(currentXPos, currentYPos - item.renderForItem.getHeight());

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

            currentXPos += item.renderForItem.getWidth() + itemSpacing;
            if(currentXPos + item.renderForItem.getWidth() + itemSpacing > itemBox.getWidth()){
                currentXPos = itemPadding.x;

                currentYPos -= item.renderForItem.getHeight();
                currentYPos -= itemSpacing;
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

    @Override
    protected int recalculateScrollOffset(float scrollPercentage) {
        int totalHeight = itemBox.getHeight() - itemPadding.y;
        int currentXPos = itemPadding.x;

        for (int i = 0; i < originalItems.size(); i++) {
            ItemBoxItem item = originalItems.get(i);
            currentXPos += item.renderForItem.getWidth() + itemSpacing;
            if (currentXPos + item.renderForItem.getWidth() + itemSpacing > itemBox.getWidth()) {
                currentXPos = itemPadding.x;

                totalHeight += item.renderForItem.getHeight();
                totalHeight += itemSpacing;
            }
            else if(i == originalItems.size() - 1){
                totalHeight += item.renderForItem.getHeight();
            }
        }

        return (int) ((totalHeight - itemBox.getHeight()) * scrollPercentage);
    }

    //endregion

    public static class VerticalGridBoxData extends VerticalItemBoxData implements Serializable {
        private static final long serialVersionUID = 1L;

        @Override
        public UIElement makeUIElement() {
            return new VerticalGridBox<>(this);
        }
    }
}
