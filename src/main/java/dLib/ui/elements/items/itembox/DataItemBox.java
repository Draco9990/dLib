package dLib.ui.elements.items.itembox;

import basemod.Pair;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.helpers.FontHelper;
import dLib.properties.objects.EnumProperty;
import dLib.properties.objects.MethodBindingProperty;
import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.components.UIOverlayElementComponent;
import dLib.ui.elements.components.UITransientElementComponent;
import dLib.ui.elements.items.buttons.Button;
import dLib.ui.elements.items.buttons.Checkbox;
import dLib.ui.elements.items.buttons.RadioButton;
import dLib.ui.elements.items.buttons.Toggle;
import dLib.ui.elements.items.text.ImageTextBox;
import dLib.ui.resources.UICommonResources;
import dLib.ui.util.ESelectionMode;
import dLib.util.BiMap;
import dLib.util.bindings.font.Font;
import dLib.util.bindings.string.Str;
import dLib.util.bindings.string.interfaces.ITextProvider;
import dLib.util.bindings.texture.Tex;
import dLib.util.events.localevents.*;
import dLib.util.ui.dimensions.AbstractDimension;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.position.AbstractPosition;

import java.io.Serializable;
import java.util.*;
import java.util.function.Supplier;

public abstract class DataItemBox<ItemType> extends ItemBox {
    //region Variables

    static String wrapperId = "wrapper";
    static String wrapOverlayID = "wrap_overlay";

    protected BiMap<UIElement, ItemType> childWrapperMap = new BiMap<>();

    private boolean shouldOverlayToggle = true;
    private boolean externalToggling = false; //TODO expose

    private boolean canReorder = false; //TODO
    public BiConsumerEvent<ItemType, ItemType> onItemsSwappedEvent = new BiConsumerEvent<>(); //TODO expose

    private boolean canDelete = false; //TODO
    public ConsumerEvent<ItemType> onItemDeletedEvent = new ConsumerEvent<>(); //TODO expose

    private ESelectionMode selectionMode = ESelectionMode.SINGLE;
    private int selectionCountLimit = 1; //TODO expose

    public ConsumerEvent<ArrayList<ItemType>> onItemSelectionChangedEvent = new ConsumerEvent<>();

    protected int defaultItemWidth = 15;
    protected int defaultItemHeight = 30;

    public ConsumerEvent<ItemType> onItemAddedEvent = new ConsumerEvent<>();
    public ConsumerEvent<ItemType> onItemRemovedEvent = new ConsumerEvent<>();

    public FunctionEvent<ItemType, Boolean> filterToModifyEvent = new FunctionEvent<>();
    public SerializableComparatorEvent<ItemType> sortToModifyEvent = new SerializableComparatorEvent<>();

    //endregion

    //region Constructors

    public DataItemBox(AbstractPosition xPos, AbstractPosition yPos, AbstractDimension width, AbstractDimension height){
        super(xPos, yPos, width, height);

        setSayTheSpireElementType(Str.lambda(() -> "Item List Box with " + filteredChildren.size() + " items. Select up to " + getSelectionCountLimit() + " items."));
    }

    public DataItemBox(DataItemBoxData data){
        super(data);

        this.setSelectionMode(data.selectionMode.getValue());
        this.onItemSelectionChangedEvent.subscribeManaged(itemType -> data.onItemSelectionChanged.getValue().executeBinding(this, itemType));

        this.canReorder = data.canReorder;

        setSayTheSpireElementType("Item List Box");
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
        disposeChildren();
        for(ItemType item : items){
            addChild(item);
        }
    }
    @Override
    public void setChildren(ArrayList<? extends UIElement> children) {
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
                updateUIForItem(existingChildItem, existingChild.findChildById(wrapperId).getFirstChild());
            }
            else {
                if(existingChild.isSelected()){
                    existingChild.deselect();
                    selectionChanged = true;
                }

                existingChild.dispose();
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
    public void removeChild(UIElement child) {
        super.removeChild(child);

        childWrapperMap.removeByKey(child);
    }

    public void removeChild(ItemType item){
        UIElement child = childWrapperMap.getByValue(item);
        if(child != null){
            child.dispose();
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
        ImageTextBox box = null;
        if(isGridMode()){
            box = new ImageTextBox(itemToString(item), Dim.mirror(), Dim.px(defaultItemHeight));
        }
        else{
            box = new ImageTextBox(
                    itemToString(item),
                    getContentAlignmentType() == Alignment.AlignmentType.VERTICAL ? Dim.fill() : Dim.px(defaultItemWidth),
                    getContentAlignmentType() == Alignment.AlignmentType.HORIZONTAL ? Dim.fill() : Dim.px(defaultItemHeight)
            );
        }
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

        UIItemBox holder;

        boolean contentHorizontal = getContentAlignmentType() == Alignment.AlignmentType.HORIZONTAL;
        if(contentHorizontal) holder = new VerticalBox(Dim.auto(), Dim.auto());
        else holder = new HorizontalBox(Dim.auto(), Dim.auto());

        holder.setControllerSelectable(
                canReorder() || isToggleOverlayEnabled() || isExternalToggling() || canDelete()
        );

        {
            if(canReorder()){
                Button moveUpButton = new Button(
                        contentHorizontal ? Dim.fill() : Dim.mirror(),
                        contentHorizontal ? Dim.mirror() : Dim.fill());
                moveUpButton.setTexture(contentHorizontal ? UICommonResources.arrow_left : UICommonResources.arrow_up);
                moveUpButton.postLeftClickEvent.subscribe(moveUpButton, () -> moveItemUp(item));
                moveUpButton.disableControllerSelectionAndSayTheSpireIntegration();
                holder.onLeftInteractionEvent.subscribe(moveUpButton, (byProxy) -> moveUpButton.onConfirmInteraction(true));
                holder.postSelectionStateChangedEvent.subscribe(moveUpButton, (selected) -> {
                    if(selected) moveUpButton.proxyHover();
                    else moveUpButton.proxyUnhover();
                });
                holder.addChild(moveUpButton);

                Button moveDownButton = new Button(
                        contentHorizontal ? Dim.fill() : Dim.mirror(),
                        contentHorizontal ? Dim.mirror() : Dim.fill());
                moveDownButton.setTexture(contentHorizontal ? UICommonResources.arrow_right : UICommonResources.arrow_down);
                moveDownButton.postLeftClickEvent.subscribe(moveDownButton, () -> moveItemDown(item));
                moveDownButton.disableControllerSelectionAndSayTheSpireIntegration();
                holder.onRightInteractionEvent.subscribe(moveDownButton, (byProxy) -> moveDownButton.onConfirmInteraction(true));
                holder.postSelectionStateChangedEvent.subscribe(moveDownButton, (selected) -> {
                    if(selected) moveDownButton.proxyHover();
                    else moveDownButton.proxyUnhover();
                });
                holder.addChild(moveDownButton);
            }

            if(isExternalToggling()){
                Toggle toggle;
                if(selectionMode == ESelectionMode.MULTIPLE){
                    Checkbox selectionButton = new Checkbox(
                            contentHorizontal ? Dim.fill() : Dim.mirror(),
                            contentHorizontal ? Dim.mirror() : Dim.fill()){
                        @Override
                        public void toggle(boolean byProxy) {
                            if(isToggled() || trySelectItem(item)){
                                super.toggle(byProxy);
                                onItemSelectionChanged();
                            }
                        }
                    };
                    toggle = selectionButton;
                }
                else if(selectionMode == ESelectionMode.SINGLE){
                    RadioButton selectionButton = new RadioButton(
                            "group1",
                            contentHorizontal ? Dim.fill() : Dim.mirror(),
                            contentHorizontal ? Dim.mirror() : Dim.fill()){
                        @Override
                        public void toggle(boolean byProxy) {
                            if(trySelectItem(item)){
                                super.toggle(byProxy);
                                onItemSelectionChanged();
                            }
                        }
                    };
                    toggle = selectionButton;
                }
                else{
                    toggle = null;
                }
                toggle.disableControllerSelectionAndSayTheSpireIntegration();
                holder.onConfirmInteractionEvent.subscribe(holder, (byProxy) -> toggle.onConfirmInteraction(true));
                holder.postSelectionStateChangedEvent.subscribe(toggle, (selected) -> {
                    if(selected) toggle.proxyHover();
                    else toggle.proxyUnhover();
                });
                holder.addChild(toggle);
            }

            UIElement parent = new UIElement(Dim.auto(), Dim.auto());
            parent.setID(wrapperId);
            {
                parent.addChild(itemUI);

                if(isToggleOverlayEnabled()){
                    Toggle overlay = new Toggle(Tex.stat(UICommonResources.white_pixel), itemUI.getWidthRaw(), itemUI.getHeightRaw()){
                        @Override
                        public void toggle(boolean byProxy) {
                            if((isToggled() && getSelectionMode() == ESelectionMode.MULTIPLE) || trySelectItem(item)){
                                super.toggle(byProxy);
                                onItemSelectionChanged();

                                if(selectionMode == ESelectionMode.SINGLE_NOPERSIST && isToggled()){
                                    setToggled(false);
                                }
                            }
                        }
                    };
                    overlay.setID(wrapOverlayID);
                    overlay.setRenderColor(new Color(0, 0, 0, 0f));
                    overlay.addComponent(new UIOverlayElementComponent());
                    overlay.disableControllerSelectionAndSayTheSpireIntegration();
                    overlay.setOnTriggerLine(Str.lambda(() -> {
                        String line = "";
                        if(!overlay.isToggled()) line = "De";

                        line += "selected ";
                        if(itemUI instanceof ITextProvider) line += ((ITextProvider) itemUI).getText();
                        else line += itemUI.toString();

                        return line;
                    }));
                    holder.onConfirmInteractionEvent.subscribe(holder, (byProxy) -> overlay.onConfirmInteraction(true));
                    holder.postSelectionStateChangedEvent.subscribe(overlay, (selected) -> {
                        if(selected) overlay.proxyHover();
                        else overlay.proxyUnhover();
                    });
                    parent.addChild(overlay);
                }
                else{
                    if(!isExternalToggling()){
                        holder.onLeftInteractionEvent.subscribe(holder, (byProxy) -> itemUI.onConfirmInteraction(true));
                    }

                    holder.postSelectionStateChangedEvent.subscribe(itemUI, (selected) -> {
                        if(selected) itemUI.proxyHover();
                        else itemUI.proxyUnhover();
                    });
                }
            }
            holder.addChild(parent);
            if(canDelete()){
                Button deleteButton = new Button(
                        contentHorizontal ? Dim.fill() : Dim.mirror(),
                        contentHorizontal ? Dim.mirror() : Dim.fill());
                deleteButton.setTexture(UICommonResources.deleteButton);
                deleteButton.postLeftClickEvent.subscribe(deleteButton, () -> deleteItem(item));
                deleteButton.disableControllerSelectionAndSayTheSpireIntegration();
                holder.onCancelInteractionEvent.subscribe(holder, (byProxy) -> deleteButton.onConfirmInteraction(true));
                holder.postSelectionStateChangedEvent.subscribe(deleteButton, (selected) -> {
                    if(selected) deleteButton.proxyHover();
                    else deleteButton.proxyUnhover();
                });
                holder.addChild(deleteButton);
            }
        }

        postMakeUIForItem(item, itemUI);

        if(holder.isControllerSelectable()){
            Supplier<String> hoverSupplier = () -> {
                String line = "";

                if(itemUI instanceof ITextProvider) line += ((ITextProvider) itemUI).getText();
                else line += itemUI.toString();

                line += " entry " + (filteredChildren.indexOf(holder) + 1) + " of " + children.size();

                return line;
            };
            holder.setOnHoverLine(Str.lambda(hoverSupplier));
        }

        return holder;
    }

    protected void postMakeUIForItem(ItemType item, UIElement itemUI){
    }

    //endregion

    //region Item Selection

    private boolean trySelectItem(ItemType selectedItem){
        if(selectionMode == ESelectionMode.NONE){
            return false;
        }

        if(selectionMode == ESelectionMode.MULTIPLE && getSelectedItems().size() + 1 > getSelectionCountLimit()){
            return false;
        }

        if(getToggleForHolder(childWrapperMap.getByValue(selectedItem)).isToggled() && !selectionMode.equals(ESelectionMode.MULTIPLE)){
            return false;
        }

        if(selectionMode == ESelectionMode.SINGLE || selectionMode == ESelectionMode.SINGLE_NOPERSIST){
            for(UIElement child : children){
                Toggle wrapOverlay = getToggleForHolder(child);
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
        onItemSelectionChangedEvent.invoke(getSelectedItems());
    } //TODO expose

    public ArrayList<ItemType> getSelectedItems(){
        ArrayList<ItemType> selectedItems = new ArrayList<>();
        for(UIElement child : children){
            Toggle wrapOverlay = getToggleForHolder(child);
            if(wrapOverlay == null){
                continue;
            }

            if(wrapOverlay.isToggled()){
                selectedItems.add(childWrapperMap.getByKey(child));
            }
        }

        return selectedItems;
    }

    public void setSelectedItems(ArrayList<ItemType> items){
        boolean changed = false;

        for(UIElement child : children){
            Toggle wrapOverlay = getToggleForHolder(child);
            if(wrapOverlay == null){
                continue;
            }

            ItemType item = childWrapperMap.getByKey(child);
            if(items.contains(item)){
                if(!wrapOverlay.isToggled()){
                    wrapOverlay.setToggled(true);
                    changed = true;
                }
            }
            else {
                if(wrapOverlay.isToggled()){
                    wrapOverlay.setToggled(false);
                    changed = true;
                }
            }
        }

        if(changed){
            onItemSelectionChanged();
        }
    }

    private Toggle getToggleForHolder(UIElement holder){
        Toggle wrapOverlay = null;
        if(isToggleOverlayEnabled()){
            wrapOverlay = holder.findChildById(wrapperId).findChildById(wrapOverlayID);
        }
        else if(isExternalToggling()){
            if(selectionMode == ESelectionMode.SINGLE) wrapOverlay = holder.getChildren(RadioButton.class).get(0);
            else if(selectionMode == ESelectionMode.MULTIPLE) wrapOverlay = holder.getChildren(Checkbox.class).get(0);
        }

        return wrapOverlay;
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
        else if(selectionCountLimit < 0) return Integer.MAX_VALUE; // -1 means no limit
        else return selectionCountLimit;
    }

    public void deselectAllItems(){
        boolean changed = false;

        for(UIElement child : children){
            Toggle wrapOverlay = getToggleForHolder(child);
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

    //region Toggle Overlay

    public void setExternalToggling(boolean externalToggling){
        this.externalToggling = externalToggling;
    }
    public boolean isExternalToggling(){
        return externalToggling && (selectionMode == ESelectionMode.SINGLE || selectionMode == ESelectionMode.MULTIPLE);
    }

    public void disableToggleOverlay(){
        shouldOverlayToggle = false;
    }
    public boolean isToggleOverlayEnabled(){
        return shouldOverlayToggle && !isExternalToggling() && selectionMode != ESelectionMode.NONE;
    }

    //endregion

    //region Reordering

    public DataItemBox<ItemType> setCanReorder(boolean canReorder){
        this.canReorder = canReorder;
        return this;
    }
    public boolean canReorder(){
        return canReorder;
    }

    protected void moveItemDown(ItemType item){
        UIElement itemElement = childWrapperMap.getByValue(item);
        int itemIndex = children.indexOf(itemElement);
        if(itemIndex == -1 || itemIndex == children.size() - 1){
            return;
        }

        int itemFilteredIndex = filteredChildren.indexOf(itemElement);
        int swapFilteredIndex = itemFilteredIndex + (invertedItemOrder ? -1 : 1);
        if(swapFilteredIndex < 0 || swapFilteredIndex >= filteredChildren.size()){
            return;
        }

        UIElement swapElement = filteredChildren.get(swapFilteredIndex);
        int swapIndex = children.indexOf(swapElement);

        swapChildren(itemIndex, swapIndex);
    }
    protected void moveItemUp(ItemType item){
        UIElement itemElement = childWrapperMap.getByValue(item);
        int itemIndex = children.indexOf(itemElement);
        if(itemIndex == -1 || itemIndex == children.size() - 1){
            return;
        }

        int itemFilteredIndex = filteredChildren.indexOf(itemElement);
        int swapFilteredIndex = itemFilteredIndex + (invertedItemOrder ? 1 : -1);
        if(swapFilteredIndex < 0 || swapFilteredIndex >= filteredChildren.size()){
            return;
        }

        UIElement swapElement = filteredChildren.get(swapFilteredIndex);
        int swapIndex = children.indexOf(swapElement);

        swapChildren(itemIndex, swapIndex);
    }

    @Override
    public void swapChildren(int index1, int index2) {
        super.swapChildren(index1, index2);

        UIElement child1 = children.get(index1);
        UIElement child2 = children.get(index2);

        ItemType item1 = childWrapperMap.getByKey(child1);
        ItemType item2 = childWrapperMap.getByKey(child2);

        refilterItems();
        onItemsSwappedEvent.invoke(item2, item1);
    }

    //endregion

    //region Erasing

    public void setCanDelete(boolean canDelete){
        this.canDelete = canDelete;
    }
    public boolean canDelete(){
        return canDelete;
    }

    public void deleteItem(ItemType item){
        removeChild(item);

        refilterItems();
        onItemDeletedEvent.invoke(item);
    }

    //endregion Erasing

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

        ArrayList<ItemType> filteredItems = new ArrayList<>();
        for(UIElement child : filteredChildren){
            filteredItems.add(childWrapperMap.getByKey(child));
        }
        sortToModifyEvent.invoke(filteredItems);

        filteredChildren.clear();
        for(ItemType item : filteredItems){
            UIElement child = childWrapperMap.getByValue(item);
            if(child != null){
                filteredChildren.add(child);
            }
        }

        if(invertedItemOrder){
            Collections.reverse(filteredChildren);
        }
    }

    protected boolean filterCheck(String filterText, UIElement item, ItemType originalItem){
        return originalItem.toString().toLowerCase(Locale.ROOT).contains(filterText.toLowerCase(Locale.ROOT)) && !item.hasComponent(UITransientElementComponent.class) && !filterToModifyEvent.invokeWhile(originalItem, (condition) -> condition).contains(false);
    }

    //endregion

    //region Modality

    @Override
    public boolean isModal() {
        return super.isModal() || getSelectionMode() != ESelectionMode.NONE;
    }


    //endregion Modality

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
