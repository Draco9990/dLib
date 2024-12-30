package dLib.ui.elements.items.itembox;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.helpers.FontHelper;
import dLib.properties.objects.*;
import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.components.ItemboxChildComponent;
import dLib.ui.elements.items.Button;
import dLib.ui.elements.items.text.ImageTextBox;
import dLib.ui.elements.items.Renderable;
import dLib.ui.resources.UICommonResources;


import dLib.ui.util.ESelectionMode;
import dLib.util.bindings.texture.Tex;
import dLib.util.events.Event;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;
import dLib.util.ui.position.Pos;

import java.io.Serializable;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class ItemBox<ItemType> extends Renderable {
    //region Variables

    // Elements
    protected ArrayList<ItemBoxItem> items = new ArrayList<>();
    protected ArrayList<ItemBoxItem> originalItems = new ArrayList<>();

    private Alignment contentAlignment = new Alignment(Alignment.HorizontalAlignment.LEFT, Alignment.VerticalAlignment.BOTTOM);

    private String filterText = "";

    // Properties
    protected int itemSpacing = 1;
    protected boolean invertedItemOrder = false;

    public Event<Consumer<ItemType>> onItemAddedEvent = new Event<>();
    public Event<Consumer<ItemType>> onItemRemovedEvent = new Event<>();
    public Event<Runnable> onItemsChangedEvent = new Event<>();

    private boolean canReorder = false;
    public Event<BiConsumer<ItemType, ItemType>> onItemsSwappedEvent = new Event<>();

    private ESelectionMode selectionMode = ESelectionMode.SINGLE;
    private int selectionCountLimit = 1;

    public Event<Consumer<ArrayList<ItemType>>> onItemSelectionChangedEvent = new Event<>();

    protected Integer defaultItemWidth = null;
    protected Integer defaultItemHeight = null;

    private boolean disableItemWrapping = false;
    //endregion

    //region Constructors

    public ItemBox(AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height){
        super(Tex.stat(UICommonResources.white_pixel), xPos, yPos, width, height);

        Color bgColor = Color.BLACK.cpy();
        bgColor.a = 0.4f;

        setRenderColor(bgColor);

        setPassthrough(true);

        registerCommonEvents();
    }

    public ItemBox(ItemBoxData data){
        super(data);

        this.itemSpacing = data.itemSpacing.getValue();
        this.invertedItemOrder = data.invertedItemOrder.getValue();

        this.setSelectionMode(data.selectionMode.getValue());
        this.setSelectionCountLimit(data.selectionLimit);

        this.canReorder = data.canReorder;

        registerCommonEvents();
    }

    public void registerCommonEvents(){
        onItemsChangedEvent.subscribe(this, this::refilterItems);
    }

    //endregion

    //region Methods

    //region Item Management

    public void addItem(ItemType item){
        UIElement compositeItem;
        if(!disableItemWrapping){
            compositeItem = wrapUIForItem(item);
            postMakeWrapperForItem(item, compositeItem);
        }
        else{
            compositeItem = makeUIForItem(item);
        }

        compositeItem.setElementMask(this);

        originalItems.add(new ItemBoxItem(item, compositeItem));
        compositeItem.addComponent(new ItemboxChildComponent());
        addChildCS(compositeItem);
        onItemAdded(item);
    }
    public void insertItem(int insertIndex, ItemType item){
        UIElement compositeItem;
        if(!disableItemWrapping){
            compositeItem = wrapUIForItem(item);
            postMakeWrapperForItem(item, compositeItem);
        }
        else{
            compositeItem = makeUIForItem(item);
        }
        originalItems.add(insertIndex, new ItemBoxItem(item, compositeItem));
        addChildCS(compositeItem);

        onItemAdded(item);
    }
    private void onItemAdded(ItemType item){
        onItemAddedEvent.invoke(itemTypeConsumer -> itemTypeConsumer.accept(item));
        onItemsChanged();
    }

    public void setItems(ArrayList<ItemType> items){
        clearItems();
        for(ItemType item : items){
            addItem(item);
        }

        onItemsSet(items);
    }
    private void onItemsSet(ArrayList<ItemType> items){
        onItemsChanged();
    }

    public void updateItems(ArrayList<ItemType> items){
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
                existingItem.renderForItem.dispose();
                itemsChanged = true;
            }
        }

        for(ItemType item : itemsToAdd){
            addItem(item);
            itemsChanged = true;
        }

        originalItems.sort(Comparator.comparingInt(o -> items.indexOf(o.item)));

        if(itemsChanged){
            onItemsChanged();
        }
        if(selectionChanged){
            onItemSelectionChanged(getCurrentlySelectedItems());
        }
    }

    public void clearItems(){
        ArrayList<UIElement> childrenToRemove = new ArrayList<>();
        for(UIElement child : getChildren()){
            for(ItemBoxItem item : originalItems){
                if(Objects.equals(item.renderForItem, child)){
                    childrenToRemove.add(child);
                }
            }
        }

        for(UIElement childToRemove : childrenToRemove){
            childToRemove.dispose();
        }

        for(ItemBoxItem item : originalItems){
            onItemRemoved(item.item);
        }
        originalItems.clear();

        onItemsChanged();
    }
    public void removeItem(ItemType item){
        for(ItemBoxItem itemBoxItem : originalItems){
            if(itemBoxItem.item.equals(item)){
                originalItems.remove(itemBoxItem);
                itemBoxItem.renderForItem.dispose();
                break;
            }
        }
    }
    private void onItemRemoved(ItemType item){
        onItemRemovedEvent.invoke(itemTypeConsumer -> itemTypeConsumer.accept(item));
        onItemsChanged();
    }

    private void onItemsChanged(){
        onItemsChangedEvent.invoke(Runnable::run);
    }

    public boolean containsItem(ItemType item){
        for(ItemBoxItem itemBoxItem : originalItems){
            if(itemBoxItem.item.equals(item)){
                return true;
            }
        }

        return false;
    }
    public boolean containsRenderItem(UIElement renderItem){
        for(ItemBoxItem itemBoxItem : originalItems){
            if(itemBoxItem.renderForItem.equals(renderItem)){
                return true;
            }
        }

        return false;
    }

    //endregion

    //region Item Management Overrides

    @Override
    public void replaceChild(UIElement original, UIElement replacement) {
        for(ItemBoxItem item : originalItems){
            if(item.renderForItem.equals(original)){
                item.renderForItem = replacement;
                if(item.item.equals(original)) item.item = (ItemType) original;
                break;
            }
        }

        for(ItemBoxItem item : items){
            if(item.renderForItem.equals(original)){
                item.renderForItem = replacement;
                if(item.item.equals(original)) item.item = (ItemType) original;
                break;
            }
        }

        if(!replacement.hasComponent(ItemboxChildComponent.class)){
            replacement.addComponent(new ItemboxChildComponent());
        }

        super.replaceChild(original, replacement);
    }

    //endregion

    //region Item UI

    public UIElement makeUIForItem(ItemType item){
        ImageTextBox box = new ImageTextBox(itemToString(item), defaultItemWidth == null ? Dim.fill() : Dim.px(defaultItemWidth), defaultItemHeight == null ? Dim.fill() : Dim.px(defaultItemHeight));
        box.textBox.setFont(FontHelper.buttonLabelFont);
        box.setAlignment(Alignment.HorizontalAlignment.LEFT, Alignment.VerticalAlignment.CENTER);
        return box;
    } //TODO expose with listeners

    public String itemToString(ItemType item){
        return item.toString();
    }

    public void updateUIForItem(ItemType item, UIElement element){

    }

    public UIElement wrapUIForItem(ItemType item){
        UIElement itemUI = makeUIForItem(item);

        Color transparent = Color.WHITE.cpy();
        transparent.a = 0f;
        Button mainButton = new Button(Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill()){
            @Override
            protected void onLeftClick() {
                super.onLeftClick();
                trySelectItem(item);
            }

            @Override
            public boolean isActive() {
                return getSelectionCountLimit() != 0;
            }
        };
        mainButton.setImage(Tex.stat(UICommonResources.white_pixel));
        mainButton.setRenderColor(transparent);

        Color hoverColor = mainButton.getHoveredColor().cpy();
        hoverColor.a = 0.4f;
        mainButton.setHoveredColor(hoverColor);
        mainButton.setHoveredColorMultiplier(1.0f);

        itemUI.addChildCS(mainButton);

        return itemUI;
    } //TODO expose
    public void postMakeWrapperForItem(ItemType item, UIElement itemUI){ } //TODO expose

    public void disableItemWrapping(){
        disableItemWrapping = true;
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
            if(selectionMode == ESelectionMode.SINGLE_NOPERSIST){
                item.selected = false;
                continue;
            }

            if(item.item.equals(selectedItem)){
                if(selectionMode == ESelectionMode.SINGLE){
                    item.selected = true;
                }
                else{
                    item.selected = !item.selected;
                }
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
    public void onItemSelectionChanged(ArrayList<ItemType> items){
        onItemSelectionChangedEvent.invoke(itemTypeConsumer -> itemTypeConsumer.accept(items));
    } //TODO expose

    public ArrayList<ItemType> getCurrentlySelectedItems(){
        ArrayList<ItemType> selectedItems = new ArrayList<>();
        for(ItemBoxItem item : items){
            if(item.selected) selectedItems.add(item.item);
        }

        return selectedItems;
    }

    public void setSelectionMode(ESelectionMode selectionMode){
        this.selectionMode = selectionMode;
    } //TODO expose
    public ESelectionMode getSelectionMode(){
        return selectionMode;
    }

    public void setSelectionCountLimit(int selectionCount){
        this.selectionCountLimit = selectionCount;
    } //TODO expose
    public int getSelectionCountLimit(){
        if(selectionMode.equals(ESelectionMode.NONE)) return 0;
        else if(selectionMode.equals(ESelectionMode.SINGLE)) return 1;
        else return selectionCountLimit;
    }

    public void deselectAllItems(){
        for(ItemBoxItem item : items){
            item.selected = false;
        }

        onItemSelectionChanged(new ArrayList<>());
    }

    //endregion

    //region Item Properties

    public void setItemSpacing(int spacing){
        this.itemSpacing = spacing;
    }
    public int getItemSpacing(){
        return itemSpacing;
    }

    public void setInvertedItemOrder(boolean invertedItemOrder){
        this.invertedItemOrder = invertedItemOrder;
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

        onItemsSwappedEvent.invoke(itemTypeItemTypeBiConsumer -> itemTypeItemTypeBiConsumer.accept(itemtoSwap1.item, itemtoSwap2.item));
        onItemsChangedEvent.invoke(Runnable::run);
    }

    //endregion

    //region Filter

    public void setFilterText(String filterText){
        if(filterText == null) filterText = "";

        this.filterText = filterText;
        refilterItems();
    }

    public boolean filterCheck(String filterText, ItemType item){
        return item.toString().toLowerCase(Locale.ROOT).contains(filterText.toLowerCase(Locale.ROOT));
    }

    public void refilterItems(){
        items.clear();

        for(ItemBoxItem item : originalItems){
            if(!filterCheck(filterText, item.item)){
                item.renderForItem.hideAndDisableInstantly();
                continue;
            }

            items.add(item);
            item.renderForItem.showAndEnableInstantly();
        }
    }

    //endregion Filter

    //region Content Alignment

    public void setHorizontalContentAlignment(Alignment.HorizontalAlignment horizontalAlignment){
        setContentAlignment(horizontalAlignment, contentAlignment.verticalAlignment);
    }
    public void setVerticalContentAlignment(Alignment.VerticalAlignment verticalAlignment){
        setContentAlignment(contentAlignment.horizontalAlignment, verticalAlignment);
    }
    public void setContentAlignment(Alignment.HorizontalAlignment horizontalAlignment, Alignment.VerticalAlignment verticalAlignment){
        contentAlignment.horizontalAlignment = horizontalAlignment;
        contentAlignment.verticalAlignment = verticalAlignment;
    }

    public Alignment.HorizontalAlignment getHorizontalContentAlignment(){
        return contentAlignment.horizontalAlignment;
    }
    public Alignment.VerticalAlignment getVerticalContentAlignment(){
        return contentAlignment.verticalAlignment;
    }
    public Alignment getContentAlignment(){
        return contentAlignment;
    }

    //endregion Alignment

    //region Local Child Offsets

    @Override
    public int getLocalChildOffsetX() {
        if(getParent() == null){
            return super.getLocalChildOffsetX();
        }

        //! This is a hack to get the local offset of the parent in case of recursive item boxes
        return getParent().getLocalChildOffsetXRaw() + super.getLocalChildOffsetX();
    }

    @Override
    public int getLocalChildOffsetY() {
        if(getParent() == null){
            return super.getLocalChildOffsetY();
        }

        return getParent().getLocalChildOffsetYRaw() + super.getLocalChildOffsetY();
    }

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

    public static class ItemBoxData extends RenderableData implements Serializable {
        private static final long serialVersionUID = 1L;

        public IntegerProperty itemSpacing = new IntegerProperty(1).setMinimumValue(1).setName("Item Spacing");
        public BooleanProperty invertedItemOrder = new BooleanProperty(false).setName("Inverted Item Order");

        public EnumProperty<ESelectionMode> selectionMode = new EnumProperty<>(ESelectionMode.SINGLE).setName("Selection Mode");
        public int selectionLimit = 1; //TODO allow

        public boolean canReorder = false;
    }
}
