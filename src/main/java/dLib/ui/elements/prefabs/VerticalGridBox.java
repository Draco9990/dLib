package dLib.ui.elements.prefabs;

import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Interactable;
import dLib.ui.themes.UITheme;
import dLib.ui.themes.UIThemeManager;
import org.lwjgl.input.Mouse;

import java.io.Serializable;
import java.util.ArrayList;

//Gridboxes do not support elements that change their size after being added. All GridBox items must be identical in dimensions. This is a TODO.
public class VerticalGridBox<ItemType> extends ItemBox<ItemType>{
    //region Variables

    private int scrollbarWidth = 50;

    private int effectiveScrollbarWidth = 0;
    private int itemsPerRow = 0;
    private int rowsPerPage = 0;

    //endregion

    //region Constructors

    public VerticalGridBox(int xPos, int yPos, int width, int height){
        super(xPos, yPos, width, height);

        defaultItemHeight = 75;
        defaultItemWidth = 75;

        itemSpacing = 5;

        reinitializeElements();
    }

    public VerticalGridBox(VerticalGridBoxData data){
        super(data);

        scrollbarWidth = data.scrollbarWidth;

        reinitializeElements();
    }

    protected void updateScrollBar(int xPos, int yPos, int width, int height){
        xPos = width - scrollbarWidth;

        if(scrollbar == null){
            buildScrollBar(xPos, 0, scrollbarWidth, height);
        }
        scrollbar.setLocalPosition(xPos, 0);
        scrollbar.setDimensions(scrollbarWidth, height);
    }
    protected void buildScrollBar(int x, int y, int width, int height){
        scrollbar = new VerticalScrollbar(x, y, width, height) {
            @Override
            public int getPageCount() {
                return calculatePageCount();
            }

            @Override
            public boolean isActive() {
                return calculatePageCount() > 1 && super.isActive();
            }
        };
        addChildCS(scrollbar);
    }

    //endregion

    //region Methods

    //region Update & Render

    @Override
    public void updateSelf() {
        super.updateSelf();

        if(trackScrollWheelScroll){
            int scrollDelta = (int)(Math.signum((float)Mouse.getDWheel()));
            scrollbar.getSlider().setLocalPositionY(scrollbar.getSlider().getLocalPositionY() + scrollDelta * 10);
        }

        int currentYPos = itemBoxBackground.getHeight() - itemPadding.y;
        int currentXPos = itemPadding.x;

        for(ItemBoxItem item : originalItems){
            item.renderForItem.hideAndDisable();

            if(!item.selected) item.renderForItem.lightenInstantly();
            else item.renderForItem.darkenInstantly();
        }

        for(UIElement item : getItemsForDisplay()){
            item.setLocalPosition(currentXPos, currentYPos - item.getHeight()); //TODO RF BOUNDING HEIGHT

            item.showAndEnable();

            currentXPos += item.getWidth() + itemSpacing;
            if(currentXPos + item.getWidth() + itemSpacing > itemBoxBackground.getWidth() - effectiveScrollbarWidth){
                currentXPos = itemPadding.x;

                currentYPos -= item.getHeight();
                currentYPos -= itemSpacing;
            }
        }
    }

    //endregion

    //region Item Pages

    @Override
    public void onItemsChanged(){
        effectiveScrollbarWidth = 0;
        calculateItemPerRow();
        calculateRowsPerPage();

        calculateEffectiveScrollbarWidth();
        calculateItemPerRow();
        calculateRowsPerPage();
    }

    private void calculateEffectiveScrollbarWidth(){
        if(calculatePageCount() <= 1){
            effectiveScrollbarWidth = 0;
        }
        else{
            effectiveScrollbarWidth = scrollbarWidth;
        }
    }

    private void calculateItemPerRow(){
        int totalItemWidth = itemPadding.x;
        for(int i = 0; i < items.size(); i++){
            totalItemWidth += items.get(i).renderForItem.getWidth() + itemSpacing;

            itemsPerRow = i;
            if(totalItemWidth > itemBoxBackground.getWidth() - effectiveScrollbarWidth){
                break;
            }
        }
    }

    private void calculateRowsPerPage(){
        if(items.isEmpty()) return;

        int itemHeight = items.get(0).renderForItem.getHeight() + itemSpacing - itemPadding.y;
        rowsPerPage = (int) Math.floor((float)itemBoxBackground.getHeight() / itemHeight);
    }

    public int calculatePageCount(){
        //Recalculate width not accounting for the scrollbar.
        int totalRowCount = (int) Math.ceil((float)items.size() / itemsPerRow);
        totalRowCount -= rowsPerPage - 1;
        return totalRowCount;
    }

    //endregion

    public ArrayList<UIElement> getItemsForDisplay(){
        ArrayList<UIElement> activeItems = new ArrayList<>();

        if(!invertedItemOrder){
            int startingItemIndex = (scrollbar.getCurrentPage() - 1) * itemsPerRow;
            int endingItemIndex = startingItemIndex + rowsPerPage * itemsPerRow;
            for(int i = startingItemIndex; i < Math.min(endingItemIndex, items.size()); i++){
                activeItems.add(items.get(i).renderForItem);
            }
        }
        else{
            //TODO
        }

        return activeItems;
    }

    //region Item Management

    //region Item UI

    @Override
    public UIElement makeUIForItem(ItemType item) {
        TextBox box = (TextBox) super.makeUIForItem(item);
        box.setImage(UIThemeManager.getDefaultTheme().button_small);
        return box;
    }

    //endregion

    //region ScrollBar

    public VerticalGridBox<ItemType> setScrollbarWidth(int width){
        this.scrollbarWidth = width;
        return this;
    }

    //endregion

    //endregion

    public static class VerticalGridBoxData extends ItemBoxData implements Serializable {
        private static final long serialVersionUID = 1L;

        public int scrollbarWidth = 50;

        @Override
        public UIElement makeUIElement() {
            return new VerticalGridBox<>(this);
        }
    }
}
