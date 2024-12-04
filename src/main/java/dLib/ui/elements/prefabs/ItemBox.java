package dLib.ui.elements.prefabs;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.helpers.FontHelper;
import dLib.properties.objects.*;
import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.ui.themes.UITheme;
import dLib.ui.util.ESelectionMode;
import dLib.util.IntegerVector2;

import java.io.Serializable;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class ItemBox<ItemType> extends UIElement {
    //region Variables

    // Elements
    protected TextBox titleBox;
    protected UIElement itemBox;
    protected Scrollbar scrollbar;

    protected ArrayList<ItemBoxItem> items = new ArrayList<>();
    protected ArrayList<ItemBoxItem> originalItems = new ArrayList<>();

    private String filterText = "";

    // Properties
    protected boolean noInitScrollbar = false; //TODO expose

    private String title;
    protected int titleBoxHeight = 50;

    protected int itemSpacing = 0;
    protected boolean invertedItemOrder = false;

    protected IntegerVector2 itemPadding = new IntegerVector2(0, 0);

    private ESelectionMode selectionMode = ESelectionMode.SINGLE;
    private int selectionCountLimit = 1;

    private boolean canReorder = false;
    private ArrayList<BiConsumer<ItemType, ItemType>> onElementsSwappedListeners = new ArrayList<>();

    private ArrayList<Consumer<ItemType>> onPropertyAddedConsumers = new ArrayList<>();
    private ArrayList<Consumer<ItemType>> onPropertyRemovedConsumers = new ArrayList<>();

    protected Integer defaultItemWidth = null;
    protected Integer defaultItemHeight = null;

    private boolean disableItemWrapping = false;

    // Locals
    protected boolean trackScrollWheelScroll = false;

    protected int currentScrollbarOffset = 0;

    //endregion

    //region Constructors

    public ItemBox(int xPos, int yPos, int width, int height){
        this(xPos, yPos, width, height, false);
    }

    public ItemBox(int xPos, int yPos, int width, int height, boolean noInitScrollbar){
        super(xPos, yPos, width, height);

        this.noInitScrollbar = noInitScrollbar;

        reinitializeElements(); //TODO move after constructor
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

        reinitializeElements(); //TODO move after constructor
    }

    protected void reinitializeElements() {
        //Update the title box
        if(titleBox != null){
            removeChild(titleBox);
            titleBox = null;
        }
        if(titleBoxHeight > 0 && title != null && !title.isEmpty()){
            titleBox = buildTitleBox();
            titleBox.setText(title);

            addChildNCS(titleBox);
        }

        //Update the item box
        if(itemBox != null){
            removeChild(itemBox);
            itemBox = null;
        }
        this.itemBox = buildItemBox();
        addChildNCS(itemBox);

        //Update the scrollbar
        if(!noInitScrollbar){
            if(scrollbar != null){
                removeChild(scrollbar);
                scrollbar = null;
            }
            this.scrollbar = buildScrollBar();
            addChildNCS(scrollbar);
        }
    }

    protected TextBox buildTitleBox(){
        TextBox titleBox = new TextBox(title, 0, getHeightUnscaled() - titleBoxHeight, getWidthUnscaled(), titleBoxHeight);
        titleBox.setImage(UITheme.whitePixel);
        titleBox.setRenderColor(Color.valueOf("#151515FF"));
        titleBox.setTextRenderColor(Color.WHITE);
        titleBox.setHorizontalAlignment(Alignment.HorizontalAlignment.LEFT);
        titleBox.setMarginPercX(0.005f);

        return titleBox;
    }

    protected abstract UIElement buildItemBox();
    protected abstract Scrollbar buildScrollBar();

    //endregion

    //region Methods

    public UIElement getItemBox(){
        return itemBox;
    }

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

        compositeItem.setElementMask(itemBox);

        originalItems.add(new ItemBoxItem(item, compositeItem));
        itemBox.addChildCS(compositeItem);

        onItemAdded(item);
        return this;
    }
    public void onItemAdded(ItemType item){
        for (Consumer<ItemType> consumer : onPropertyAddedConsumers) {
            consumer.accept(item);
        }

        onItemsChanged();
    }

    public ItemBox<ItemType> insertItem(int insertIndex, ItemType item){
        UIElement compositeItem;
        if(!disableItemWrapping){
            compositeItem = wrapUIForItem(item);
            postMakeWrapperForItem(item, compositeItem);
        }
        else{
            compositeItem = makeUIForItem(item);
        }
        originalItems.add(insertIndex, new ItemBoxItem(item, compositeItem));
        itemBox.addChildCS(compositeItem);

        onItemAdded(item);
        return this;
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

    public ItemBox<ItemType> updateItems(ArrayList<ItemType> items){
        boolean itemsChanged = false;
        boolean selectionChanged = false;

        ArrayList<ItemType> itemsToAdd = new ArrayList<>(items);

        Iterator<ItemBoxItem> existingItems = originalItems.iterator();
        while(existingItems.hasNext()){
            ItemBoxItem existingItem = existingItems.next();
            if(itemsToAdd.contains(existingItem.item)){
                ItemType item = itemsToAdd.get(itemsToAdd.indexOf(existingItem.item));
                itemsToAdd.remove(existingItem.item);
                updateUIForItem(item, existingItem.renderForItem);
            }
            else {
                if(existingItem.selected){
                    selectionChanged = true;
                }

                existingItems.remove();
                itemBox.removeChild(existingItem.renderForItem);
                itemsChanged = true;
            }
        }

        for(ItemType item : itemsToAdd){
            addItem(item);
            itemsChanged = true;
        }

        originalItems.sort(Comparator.comparingInt(o -> items.indexOf(o.item)));

        onItemsUpdated(items, itemsChanged);
        if(selectionChanged) onItemSelectionChanged(getCurrentlySelectedItems());

        return this;
    }
    public void onItemsUpdated(ArrayList<ItemType> items, boolean itemsChanged){
        if(itemsChanged){
            onItemsChanged();
        }
    }

    public void clearItems(){
        ArrayList<UIElement> childrenToRemove = new ArrayList<>();
        for(UIElement child : itemBox.getChildren()){
            for(ItemBoxItem item : originalItems){
                if(Objects.equals(item.renderForItem, child)){
                    childrenToRemove.add(child);
                }
            }
        }

        for(UIElement childToRemove : childrenToRemove){
            itemBox.removeChild(childToRemove);
        }

        for(ItemBoxItem item : originalItems){
            onItemRemoved(item.item);
        }
        originalItems.clear();
        if(scrollbar != null) scrollbar.reset();

        onItemsCleared();
    }
    public void onItemsCleared(){
        onItemsChanged();
    }

    public void onItemsChanged(){
        refilterItems();
    }

    public boolean containsItem(ItemType item){
        for(ItemBoxItem itemBoxItem : originalItems){
            if(itemBoxItem.item.equals(item)){
                return true;
            }
        }

        return false;
    }

    public ItemBox<ItemType> removeItem(ItemType item){
        for(ItemBoxItem itemBoxItem : originalItems){
            if(itemBoxItem.item.equals(item)){
                originalItems.remove(itemBoxItem);
                itemBox.removeChild(itemBoxItem.renderForItem);
                break;
            }
        }

        onItemRemoved(item);
        return this;
    }

    public void onItemRemoved(ItemType item){
        for (Consumer<ItemType> consumer : onPropertyRemovedConsumers) {
            consumer.accept(item);
        }

        onItemsChanged();
    }

    //region Consumers

    public ItemBox<ItemType> addOnPropertyAddedConsumer(Consumer<ItemType> consumer){
        onPropertyAddedConsumers.add(consumer);
        return this;
    }

    public ItemBox<ItemType> addOnPropertyRemovedConsumer(Consumer<ItemType> consumer){
        onPropertyRemovedConsumers.add(consumer);
        return this;
    }

    //endregion

    //endregion

    //region Item UI

    public UIElement makeUIForItem(ItemType item){
        TextBox box = new TextBox(item.toString(), 0, 0, defaultItemWidth == null ? itemBox.getWidth() : defaultItemWidth, defaultItemHeight == null ? itemBox.getHeight() : defaultItemHeight);
        box.setMarginPercX(0.025f).setMarginPercY(0.2f);
        box.setFont(FontHelper.buttonLabelFont);
        box.setTextRenderColor(Color.WHITE);
        box.setAlignment(Alignment.HorizontalAlignment.LEFT, Alignment.VerticalAlignment.CENTER);
        return box;
    } //TODO expose with listeners

    public void updateUIForItem(ItemType item, UIElement element){

    }

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
        if(selectionMode == ESelectionMode.NONE){
            return;
        }

        if(selectionMode == ESelectionMode.MULTIPLE && getCurrentlySelectedItems().size() + 1 > getSelectionCountLimit()){
            return;
        }

        for(ItemBoxItem item : items){
            if(item.item.equals(selectedItem) && !selectionMode.equals(ESelectionMode.SINGLE_NOPERSIST)){
                item.selected = true;
            }
            else if(selectionMode == ESelectionMode.SINGLE){
                item.selected = false;
            }
        }

        if(selectionMode.equals(ESelectionMode.SINGLE) || selectionMode.equals(ESelectionMode.SINGLE_NOPERSIST)){
            onItemSelectionChanged(new ArrayList<>(Collections.singletonList(selectedItem)));
        }
        else if(selectionMode.equals(ESelectionMode.MULTIPLE)){
            onItemSelectionChanged(getCurrentlySelectedItems());
        }
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
        reinitializeElements();
        return this;
    }

    //endregion

    //region Background

    public UIElement getBackground(){
        return itemBox;
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
        ItemBoxItem itemtoSwap1 = items.get(index1);
        ItemBoxItem itemtoSwap2 = items.get(index2);

        Integer originalIndex1 = originalItems.indexOf(itemtoSwap1);
        Integer originalIndex2 = originalItems.indexOf(itemtoSwap2);

        Collections.swap(originalItems, originalIndex1, originalIndex2);
        onElementsSwapped(itemtoSwap1.item, itemtoSwap2.item);
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
        return item.toString().toLowerCase(Locale.ROOT).contains(filterText.toLowerCase(Locale.ROOT));
    }

    public void refilterItems(){
        items.clear();

        for(ItemBoxItem item : originalItems){
            if(!filterCheck(filterText, item.item)){
                continue;
            }

            items.add(item);
        }

        if(scrollbar != null){
            scrollbar.setScrollbarScrollPercentageForExternalChange(recalculateScrollPercentageForItemChange());
        }
    }

    //endregion Filter

    //region Scrolling

    protected abstract int recalculateScrollOffset(float scrollPercentage);

    protected abstract float recalculateScrollPercentageForItemChange();

    //endregion

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
