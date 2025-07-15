package dLib.betterscreens.ui.elements.items;

import dLib.ui.elements.UIElement;
import dLib.ui.elements.items.DarkenLayer;
import dLib.ui.elements.items.Image;
import dLib.ui.elements.items.Renderable;
import dLib.ui.elements.items.itembox.GridItemBox;
import dLib.ui.elements.items.itembox.VerticalBox;
import dLib.ui.elements.items.scroll.Scrollbox;
import dLib.ui.elements.items.text.ImageTextBox;
import dLib.ui.resources.UICommonResources;
import dLib.ui.util.ESelectionMode;
import dLib.util.bindings.texture.AbstractTextureBinding;
import dLib.util.bindings.texture.Tex;
import dLib.util.events.Event;
import dLib.util.ui.dimensions.Dim;
import dLib.util.ui.padding.Padd;
import dLib.util.ui.position.Pos;

import java.util.ArrayList;

public abstract class GameItemSelectPopup<GameItemType> extends UIElement {

    //region Variables

    ItemListWindow<GameItemType> itemListWindow;

    ItemFiltersSidebar itemFiltersSidebar;
    ItemDetailsSidebar<GameItemType> itemDetailsSidebar;

    private boolean ignoreUnlockStatus = true;

    public Event<ArrayList<GameItemType>> onItemsSelected = new Event<>();

    //endregion Variables

    //region Constructors

    public GameItemSelectPopup() {
        super();

        addChild(new DarkenLayer());

        itemListWindow = new ItemListWindow<>();
        addChild(itemListWindow);

        itemFiltersSidebar = new ItemFiltersSidebar();
        addChild(itemFiltersSidebar);
        itemDetailsSidebar = new ItemDetailsSidebar<>();
        addChild(itemDetailsSidebar);
    }

    //endregion Constructors

    //region Methods

    //region Item Descriptors

    public abstract AbstractTextureBinding getItemTexture(GameItemType item);
    public abstract String getItemName(GameItemType item);
    public abstract String getItemRarity(GameItemType item);
    public abstract String getItemFlavorText(GameItemType item);

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

    //endregion Methods

    //region Child UI

    private static class ItemListWindow<GameItemType> extends Renderable {
        GridItemBox<GameItemType> itemBox;

        public ItemListWindow() {
            super(Tex.stat(UICommonResources.bg02_background), Pos.px(69), Pos.px(1080-1000), Dim.px(1268), Dim.px(939));

            Renderable inner = new Renderable(Tex.stat(UICommonResources.bg02_inner), Dim.fill(), Dim.fill());
            inner.setPadding(Padd.px(25));
            {
                Scrollbox scrollbox = new Scrollbox(Pos.px(0), Pos.px(0), Dim.fill(), Dim.fill());
                scrollbox.setIsHorizontal(false);
                {
                    itemBox = new GridItemBox<GameItemType>(Pos.px(14), Pos.px(0), Dim.fill(), Dim.fill()){
                        @Override
                        public UIElement makeUIForItem(GameItemType item) {
                            return new GridItem<>(getParentOfType(GameItemSelectPopup.class), item);
                        }
                    };
                    itemBox.setTexture(Tex.stat(UICommonResources.transparent_pixel));
                    itemBox.setItemSpacing(10);
                    itemBox.setSelectionMode(ESelectionMode.MULTIPLE);
                    itemBox.setSelectionCountLimit(Integer.MAX_VALUE);
                    scrollbox.addChild(itemBox);
                }
                inner.addChild(scrollbox);
            }
            addChild(inner);
        }

        private static class GridItem<GameItemType> extends VerticalBox {
            public GridItem(GameItemSelectPopup<GameItemType> inParent, GameItemType item) {
                super(Dim.px(180), Dim.px(230));

                setTexture(Tex.stat(UICommonResources.white_pixel));

                Image gameItemImage = new Image(inParent.getItemTexture(item), Dim.fill(), Dim.px(170));
                addChild(gameItemImage);

                ImageTextBox itemNameBox = new ImageTextBox(inParent.getItemName(item), Dim.fill(), Dim.px(60));
                itemNameBox.textBox.setWrap(true);
                addChild(itemNameBox);
            }
        }
    }

    private static class ItemDetailsSidebar<GameItemType> extends Renderable{
        public ItemDetailsSidebar() {
            super(Tex.stat(UICommonResources.bg03), Pos.px(1345), Pos.px(1080-1045), Dim.px(534), Dim.px(995));
        }

        public void setDetailsItem(GameItemType item) {

        }
    }

    private static class ItemFiltersSidebar extends Renderable{
        public ItemFiltersSidebar() {
            super(Tex.stat(UICommonResources.bg03), Pos.px(1345), Pos.px(1080-1045), Dim.px(534), Dim.px(995));
        }
    }

    //endregion Child UI
}
