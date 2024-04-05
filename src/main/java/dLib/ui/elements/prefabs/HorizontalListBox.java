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

    private int scrollbarHeight = 50;

    //endregion

    //region Constructors

    public HorizontalListBox(int xPos, int yPos, int width, int height){
        super(xPos, yPos, width, height);

        defaultItemWidth = 30;

        reinitializeElements();
    }

    public HorizontalListBox(HorizontalListBoxData data){
        super(data);

        scrollbarHeight = data.scrollbarHeight;

        reinitializeElements();
    }

    protected void updateScrollBar(int xPos, int yPos, int width, int height){
        if(scrollbar == null){
            buildScrollBar(0, 0, getWidth(), scrollbarHeight);
        }
        scrollbar.setLocalPosition(0, 0);
        scrollbar.setDimensions(getWidth(), scrollbarHeight);
    }
    protected void buildScrollBar(int x, int y, int width, int height){
        scrollbar = new HorizontalScrollbar(x, y, width, height) {
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
            scrollbar.getSlider().setLocalPositionX(scrollbar.getSlider().getLocalPositionX() - scrollDelta * 10);
        }

        int currentXPos = 0;

        for(ListBoxItem item : items){
            item.renderForItem.hideAndDisable();
        }

        for(UIElement item : getItemsForDisplay()){
            item.setLocalPosition(currentXPos, (scrollbar.isActive() ? scrollbar.getHeight() : 0)); //TODO RF BOUNDING HEIGHT
            item.setHeight(defaultItemHeight == null ? itemBoxBackground.getHeightUnscaled() + (scrollbar.isActive() ? -scrollbar.getHeightUnscaled() : 0) : defaultItemHeight);

            item.showAndEnable();

            currentXPos += item.getWidth();
            currentXPos += itemSpacing;
        }
    }

    //endregion

    public ArrayList<UIElement> getItemsForDisplay(){
        ArrayList<UIElement> activeItems = new ArrayList<>();

        int currentPageWidth = 0;
        if(!invertedItemOrder){
            for(int i = scrollbar.getCurrentPage() - 1; i < items.size(); i++){
                UIElement item = items.get(i).renderForItem;
                if(currentPageWidth + item.getWidth() + itemSpacing > itemBoxBackground.getWidth()){
                    break;
                }
                //TODO RF getBoundingHeight

                currentPageWidth += item.getWidth() + itemSpacing;
                activeItems.add(item);
            }
        }
        else{
            for(int i = items.size() - (scrollbar.getCurrentPage() - 1) - 1; i >= 0; i--){
                UIElement item = items.get(i).renderForItem;
                if(currentPageWidth + item.getWidth() + itemSpacing > itemBoxBackground.getWidth()){
                    break;
                }
                //TODO RF getBoundingHeight

                currentPageWidth += item.getWidth() + itemSpacing;
                activeItems.add(item);
            }
        }

        return activeItems;
    }

    //region Item Management

    //region Item UI

    public UIElement makeUIForItem(ItemType item){
        TextBox box = new TextBox(item.toString(), 0, 0, 30, itemBoxBackground.getHeight());
        box.setImage(UIThemeManager.getDefaultTheme().button_large);
        box.setMarginPercX(0.025f).setMarginPercY(0.05f);
        box.setAlignment(Alignment.HorizontalAlignment.LEFT, Alignment.VerticalAlignment.CENTER);
        return box;
    } //TODO expose with listeners

    public final UIElement wrapUIForItem(ItemType item){
        UIElement itemUI = super.wrapUIForItem(item);

        if(canReorder()){
            //Controls
            int elementControlsHeight = (int) (itemUI.getHeight() * 0.2f);
            VerticalBox elementControls = new VerticalBox(0, 0, itemUI.getWidth(), elementControlsHeight);
            elementControls.disableItemWrapping();

            if(canReorder()){
                //Reorder
                int reorderArrowWidth = (int) (elementControls.getWidth() * 0.5f);
                int reorderArrowHeight = (int) (elementControls.getHeight() * 0.5f);

                HorizontalBox reorderArrows = new HorizontalBox(0, 0, itemUI.getWidth(), reorderArrowHeight);
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

            itemUI.addChildCS(elementControls);
        }

        return itemUI;
    } //TODO expose

    //endregion

    //region ScrollBar

    public HorizontalListBox<ItemType> setScrollbarHeight(int height){
        this.scrollbarHeight = height;
        return this;
    }

    public int calculatePageCount(){
        int totalItemWidth = 0;
        if(!invertedItemOrder){
            for(int i = 0; i < items.size(); i++){
                totalItemWidth += items.get(i).renderForItem.getWidth() + itemSpacing; //TODO RF BOUNDING WIDTH
                if(totalItemWidth > itemBoxBackground.getWidth()){
                    int pageCount = items.size() - i;
                    return pageCount + 1;
                }
            }
        }
        else{
            for(int i = items.size() - 1; i >= 0; i--){
                totalItemWidth += items.get(i).renderForItem.getWidth() + itemSpacing; //TODO RF BOUNDING WIDTH
                if(totalItemWidth > itemBoxBackground.getWidth()){
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

        public int scrollbarHeight = 50;

        @Override
        public UIElement makeUIElement() {
            return new HorizontalListBox<>(this);
        }
    }
}
