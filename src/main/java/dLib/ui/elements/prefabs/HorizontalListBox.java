package dLib.ui.elements.prefabs;

import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Interactable;
import dLib.ui.themes.UIThemeManager;
import org.lwjgl.input.Mouse;

import java.io.Serializable;
import java.util.ArrayList;

public class HorizontalListBox<ItemType> extends ListBox<ItemType> {
    //region Variables

    private int scrollbarWidth = 50;

    //endregion

    //region Constructors

    public HorizontalListBox(int xPos, int yPos, int width, int height){
        super(xPos, yPos, width, height);

        setItemHeight(30);
    }

    public HorizontalListBox(HorizontalListBoxData data){
        super(data);

        scrollbarWidth = data.scrollbarWidth;
    }

    protected void updateScrollBar(){
        int updatePosX = width - scrollbarWidth;

        int updateHeight = height;
        if(titleBox != null) updateHeight -= titleBox.getHeight();

        if(scrollbar == null){
            buildScrollBar(updatePosX, 0, scrollbarWidth, updateHeight);
        }
        scrollbar.setLocalPosition(updatePosX, 0);
        scrollbar.setDimensions(scrollbarWidth, updateHeight);
    }
    protected void buildScrollBar(int x, int y, int width, int height){
        scrollbar = new HorizontalScrollbar(x, y, width, height) {
            @Override
            public int getPageCount() {
                return calculatePageCount();
            }

            @Override
            public boolean isActive() {
                return calculatePageCount() > 1;
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

        int currentYPos = itemBoxBackground.getHeight();

        for(ListBoxItem item : items){
            item.renderForItem.hideAndDisable();
        }

        for(UIElement item : getItemsForDisplay()){
            item.setLocalPosition(0, currentYPos - item.getHeight()); //TODO RF BOUNDING HEIGHT
            item.setWidth(defaultItemWidth == null ? itemBoxBackground.getWidth() + (scrollbar.isActive() ? -scrollbar.getWidth() : 0) : defaultItemWidth);

            item.showAndEnable();

            currentYPos -= item.getHeight();
            currentYPos -= itemSpacing;
        }
    }

    //endregion

    public ArrayList<UIElement> getItemsForDisplay(){
        ArrayList<UIElement> activeItems = new ArrayList<>();

        int currentPageHeight = 0;
        if(!invertedItemOrder){
            for(int i = scrollbar.getCurrentPage() - 1; i < items.size(); i++){
                UIElement item = items.get(i).renderForItem;
                if(currentPageHeight + item.getHeight() + itemSpacing > itemBoxBackground.getHeight()){
                    break;
                }
                //TODO RF getBoundingHeight

                currentPageHeight += item.getHeight() + itemSpacing;
                activeItems.add(item);
            }
        }
        else{
            for(int i = items.size() - (scrollbar.getCurrentPage() - 1) - 1; i >= 0; i--){
                UIElement item = items.get(i).renderForItem;
                if(currentPageHeight + item.getHeight() + itemSpacing > itemBoxBackground.getHeight()){
                    break;
                }
                //TODO RF getBoundingHeight
                currentPageHeight += item.getHeight() + itemSpacing;
                activeItems.add(item);
            }
        }

        return activeItems;
    }

    //region Item Management

    //region Item UI
    public final UIElement wrapUIForItem(ItemType item){
        UIElement itemUI = super.wrapUIForItem(item);

        if(canReorder()){
            //Controls
            int elementControlsWidth = (int) (itemUI.getWidth() * 0.2f);
            VerticalBox elementControls = new VerticalBox(itemUI.getWidth() - elementControlsWidth, 0, elementControlsWidth, itemUI.getHeight());
            elementControls.setItemWidth((int) (elementControlsWidth * 0.5f));
            elementControls.disableItemWrapping();

            if(canReorder()){
                //Reorder
                int reorderArrowWidth = (int) (elementControls.getWidth() * 0.5f);
                int reorderArrowHeight = (int) (itemUI.getHeight() * 0.5f);

                HorizontalBox reorderArrows = new HorizontalBox(reorderArrowWidth, itemUI.getHeight(), 0, 0);
                reorderArrows.setItemHeight((int) (itemUI.getHeight() * 0.5f));
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

    //region ScrollBar

    public HorizontalListBox<ItemType> setScrollbarWidth(int width){
        this.scrollbarWidth = width;
        return this;
    }

    public int calculatePageCount(){
        int totalItemHeight = 0;
        if(!invertedItemOrder){
            for(int i = 0; i < items.size(); i++){
                totalItemHeight += items.get(i).renderForItem.getHeight() + itemSpacing; //TODO RF BOUNDING HEIGHT
                if(totalItemHeight > itemBoxBackground.getHeight()){
                    int pageCount = items.size() - i;
                    return pageCount + 1;
                }
            }
        }
        else{
            for(int i = items.size() - 1; i >= 0; i--){
                totalItemHeight += items.get(i).renderForItem.getHeight() + itemSpacing; //TODO RF BOUNDING HEIGHT
                if(totalItemHeight > itemBoxBackground.getHeight()){
                    return i + 2;
                }
            }
        }
        return 1;
    }

    //endregion

    //endregion

    public static class HorizontalListBoxData extends ListBoxData implements Serializable {
        private static final long serialVersionUID = 1L;

        public int scrollbarWidth = 50;

        public HorizontalListBoxData(){
            defaultItemHeight = 30;
        }

        @Override
        public UIElement makeUIElement() {
            return new HorizontalListBox<>(this);
        }
    }
}
