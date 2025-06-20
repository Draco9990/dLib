package dLib.ui.elements.items.itembox;

import basemod.Pair;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.helpers.FontHelper;
import dLib.properties.objects.*;
import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.components.UIOverlayElementComponent;
import dLib.ui.elements.components.UITransientElementComponent;
import dLib.ui.elements.items.Toggle;
import dLib.ui.elements.items.text.ImageTextBox;
import dLib.ui.resources.UICommonResources;
import dLib.ui.util.ESelectionMode;
import dLib.util.BiMap;
import dLib.util.bindings.font.Font;
import dLib.util.bindings.texture.Tex;
import dLib.util.events.Event;
import dLib.util.events.localevents.ConsumerEvent;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;

import java.io.Serializable;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class DataItemBox<ItemType> extends ItemBox {
    //region Variables

    // Elements
    protected BiMap<UIElement, ItemType> childWrapperMap = new BiMap<>();

    // Properties
    private boolean canReorder = false; //TODO
    public Event<BiConsumer<ItemType, ItemType>> onItemsSwappedEvent = new Event<>(); //TODO expose

    private ESelectionMode selectionMode = ESelectionMode.SINGLE;
    private int selectionCountLimit = 1; //TODO expose

    public Event<Consumer<ArrayList<ItemType>>> onItemSelectionChangedEvent = new Event<>();

    protected Integer defaultItemWidth = null;
    protected Integer defaultItemHeight = null;

    private boolean disableItemWrapping = false;

    public ConsumerEvent<ItemType> onItemAddedEvent = new ConsumerEvent<>();
    public ConsumerEvent<ItemType> onItemRemovedEvent = new ConsumerEvent<>();

    //endregion

    //region Constructors

    public DataItemBox(AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height){
        super(xPos, yPos, width, height);
    }

    public DataItemBox(DataItemBoxData data){
        super(data);

        this.setSelectionMode(data.selectionMode.getValue());
        this.onItemSelectionChangedEvent.subscribeManaged(itemType -> data.onItemSelectionChanged.getValue().executeBinding(this, itemType));

        this.canReorder = data.canReorder;
    }

    @Override
    public void registerCommonEvents() {
        super.registerCommonEvents();

        onChildAddedEvent.subscribeManaged(element -> onItemAddedEvent.invoke(childWrapperMap.getByKey(element)));
        onChildRemovedEvent.subscribeManaged(element -> onItemRemovedEvent.invoke(childWrapperMap.getByKey(element)));
    }
//endregion

    //region Methods

    //region Item Management

    public void addChild(ItemType item){
        UIElement toAdd = generateElementForItem(item);

        childWrapperMap.put(toAdd, item);
        super.addChild(toAdd);
    }
    @Override
    public void addChild(UIElement item) {
        throw new UnsupportedOperationException("Use addChild(ItemType item) instead.");
    }

    public void insertChild(int insertIndex, ItemType item){
        UIElement toAdd = generateElementForItem(item);

        childWrapperMap.put(toAdd, item);
        super.insertChild(insertIndex, toAdd);
    }

    public UIElement generateElementForItem(ItemType item){
        UIElement toAdd;
        toAdd = wrapUIForItem(item);

        toAdd.setElementMask(this);

        return toAdd;
    }

    public void setChildren(List<ItemType> items){
        clearChildren();
        for(ItemType item : items){
            addChild(item);
        }
    }
    @Override
    public void setChildren(ArrayList<UIElement> children) {
        throw new UnsupportedOperationException("Use setChildren(List<ItemType> items) instead.");
    }

    public void updateChildren(ArrayList<ItemType> items){
        boolean selectionChanged = false;

        ArrayList<ItemType> itemsToAdd = new ArrayList<>(items);

        ArrayList<UIElement> existingChildren = new ArrayList<>(children);
        Iterator<UIElement> existingChildrenIterator = existingChildren.iterator();
        while(existingChildrenIterator.hasNext()){
            UIElement existingChild = existingChildrenIterator.next();
            ItemType existingChildItem = childWrapperMap.getByKey(existingChild);
            if(itemsToAdd.contains(existingChildItem)){
                itemsToAdd.remove(existingChildItem);
                updateUIForItem(existingChildItem, existingChild);
            }
            else {
                if(existingChild.isSelected()){
                    existingChild.deselect();
                    selectionChanged = true;
                }

                removeChildById(existingChild);
                childWrapperMap.removeByKey(existingChild);
            }
        }

        for (int i = 0; i < itemsToAdd.size(); i++) {
            ItemType item = itemsToAdd.get(i);
            insertChild(i, item);
        }

        if(selectionChanged){
            onItemSelectionChanged();
        }
    }

    @Override
    public void clearChildren() {
        super.clearChildren();

        childWrapperMap.clear();
    }

    @Override
    public void removeChildById(UIElement child) {
        super.removeChildById(child);

        childWrapperMap.removeByKey(child);
    }

    public void removeChild(ItemType item){
        UIElement child = childWrapperMap.getByValue(item);
        if(child != null){
            removeChildById(child);
        }
    }

    public boolean hasChild(ItemType item){
        return childWrapperMap.containsValue(item);
    }

    public ArrayList<ItemType> getItems(){
        ArrayList<ItemType> items = new ArrayList<>();
        for(UIElement child : children){
            items.add(childWrapperMap.getByKey(child));
        }
        return items;
    }

    //endregion

    //region Item Management Overrides

    @Override
    public void replaceChild(UIElement original, UIElement replacement) {
        ItemType item = childWrapperMap.getByKey(original);
        childWrapperMap.removeByKey(original);
        childWrapperMap.put(replacement, item);
    }

    //endregion

    //region Item UI

    public UIElement makeUIForItem(ItemType item){
        ImageTextBox box = new ImageTextBox(itemToString(item), defaultItemWidth == null ? Dim.fill() : Dim.px(defaultItemWidth), defaultItemHeight == null ? Dim.fill() : Dim.px(defaultItemHeight));
        box.textBox.setFont(Font.stat(FontHelper.buttonLabelFont));
        box.setTexture(Tex.stat(UICommonResources.button03_square));
        box.setAlignment(Alignment.HorizontalAlignment.LEFT, Alignment.VerticalAlignment.CENTER);
        return box;
    } //TODO expose with listeners

    public String itemToString(ItemType item){
        return item.toString();
    }

    public void updateUIForItem(ItemType item, UIElement element){

    }

    private UIElement wrapUIForItem(ItemType item){
        UIElement itemUI = makeUIForItem(item);
        if(disableItemWrapping){
            postMakeUIForItem(item, itemUI);
            return itemUI;
        }

        UIElement parent = new UIElement(Dim.auto(), Dim.auto());
        parent.addChild(itemUI);

        Toggle overlay = new Toggle(Tex.stat(UICommonResources.white_pixel), itemUI.getWidthRaw(), itemUI.getHeightRaw()){
            @Override
            public void toggle() {
                if((isToggled() && getSelectionMode() == ESelectionMode.MULTIPLE) || trySelectItem(item)){
                    super.toggle();
                    onItemSelectionChanged();

                    if(selectionMode == ESelectionMode.SINGLE_NOPERSIST && isToggled()){
                        setToggled(false);
                    }
                }
            }

            @Override
            public boolean isActive() {
                return getSelectionMode() != ESelectionMode.NONE && getSelectionCountLimit() > 0;
            }
        };
        overlay.setID("wrap_overlay");
        overlay.setRenderColor(new Color(0, 0, 0, 0f));
        overlay.addComponent(new UIOverlayElementComponent());
        overlay.setControllerSelectable(getSelectionMode() != ESelectionMode.NONE);
        parent.addChild(overlay);

        postMakeUIForItem(item, itemUI);
        return parent;
    }

    protected void postMakeUIForItem(ItemType item, UIElement itemUI){
    }

    //endregion

    //region Item Selection

    private boolean trySelectItem(ItemType selectedItem){
        if(selectionMode == ESelectionMode.NONE){
            return false;
        }

        if(selectionMode == ESelectionMode.MULTIPLE && getCurrentlySelectedItems().size() + 1 > getSelectionCountLimit()){
            return false;
        }

        if(((Toggle) childWrapperMap.getByValue(selectedItem).findChildById("wrap_overlay")).isToggled() && !selectionMode.equals(ESelectionMode.MULTIPLE)){
            return false;
        }

        if(selectionMode == ESelectionMode.SINGLE || selectionMode == ESelectionMode.SINGLE_NOPERSIST){
            for(UIElement child : children){
                Toggle wrapOverlay = child.findChildById("wrap_overlay");
                if(wrapOverlay == null){
                    continue;
                }

                if(childWrapperMap.getByValue(selectedItem) != child && wrapOverlay.isToggled()){
                    wrapOverlay.setToggled(false);
                }
            }
        }

        return true;
    }
    public void onItemSelectionChanged(){
        onItemSelectionChangedEvent.invoke(itemTypeConsumer -> itemTypeConsumer.accept(getCurrentlySelectedItems()));
    } //TODO expose

    public ArrayList<ItemType> getCurrentlySelectedItems(){
        ArrayList<ItemType> selectedItems = new ArrayList<>();
        for(UIElement child : children){
            Toggle wrapOverlay = child.findChildById("wrap_overlay");
            if(wrapOverlay == null){
                continue;
            }

            if(wrapOverlay.isToggled()){
                selectedItems.add(childWrapperMap.getByKey(child));
            }
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
        boolean changed = false;

        for(UIElement child : children){
            Toggle wrapOverlay = child.findChildById("wrap_overlay");
            if(wrapOverlay == null){
                continue;
            }

            if(wrapOverlay.isToggled()){
                wrapOverlay.setToggled(false);
                changed = true;
            }
        }

        if(changed){
            onItemSelectionChanged();
        }
    }

    //endregion

    //region Item Wrapping

    public void disableItemWrapping(){
        disableItemWrapping = true;
    }

    //endregion

    //region Reordering

    /*public DataItemBox<ItemType> setCanReorder(boolean canReorder){
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
        onChildrenChangedEvent.invoke();
    }*/

    //endregion

    //region Filters

    @Override
    public void refilterItems(){
        filteredChildren.clear();

        for(UIElement child : children){
            if(!filterCheck(filterText, child, childWrapperMap.getByKey(child))){
                continue;
            }

            if(!child.isActiveRaw()){
                continue;
            }

            filteredChildren.add(child);
        }

        if(invertedItemOrder){
            Collections.reverse(filteredChildren);
        }
    }

    protected boolean filterCheck(String filterText, UIElement item, ItemType originalItem){
        return originalItem.toString().toLowerCase(Locale.ROOT).contains(filterText.toLowerCase(Locale.ROOT)) && !item.hasComponent(UITransientElementComponent.class);
    }

    //endregion

    //endregion

    public static class DataItemBoxData extends ItemBox.ItemBoxData implements Serializable {
        private static final long serialVersionUID = 1L;

        //TODO add a class picker property that determines item types

        public EnumProperty<ESelectionMode> selectionMode = new EnumProperty<>(ESelectionMode.SINGLE)
                .setName("Selection Mode")
                .setDescription("The selection mode of the item box." + "\n" +
                        "NONE: No items can be selected." + "\n" +
                        "SINGLE_NOPERSIST: Only one item can be selected at a time, but the selection is not persisted." + "\n" +
                        "SINGLE: Only one item can be selected at a time." + "\n" +
                        "MULTIPLE: Multiple items can be selected at a time.")
                .setCategory("Item Box");
        public MethodBindingProperty onItemSelectionChanged = new MethodBindingProperty()
                .setName("On Item Selection Changed")
                .setDescription("The method to call when the selection of items in the item box is changed.")
                .setCategory("Item Box")
                .setDynamicCreationParameters(new Pair<>("selectedItems", ArrayList.class));
    }
}
