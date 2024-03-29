package dLib.ui.elements.prefabs;

import com.badlogic.gdx.graphics.Color;
import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Hoverable;
import dLib.ui.themes.UITheme;
import dLib.ui.themes.UIThemeManager;
import dLib.ui.util.ESelectionMode;
import org.lwjgl.input.Mouse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class ListBox<ItemType> extends UIElement {
    //region Variables

    // Elements
    private TextBox titleBox;

    private Hoverable itemBoxBackground;
    private ArrayList<ListBoxItem> items = new ArrayList<>();

    private Scrollbox scrollbar;

    // Properties
    private String title;
    private int titleBoxHeight = 50;

    private int itemSpacing = 0;
    private boolean invertedItemOrder = false;

    private int scrollbarWidth = 50;

    private ESelectionMode selectionMode = ESelectionMode.SINGLE;
    private int selectionCountLimit = 1;

    // Locals
    private boolean trackScrollWheelScroll = false;

    //endregion

    //region Constructors

    public ListBox(int xPos, int yPos, int width, int height){
        super(xPos, yPos, width, height);

        reinitializeElements();
    }

    public ListBox(ListBoxData data){
        super(data);

        this.title = data.titleBoxText;
        this.titleBoxHeight = data.titleBoxHeight;

        this.itemSpacing = data.itemSpacing;
        this.invertedItemOrder = data.invertedItemOrder;

        this.scrollbarWidth = data.scrollbarWidth;

        this.setSelectionMode(data.selectionMode);
        this.setSelectionCountLimit(data.selectionLimit);

        reinitializeElements();
    }

    private void reinitializeElements(){
        updateTitleBox();
        updateItemBox();
        updateScrollBar();
    }

    private void updateTitleBox(){
        if(title != null && !title.isEmpty()){
            if(titleBox == null){
                buildTitleBox();
            }
            titleBox.setText(title);
            titleBox.setLocalPosition(0, height - titleBoxHeight);
            titleBox.setDimensions(width, titleBoxHeight);
        }
        else if(titleBox != null){
            removeChild(titleBox);
            titleBox = null;
        }
    }
    private void buildTitleBox(){
        titleBox = new TextBox(title, 0, height - titleBoxHeight, width, titleBoxHeight);
        titleBox.setImage(UITheme.whitePixel);
        titleBox.setRenderColor(Color.valueOf("#151515FF"));
        titleBox.setTextRenderColor(Color.WHITE);
        titleBox.setHorizontalAlignment(Alignment.HorizontalAlignment.LEFT);
        titleBox.setMarginPercX(0.005f);
        addChildNCS(titleBox);
    }

    private void updateItemBox(){
        int updateHeight = height;
        if(titleBox != null) updateHeight -= titleBox.getHeight();

        // We can span entire width since elements will get shrunk if Scrollbox is present

        if(itemBoxBackground == null){
            buildItemBox(0, 0, width, updateHeight);
        }

        itemBoxBackground.setLocalPosition(0, 0);
        itemBoxBackground.setDimensions(width, updateHeight);
    }
    private void buildItemBox(int x, int y, int width, int height){
        Color bgColor = Color.BLACK.cpy();
        bgColor.a = 0.4f;
        itemBoxBackground = new Hoverable(UIThemeManager.getDefaultTheme().listbox, x, y, width, height){
            @Override
            protected void onHovered() {
                super.onHovered();
                trackScrollWheelScroll = true;
            }

            @Override
            protected void onUnhovered() {
                super.onUnhovered();
                trackScrollWheelScroll = false;
            }
        };
        itemBoxBackground.setRenderColor(bgColor);
        addChildCS(itemBoxBackground);
    }

    private void updateScrollBar(){
        int updatePosX = width - scrollbarWidth;

        int updateHeight = height;
        if(titleBox != null) updateHeight -= titleBox.getHeight();

        if(scrollbar == null){
            buildScrollBar(updatePosX, 0, scrollbarWidth, updateHeight);
        }
        scrollbar.setLocalPosition(updatePosX, 0);
        scrollbar.setDimensions(scrollbarWidth, updateHeight);
    }
    private void buildScrollBar(int x, int y, int width, int height){
        scrollbar = new Scrollbox(x, y, width, height) {
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
            item.renderForItem.hide();
        }

        for(UIElement item : getItemsForDisplay()){
            item.setLocalPosition(0, currentYPos - item.getHeight()); //TODO RF BOUNDING HEIGHT
            item.setWidth(itemBoxBackground.getWidth() + (scrollbar.isActive() ? -scrollbar.getWidth() : 0));

            item.show();

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

    public ListBox<ItemType> addItem(ItemType item){
        UIElement compositeItem = wrapUIForItem(item);
        items.add(new ListBoxItem(item, compositeItem));
        addChildCS(compositeItem);

        return this;
    }
    public ListBox<ItemType> setItems(ArrayList<ItemType> items){
        clearItems();
        for(ItemType item : items){
            addItem(item);
        }

        return this;
    }
    public void clearItems(){
        ArrayList<UIElement> childrenToRemove = new ArrayList<>();
        for(UIElementChild child : children){
            for(ListBoxItem item : items){
                if(Objects.equals(item.renderForItem, child.element)){
                    childrenToRemove.add(child.element);
                }
            }
        }

        for(UIElement childToRemove : childrenToRemove){
            removeChild(childToRemove);
        }

        items.clear();
        scrollbar.setFirstPage();
    }

    //endregion

    //region Item UI

    public UIElement makeUIForItem(ItemType item){
        TextBox box = new TextBox(item.toString(), 0, 0, itemBoxBackground.getWidth(), 30);
        box.setImage(UIThemeManager.getDefaultTheme().button_large);
        box.setMarginPercX(0.025f).setMarginPercY(0.05f);
        box.setAlignment(Alignment.HorizontalAlignment.LEFT, Alignment.VerticalAlignment.CENTER);
        return box;
    } //TODO expose

    public final UIElement wrapUIForItem(ItemType item){
        UIElement itemUI = makeUIForItem(item);

        Color transparent = Color.WHITE.cpy();
        transparent.a = 0f;
        Button mainButton = (Button) new Button(0, 0, itemUI.getWidth(), itemUI.getHeight()){
            @Override
            protected void onLeftClick() {
                super.onLeftClick();
                trySelectItem(item);
            }

            @Override
            public boolean isActive() {
                return getSelectionCountLimit() != 0;
            }
        }.setImage(UITheme.whitePixel).setRenderColor(transparent);
        mainButton.setID("MainSection");

        Color hoverColor = mainButton.getHoveredColor().cpy();
        hoverColor.a = 0.4f;
        mainButton.setHoveredColor(hoverColor);
        mainButton.setHoveredColorMultiplier(1.0f);

        itemUI.addChildCS(mainButton);

        postMakeWrapperForItem(item, itemUI);

        return itemUI;
    } //TODO expose
    public void postMakeWrapperForItem(ItemType item, UIElement itemUI){ } //TODO expose

    //endregion

    //region Item Selection

    private void trySelectItem(ItemType selectedItem){
        if(getCurrentlySelectedItems().size() + 1 > getSelectionCountLimit()) return;

        for(ListBoxItem item : items){
            if(item.item.equals(selectedItem)){
                if(selectionMode.equals(ESelectionMode.SINGLE)){
                    onItemSelectionChanged(new ArrayList<>(Arrays.asList(item.item)));
                    return;
                }

                item.selected = true;
            }
        }

        onItemSelectionChanged(getCurrentlySelectedItems());
    }
    public void onItemSelectionChanged(ArrayList<ItemType> item){} //TODO expose

    public ArrayList<ItemType> getCurrentlySelectedItems(){
        ArrayList<ItemType> selectedItems = new ArrayList<>();
        for(ListBoxItem item : items){
            if(item.selected) selectedItems.add(item.item);
        }

        return selectedItems;
    }

    public ListBox<ItemType> setSelectionMode(ESelectionMode selectionMode){
        this.selectionMode = selectionMode;
        return this;
    } //TODO expose
    public ESelectionMode getSelectionMode(){
        return selectionMode;
    }

    public ListBox<ItemType> setSelectionCountLimit(int selectionCount){
        this.selectionCountLimit = selectionCount;
        return this;
    } //TODO expose
    public int getSelectionCountLimit(){
        if(selectionMode.equals(ESelectionMode.NONE)) return 0;
        else if(selectionMode.equals(ESelectionMode.SINGLE)) return 1;
        else return selectionCountLimit;
    }

    //endregion

    //region Item Properties

    public ListBox<ItemType> setItemSpacing(int spacing){
        this.itemSpacing = spacing;
        return this;
    }
    public int getItemSpacing(){
        return itemSpacing;
    }

    public ListBox<ItemType> setInvertedItemOrder(boolean invertedItemOrder){
        this.invertedItemOrder = invertedItemOrder;
        return this;
    }

    //endregion

    //region Title & TitleBox

    public ListBox<ItemType> setTitle(String title){
        if(this.title != null && (title == null || title.isEmpty())){
            removeTitle();
            return this;
        }

        this.title = title;
        reinitializeElements();
        return this;
    }
    public void removeTitle(){
        removeChild(titleBox);

        this.title = null;
        this.titleBox = null;

        reinitializeElements();
    }

    public ListBox<ItemType> setTitleHeight(int titleHeight){
        if(titleHeight <= 0) return this;

        this.titleBoxHeight = titleHeight;
        return this;
    }

    //endregion

    //region ScrollBar

    public ListBox<ItemType> setScrollbarWidth(int width){
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

    //region Background

    public Hoverable getBackground(){
        return itemBoxBackground;
    }

    //endregion

    //endregion

    public class ListBoxItem{
        /** Variables */
        public ItemType item;
        public UIElement renderForItem;
        public boolean selected;

        /** Constructors */
        public ListBoxItem(ItemType item, UIElement renderElement){
            this.item = item;
            this.renderForItem = renderElement;
        }
    }

    public static class ListBoxData extends UIElement.UIElementData implements Serializable {
        private static final long serialVersionUID = 1L;

        public String titleBoxText = "";
        public int titleBoxHeight = 50;

        public int itemSpacing = 0;
        public boolean invertedItemOrder = false;

        public int scrollbarWidth = 50;

        public ESelectionMode selectionMode = ESelectionMode.SINGLE;
        public int selectionLimit = 1;

        @Override
        public UIElement makeUIElement() {
            return new ListBox<>(this);
        }
    }
}
