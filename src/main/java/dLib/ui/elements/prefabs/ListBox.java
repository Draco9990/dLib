package dLib.ui.elements.prefabs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import dLib.ui.Alignment;
import dLib.ui.data.prefabs.ListBoxData;
import dLib.ui.elements.CompositeUIElement;
import dLib.ui.elements.ListCompositeUIElement;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Hoverable;
import dLib.ui.themes.UITheme;
import dLib.ui.themes.UIThemeManager;
import dLib.ui.util.ESelectionMode;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.Arrays;

public class ListBox<ItemType> extends ListCompositeUIElement {
    /** Variables */
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

    /** Constructors */
    public ListBox(int xPos, int yPos, int width, int height){
        super(xPos, yPos, width, height);

        reinitializeElements();
    }

    public ListBox(ListBoxData<ItemType> data){
        super(data);

        this.title = data.titleBoxText;
        this.titleBoxHeight = data.titleBoxHeight;

        this.itemSpacing = data.itemSpacing;
        this.invertedItemOrder = data.invertedItemOrder;

        this.scrollbarWidth = data.scrollbarWidth;

        reinitializeElements();
    }

    private void reinitializeElements(){
        updateTitleBox();
        updateItemBox();
        updateScrollBar();
    }

    /** Update and Render */
    @Override
    public void update() {
        if(trackScrollWheelScroll){
            int scrollDelta = (int)(Math.signum((float)Mouse.getDWheel()));
            scrollbar.getSlider().setPositionY(scrollbar.getSlider().getPositionY() + scrollDelta * 10);
        }

        if(titleBox != null) {
            titleBox.update();
        }

        itemBoxBackground.update();

        boolean renderScrollbar = calculatePageCount() > 1;
        int currentYPos = y + itemBoxBackground.getHeight();
        for(CompositeUIElement item : getActiveItems()){
            item.setPositionX(itemBoxBackground.getPositionX());
            item.setPositionY(currentYPos - item.getBoundingHeight());
            item.setWidth(itemBoxBackground.getWidth() + (renderScrollbar ? -scrollbar.getWidth() : 0));

            item.update();

            currentYPos -= item.getBoundingHeight();
            currentYPos -= itemSpacing;
        }

        if(renderScrollbar) scrollbar.update();
    }

    @Override
    public void render(SpriteBatch sb) {
        if(titleBox != null) titleBox.render(sb);
        itemBoxBackground.render(sb);

        for(CompositeUIElement item : getActiveItems()){
            item.render(sb);
        }

        if(calculatePageCount() > 1) scrollbar.render(sb);
    }

    public ArrayList<CompositeUIElement> getActiveItems(){
        ArrayList<CompositeUIElement> activeItems = new ArrayList<>();

        int currentPageHeight = 0;
        if(!invertedItemOrder){
            for(int i = scrollbar.getCurrentPage() - 1; i < items.size(); i++){
                CompositeUIElement item = items.get(i).renderForItem;
                if(currentPageHeight + item.getBoundingHeight() + itemSpacing > itemBoxBackground.getHeight()){
                    break;
                }

                currentPageHeight += item.getBoundingHeight() + itemSpacing;
                activeItems.add(item);
            }
        }
        else{
            for(int i = items.size() - (scrollbar.getCurrentPage() - 1) - 1; i >= 0; i--){
                CompositeUIElement item = items.get(i).renderForItem;
                if(currentPageHeight + item.getBoundingHeight() + itemSpacing > itemBoxBackground.getHeight()){
                    break;
                }

                currentPageHeight += item.getBoundingHeight() + itemSpacing;
                activeItems.add(item);
            }
        }

        return activeItems;
    }

    /** Items */
    public ListBox<ItemType> addItem(ItemType item){
        CompositeUIElement compositeItem = makeCompositeForItem(item);
        items.add(new ListBoxItem(item, compositeItem));
        addElement(compositeItem);

        return this;
    }
    public ListBox<ItemType> setItems(ArrayList<ItemType> items){
        this.items.clear();
        for(ItemType item : items){
            addItem(item);
        }

        return this;
    }
    public void clearItems(){
        items.clear();
        scrollbar.setFirstPage();
        clearElements();
    }

    public class ListBoxItem{
        /** Variables */
        public ItemType item;
        public CompositeUIElement renderForItem;
        public boolean selected;

        /** Constructors */
        public ListBoxItem(ItemType item, CompositeUIElement renderElement){
            this.item = item;
            this.renderForItem = renderElement;
        }
    }

    /** Item Render */
    public UIElement makeUIForItem(ItemType item){
        TextBox box = new TextBox(item.toString(), x, y, itemBoxBackground.getWidth(), 30);
        box.setImage(UIThemeManager.getDefaultTheme().button_large);
        box.setMarginPercX(0.025f).setMarginPercY(0.05f);
        box.setAlignment(Alignment.HorizontalAlignment.LEFT, Alignment.VerticalAlignment.CENTER);
        return box;
    } //TODO expose

    public CompositeUIElement makeCompositeForItem(ItemType item){
        UIElement itemUI = makeUIForItem(item);

        CompositeUIElement composite = new CompositeUIElement(itemUI.getPositionX(), itemUI.getPositionY(), itemUI.getWidth(), itemUI.getHeight());
        composite.background.add(itemUI);

        Color transparent = Color.WHITE.cpy();
        transparent.a = 0f;
        composite.middle = new Button(itemUI.getPositionX(), itemUI.getPositionY(), itemUI.getWidth(), itemUI.getHeight()){
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

        Color hoverColor = ((Button) composite.middle).getHoveredColor().cpy();
        hoverColor.a = 0.4f;
        ((Button) composite.middle).setHoveredColor(hoverColor);

        postMakeCompositeForItem(item, composite);

        return composite;
    } //TODO expose
    public void postMakeCompositeForItem(ItemType item, CompositeUIElement compositeUIElement){ } //TODO expose

    /** Item Selection */
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

    /** LAYOUT */

    /** Title & TitleBox */
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
        this.title = null;
        this.titleBox = null;

        reinitializeElements();
    }

    public ListBox<ItemType> setTitleHeight(int titleHeight){
        if(titleHeight <= 0) return this;

        this.titleBoxHeight = titleHeight;
        return this;
    }

    private void updateTitleBox(){
        if(title != null && !title.isEmpty()){
            if(titleBox == null){
                buildTitleBox();
            }
            titleBox.setText(title);
            titleBox.setPosition(x, y + height - titleBoxHeight);
            titleBox.setDimensions(width, titleBoxHeight);

            if(!foreground.contains(titleBox)){
                foreground.add(titleBox);
            }
        }
        else if(titleBox != null){
            foreground.remove(titleBox);
            titleBox = null;
        }
    }
    private void buildTitleBox(){
        titleBox = new TextBox(title, x, y + height - titleBoxHeight, width, titleBoxHeight);
        titleBox.setImage(UITheme.whitePixel);
        titleBox.setRenderColor(Color.valueOf("#151515FF"));
        titleBox.setTextRenderColor(Color.WHITE);
        titleBox.setHorizontalAlignment(Alignment.HorizontalAlignment.LEFT);
        titleBox.setMarginPercX(0.005f);
        foreground.add(titleBox);
    }

    /** Item Box */
    private void updateItemBox(){
        int updateHeight = height;
        if(titleBox != null) updateHeight -= titleBox.getHeight();

        // We can span entire width since elements will get shrunk if Scrollbox is present

        if(itemBoxBackground == null){
            buildItemBox(0, 0, width, updateHeight);
        }

        itemBoxBackground.setLocalPosition(0, 0);
        itemBoxBackground.setDimensions(width, updateHeight);

        this.middle = itemBoxBackground;
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
    }

    /** Scroll Box */
    public ListBox<ItemType> setScrollbarWidth(int width){
        this.scrollbarWidth = width;
        return this;
    }

    private void updateScrollBar(){
        int updatePosX = x + width - scrollbarWidth;

        int updateHeight = height;
        if(titleBox != null) updateHeight -= titleBox.getHeight();

        if(scrollbar == null){
            buildScrollBar(updatePosX, y, scrollbarWidth, updateHeight);
        }
        scrollbar.setPosition(updatePosX, y);
        scrollbar.setDimensions(scrollbarWidth, updateHeight);

        if(!foreground.contains(scrollbar)) foreground.add(scrollbar);
    }
    private void buildScrollBar(int x, int y, int width, int height){
        scrollbar = new Scrollbox(x, y, width, height) {
            @Override
            public int getPageCount() {
                return calculatePageCount();
            }
        };
    }

    /** Item Spacing */
    public ListBox<ItemType> setItemSpacing(int spacing){
        this.itemSpacing = spacing;
        return this;
    }
    public int getItemSpacing(){
        return itemSpacing;
    }

    /** Item Order */
    public ListBox<ItemType> setInvertedItemOrder(boolean invertedItemOrder){
        this.invertedItemOrder = invertedItemOrder;
        return this;
    }

    /** Background */
    public Hoverable getBackground(){
        return itemBoxBackground;
    }

    /** Scrollbar */
    public int calculatePageCount(){
        int totalItemHeight = 0;
        if(!invertedItemOrder){
            for(int i = 0; i < items.size(); i++){
                totalItemHeight += items.get(i).renderForItem.getBoundingHeight() + itemSpacing;
                if(totalItemHeight > itemBoxBackground.getHeight()){
                    int pageCount = items.size() - i;
                    return pageCount + 1;
                }
            }
        }
        else{
            for(int i = items.size() - 1; i >= 0; i--){
                totalItemHeight += items.get(i).renderForItem.getBoundingHeight() + itemSpacing;
                if(totalItemHeight > itemBoxBackground.getHeight()){
                    return i + 2;
                }
            }
        }
        return 1;
    }
}
