package dLib.ui.elements.prefabs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import dLib.ui.HorizontalAlignment;
import dLib.ui.VerticalAlignment;
import dLib.ui.elements.CompositeUIElement;
import dLib.ui.elements.UIElement;
import dLib.ui.elements.implementations.Renderable;
import dLib.ui.themes.UITheme;
import dLib.ui.themes.UIThemeManager;

import java.util.ArrayList;

public class ListBox<ItemType> extends Renderable {
    /** Variables */
    private ArrayList<ListBoxItem> items = new ArrayList<>();

    private Scrollbox scrollbar;

    /** Constructors */
    public ListBox(int xPos, int yPos, int width, int height){
        super(UITheme.whitePixel, xPos, yPos, width, height);

        Color bgColor = Color.BLACK.cpy();
        bgColor.a = 0.4f;
        setRenderColor(bgColor);

        int scrollbarWidth = 100;
        scrollbar = new Scrollbox(xPos + width - scrollbarWidth, yPos, scrollbarWidth, height) {
            @Override
            public int getPageCount() {
                return calculatePageCount();
            }
        };
    }

    /** Update and Render */
    @Override
    public void update() {
        super.update();

        int currentYPos = y + height;
        for(UIElement item : getActiveItems()){
            item.setPositionY(currentYPos - item.getHeight());

            item.update();

            currentYPos -= item.getHeight();
        }

        if(calculatePageCount() > 1) scrollbar.update();
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);

        for(UIElement item : getActiveItems()){
            item.render(sb);
        }

        if(calculatePageCount() > 1) scrollbar.render(sb);
    }

    public ArrayList<UIElement> getActiveItems(){
        ArrayList<UIElement> activeItems = new ArrayList<>();

        int currentPageHeight = 0;
        for(int i = scrollbar.getCurrentPage() - 1; i < items.size(); i++){
            UIElement item = items.get(i).renderForItem;
            if(currentPageHeight + item.getHeight() > height){
                break;
            }

            currentPageHeight += item.getHeight();
            activeItems.add(item);
        }

        return activeItems;
    }

    /** Scrollbar */
    public int calculatePageCount(){
        int pageCount = 1;

        for(int i = items.size() - 1; i >= 0; i--){
            pageCount = i;

            int totalItemHeight = 0;
            for(int j = pageCount; j < items.size(); j++){
                totalItemHeight += items.get(j).renderForItem.getHeight();
                if(totalItemHeight > height){
                    return pageCount + 1;
                }
            }
        }

        return pageCount;
    }

    /** Items */
    public ListBox<ItemType> addItem(ItemType item){
        items.add(new ListBoxItem(item, makeRenderElementForItem(item)));

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
    }

    public UIElement getRenderElementForItem(ItemType item){
        TextBox label = new TextBox(item.toString(), x, y, width, 50, 0.025f, 0.05f);
        label.setAlignment(HorizontalAlignment.LEFT, VerticalAlignment.CENTER);
        return label;
    }
    private CompositeUIElement makeRenderElementForItem(ItemType item){
        UIElement elementRender = getRenderElementForItem(item);

        CompositeUIElement composite = new CompositeUIElement(elementRender.getPositionX(), elementRender.getPositionY());
        composite.other.add(elementRender);

        composite.middle = new Button(elementRender.getPositionX(), elementRender.getPositionY(), width, elementRender.getHeight()){
            @Override
            protected void onLeftClick() {
                super.onLeftClick();
                onItemSelected(item);
            }
        }.setImage(UITheme.whitePixel).setRenderColor(Color.DARK_GRAY);

        return composite;
    }

    public void onItemSelected(ItemType item){}

    public class ListBoxItem{
        /** Variables */
        public ItemType item;
        public UIElement renderForItem;

        /** Constructors */
        public ListBoxItem(ItemType item, UIElement renderElement){
            this.item = item;
            this.renderForItem = renderElement;
        }
    }
}
