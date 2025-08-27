package dLib.betterscreens.ui.elements.items;

import dLib.properties.objects.*;
import dLib.properties.ui.elements.OnValueChangedStringValueEditor;
import dLib.ui.Alignment;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.*;
import dLib.ui.elements.items.buttons.CancelButtonSmall;
import dLib.ui.elements.items.buttons.ConfirmButtonSmall;
import dLib.ui.elements.items.buttons.Toggle;
import dLib.ui.elements.items.itembox.VerticalBox;
import dLib.ui.elements.items.itembox.VerticalDataBox;
import dLib.ui.elements.items.scroll.Scrollbox;
import dLib.ui.elements.items.text.ImageTextBox;
import dLib.ui.elements.items.text.TextBox;
import dLib.ui.elements.items.text.TokenizedDescriptionBox;
import dLib.ui.resources.UICommonResources;
import dLib.ui.util.ESelectionMode;
import dLib.util.bindings.string.interfaces.ITextProvider;
import dLib.util.bindings.texture.AbstractTextureBinding;
import dLib.util.bindings.texture.Tex;
import dLib.util.events.localevents.ConsumerEvent;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.padding.Padd;
import dLib.util.ui.position.Pos;

import java.util.ArrayList;

public abstract class GameItemSelectPopup<GameItemType> extends UIElement {

    //region Variables

    public ItemListWindow<GameItemType> itemListWindow;

    public ItemFiltersSidebar itemFiltersSidebar;
    public ItemDetailsSidebar<GameItemType> itemDetailsSidebar;

    private boolean ignoreUnlockStatus = true;

    public ConsumerEvent<ArrayList<GameItemType>> onItemsSelected = new ConsumerEvent<>();

    private int seletionMinimum = -1;

    //endregion Variables

    //region Constructors

    public GameItemSelectPopup(String itemTypeName) {
        super();

        addChild(new DarkenLayer());

        itemListWindow = new ItemListWindow<>();
        itemListWindow.itemBox.setSayTheSpireElementName(itemTypeName);
        addChild(itemListWindow);

        itemFiltersSidebar = new ItemFiltersSidebar(getItemRaritiesForFilter());
        addChild(itemFiltersSidebar);
        itemDetailsSidebar = new ItemDetailsSidebar<>(this);
        itemDetailsSidebar.setVisibilityAndEnabledInstantly(false, false);
        addChild(itemDetailsSidebar);
    }

    //endregion Constructors

    //region Methods

    //region Item Descriptors

    public abstract AbstractTextureBinding getItemTexture(GameItemType item);
    public abstract String getItemName(GameItemType item);
    public Enum<?> getItemRarity(GameItemType item) { return null; }
    public abstract String getItemDescription(GameItemType item);

    public ArrayList<? extends Enum<?>> getItemRaritiesForFilter() { return new ArrayList<>(); }

    public boolean hasItemFlavorText() { return true; }
    public String getItemFlavorText(GameItemType item) { return ""; }

    //endregion Item Descriptors

    //region Setting Selected Items Externally

    public void setSelectedItems(ArrayList<GameItemType> selectedItems) {
        itemListWindow.itemBox.setSelectedItems(selectedItems);
    }

    //endregion Setting Selected Items Externally

    //region Selection Limit

    public void setSelectionLimit(int selectionLimit) {
        itemListWindow.itemBox.setSelectionCountLimit(selectionLimit);
    }
    public void disableSelection(){
        itemListWindow.itemBox.setSelectionCountLimit(0);
    }

    //endregion Selection Limit

    //region Unlock Status

    public void setIgnoreUnlockStatus(boolean ignoreUnlockStatus) {
        this.ignoreUnlockStatus = ignoreUnlockStatus;
    }

    //endregion Unlock Status

    //region Items

    public void setItemSelection(ArrayList<GameItemType> itemSelection) {
        itemListWindow.itemBox.setChildren(itemSelection);
    }

    //endregion Items

    //region Selection Minimum

    public void setSelectionMinimum(int selectionMinimum) {
        this.seletionMinimum = selectionMinimum;
        itemListWindow.refreshButtonAvailability();
    }

    //endregion Selection Minimum

    //endregion Methods

    //region Child UI

    public static class ItemListWindow<GameItemType> extends Renderable {
        public VerticalDataBox<GameItemType> itemBox;

        public CancelButtonSmall cancelButton;
        public ConfirmButtonSmall confirmButton;

        public ItemListWindow() {
            super(Tex.stat(UICommonResources.bg02_background), Pos.px(69), Pos.px(1080-1000), Dim.px(1268), Dim.px(939));

            setHueShiftAmount(30);

            Renderable inner = new Renderable(Tex.stat(UICommonResources.bg02_inner), Pos.px(0), Pos.px(70), Dim.fill(), Dim.fill());
            inner.setHueShiftAmount(30);
            inner.setPadding(Padd.px(25));
            {
                Scrollbox scrollbox = new Scrollbox(Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill());
                scrollbox.setIsHorizontal(false);
                {
                    itemBox = new VerticalDataBox<GameItemType>(Pos.px(14), Pos.px(0), Dim.fill(), Dim.fill()){
                        @Override
                        public UIElement makeUIForItem(GameItemType item) {
                            return new GridItem<>(getParentOfType(GameItemSelectPopup.class), item);
                        }

                        @Override
                        protected void postMakeUIForItem(GameItemType item, UIElement itemUI) {
                            super.postMakeUIForItem(item, itemUI);

                            Toggle overlay = itemUI.getParent().findChildById("wrap_overlay");
                            overlay.postHoveredEvent.subscribe(this, () -> {
                                getParentOfType(GameItemSelectPopup.class);
                                getParentOfType(GameItemSelectPopup.class).itemFiltersSidebar.setVisibilityAndEnabledInstantly(false, false);

                                getParentOfType(GameItemSelectPopup.class);
                                getParentOfType(GameItemSelectPopup.class).itemDetailsSidebar.setVisibilityAndEnabledInstantly(true, true);
                                getParentOfType(GameItemSelectPopup.class).itemDetailsSidebar.setDetailsItem(item);
                            });
                            overlay.postUnhoveredEvent.subscribe(this, () -> {
                                getParentOfType(GameItemSelectPopup.class);
                                getParentOfType(GameItemSelectPopup.class).itemDetailsSidebar.setVisibilityAndEnabledInstantly(false, false);

                                getParentOfType(GameItemSelectPopup.class);
                                getParentOfType(GameItemSelectPopup.class).itemFiltersSidebar.setVisibilityAndEnabledInstantly(true, true);
                            });
                        }

                        @Override
                        protected boolean filterCheck(String filterText, UIElement item, GameItemType originalItem) {
                            GameItemSelectPopup parent = getParentOfType(GameItemSelectPopup.class);
                            return super.filterCheck(filterText, item, originalItem) &&
                                    (parent.itemFiltersSidebar.displayRelicTiers == null || parent.itemFiltersSidebar.displayRelicTiers.getValues().isEmpty() || parent.itemFiltersSidebar.displayRelicTiers.getValues().contains(parent.getItemRarity(originalItem)));
                        }
                    };
                    itemBox.setGridMode(true);
                    itemBox.setTexture(Tex.stat(UICommonResources.transparent_pixel));
                    itemBox.setItemSpacing(10);
                    itemBox.setSelectionMode(ESelectionMode.MULTIPLE);
                    itemBox.setSelectionCountLimit(Integer.MAX_VALUE);
                    itemBox.onItemSelectionChangedEvent.subscribe(itemBox, gameItemTypes -> ItemListWindow.this.refreshButtonAvailability());
                    scrollbox.addChild(itemBox);
                }
                inner.addChild(scrollbox);
            }
            addChild(inner);

            cancelButton = new CancelButtonSmall(Pos.px(-21), Pos.px(6));
            cancelButton.postLeftClickEvent.subscribe(cancelButton, () -> getTopParent().dispose());
            addChild(cancelButton);

            confirmButton = new ConfirmButtonSmall(Pos.px(20), Pos.px(5));
            confirmButton.setHorizontalAlignment(Alignment.HorizontalAlignment.RIGHT);
            confirmButton.postLeftClickEvent.subscribe(confirmButton, () -> {
                ArrayList<GameItemType> selectedItems = itemBox.getSelectedItems();
                if (!selectedItems.isEmpty()) {
                    getParentOfType(GameItemSelectPopup.class).onItemsSelected.invoke(selectedItems);
                }
                getTopParent().dispose();
            });
            addChild(confirmButton);
        }

        public void refreshButtonAvailability(){
            GameItemSelectPopup parent = getParentOfType(GameItemSelectPopup.class);
            boolean hasEnoughSelected = false;

            if(parent.seletionMinimum == -1){
                hasEnoughSelected = true;
            }
            else{
                hasEnoughSelected = itemBox.getSelectedItems().size() >= parent.seletionMinimum;
            }

            if(hasEnoughSelected){
                confirmButton.setVisibilityAndEnabled(true, true);
            }
            else{
                confirmButton.setVisibilityAndEnabled(false, false);
            }
        }

        private static class GridItem<GameItemType> extends VerticalBox implements ITextProvider {
            GameItemType item;

            public GridItem(GameItemSelectPopup<GameItemType> inParent, GameItemType item) {
                super(Dim.px(180), Dim.px(230));

                this.item = item;

                setTexture(Tex.stat(UICommonResources.white_pixel));

                Image gameItemImage = new Image(inParent.getItemTexture(item), Dim.fill(), Dim.px(170));
                gameItemImage.setPreserveAspectRatio(true);
                addChild(gameItemImage);

                ImageTextBox itemNameBox = new ImageTextBox(inParent.getItemName(item), Dim.fill(), Dim.px(60));
                itemNameBox.textBox.setWrap(true);
                addChild(itemNameBox);
            }

            @Override
            public String getText() {
                return getParentOfType(GameItemSelectPopup.class).getItemName(item);
            }
        }
    }

    public static class ItemDetailsSidebar<GameItemType> extends Renderable{
        ImageTextBox itemNameBox;
        Image itemImage;
        ImageTextBox itemRarityBox;
        TokenizedDescriptionBox itemDescription;
        ImageTextBox itemFlavor;

        public ItemDetailsSidebar(GameItemSelectPopup parent) {
            super(Tex.stat(UICommonResources.bg03), Pos.px(1345), Pos.px(1080-1045), Dim.px(534), Dim.px(995));

            setHueShiftAmount(140);

            VerticalBox vbox = new VerticalBox(Pos.px(51), Pos.px(78), Dim.px(426), Dim.px(868));
            {
                itemNameBox = new ImageTextBox("Filters", Dim.fill(), Dim.px(100));
                itemNameBox.textBox.setWrap(true);
                itemNameBox.textBox.setFontSize(28);
                vbox.addChild(itemNameBox);

                itemRarityBox = new ImageTextBox("", Dim.fill(), Dim.px(30));
                vbox.addChild(itemRarityBox);

                itemImage = new Image(Tex.stat(UICommonResources.white_pixel), Dim.fill(), Dim.px(170));
                itemImage.setPreserveAspectRatio(true);
                vbox.addChild(itemImage);

                itemDescription = new TokenizedDescriptionBox(Dim.fill(), Dim.fill());
                vbox.addChild(itemDescription);

                itemFlavor = new ImageTextBox("", Dim.fill(), Dim.px(150));
                itemFlavor.textBox.setWrap(true);
                if(parent.hasItemFlavorText()) vbox.addChild(itemFlavor);
            }
            addChild(vbox);
        }

        public void setDetailsItem(GameItemType item) {
            itemNameBox.textBox.setText(getParentOfType(GameItemSelectPopup.class).getItemName(item));
            itemImage.setTexture(getParentOfType(GameItemSelectPopup.class).getItemTexture(item));
            if(getParentOfType(GameItemSelectPopup.class).getItemRarity(item) != null) itemRarityBox.textBox.setText(getParentOfType(GameItemSelectPopup.class).getItemRarity(item).name());

            String itemDesc = getParentOfType(GameItemSelectPopup.class).getItemDescription(item);
            itemDescription.setFromText(itemDesc);

            if(getParentOfType(GameItemSelectPopup.class).hasItemFlavorText()) itemFlavor.textBox.setText("\"" + getParentOfType(GameItemSelectPopup.class).getItemFlavorText(item) + "\"");
        }
    }

    public static class ItemFiltersSidebar extends Renderable{
        StringProperty searchText = new StringProperty("");

        PropertyArray<? extends Enum<?>> displayRelicTiers;

        public ItemFiltersSidebar(ArrayList<? extends Enum<?>> selectableRarities) {
            super(Tex.stat(UICommonResources.bg03), Pos.px(1345), Pos.px(1080-1045), Dim.px(534), Dim.px(995));

            setHueShiftAmount(140);

            if(!selectableRarities.isEmpty()){
                displayRelicTiers = new PropertyArray<>(new EnumProperty<>(selectableRarities.get(0)))
                        .setName("Of Rarity");
            }

            VerticalBox filtersBox = new VerticalBox(Pos.px(51), Pos.px(78), Dim.px(426), Dim.px(868));
            filtersBox.setItemSpacing(5);
            {
                TextBox filtersTitle = new TextBox("Filters", Dim.fill(), Dim.px(70));
                filtersTitle.setFontSize(28);
                filtersBox.addChild(filtersTitle);

                Spacer spacer = new Spacer(Dim.fill(), Dim.px(30));
                filtersBox.addChild(spacer);

                TextBox searchTextTitle = new TextBox("Search", Dim.fill(), Dim.px(30));
                searchTextTitle.setHorizontalContentAlignment(Alignment.HorizontalAlignment.LEFT);
                filtersBox.addChild(searchTextTitle);
                OnValueChangedStringValueEditor searchTextEditor = new OnValueChangedStringValueEditor(searchText);
                searchTextEditor.inputfield.setSayTheSpireElementName("Search");
                filtersBox.addChild(searchTextEditor);

                if(!selectableRarities.isEmpty()){
                    filtersBox.addChild(displayRelicTiers.makeEditorFor(true));
                }
            }
            addChild(filtersBox);

            searchText.onValueChangedEvent.subscribe(this, (s, s2) -> getParentOfType(GameItemSelectPopup.class).itemListWindow.itemBox.setFilterText(s2));
            if(displayRelicTiers != null) displayRelicTiers.onSingleValueChangedEvent.subscribe(this, (oldVal, newVal, indx) -> getParentOfType(GameItemSelectPopup.class).itemListWindow.itemBox.refilterItems());
        }
    }

    //endregion Child UI
}
