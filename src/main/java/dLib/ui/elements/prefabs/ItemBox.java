package dLib.ui.elements.prefabs;

import com.badlogic.gdx.graphics.Color;
import dLib.properties.objects.*;
import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Hoverable;
import dLib.ui.themes.UITheme;
import dLib.ui.themes.UIThemeManager;
import dLib.ui.util.ESelectionMode;
import dLib.util.IntegerVector2;

import java.io.Serializable;
import java.util.*;
import java.util.function.BiConsumer;

public abstract class ItemBox<ItemType> extends UIElement {
    //region Variables

    // Elements
    protected TextBox titleBox;

    protected Hoverable itemBoxBackground;
    protected ArrayList<ItemBoxItem> items = new ArrayList<>();

    protected ArrayList<ItemBoxItem> originalItems = new ArrayList<>();

    protected Scrollbar scrollbar;

    protected Inputfield filter;
    private String filterText = "";

    // Properties
    private String title;
    private int titleBoxHeight = 50;

    protected int itemSpacing = 0;
    protected boolean invertedItemOrder = false;

    protected IntegerVector2 itemPadding = new IntegerVector2(0, 0);

    private ESelectionMode selectionMode = ESelectionMode.SINGLE;
    private int selectionCountLimit = 1;

    private boolean canReorder = false;
    private ArrayList<BiConsumer<ItemType, ItemType>> onElementsSwappedListeners = new ArrayList<>();

    protected Integer defaultItemWidth = null;
    protected Integer defaultItemHeight = null;

    private boolean disableItemWrapping = false;

    // Locals
    protected boolean trackScrollWheelScroll = false;

    //endregion

    //region Constructors

    public ItemBox(int xPos, int yPos, int width, int height){
        super(xPos, yPos, width, height);
    }

    public ItemBox(ItemBoxData data){
        super(data);

        this.title = data.titleBoxText.getValue();
        this.titleBoxHeight = data.titleBoxHeight;

        this.itemSpacing = data.itemSpacing.getValue();
        this.invertedItemOrder = data.invertedItemOrder.getValue();

        this.itemPadding = data.itemPadding.getValue();

        this.setSelectionMode(data.selectionMode.getValue());
        this.setSelectionCountLimit(data.selectionLimit);

        this.canReorder = data.canReorder;
    }

    protected void reinitializeElements(){
        int heightRemaining = getHeight();

        updateTitleBox(0, getHeightUnscaled() - titleBoxHeight, getHeightUnscaled(), titleBoxHeight);
        if(titleBox != null) heightRemaining -= titleBox.getHeightUnscaled();

        updateItemBox(0, 0, getWidthUnscaled(), heightRemaining);
        updateScrollBar(0, 0, getWidthUnscaled(), heightRemaining);
    }

    private void updateTitleBox(int xPos, int yPos, int width, int height){
        if(title != null && !title.isEmpty()){
            if(titleBox == null){
                buildTitleBox(width, height);
            }
            titleBox.setText(title);
            titleBox.setLocalPosition(xPos, yPos);
            titleBox.setDimensions(width, height);
        }
        else if(titleBox != null){
            removeChild(titleBox);
            titleBox = null;
        }
    }
    private void buildTitleBox(int width, int height){
        titleBox = new TextBox(title, 0, 0, getWidth(), titleBoxHeight);
        titleBox.setImage(UITheme.whitePixel);
        titleBox.setRenderColor(Color.valueOf("#151515FF"));
        titleBox.setTextRenderColor(Color.WHITE);
        titleBox.setHorizontalAlignment(Alignment.HorizontalAlignment.LEFT);
        titleBox.setMarginPercX(0.005f);
        addChildNCS(titleBox);
    }

    protected void updateFilter(int xPos, int yPos, int width, int height){

    }
    protected void buildFilter(int x, int y, int width, int height){

    }

    private void updateItemBox(int xPos, int yPos, int width, int height){
        int updateHeight = getHeight();
        if(titleBox != null) updateHeight -= titleBox.getHeight();

        // We can span entire width since elements will get shrunk if Scrollbox is present

        if(itemBoxBackground == null){
            buildItemBox(0, 0, getWidth(), updateHeight);
        }

        itemBoxBackground.setLocalPosition(0, 0);
        itemBoxBackground.setDimensions(getWidth(), updateHeight);
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

    protected abstract void updateScrollBar(int xPos, int yPos, int width, int height);
    protected abstract void buildScrollBar(int x, int y, int width, int height);

    //endregion

    //region Methods

    //region Item Management

    public ItemBox<ItemType> addItem(ItemType item){
        UIElement compositeItem;
        if(!disableItemWrapping){
            compositeItem = wrapUIForItem(item);
            postMakeWrapperForItem(item, compositeItem);
        }
        else{
            compositeItem = makeUIForItem(item);
        }
        originalItems.add(new ItemBoxItem(item, compositeItem));
        addChildCS(compositeItem);

        onItemAdded(item);
        return this;
    }
    public void onItemAdded(ItemType item){
        onItemsChanged();
    }

    public ItemBox<ItemType> setItems(ArrayList<ItemType> items){
        clearItems();
        for(ItemType item : items){
            addItem(item);
        }

        onItemsSet(items);
        return this;
    }
    public void onItemsSet(ArrayList<ItemType> items){
        onItemsChanged();
    }

    public void clearItems(){
        ArrayList<UIElement> childrenToRemove = new ArrayList<>();
        for(UIElementChild child : children){
            for(ItemBoxItem item : originalItems){
                if(Objects.equals(item.renderForItem, child.element)){
                    childrenToRemove.add(child.element);
                }
            }
        }

        for(UIElement childToRemove : childrenToRemove){
            removeChild(childToRemove);
        }

        originalItems.clear();
        scrollbar.setFirstPage();

        onItemsCleared();
    }
    public void onItemsCleared(){
        onItemsChanged();
    }

    public void onItemsChanged(){
        refilterItems();
    }

    //endregion

    //region Item UI

    public UIElement makeUIForItem(ItemType item){
        TextBox box = new TextBox(item.toString(), 0, 0, defaultItemWidth == null ? itemBoxBackground.getWidth() : defaultItemWidth, defaultItemHeight == null ? itemBoxBackground.getHeight() : defaultItemHeight);
        box.setImage(UIThemeManager.getDefaultTheme().button_large);
        box.setMarginPercX(0.025f).setMarginPercY(0.05f);
        box.setAlignment(Alignment.HorizontalAlignment.LEFT, Alignment.VerticalAlignment.CENTER);
        return box;
    } //TODO expose with listeners

    public UIElement wrapUIForItem(ItemType item){
        UIElement itemUI = makeUIForItem(item);

        Color transparent = Color.WHITE.cpy();
        transparent.a = 0f;
        Button mainButton = (Button) new Button(0, 0, itemUI.getWidthUnscaled(), itemUI.getHeightUnscaled()){
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

        return itemUI;
    } //TODO expose
    public void postMakeWrapperForItem(ItemType item, UIElement itemUI){ } //TODO expose

    public ItemBox<ItemType> disableItemWrapping(){
        disableItemWrapping = true;
        return this;
    }

    //endregion

    //region Item Selection

    private void trySelectItem(ItemType selectedItem){
        if(getCurrentlySelectedItems().size() + 1 > getSelectionCountLimit()) return;

        for(ItemBoxItem item : items){
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
        for(ItemBoxItem item : items){
            if(item.selected) selectedItems.add(item.item);
        }

        return selectedItems;
    }

    public ItemBox<ItemType> setSelectionMode(ESelectionMode selectionMode){
        this.selectionMode = selectionMode;
        return this;
    } //TODO expose
    public ESelectionMode getSelectionMode(){
        return selectionMode;
    }

    public ItemBox<ItemType> setSelectionCountLimit(int selectionCount){
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

    public ItemBox<ItemType> setItemSpacing(int spacing){
        this.itemSpacing = spacing;
        return this;
    }
    public int getItemSpacing(){
        return itemSpacing;
    }

    public ItemBox<ItemType> setInvertedItemOrder(boolean invertedItemOrder){
        this.invertedItemOrder = invertedItemOrder;
        return this;
    }

    //endregion

    //region Title & TitleBox

    public ItemBox<ItemType> setTitle(String title){
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

    public ItemBox<ItemType> setTitleHeight(int titleHeight){
        if(titleHeight <= 0) return this;

        this.titleBoxHeight = titleHeight;
        return this;
    }

    //endregion

    //region Background

    public Hoverable getBackground(){
        return itemBoxBackground;
    }

    //endregion

    //region Reordering

    public ItemBox<ItemType> setCanReorder(boolean canReorder){
        this.canReorder = canReorder;
        return this;
    }
    public boolean canReorder(){
        return canReorder;
    }

    public ItemBox<ItemType> addOnElementsSwappedListener(BiConsumer<ItemType, ItemType> listener){
        onElementsSwappedListeners.add(listener);
        return this;
    }
    public void onElementsSwapped(ItemType item1, ItemType item2){
        for(BiConsumer<ItemType, ItemType> listener : onElementsSwappedListeners){
            listener.accept(item1, item2);
        }
    }

    protected void moveItemDown(ItemType itemUI){
        int itemIndex = -1;
        for (int i = 0; i < items.size(); i++) {
            ItemBoxItem item = items.get(i);
            if(item.item.equals(itemUI)){
                itemIndex = i;
            }
        }

        if(itemIndex == -1){
            return;
        }

        int swapIndex = itemIndex + (invertedItemOrder ? -1 : 1);
        if(swapIndex < 0 || swapIndex >= items.size()){
            return;
        }

        swap(itemIndex, swapIndex);
    }

    protected void moveItemUp(ItemType itemUI){
        int itemIndex = -1;
        for (int i = 0; i < items.size(); i++) {
            ItemBoxItem item = items.get(i);
            if(item.item.equals(itemUI)){
                itemIndex = i;
            }
        }

        if(itemIndex == -1){
            return;
        }

        int swapIndex = itemIndex + (invertedItemOrder ? 1 : -1);
        if(swapIndex < 0 || swapIndex >= items.size()){
            return;
        }

        swap(itemIndex, swapIndex);
    }

    private void swap(int index1, int index2){
        Collections.swap(items, index1, index2);
        onElementsSwapped(items.get(index1).item, items.get(index2).item);
    }

    //endregion

    //region Padding
    public ItemBox<ItemType> setLeftPadding(int leftPadding){
        return setPadding(leftPadding, itemPadding.y);
    }
    public ItemBox<ItemType> setTopPadding(int topPadding){
        return setPadding(itemPadding.x, topPadding);
    }
    public ItemBox<ItemType> setPadding(int leftPadding, int topPadding){
        this.itemPadding = new IntegerVector2(leftPadding, topPadding);
        return this;
    }

    //endregion

    //region Filter

    public ItemBox<ItemType> setFilterText(String filterText){
        if(filterText == null) filterText = "";

        this.filterText = filterText;
        refilterItems();
        return this;
    }

    public boolean filterCheck(String filterText, ItemType item){
        return item.toString().toLowerCase(Locale.ENGLISH).contains(filterText.toLowerCase(Locale.ENGLISH));
    }

    public void refilterItems(){
        items.clear();

        for(ItemBoxItem item : originalItems){
            if(!filterCheck(filterText, item.item)){
                continue;
            }

            items.add(item);
        }
    }

    //endregion Filter

    //endregion

    public class ItemBoxItem {
        /** Variables */
        public ItemType item;
        public UIElement renderForItem;

        public boolean selected;

        /** Constructors */
        public ItemBoxItem(ItemType item, UIElement renderElement){
            this.item = item;
            this.renderForItem = renderElement;
        }
    }

    public static class ItemBoxData extends UIElement.UIElementData implements Serializable {
        private static final long serialVersionUID = 1L;

        public StringProperty titleBoxText = new StringProperty("").setName("Title");
        public int titleBoxHeight = 50;

        public IntegerProperty itemSpacing = (IntegerProperty) new IntegerProperty(0).setMinimumValue(0).setName("Item Spacing");
        public BooleanProperty invertedItemOrder = new BooleanProperty(false).setName("Inverted Item Order");

        public IntegerVector2Property itemPadding = new IntegerVector2Property(new IntegerVector2(0, 0)).setName("Item Padding").setValueNames("L:", "T:");

        public EnumProperty<ESelectionMode> selectionMode = (EnumProperty<ESelectionMode>) new EnumProperty<>(ESelectionMode.SINGLE).setName("Selection Mode");
        public int selectionLimit = 1; //TODO allow

        public boolean canReorder = false;
    }
}
