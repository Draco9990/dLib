package dLib.ui.elements.prefabs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import dLib.ui.Alignment;
import dLib.ui.data.prefabs.ListBoxData;
import dLib.ui.elements.CompositeUIElement;
import dLib.ui.elements.ListCompositeUIElement;
import dLib.ui.elements.implementations.Hoverable;
import dLib.ui.themes.UITheme;
import dLib.ui.themes.UIThemeManager;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;

public class ListBox<ItemType> extends ListCompositeUIElement {
    /** Variables */
    private TextBox titleBox;
    private String title;

    private Hoverable itemBoxBackground;
    private ArrayList<ListBoxItem> items = new ArrayList<>();

    private Scrollbox scrollbar;

    private boolean trackScrollWheelScroll = false;

    private int itemSpacing = 0;
    private boolean invertedItemOrder = false;

    /** Constructors */
    public ListBox(int xPos, int yPos, int width, int height){
        super(xPos, yPos, width, height);

        reinitializeElements();
    }

    public ListBox(ListBoxData<ItemType> data){
        super(data);

        itemBoxBackground = data.itemBoxBackground.makeLiveInstance();
        foreground.add(itemBoxBackground);

        scrollbar = data.scrollboxData.makeLiveInstance();
    }

    private void reinitializeElements(){
        foreground.clear();

        //* Build titlebox
        if(title != null && !title.isEmpty()){
            int titleboxHeight = 50;
            if(titleBox == null){
                titleBox = new TextBox(title, x, y + height - titleboxHeight, width, titleboxHeight);
                titleBox.setImage(UITheme.whitePixel);
                titleBox.setRenderColor(Color.valueOf("#151515FF"));
                titleBox.setTextRenderColor(Color.WHITE);
                titleBox.setHorizontalAlignment(Alignment.HorizontalAlignment.LEFT);
                titleBox.setMarginPercX(0.005f);
                foreground.add(titleBox);
            }
            titleBox.setText(title);
            titleBox.setPosition(x, y + height - titleboxHeight);
            titleBox.setDimensions(width, titleboxHeight);
        }

        int remainingHeight = height;
        if(titleBox != null) remainingHeight -= titleBox.getHeight();

        //* Build backgroundBox
        if(itemBoxBackground == null){
            Color bgColor = Color.BLACK.cpy();
            bgColor.a = 0.4f;
            itemBoxBackground = new Hoverable(UIThemeManager.getDefaultTheme().listbox, x, y, width, remainingHeight){
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
            foreground.add(itemBoxBackground);
        }
        itemBoxBackground.setPosition(x, y);
        itemBoxBackground.setDimensions(width, remainingHeight);

        //* Build scrollBox
        int scrollbarWidth = 50;
        if(scrollbar == null){
            scrollbar = new Scrollbox(x + width - scrollbarWidth, y, scrollbarWidth, remainingHeight) {
                @Override
                public int getPageCount() {
                    return calculatePageCount();
                }
            };
            foreground.add(scrollbar);
        }
        scrollbar.setPosition(x + width - scrollbarWidth, y);
        scrollbar.setDimensions(scrollbarWidth, remainingHeight);
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
        CompositeUIElement compositeItem = makeRenderElementForItem(item);
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
        clearElements();
    }

    public CompositeUIElement makeRenderElementForItem(ItemType item){
        TextBox label = new TextBox(item.toString(), x, y, itemBoxBackground.getWidth(), 30, 0.025f, 0.05f);
        label.setAlignment(Alignment.HorizontalAlignment.LEFT, Alignment.VerticalAlignment.CENTER);

        CompositeUIElement composite = new CompositeUIElement(label.getPositionX(), label.getPositionY(), label.getWidth(), label.getHeight());
        composite.foreground.add(label);

        composite.middle = new Button(label.getPositionX(), label.getPositionY(), itemBoxBackground.getWidth(), label.getHeight()){
            @Override
            protected void onLeftClick() {
                super.onLeftClick();
                onItemSelected(item);
            }
        }.setImage(UITheme.whitePixel).setRenderColor(Color.DARK_GRAY);

        postMakeRenderElementForItem(item, composite);
        return composite;
    }
    public void postMakeRenderElementForItem(ItemType item, CompositeUIElement compositeUIElement){ }

    public void onItemSelected(ItemType item){}

    public class ListBoxItem{
        /** Variables */
        public ItemType item;
        public CompositeUIElement renderForItem;

        /** Constructors */
        public ListBoxItem(ItemType item, CompositeUIElement renderElement){
            this.item = item;
            this.renderForItem = renderElement;
        }
    }

    /** Item Spacing */
    public ListBox<ItemType> setItemSpacing(int spacing){
        this.itemSpacing = spacing;
        return this;
    }
    public int getItemSpacing(){
        return itemSpacing;
    }

    /** Item Inversion */
    public ListBox<ItemType> setInvertedItemOrder(boolean invertedItemOrder){
        this.invertedItemOrder = invertedItemOrder;
        return this;
    }

    /** Background */
    public Hoverable getBackground(){
        return itemBoxBackground;
    }

    /** Title */
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
